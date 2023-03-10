package com.sinohealth.datax.processors;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.sinohealth.datax.common.CommonData;
import com.sinohealth.datax.common.Processor;
import com.sinohealth.datax.entity.common.BasCheckItem;
import com.sinohealth.datax.entity.common.KeywordsLevelDto;
import com.sinohealth.datax.entity.source.*;
import com.sinohealth.datax.entity.zktarget.StandardCheckRecordList;
import com.sinohealth.datax.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author mingqiang
 * @date 2022/08/29
 **/
public class CheckResultEtlProcessor implements Processor<RegCheck, StandardCheckRecordList> {
    public static final Logger LOG = LoggerFactory.getLogger(CheckResultEtlProcessor.class);
    //只需要结果，不需要离散型
    private static final List<String> CHECK_RESULT_DATA = Arrays.asList("体重指数", "身高", "体重", "骨密度检测");

    public final static String regx = "([1-9]\\d*\\.?\\d*)|(0\\.\\d*[1-9])";

    @Override
    public StandardCheckRecordList dataProcess(RegCheck check, StandardCheckRecordList checkList, CommonData commonData) {
        StandardCheckRecordList recordList = new StandardCheckRecordList();
        List<StandardCheckRecord> listRecord = new ArrayList<>();
        StandardCheckRecord checkRecord = new StandardCheckRecord();
        checkRecord.setItemName(check.getItemName());
        String results = check.getResults();
        checkRecord.setItemResults(results);
        checkRecord.setItemUnit(check.getUnit());
        checkRecord.setCleanTime(new Date());
        checkRecord.setVid(check.getVid());
        checkRecord.setClassName(check.getItemFt());
        checkRecord.setCleanStatus(1);
        checkRecord.setResultsDiscrete("0");
        checkRecord.setNormalH(check.getNormalH());
        checkRecord.setNormalL(check.getNormalL());
        try {
            if (StrUtil.isBlank(checkRecord.getClassName())) {
                checkRecord.setCleanStatus(EtlStatus.ETL_ERROR.getCode());
                checkRecord.setRemark("清洗失败，未找到检查数据大项");
                listRecord.add(checkRecord);
                recordList.setList(listRecord);
                return recordList;
            }
            String itemName = check.getItemName();
            String itemNameComn = "";
            Map<String, BasCheckItem> basCheckItemMap = commonData.getBasCheckItemMap();
            if (StrUtil.isNotBlank(itemName)) {
                itemName = itemName.replace("（", "(")
                        .replace("）", ")").trim();
            }
            BasCheckItem basCheckItem = basCheckItemMap.get(itemName);
            if (Objects.nonNull(basCheckItem)) {
                itemNameComn = basCheckItem.getItemNameStandard();
            }
            //进行标准化清洗
            checkRecord.setItemNameComn(itemNameComn);
            String unitComm = commonData.getBasTestUnitMap().get(check.getUnit());
            if (StrUtil.isNotBlank(unitComm)) {
                checkRecord.setUnitComm(unitComm);
            }
            if (StrUtil.isBlank(results)) {
                checkRecord.setCleanStatus(EtlStatus.ETL_ERROR.getCode());
                checkRecord.setRemark("清洗失败，结果值为空");
                listRecord.add(checkRecord);
                recordList.setList(listRecord);
                return recordList;
            }
            if(checkRecord.getItemName().contains("体重指数") || checkRecord.getItemNameComn().equals("收缩压") || checkRecord.getItemNameComn().equals("舒张压")){
            if(checkRecord.getItemName().contains("体重指数")){
                itemName = checkRecord.getItemName();
                itemName = itemName.replace("--", "-").replace("-","~");
                if(itemName.contains("-")){
                    String  temp=itemName.replace("体重指数", "").replace("(", "").replace("（", "").replace("）", "").replace(")", "").trim();
                    String[] str= temp.split("-");
                    if(str!=null&&str.length>=2) {
                        checkRecord.setNormalL(str[0]);
                        checkRecord.setNormalH(str[1]);
                    }
                }else if(StrUtil.isNotBlank(checkRecord.getNormalL()) && (checkRecord.getNormalL().contains("-") || checkRecord.getNormalL().contains("--") ||
                        checkRecord.getNormalL().contains("~"))){
                    String normal = checkRecord.getNormalL().replace("--", "-").replace("~","-");
                    String[] str= normal.split("-");
                    if(str!=null&&str.length>=2) {
                        checkRecord.setNormalL(str[0]);
                        checkRecord.setNormalH(str[1]);
                    }
                }
            }else if(checkRecord.getItemNameComn().equals("收缩压")) {
                if( StrUtil.isNotBlank(checkRecord.getNormalL()) && (checkRecord.getNormalL().contains("-") || checkRecord.getNormalL().contains("--") ||
                        checkRecord.getNormalL().contains("~"))){
                    String normal = checkRecord.getNormalL().replace("--", "-").replace("~","-");
                    String[] str= normal.split("-");
                    if(str!=null&&str.length>=2) {
                        checkRecord.setNormalL(str[0]);
                        checkRecord.setNormalH(str[1]);
                    }
                }else{
                    if(StringUtils.isBlank(checkRecord.getNormalL())) {
                        checkRecord.setNormalL("90");
                    }
                    if(StringUtils.isBlank(checkRecord.getNormalH())) {
                        checkRecord.setNormalH("140");
                    }else {
                        if(checkRecord.getItemUnit() != null){
                            checkRecord.setNormalH(checkRecord.getNormalH().replace(checkRecord.getItemUnit(), "").trim());
                        }
                    }
                }
            }else if(checkRecord.getItemNameComn().equals("舒张压")) {
                if (StrUtil.isNotBlank(checkRecord.getNormalL()) && (checkRecord.getNormalL().contains("-") || checkRecord.getNormalL().contains("--") ||
                        checkRecord.getNormalL().contains("~"))) {
                    String normal = checkRecord.getNormalL().replace("--", "-").replace("~", "-");
                    String[] str = normal.split("-");
                    if (str != null && str.length >= 2) {
                        checkRecord.setNormalL(str[0]);
                        checkRecord.setNormalH(str[1]);
                    }
                } else {
                    if (StringUtils.isBlank(checkRecord.getNormalL())) {
                        checkRecord.setNormalL("60");
                    }
                    if (StringUtils.isBlank(checkRecord.getNormalH())) {
                        checkRecord.setNormalH("90");
                    } else {
                        if (checkRecord.getItemUnit() != null) {
                            checkRecord.setNormalH(checkRecord.getNormalH().replace(checkRecord.getItemUnit(), "").trim());
                        }
                    }
                }
                check.setResults(results);
                checkRecord.setItemResults(results);
                resultsDiscreteProcess(checkRecord, commonData);
                listRecord.add(checkRecord);
            }} else {
                //三大结节处理
                listRecord = checkHandler(checkRecord, commonData);
            }
            recordList.setList(listRecord);
        } catch (Exception e) {
            LOG.info("清洗失败源数据[{}],异常[{}]", JSON.toJSONString(check), e.getMessage(), e);
        }
        return recordList;
    }

    public void resultsDiscreteProcess(StandardCheckRecord str, CommonData commonData) {
        if (NumberUtil.isNumber(str.getItemResults()) && NumberUtil.isNumber(str.getNormalL())
                && NumberUtil.isNumber(str.getNormalH())) {
            double value = Double.valueOf(str.getItemResults()).doubleValue();
            double normalL = Double.valueOf(str.getNormalL()).doubleValue();
            double normalH = Double.valueOf(str.getNormalH()).doubleValue();

            if (str.getItemNameComn().equals("体重指数")) {
                if (value <= normalH && value >= normalH * 0.9) {
                    str.setResultsDiscrete("1"); // 临界高
                    str.setItemResults("正常");
                } else if (value >= normalL && value <= normalL * 1.1) {
                    str.setResultsDiscrete("-1"); // 临界低
                    str.setItemResults("正常");
                } else if (value > normalH) {
                    str.setResultsDiscrete("2"); // 高
                    if (value >= 28) {
                        str.setItemResults("肥胖");
                    } else {
                        str.setItemResults("超重");
                    }
                } else if (value < normalL) {
                    str.setResultsDiscrete("-2"); // 低
                    str.setItemResults("偏瘦");
                } else {
                    str.setResultsDiscrete("0"); // 正常
                    str.setItemResults("正常");
                }
            } else {
                if (value <= normalH && value >= normalH * 0.9) {
                    str.setResultsDiscrete("1"); // 临界高
                    str.setItemResults("正常");
                } else if (value >= normalL && value <= normalL * 1.1) {
                    str.setResultsDiscrete("-1"); // 临界低
                    str.setItemResults("正常");
                } else if (value > normalH) {
                    str.setResultsDiscrete("2"); // 高
                    str.setItemResults("偏高");

                } else if (value < normalL) {
                    str.setResultsDiscrete("-2"); // 低
                    str.setItemResults("偏低");
                } else {
                    str.setResultsDiscrete("0"); // 正常
                    str.setItemResults("正常");
                }
            }
            str.setCleanStatus(EtlStatus.ETL_SUCCESS.getCode());
            str.setRemark(EtlStatus.ETL_SUCCESS.getMessage());
        } else {
            str.setCleanStatus(EtlStatus.ETL_ERROR_DISPERSE.getCode());
            str.setRemark(EtlStatus.ETL_ERROR_DISPERSE.getMessage());
        }
    }

    public List<StandardCheckRecord> checkHandler(StandardCheckRecord check, CommonData commonData) {
        List<StandardCheckRecord> temp = new ArrayList<>();

            boolean levelFlag = false;
            String results = check.getItemResults();
        if (check.getClassName().contains(EtlConst.ITEM_THYROID)) {
            try {
                String result = check.getItemResults();
                if (StringUtils.isNotBlank(result)) {
                    result = result.replaceAll("探及", "见");
                    result= result.replaceAll(EtlConst.REGX_SENSITIVE, "******");
                    //等级提取
                    if (!levelFlag) {
                        result = result.replace(" ", "");
                        result = result.toUpperCase();
                        result = result.replaceAll("TI-RADS[分|级|类]*\\d?[:：，,-]?[1|Ⅰ][分级类灶]+", "TI-RADS1级");
                        result = result.replaceAll("TI-RADS[分|级|类]*\\d?[:：，,-]?[1|Ⅰ]A[分级类灶]+", "TI-RADS1A级");
                        result = result.replaceAll("TI-RADS[分|级|类]*\\d?[:：，,-]?[1|Ⅰ]B[分级类灶]+", "TI-RADS1B级");
                        result = result.replaceAll("TI-RADS[分|级|类]*\\d?[:：，,-]?[1|Ⅰ]C[分级类灶]+", "TI-RADS1C级");
                        result = result.replaceAll("TI-RADS[分|级|类]*\\d?[:：，,-]?[2|Ⅱ]A[分级类灶]+", "TI-RADS2A级");
                        result = result.replaceAll("TI-RADS[分|级|类]*\\d?[:：，,-]?[2|Ⅱ]B[分级类灶]+", "TI-RADS2B级");
                        result = result.replaceAll("TI-RADS[分|级|类]*\\d?[:：，,-]?[2|Ⅱ]C[分级类灶]+", "TI-RADS2C级");
                        result = result.replaceAll("TI-RADS[分|级|类]*\\d?[:：，,-]?[2|Ⅱ][分级类灶]+", "TI-RADS2级");
                        result = result.replaceAll("TI-RADS[分|级|类]*\\d?[:：，,-]?[3|Ⅲ][分级类灶]+", "TI-RADS3级");
                        result = result.replaceAll("TI-RADS[分|级|类]*\\d?[:：，,-]?[3|Ⅲ]A[分级类灶]+", "TI-RADS3A级");
                        result = result.replaceAll("TI-RADS[分|级|类]*\\d?[:：，,-]?[3|Ⅲ]B[分级类灶]+", "TI-RADS3B级");
                        result = result.replaceAll("TI-RADS[分|级|类]*\\d?[:：，,-]?[3|Ⅲ]C[分级类灶]+", "TI-RADS3C级");
                        result = result.replaceAll("TI-RADS[分|级|类]*\\d?[:：，,-]?[4|Ⅳ][分级类灶]+", "TI-RADS4级");
                        result = result.replaceAll("TI-RADS[分|级|类]*\\d?[:：，,-]?[4|Ⅳ]A[分级类灶]+", "TI-RADS4A级");
                        result = result.replaceAll("TI-RADS[分|级|类]*\\d?[:：，,-]?[4|Ⅳ]B[分级类灶]+", "TI-RADS4B级");
                        result = result.replaceAll("TI-RADS[分|级|类]*\\d?[:：，,-]?[4|Ⅳ]C[分级类灶]+", "TI-RADS4C级");
                        result = result.replaceAll("TI-RADS[分|级|类]*\\d?[:：，,-]?[5|Ⅴ][分级类灶]+", "TI-RADS5级");
                        result = result.replaceAll("TI-RADS[分|级|类]*\\d?[:：，,-]?[5|Ⅴ]A[分级类灶]+", "TI-RADS5A级");
                        result = result.replaceAll("TI-RADS[分|级|类]*\\d?[:：，,-]?[5|Ⅴ]B[分级类灶]+", "TI-RADS5B级");
                        result = result.replaceAll("TI-RADS[分|级|类]*\\d?[:：，,-]?[5|Ⅴ]C[分级类灶]+", "TI-RADS5C级");
                        result = result.replaceAll("TI-RADS[分|级|类]*\\d?[:：，,-]?[6|Ⅵ][分级类灶]+", "TI-RADS6级");
                        result = result.replaceAll("TI-RADS[分|级|类]*\\d?[:：，,-]?[6|Ⅵ]A[分级类灶]+", "TI-RADS6A级");
                        result = result.replaceAll("TI-RADS[分|级|类]*\\d?[:：，,-]?[6|Ⅵ]B[分级类灶]+", "TI-RADS6B级");
                        result = result.replaceAll("TI-RADS[分|级|类]*\\d?[:：，,-]?[6|Ⅵ]C[分级类灶]+", "TI-RADS6C级");
                        List<String> levelListTi = ReUtil.findAll(EtlConst.TI_REGX_LEVEL, result, 0);
                        StandardCheckRecord pcr1 = new StandardCheckRecord();
                        pcr1.setItemName(EtlConst.RADSLEVEL);
                        pcr1.setItemNameComn(EtlConst.RADSLEVEL);
                        pcr1.setInitResult(EtlConst.INIT_RESULT_1);
                        pcr1.setClassName(check.getClassName());
                        pcr1.setVid(check.getVid());
                        pcr1.setCleanTime(new Date());
                        pcr1.setCleanStatus(1);
                        pcr1.setImageDiagnose(check.getImageDiagnose());
                        pcr1.setImageDescribe(check.getItemResults());
                        if (levelListTi != null && !levelListTi.isEmpty()) {
                            levelListTi.sort((x1, x2) -> x1.compareTo(x2));
                            String s = levelListTi.get(levelListTi.size() - 1);
                            s = s.replace("TI-RADS", "").replace("级", "").replace("类", "").trim();
                            pcr1.setItemResults(s);
                            temp.add(pcr1);
                        }
                    }
                }
                result = check.getItemResults().replaceAll(EtlConst.REGX_LEVEL, "**********");
                //取出优先级最高的那句话
                String str = "";
                int level = 0;
                //断句前对可见进行处理
                result = result.replaceAll("内见", "#")
                        .replaceAll("可见", "#");
                result = TextUtils.specialAddSigns(result);
                List<Map<String, Object>> tempList = new ArrayList<>();
                for (String str9 : result.split(EtlConst.SPLIT_JUHAO)) {
                    for (String str8 : str9.split(EtlConst.SPLIT_FENHAO2)) {
                        for (String str2 : str8.split(EtlConst.SPLIT_JIAN)) {
                            /*if (!str2.contains(EtlConst.ITEM_THYROID)) {
                                continue;
                            }*/
                            for (KeywordsLevelDto keyword : commonData.getKeywordsLevelList()) {
                                boolean flag = false;
                                for (String str3 : keyword.getKeywords()) {

                                    if (str2.contains(str3)) {
                                        if (str3.equals("实性") && str2.contains("囊实性")) {
                                            continue;
                                        }
                                        if (level == 0 || level > keyword.getLevel()) {
                                            level = keyword.getLevel();

                                        }
                                        Map<String, Object> mapBy = new HashMap<>();
                                        mapBy.put("level", keyword.getLevel());
                                        mapBy.put("result", str2);
                                        tempList.add(mapBy);
                                        flag = true;
                                        break;
                                    }

                                }
                                if (flag) {
                                    break;
                                }
                            }
                        }
                    }
                }

                if (level != 0) {
                    int curLevel = level;
                    double curValue0 = 0.0;
                    List<Map<String, Object>> tempList2 = tempList.stream().filter(x -> x.get("level").toString().equals(String.valueOf(curLevel))).collect(Collectors.toList());
                    for (Map<String, Object> map2 : tempList2) {
                        List<String> listS = new ArrayList<>();
                        String tempResult = map2.get("result").toString().toLowerCase();
                        String[] tempResultList = tempResult.split("[,，]");
                        for (String s : tempResultList) {
                            if (s.contains("cm") || s.contains("mm")) {
                                listS.addAll(ReUtil.findAll(EtlConst.regx, s, 0));
                            }
                        }
                        if (!listS.isEmpty()) {
                            listS.sort((x1, x2) -> Double.valueOf(x1).compareTo(Double.valueOf(x2)));
                            String s = listS.get(listS.size() - 1);

                            if (NumberUtil.isNumber(s)) {
                                double tempValue = Double.valueOf(s);
                                if (str.contains("cm") || str.contains("CM") || str.contains("Cm") || str.contains("cM")) {
                                    tempValue = tempValue * 10;
                                }
                                if ((tempValue >= 10 && Double.doubleToLongBits(curValue0) == Double.doubleToLongBits(0)) || (Double.doubleToLongBits(curValue0) != Double.doubleToLongBits(0) && tempValue > curValue0)) {
                                    curValue0 = tempValue;
                                    str = map2.get("result").toString();
                                }
                            }
                        }
                    }

                    if (StrUtil.isBlank(str)) {
                        double curValue = 0.0;
                        String tempStr = "";
                        for (Map<String, Object> map2 : tempList) {
                            List<String> listS = new ArrayList<>();
                            String tempResult = map2.get("result").toString().toLowerCase();
                            String[] tempResultList = tempResult.split("[,，]");
                            for (String s : tempResultList) {
                                if (s.contains("cm") || s.contains("mm")) {
                                    listS.addAll(ReUtil.findAll(EtlConst.regx, s, 0));
                                }
                            }
                            if (!listS.isEmpty()) {
                                listS.sort((x1, x2) -> Double.valueOf(x1).compareTo(Double.valueOf(x2)));
                                String s = listS.get(listS.size() - 1);
                                if (tempStr.equals("") || (NumberUtil.isNumber(s) && Double.valueOf(s) >= curValue)) {
                                    tempStr = map2.get("result").toString();
                                    curValue = Double.valueOf(s).doubleValue();
                                }
                            }
                        }
                        if (StrUtil.isNotBlank(tempStr)) {
                            str = tempStr;
                        }
                    }
                }
                if (StrUtil.isNotBlank(str)) {
                    //匹配超声特性
                    String str3 = "";
                    for (String str2 : EtlConst.alias1.split(EtlConst.SPLIT_DOUHAO)) {
                        if (str.contains(str2.split(EtlConst.SPLIT_MAOHAO)[0])) {
                            str3 = str2.split(EtlConst.SPLIT_MAOHAO)[1];
                            break;
                        }
                    }
                    //匹配结节类型
                    String str4 = "";
                    for (String str2 : EtlConst.alias2.split(EtlConst.SPLIT_DOUHAO)) {
                        if (str.contains(str2.split(EtlConst.SPLIT_MAOHAO)[0])) {
                            str4 = str2.split(EtlConst.SPLIT_MAOHAO)[1];
                            break;
                        }
                    }
                    //匹配最大结节直接
                    String str5 = "";
                    List<String> listS = new ArrayList<>();
                    String tempResult = str.toLowerCase();
                    String[] tempResultList = tempResult.split("[,，]");
                    for (String s : tempResultList) {
                        if (s.contains("cm") || s.contains("mm")) {
                            listS.addAll(ReUtil.findAll(EtlConst.regx, s, 0));
                        }
                    }
                    if (!listS.isEmpty()) {
                        listS.sort((x1, x2) -> Double.valueOf(x1).compareTo(Double.valueOf(x2)));
                        String s = listS.get(listS.size() - 1);
                        if (NumberUtil.isNumber(s)) {
                            str5 = s;
                        }
                    }
                    //超声类型
                    if (StrUtil.isNotBlank(str3)) {
                        StandardCheckRecord pcr1 = new StandardCheckRecord();
                        pcr1.setItemName(EtlConst.CHAOSHENGTEXING);
                        pcr1.setItemNameComn(EtlConst.CHAOSHENGTEXING);
                        pcr1.setVid(check.getVid());
                        pcr1.setClassName(check.getClassName());
                        pcr1.setInitResult(EtlConst.INIT_RESULT_1);
                        pcr1.setCleanTime(new Date());
                        pcr1.setItemResults(str3);
                        pcr1.setCleanStatus(1);
                        pcr1.setImageDiagnose(check.getImageDiagnose());
                        pcr1.setImageDescribe(check.getItemResults());
                        temp.add(pcr1);
                    }
                    //结节类型
                    if (StrUtil.isNotBlank(str4)) {
                        StandardCheckRecord pcr2 = new StandardCheckRecord();
                        pcr2.setItemName(EtlConst.JIEJIELEIXING);
                        pcr2.setItemNameComn(EtlConst.JIEJIELEIXING);
                        pcr2.setVid(check.getVid());
                        pcr2.setClassName(check.getClassName());
                        pcr2.setInitResult(EtlConst.INIT_RESULT_1);
                        pcr2.setCleanTime(new Date());
                        pcr2.setItemResults(str4);
                        pcr2.setCleanStatus(1);
                        pcr2.setImageDiagnose(check.getImageDiagnose());
                        pcr2.setImageDescribe(check.getItemResults());
                        temp.add(pcr2);
                    }
                    //最大结节直径
                    if (StrUtil.isNotBlank(str5)) {
                        StandardCheckRecord pcr3 = new StandardCheckRecord();
                        pcr3.setItemName(EtlConst.ZHIJING);
                        pcr3.setItemNameComn(EtlConst.JIEJIELEIXING);
                        pcr3.setVid(check.getVid());
                        pcr3.setClassName(check.getClassName());
                        pcr3.setInitResult(EtlConst.INIT_RESULT_1);
                        pcr3.setCleanTime(new Date());
                        pcr3.setItemResults(str5);
                        pcr3.setCleanStatus(1);
                        pcr3.setImageDiagnose(check.getImageDiagnose());
                        pcr3.setImageDescribe(check.getItemResults());
                        if (str.contains("cm") || str.contains("CM") || str.contains("Cm") || str.contains("cM")) {
                            pcr3.setItemUnit("cm");
                            pcr3.setUnitComm("cm");
                        } else if (str.contains("mm") || str.contains("MM") || str.contains("Mm") || str.contains("mM")) {
                            pcr3.setItemUnit("mm");
                            pcr3.setUnitComm("mm");
                        }
                        temp.add(pcr3);
                    }
                }

            } catch (Exception e) {
                LOG.warn("甲状腺清洗失败：vid:{}", check.getVid(), e);
            }
        }
        if (results.contains(EtlConst.FEI_KEY) || results.contains(EtlConst.XIONG_KEY) || results.contains(EtlConst.QI_KEY)) {
            String result = check.getItemResults();
            if (StrUtil.isNotBlank(result)) {
                result = result.replace(" ", "");
                result = result.toUpperCase();
                result = result.replaceAll("(TI|LU)-RADS[分|级|类]*\\d?[:：，,-]?[1|Ⅰ][分级类灶]+", "LU-RADS1级");
                result = result.replaceAll("(TI|LU)-RADS[分|级|类]*\\d?[:：，,-]?[1|Ⅰ]A[分级类灶]+", "LU-RADS1A级");
                result = result.replaceAll("(TI|LU)-RADS[分|级|类]*\\d?[:：，,-]?[1|Ⅰ]B[分级类灶]+", "LU-RADS1B级");
                result = result.replaceAll("(TI|LU)-RADS[分|级|类]*\\d?[:：，,-]?[1|Ⅰ]C[分级类灶]+", "LU-RADS1C级");
                result = result.replaceAll("(TI|LU)-RADS[分|级|类]*\\d?[:：，,-]?[2|Ⅱ]A[分级类灶]+", "LU-RADS2A级");
                result = result.replaceAll("(TI|LU)-RADS[分|级|类]*\\d?[:：，,-]?[2|Ⅱ]B[分级类灶]+", "LU-RADS2B级");
                result = result.replaceAll("(TI|LU)-RADS[分|级|类]*\\d?[:：，,-]?[2|Ⅱ]C[分级类灶]+", "LU-RADS2C级");
                result = result.replaceAll("(TI|LU)-RADS[分|级|类]*\\d?[:：，,-]?[2|Ⅱ][分级类灶]+", "LU-RADS2级");
                result = result.replaceAll("(TI|LU)-RADS[分|级|类]*\\d?[:：，,-]?[3|Ⅲ][分级类灶]+", "LU-RADS3级");
                result = result.replaceAll("(TI|LU)-RADS[分|级|类]*\\d?[:：，,-]?[3|Ⅲ]A[分级类灶]+", "LU-RADS3A级");
                result = result.replaceAll("(TI|LU)-RADS[分|级|类]*\\d?[:：，,-]?[3|Ⅲ]B[分级类灶]+", "LU-RADS3B级");
                result = result.replaceAll("(TI|LU)-RADS[分|级|类]*\\d?[:：，,-]?[3|Ⅲ]C[分级类灶]+", "LU-RADS3C级");
                result = result.replaceAll("(TI|LU)-RADS[分|级|类]*\\d?[:：，,-]?[4|Ⅳ][分级类灶]+", "LU-RADS4级");
                result = result.replaceAll("(TI|LU)-RADS[分|级|类]*\\d?[:：，,-]?[4|Ⅳ]A[分级类灶]+", "LU-RADS4A级");
                result = result.replaceAll("(TI|LU)-RADS[分|级|类]*\\d?[:：，,-]?[4|Ⅳ]B[分级类灶]+", "LU-RADS4B级");
                result = result.replaceAll("(TI|LU)-RADS[分|级|类]*\\d?[:：，,-]?[4|Ⅳ]C[分级类灶]+", "LU-RADS4C级");
                result = result.replaceAll("(TI|LU)-RADS[分|级|类]*\\d?[:：，,-]?[5|Ⅴ][分级类灶]+", "LU-RADS5级");
                result = result.replaceAll("(TI|LU)-RADS[分|级|类]*\\d?[:：，,-]?[5|Ⅴ]A[分级类灶]+", "LU-RADS5A级");
                result = result.replaceAll("(TI|LU)-RADS[分|级|类]*\\d?[:：，,-]?[5|Ⅴ]B[分级类灶]+", "LU-RADS5B级");
                result = result.replaceAll("(TI|LU)-RADS[分|级|类]*\\d?[:：，,-]?[5|Ⅴ]C[分级类灶]+", "LU-RADS5C级");
                result = result.replaceAll("(TI|LU)-RADS[分|级|类]*\\d?[:：，,-]?[6|Ⅵ][分级类灶]+", "LU-RADS6级");
                result = result.replaceAll("(TI|LU)-RADS[分|级|类]*\\d?[:：，,-]?[6|Ⅵ]A[分级类灶]+", "LU-RADS6A级");
                result = result.replaceAll("(TI|LU)-RADS[分|级|类]*\\d?[:：，,-]?[6|Ⅵ]B[分级类灶]+", "LU-RADS6B级");
                result = result.replaceAll("(TI|LU)-RADS[分|级|类]*\\d?[:：，,-]?[6|Ⅵ]C[分级类灶]+", "LU-RADS6C级");
                List<String> levelListTi = ReUtil.findAll(EtlConst.LU_REGX_LEVEL, result, 0);
                StandardCheckRecord pcr1 = new StandardCheckRecord();
                pcr1.setItemName(EtlConst.LU_RADSLEVEL);
                pcr1.setItemNameComn(EtlConst.LU_RADSLEVEL);
                pcr1.setInitResult(EtlConst.INIT_RESULT_FEI);
                pcr1.setClassName(check.getClassName());
                pcr1.setVid(check.getVid());
                pcr1.setCleanTime(new Date());
                pcr1.setCleanStatus(1);
                pcr1.setImageDiagnose(check.getImageDiagnose());
                pcr1.setImageDescribe(check.getItemResults());
                if (levelListTi != null && !levelListTi.isEmpty()) {
                    levelListTi.sort((x1, x2) -> x1.compareTo(x2));
                    String s = levelListTi.get(levelListTi.size() - 1);
                    s = s.replace("LU-RADS", "").replace("级", "").replace("类", "").trim();
                    pcr1.setItemResults(s);
                    temp.add(pcr1);
                }
            }
            String resultFei = results.replaceAll(EtlConst.KH_REGX, "");
            check.setItemResults(resultFei);
            List<StandardCheckRecord> records = new ArrayList<>();
            EtlFeiUtils.etl(check, records);
            temp.addAll(records);
        }
        //乳腺结节
        if (check.getItemResults().contains(EtlConst.RU_KEY)) {
            String result = check.getItemResults() == null ? "" : check.getItemResults();
            result = result.replace(" ", "");
            result = result.toUpperCase();
            result = result.replaceAll("BI-RADS[分|级|类]*\\d?[:：，,-]?[1|Ⅰ][分级类灶]+", "BI-RADS1级");
            result = result.replaceAll("BI-RADS[分|级|类]*\\d?[:：，,-]?[1|Ⅰ]A[分级类灶]+", "BI-RADS1A级");
            result = result.replaceAll("BI-RADS[分|级|类]*\\d?[:：，,-]?[1|Ⅰ]B[分级类灶]+", "BI-RADS1B级");
            result = result.replaceAll("BI-RADS[分|级|类]*\\d?[:：，,-]?[1|Ⅰ]C[分级类灶]+", "BI-RADS1C级");
            result = result.replaceAll("BI-RADS[分|级|类]*\\d?[:：，,-]?[2|Ⅱ]A[分级类灶]+", "BI-RADS2A级");
            result = result.replaceAll("BI-RADS[分|级|类]*\\d?[:：，,-]?[2|Ⅱ]B[分级类灶]+", "BI-RADS2B级");
            result = result.replaceAll("BI-RADS[分|级|类]*\\d?[:：，,-]?[2|Ⅱ]C[分级类灶]+", "BI-RADS2C级");
            result = result.replaceAll("BI-RADS[分|级|类]*\\d?[:：，,-]?[2|Ⅱ][分级类灶]+", "BI-RADS2级");
            result = result.replaceAll("BI-RADS[分|级|类]*\\d?[:：，,-]?[3|Ⅲ][分级类灶]+", "BI-RADS3级");
            result = result.replaceAll("BI-RADS[分|级|类]*\\d?[:：，,-]?[3|Ⅲ]A[分级类灶]+", "BI-RADS3A级");
            result = result.replaceAll("BI-RADS[分|级|类]*\\d?[:：，,-]?[3|Ⅲ]B[分级类灶]+", "BI-RADS3B级");
            result = result.replaceAll("BI-RADS[分|级|类]*\\d?[:：，,-]?[3|Ⅲ]C[分级类灶]+", "BI-RADS3C级");
            result = result.replaceAll("BI-RADS[分|级|类]*\\d?[:：，,-]?[4|Ⅳ][分级类灶]+", "BI-RADS4级");
            result = result.replaceAll("BI-RADS[分|级|类]*\\d?[:：，,-]?[4|Ⅳ]A[分级类灶]+", "BI-RADS4A级");
            result = result.replaceAll("BI-RADS[分|级|类]*\\d?[:：，,-]?[4|Ⅳ]B[分级类灶]+", "BI-RADS4B级");
            result = result.replaceAll("BI-RADS[分|级|类]*\\d?[:：，,-]?[4|Ⅳ]C[分级类灶]+", "BI-RADS4C级");
            result = result.replaceAll("BI-RADS[分|级|类]*\\d?[:：，,-]?[5|Ⅴ][分级类灶]+", "BI-RADS5级");
            result = result.replaceAll("BI-RADS[分|级|类]*\\d?[:：，,-]?[5|Ⅴ]A[分级类灶]+", "BI-RADS5A级");
            result = result.replaceAll("BI-RADS[分|级|类]*\\d?[:：，,-]?[5|Ⅴ]B[分级类灶]+", "BI-RADS5B级");
            result = result.replaceAll("BI-RADS[分|级|类]*\\d?[:：，,-]?[5|Ⅴ]C[分级类灶]+", "BI-RADS5C级");
            result = result.replaceAll("BI-RADS[分|级|类]*\\d?[:：，,-]?[6|Ⅵ][分级类灶]+", "BI-RADS6级");
            result = result.replaceAll("BI-RADS[分|级|类]*\\d?[:：，,-]?[6|Ⅵ]A[分级类灶]+", "BI-RADS6A级");
            result = result.replaceAll("BI-RADS[分|级|类]*\\d?[:：，,-]?[6|Ⅵ]B[分级类灶]+", "BI-RADS6B级");
            result = result.replaceAll("BI-RADS[分|级|类]*\\d?[:：，,-]?[6|Ⅵ]C[分级类灶]+", "BI-RADS6C级");
            List<String> levelList = ReUtil.findAll(EtlConst.BI_REGX_LEVEL, result, 0);
            // SDI-RADS也是乳腺分级
            String addSDI = result.replaceAll("SDI-RADS[:：]?(分类)?[:：]?", "SDI-RADS");
            List<String> list = ReUtil.findAll("SDI-RADS\\d+[A-Z]?", addSDI, 0);
            for (String s : list) {
                levelList.add(s.replaceAll("SDI-RADS", "BI-RADS") + "级");
            }

            if (levelList != null && !levelList.isEmpty()) {
                levelList.sort(Comparator.naturalOrder());
                String s = levelList.get(levelList.size() - 1);
                StandardCheckRecord pcr1 = new StandardCheckRecord();
                pcr1.setItemName(EtlConst.BI_RADSLEVEL);
                pcr1.setItemNameComn(EtlConst.BI_RADSLEVEL);
                pcr1.setInitResult(EtlConst.INIT_RESULT_2);
                pcr1.setCleanStatus(1);
                pcr1.setItemResults(s.replace("BI-RADS", "").replace("级", "").replace("类", "").trim());
                temp.add(pcr1);
            }
            //在描述里面，乳腺结节处理
            List<String> hitStrs = new ArrayList<>();
            String[] splitStrings = TextTrimUtils.splitSignsToArrByParam(result, ";；。");
            boolean flag2 = false;
            for (String str1 : splitStrings) {
                boolean flagRu = false;
                boolean flag7 = ReUtil.contains(regx, str1) && (str1.contains("cm") || str1.contains("CM") || str1.contains("Cm") || str1.contains("cM") || str1.contains("mM") || str1.contains("MM") || str1.contains("mm") || str1.contains("Mm"));
                for (String s : EtlConst.RU_DATA_KEY) {
                    if (str1.contains(s)) {
                        flagRu = true;
                        break;
                    }
                }
                boolean flagNormal = false;
                for (String s : EtlConst.NORMAL_LIST) {
                    if(str1.contains(s) && !flag7){
                        flagNormal = true;
                        break;
                    }
                }
                if ((flagRu ||flagNormal)) {
                    continue;
                }
                if (str1.contains(EtlConst.RU_KEY)) {
                    for (String ru : EtlConst.ruMethod) {
                        if (str1.contains(ru)) {
                            flag2 = true;
                            break;
                        }
                    }
                }
                if(flag2){
                    break;
                }
            }
            if (flag2) {
                StandardCheckRecord pcr3 = new StandardCheckRecord();
                pcr3.setItemName(EtlConst.INIT_RESULT_RU);
                pcr3.setItemNameComn(EtlConst.INIT_RESULT_RU);
                pcr3.setInitResult(EtlConst.INIT_RESULT_2);
                pcr3.setCleanStatus(1);
                pcr3.setItemResults("1");
                temp.add(pcr3);
            }
        }
        StandardCheckRecord pcr1 = new StandardCheckRecord();
        pcr1.setItemName(check.getItemName());
        pcr1.setItemResults(check.getItemResults());
        pcr1.setItemUnit(check.getItemUnit());
        pcr1.setVid(check.getVid());
        pcr1.setCleanTime(new Date());
        pcr1.setClassName(check.getClassName());
        pcr1.setItemResults("0");
        pcr1.setImageDescribe(check.getItemResults());
        pcr1.setImageDiagnose(check.getImageDiagnose());
        pcr1.setItemNameComn(check.getItemNameComn());

        if (temp.isEmpty()) {
            String result = check.getItemResults();
            boolean flagA = false;
            boolean flagB = false;
            boolean flagC = false;
            for (String  s: EtlConst.NORMAL_LIST) {
                if(result.contains(s)){
                    flagA = true;
                    break;
                }
            }
            for (String  s: EtlConst.ETL_NOT_DOING) {
                if(result.contains(s)){
                    flagB = true;
                    break;
                }
            }
            for (String  s: EtlConst.ETL_SUSPICIOUS) {
                if(result.contains(s)){
                    flagC = true;
                    break;
                }
            }

            if(flagA){
                pcr1.setRemark(EtlStatus.ETL_SUCCESS_NORMAL.getMessage());
                pcr1.setCleanStatus(EtlStatus.ETL_SUCCESS_NORMAL.getCode());
            }else if(flagB){
                pcr1.setRemark(EtlStatus.ETL_NOT_DOING.getMessage());
                pcr1.setCleanStatus(EtlStatus.ETL_NOT_DOING.getCode());
            }else if(flagC){
                pcr1.setRemark(EtlStatus.ETL_SUSPICIOUS.getMessage());
                pcr1.setCleanStatus(EtlStatus.ETL_SUSPICIOUS.getCode());
            }else{
                pcr1.setRemark(EtlStatus.ETL_DATA.getMessage());
                pcr1.setCleanStatus(EtlStatus.ETL_DATA.getCode());
            }
            temp.add(pcr1);
        }
        return temp;
    }

    public static void main(String[] args) {
        String str = "1.左乳腺Ca术后改变2.右下肺结节，较前片未见明显变化。";
        boolean matches = str.matches("\\d*[.、]+");
        str = str.replaceAll("\\d*[.、]+", "99999");
        System.out.println(str);
        //testFei("右侧甲状腺囊实性结节左侧甲状腺囊肿双侧乳腺轻度增生右侧乳腺实性结节BI-RADS4a级右侧乳腺囊肿");
    }

    public void test() {
        String trim = TextUtils.textTrim(null);
        String str = "+1.00";
        String s = str.replaceAll("\\.+\\d*", "");
        System.out.println(s);
        String str1 = "BI-RADS  1类";
        String trim1 = str1.trim();
        //trim1 = trim1.replace(" ", "");
        trim1 = trim1.replace("\\s", "");
        str = "乳腺增生症（BI-RADS分级Ⅳs类类类）";
        String BI_REGX_LEVEL = "BI-RADS[分|级|类]*[4|Ⅳ].+[分|级|类]";
        String s1 = str.replaceAll(BI_REGX_LEVEL, "BI-RADS1级");
        System.out.println(s1);
    }

    public static void testFei(String str) {

        String result = str;
        result = result.toUpperCase();
        result = result.replace(" ", "");
        result = result.replaceAll("BI-RADS[分|级|类]*[:|：]?[1|Ⅰ][分|级|类|灶]*", "BI-RADS1级");
        result = result.replaceAll("BI-RADS[分|级|类]*[:|：]?[2|Ⅱ][分|级|类|灶]*", "BI-RADS2级");
        result = result.replaceAll("BI-RADS[分|级|类]*[:|：]?[3|Ⅲ][分|级|类|灶]*", "BI-RADS3级");
        result = result.replaceAll("BI-RADS[分|级|类]*[:|：]?[4|Ⅳ][分|级|类|灶]*", "BI-RADS4级");
        result = result.replaceAll("BI-RADS[分|级|类]*[:|：]?[5|Ⅴ][分|级|类|灶]*", "BI-RADS5级");
        result = result.replaceAll("BI-RADS[分|级|类]*[:|：]?[6|Ⅵ][分|级|类|灶]*", "BI-RADS6级");
        List<String> levelListBi = ReUtil.findAll(EtlConst.BI_REGX_LEVEL, result, 0);
        StandardCheckRecord pcr1 = new StandardCheckRecord();
        pcr1.setItemName(EtlConst.BI_RADSLEVEL);
        pcr1.setItemNameComn(EtlConst.BI_RADSLEVEL);
        pcr1.setInitResult(EtlConst.INIT_RESULT_2);

        if (levelListBi != null && !levelListBi.isEmpty()) {
            levelListBi.sort((x1, x2) -> x1.compareTo(x2));
            String s = levelListBi.get(levelListBi.size() - 1);
            s = s.replace("BI-RADS", "").replace("级", "").replace("类", "").trim();
            if (!NumberUtil.isNumber(s)) {
                pcr1.setCleanStatus(3);
                pcr1.setRemark("清洗失败，未获取到数值");
            }
            pcr1.setItemResults(s);
            System.out.println("结果" + s);
        }
    }
}


