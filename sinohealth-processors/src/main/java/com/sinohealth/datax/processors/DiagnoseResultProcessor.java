package com.sinohealth.datax.processors;

import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import com.sinohealth.datax.common.CommonData;
import com.sinohealth.datax.common.Processor;
import com.sinohealth.datax.entity.common.BasInspectionKeyword;
import com.sinohealth.datax.entity.common.BasItemAlias;
import com.sinohealth.datax.entity.source.RegCheck;
import com.sinohealth.datax.entity.source.StandardDiagnoseRecord;
import com.sinohealth.datax.entity.zktarget.StandardDiagnoseRecordList;
import com.sinohealth.datax.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author mingqiang
 * @date 20220905
 **/
public class DiagnoseResultProcessor implements Processor<RegCheck, StandardDiagnoseRecordList> {

    public static final Logger logger = LoggerFactory.getLogger(DiagnoseResultProcessor.class);
    private static final List<String> negativeWordList = Lists.newArrayList("无", "没有", "未见", "未发现", "弃查", "弃检", "趋势");

    private static final List<String> specialProcess2List = Lists.newArrayList("未见明显异常", "未发现明显异常", "未见异常", "未发现异常", "未见", "无异常");

    public static List<BasInspectionKeyword> inspectionKeywordList;

    public static final String skipAllKey = "降低,增高,减少,升高,偏高,偏低,阳性";

    public static final String defaultNameStr = "超声,MR,CT,DR,心电图,宫颈脱落细胞学检测";

    @Override
    public StandardDiagnoseRecordList dataProcess(RegCheck check, StandardDiagnoseRecordList list, CommonData commonData) {
        ArrayList<StandardDiagnoseRecord> recordList = new ArrayList<>();
        String summary = check.getResults();
        if (StrUtil.isNotBlank(summary)) {
            summary = summary.replaceAll("\\r\\n\\r\\n", "。");
            summary = summary.replaceAll("\\r\\n", "。");
            summary = summary.replaceAll("\\n", "。");
        }
        if (StrUtil.isNotBlank(summary)) {
            summary = summary.replaceFirst("★", "");
        } else {
            summary = "超声、X线、CT、磁共振、红外线等影像医学检查结论已根据体检结果进行评估，相关图像资料，请参阅体检报告或至体检中心获取。";
        }
        summary = specialProcess4(summary);
        summary = summary.replaceAll(" ", "");
        inspectionKeywordList = commonData.getBasInspectionKeywordList();
        summary = summary.replace("*", "");
        String[] summaryArr = summary.split("★");
        for (String s : summaryArr) {
            List<StandardDiagnoseRecord> standardDiagnoseRecords = logicJudgeInspection(s, commonData.getBasItemAliasList(), check);
            if (standardDiagnoseRecords == null) {
                standardDiagnoseRecords = new ArrayList<StandardDiagnoseRecord>();
            }
            recordList.addAll(standardDiagnoseRecords);
        }
        //对于相同疾病去重处理
        Map<String, List<StandardDiagnoseRecord>> showNameMap = recordList.stream().collect(Collectors.groupingBy(StandardDiagnoseRecord::getItemName));
        for (String showName : showNameMap.keySet()) {
            List<StandardDiagnoseRecord> temp = showNameMap.get(showName);
            // 根据等级排序
            ArrayList<String> listS = new ArrayList<>();
            int i = 0;
            HashMap<String, StandardDiagnoseRecord> hashMapDisease = new HashMap<>();
            for (StandardDiagnoseRecord drnd : temp) {
                if(drnd.getItemName().contains("RADS")){
                    List<String> listDisease = ReUtil.findAll(EtlConst.regx, drnd.getItemName(), 0);
                    listDisease.sort(Comparator.comparing(Double::valueOf));
                    String s = listDisease.get(listDisease.size() - 1);
                    listS.add(s);
                    hashMapDisease.put(s,drnd);
                }
                i++;
            }
        }
        list.setList(recordList);
        return list;
    }
    private List<StandardDiagnoseRecord> logicJudgeInspection(String inspectionItem, List<BasItemAlias> allItemAlias, RegCheck check) {
        /**
         * -1、先判断该总结项中是否含有冒号，不含冒号的选项，不做处理，直接跳过 0、 先根据冒号切断，取第一个项目别名，
         * 根据项目别名查询base_item_alias匹配项目名，再根据项目名查询base_inspection_keyword
         * 1、请逐个分句匹配关键字，（没有逗号的按照句号匹配）， 关键字匹配中，遇到【建议】请删掉建议后面的内容，是发散的故事性的，不客观的；
         * 4、否定副词：negativeWordList = ["无","没有", "未见", "正常", "未发现"] 上述情况均排除后，
         * 查询可能匹配大于一个异常名称，唯一最大匹配，去掉重复。
         */
        String itemName = "";
        String itemResults = "";
        String itemResultsBak = "";
        String useDefaultName = "";
        List<StandardDiagnoseRecord> records = new ArrayList<>();
        if (inspectionItem.indexOf(":") > -1 || inspectionItem.indexOf("：") > -1) {// 根据中英文冒号截取检测项目
            int chIndex = inspectionItem.indexOf(":");
            int enIndex = inspectionItem.indexOf("：");
            int splitIndex = 0;
            if (chIndex == -1) {
                splitIndex = enIndex;
            } else if (enIndex == -1) {
                splitIndex = chIndex;
            } else {// 都包含
                splitIndex = chIndex > enIndex ? enIndex : chIndex;
            }
            String itemAlias = inspectionItem.substring(0, splitIndex);
            if(itemAlias.endsWith("提示")){
                itemAlias = itemAlias.substring(0,itemAlias.length()-2);
            }else if(itemAlias.endsWith("结果")){
                itemAlias = itemAlias.substring(0,itemAlias.length()-2);
            }
            BasItemAlias resultAlias = DocSimulateUtils.calcuAliasContain(itemAlias, allItemAlias);
            if (resultAlias != null) {
                // 获取项目名和检查结果
                itemName = resultAlias.getName();
                itemResults = inspectionItem.substring(splitIndex);
                itemResultsBak = inspectionItem.substring(0, splitIndex);
            } else {
                logger.info("未识别出的别名：{}",itemAlias);
                //条件筛选
                if(splitIndex > 0) {
                    String itemResultTemp = "";
                    itemResultTemp = inspectionItem.substring(splitIndex);
                    itemResultTemp = itemResultTemp.replace(":", "")
                            .replace("：", "")
                            .replace("；", "")
                            .replace("。", "");
                    if ("详见纸质报告".equals(itemResultTemp)) {
                        return null;
                    }
                    boolean flagSkip = false;
                    boolean flagResult = false;
                    for (String s : skipAllKey.split(",")) {
                        if (itemAlias.contains(s)) {
                            flagSkip = true;
                            break;
                        }
                    }
                    if (flagSkip) {
                        for (String s : skipAllKey.split(",")) {
                            if (itemResultTemp.contains(s)) {
                                flagResult = true;
                                break;
                            }
                        }
                    }
                    if (flagSkip && flagResult) {
                        return null;
                    }
                }
                useDefaultName = inspectionItem.replace(":","。").replace("：", "。");
            }
        } else {
            /*
             * 如果无法根据冒号切分出项目别名， 则直接根据整句话的相似度获取最相近的项目别名，然后得到项目名称， 并截取项目别名之后的内容，即为检查结果，继续判断
             */
            if (inspectionItem.indexOf("建议") > -1) {
                inspectionItem = inspectionItem.substring(0, inspectionItem.indexOf("建议"));
            }
            BasItemAlias resultAlias = DocSimulateUtils.calcuAliasContain(inspectionItem, allItemAlias);
            if (resultAlias != null) {
                // 获取项目名和检查结果
                itemName = resultAlias.getName();
                String aliasName = resultAlias.getAliasName();
                char[] cArr = aliasName.toCharArray();

                for (char c : cArr) {
                    String str = makeQueryStringAllRegExp(String.valueOf(c));
                    inspectionItem = inspectionItem.replaceFirst(str, "");
                }
                itemResults = inspectionItem;
            } else {
                useDefaultName = inspectionItem;
            }
        }
        if (StringUtils.isNotBlank(itemName)) {
            records = preProcessItemResult(itemName, itemResults, check);
            if (records == null) {
                records = preProcessItemResult(itemName, itemResults, check);
            }
        }else {
            String[] defualtNames = defaultNameStr.split(",");
            if (StringUtils.isNotBlank(useDefaultName)) {
                for (String defualtName : defualtNames) {
                    records = preProcessItemResult(defualtName, useDefaultName, check);
                    if (records != null) {
                        break;
                    }
                }
            }
        }
        return records;
    }

    private  List<StandardDiagnoseRecord> preProcessItemResult(String itemName, String itemResults, RegCheck check) {

        StandardDiagnoseRecordList recordList = new StandardDiagnoseRecordList();
        ArrayList<StandardDiagnoseRecord> list = new ArrayList<>();
        /*
         * start 判断总检结果是否正常 开始 请逐个分句匹配关键字，（没有逗号的按照句号匹配）
         * 若去掉建议后该分句后仍然大于40个字符，去掉40个字符以后的内容；
         */
        // 如果整个句子中出现了否定副词，则判断"肝、胆、脾、肾、膀胱、输尿管、附件、"是否存在在分句中
        itemResults = specialProcess(itemResults);

        // 当出现未见/未发现时，则在异常/明显异常后面加个逗号分割
        itemResults = specialProcess2(itemResults);

        // 当同时出现未见/未发现和病变时，则在病变后面加个逗号分割
        itemResults = specialProcess3(itemResults);

        // 预处理语句：特殊处理部位针对新一部部位的检测前未加标点的问题，如肝囊肿肾：肾结石
        itemResults = TextUtils.specialAddSigns(itemResults);

        // 根据特殊符号，将整句切分成String数组
        String[] temp = splitSignsToArr(itemResults);

        List<String> tempList1 = null;// 用来判断“肝胆脾肾输尿管膀胱附件”的特殊逻辑1
        for (int i = 0; i < temp.length; i++) {
            String clause = temp[i];
            if (":".equals(clause) || "：".equals(clause)) {
                continue;
            }
            if (StrUtil.isNotBlank(clause)) {
                int index = clause.indexOf("建议");
                if (index > -1) {
                    clause = clause.substring(0, index);
                }
                index = clause.indexOf("多见于");
                if (index > -1) {
                    clause = clause.substring(0, index);
                }
                if (clause.length() >= 100) {
                    clause = clause.substring(0, 100);
                }
                list.addAll(addRelateDisease(itemName, clause, check));
            }
        }
        return list;
    }

    // 预处理语句:如果整句出现否定副词，就删掉"**、"
    private String specialProcess(String itemResults) {
        if (StrUtil.isNotBlank(itemResults)) {
            for (int j = 0; j < negativeWordList.size(); j++) {
                if (itemResults.contains(negativeWordList.get(j))) {
                    // 删掉"肝、胆、脾、肾、膀胱、输尿管、附件、前列腺、胰、"
                    itemResults = itemResults.replaceAll(":.+?趋势", "****").replaceAll("：.+?趋势", "****").replace("肝、", "").replace("胆、", "").replace("脾、", "").replace("肾、", "")
                            .replace("输尿管、", "").replace("膀胱、", "").replace("附件、", "").replace("前列腺、", "")
                            .replace("胰、", "");
                    break;
                }
            }
        }
        return itemResults;
    }

    private String specialProcess2(String itemResults) {
        if (StrUtil.isNotBlank(itemResults)) {
            for (int j = 0; j < specialProcess2List.size(); j++) {
                if (itemResults.contains(specialProcess2List.get(j))) {
                    int index = itemResults.indexOf(specialProcess2List.get(j)) + specialProcess2List.get(j).length();
                    itemResults = addSigns(itemResults, index);
                }
            }
        }
        return itemResults;
    }

    private String specialProcess3(String itemResults) {
        if (StrUtil.isNotBlank(itemResults)) {
            if (itemResults.contains("病变")) {
                if (itemResults.contains("未见") || itemResults.contains("未发现")) {
                    int index = itemResults.indexOf("病变") + 2;
                    itemResults = addSigns(itemResults, index);
                }
            }
        }
        return itemResults;
    }

    private String specialProcess4(String itemResults) {
        if (StrUtil.isNotBlank(itemResults)) {
            if (itemResults.endsWith("\r\r \n\r")) {
                itemResults = itemResults.replace("\r\r \n\r", "");
            }
            if (itemResults.contains("\r\n")) {
                String[] strings = itemResults.split("\r\n");
                itemResults = Arrays.asList(strings).stream().collect(Collectors.joining("。"));
            }
            if (itemResults.contains("。。")) {
                itemResults = itemResults.replace("。。", "。");
            }
        }
        return itemResults;
    }

    private String addSigns(String itemResults, int index) {
        StringBuffer stringBuffer = new StringBuffer(itemResults);
        return stringBuffer.insert(index, ",").toString();
    }

    private String[] splitSignsToArr(String s) {
        StringTokenizer str1 = new StringTokenizer(s, ",，.。 ；;");
        int count = 0;
        String[] temp = new String[str1.countTokens()];

        while (str1.hasMoreTokens()) {
            temp[count] = str1.nextToken();
            count++;
        }
        return temp;
    }



    private List<StandardDiagnoseRecord> addRelateDisease(String itemName, String clause, RegCheck check) {
        List<BasInspectionKeyword> mostSimuList = new ArrayList<>();
        List<StandardDiagnoseRecord> list = new ArrayList<>();
        if (StringUtils.isNotBlank(clause) && inspectionKeywordList != null) {
            List<BasInspectionKeyword> mostSimuList2 = new ArrayList<>();
            //检测方法匹配过滤
            List<BasInspectionKeyword> mostSimuList3 = inspectionKeywordList.stream().filter(x -> x.getMethod().equals(itemName)).collect(Collectors.toList());
            //关键字匹配过滤
            for (BasInspectionKeyword bsd : mostSimuList3) {
                if (StrUtil.isNotBlank(bsd.getKeyword())) {
                    if (bsd.getKeyword().trim().split("、").length == 1) {
                        if (clause.contains(bsd.getKeyword().trim())) {
                            mostSimuList2.add(bsd);
                        }
                    } else {
                        boolean flag = true;
                        for (String str : bsd.getKeyword().trim().split("、")) {
                            if (!clause.contains(str)) {
                                flag = false;
                                break;
                            }
                        }
                        if (flag) {
                            mostSimuList2.add(bsd);
                        }
                    }
                } else {
                    mostSimuList2.add(bsd);
                }
            }
            //结果匹配过滤
            for (BasInspectionKeyword bsd : mostSimuList2) {
                if (StrUtil.isNotBlank(bsd.getResult())) {
                    if (clause.contains(bsd.getResult().trim())) {
                        mostSimuList.add(bsd);
                    }
                } else {
                    mostSimuList.add(bsd);
                }
            }
        }
        if (!mostSimuList.isEmpty()) {
            /**
             * 有可能不止一种疾病
             * 根据DiseaseName是否包含，唯一最大匹配，然后new对象
             */
            List<BasInspectionKeyword> removeContainsList = removeContains(mostSimuList);
            for (BasInspectionKeyword inspectionKeyword : removeContainsList) {
                StandardDiagnoseRecord record = new StandardDiagnoseRecord();
                record.setVid(check.getVid());
                record.setItemResults(inspectionKeyword.getDiseaseSigns());
                record.setItemName(inspectionKeyword.getShowName());
                record.setCleanStatus(1);
                record.setCleanTime(new Date());
                record.setItemId(inspectionKeyword.getId().toString());
                record.setClassName(itemName);
                list.add(record);
            }
        }
        return list;
    }

    // 根据字段是否互相包含去重（冒泡排序）
    private List<BasInspectionKeyword> removeContains(List<BasInspectionKeyword> oriList) {
        Map<String, BasInspectionKeyword> newMap = new HashMap<>();
        for (int i = 0; i < oriList.size(); i++) {
            BasInspectionKeyword ori1 = oriList.get(i);
            for (int j = 0; j < oriList.size(); j++) {
                BasInspectionKeyword ori2 = oriList.get(j);
                if (ori2.getDiseaseSigns().contains(ori1.getDiseaseSigns())) {
                    ori1 = ori2;
                }
            }
            newMap.put(ori1.getDiseaseSigns(), ori1);
        }
        List<BasInspectionKeyword> newList = new ArrayList<>(newMap.values());
        return newList;
    }

    /**
     * 转义正则特殊字符 （$()*+.[]?\^{} \\需要第一个替换，否则replace方法替换时会有逻辑bug
     */
    public String makeQueryStringAllRegExp(String str) {
        if (StringUtils.isBlank(str)) {
            return str;
        }

        return str.replace("\\", "\\\\").replace("*", "\\*").replace("+", "\\+").replace("|", "\\|").replace("{", "\\{")
                .replace("}", "\\}").replace("(", "\\(").replace(")", "\\)").replace("^", "\\^").replace("$", "\\$")
                .replace("[", "\\[").replace("]", "\\]").replace("?", "\\?").replace(",", "\\,").replace(".", "\\.")
                .replace("&", "\\&");
    }
}
