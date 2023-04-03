package com.sinohealth.datax.processors;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.sinohealth.datax.common.CommonData;
import com.sinohealth.datax.common.Processor;
import com.sinohealth.datax.entity.common.StandardBasTestItem;
import com.sinohealth.datax.entity.source.BasTestItemTemp;
import com.sinohealth.datax.entity.source.RegTest;
import com.sinohealth.datax.entity.source.StandardTestRecord;
import com.sinohealth.datax.entity.zktarget.StandardTestRecordList;
import com.sinohealth.datax.utils.EtlConst;
import com.sinohealth.datax.utils.EtlSTConst;
import com.sinohealth.datax.utils.EtlStatus;
import com.sinohealth.datax.utils.TextUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * @author mingqiang
 * @date 2022/08/29
 * @desc 处理检验
 **/
public class TestResultEtlProcessor implements Processor<BasTestItemTemp, StandardTestRecordList> {
    public static final Logger LOG = LoggerFactory.getLogger(TestResultEtlProcessor.class);
    private static final HashMap<String, RegTest> hashMapData = new HashMap<>();
    private static Map<String, String> resultDiscreteMap = Maps.newHashMap();
    private static final List<String> TEST_RESULT_DATA = Arrays.asList("ABO血型");

    static {
        // 加载离散类型为2的字典
        resultDiscreteMap.put("-", "-1");
        resultDiscreteMap.put("--", "-1");
        resultDiscreteMap.put("---", "-1");
        resultDiscreteMap.put("----", "-1");
        resultDiscreteMap.put("-----", "-1");
        resultDiscreteMap.put("阴", "-1");
        resultDiscreteMap.put("阴性", "-1");
        resultDiscreteMap.put("阴性（-）", "-1");
        resultDiscreteMap.put("阴性(-)", "-1");
        resultDiscreteMap.put("弱阳性", "1");
        resultDiscreteMap.put("阳性(轻度)", "1");
        resultDiscreteMap.put("阳性（轻度）", "1");
        resultDiscreteMap.put("弱阳+-", "1");
        resultDiscreteMap.put("弱阳", "1");
        resultDiscreteMap.put("+-", "1");
        resultDiscreteMap.put("+/-", "1");
        resultDiscreteMap.put("-+", "1");
        resultDiscreteMap.put("±", "1");
        resultDiscreteMap.put("阳", "2");
        resultDiscreteMap.put("阳性", "2");
        resultDiscreteMap.put("强阳性", "2");
        resultDiscreteMap.put("阳性(", "2");
        resultDiscreteMap.put("阳性+", "2");
        resultDiscreteMap.put("“阳性”", "2");
        resultDiscreteMap.put("检出（+)", "2");
        resultDiscreteMap.put("检出（+）", "2");
        resultDiscreteMap.put("陽性(+)", "2");
        resultDiscreteMap.put("阳性(+）", "2");
        resultDiscreteMap.put("+", "2");
        resultDiscreteMap.put("++", "2");
        resultDiscreteMap.put("+++", "2");
        resultDiscreteMap.put("++++", "2");
        resultDiscreteMap.put("+++++", "2");
        resultDiscreteMap.put("＋", "2");
        resultDiscreteMap.put("＋＋", "2");
        resultDiscreteMap.put("＋＋＋", "2");
        resultDiscreteMap.put("＋＋＋＋", "2");
        resultDiscreteMap.put("＋＋＋＋＋", "2");
        resultDiscreteMap.put("阳性(+)", "2");
        resultDiscreteMap.put("阳性（+）", "2");
        resultDiscreteMap.put("异常", "2");
        resultDiscreteMap.put("阳性(3+)", "2");
        resultDiscreteMap.put("强阳性(3+)", "2");
        resultDiscreteMap.put("阳性(2+)", "2");
        resultDiscreteMap.put("阳性(4+)", "2");
        resultDiscreteMap.put("未见", "0");
        resultDiscreteMap.put("未检出", "0");
        resultDiscreteMap.put("未见异常", "0");
        resultDiscreteMap.put("1+", "2");
        resultDiscreteMap.put("2+", "2");
        resultDiscreteMap.put("3+", "2");
        resultDiscreteMap.put("4+", "2");
        resultDiscreteMap.put("5+", "2");
        resultDiscreteMap.put("+1", "2");
        resultDiscreteMap.put("+2", "2");
        resultDiscreteMap.put("+3", "2");
        resultDiscreteMap.put("+4", "2");
        resultDiscreteMap.put("+5", "2");
        resultDiscreteMap.put("微浑", "2");
        resultDiscreteMap.put("浑浊", "2");
        resultDiscreteMap.put("明显浑浊", "2");
        resultDiscreteMap.put("清晰", "0");
        resultDiscreteMap.put("透明", "0");
    }

    @Override
    public StandardTestRecordList dataProcess(BasTestItemTemp itemTemp, StandardTestRecordList o, CommonData commonData) {
        StandardTestRecordList recordList = new StandardTestRecordList();
        List<StandardTestRecord> listRecord = new ArrayList<>();
        if (Objects.isNull(itemTemp)) {
            LOG.error("检验项结果不能为空");
            return recordList;
        }
        String itemName = itemTemp.getItemName();
        StandardTestRecord testRecord = new StandardTestRecord();
        String resultValue = TextUtils.textTrim(itemTemp.getResultValue());

        String name = StrUtil.isNotBlank(itemTemp.getItemName()) ? itemTemp.getItemName().trim() : "";
        itemTemp.setResultValue(resultValue);
        itemTemp.setItemName(name);

        testRecord.setItemName(itemTemp.getItemName());
        testRecord.setItemResults(itemTemp.getResultValue());
        testRecord.setNormalL(itemTemp.getReference());
        testRecord.setItemUnit(itemTemp.getUnit());
        testRecord.setCleanTime(new Date());
        testRecord.setVid(itemTemp.getVid());
        testRecord.setClassName(itemTemp.getClassName());
        testRecord.setCleanStatus(1);
        testRecord.setOrgId(EtlSTConst.orgId);
        testRecord.setStoreId(EtlSTConst.storeId);
        testRecord.setReportTime(itemTemp.getReportTime());
        String normalL = itemTemp.getReference();
        String normalH = "";
        try {
            if (StringUtils.isBlank(itemName)) {
                testRecord.setRemark("清洗失败，itemName为空");
                testRecord.setCleanStatus(EtlStatus.ETL_ERROR.getCode());
                listRecord.add(testRecord);
                recordList.setList(listRecord);
                return recordList;
            }
            // TODO: 2023/3/1
            if (StringUtils.isNotBlank(resultValue) && resultValue.contains(EtlConst.BAO_GAO)) {
                testRecord.setRemark(EtlStatus.ETL_DATA_ERROR.getMessage());
                testRecord.setCleanStatus(EtlStatus.ETL_DATA_ERROR.getCode());
                testRecord.setResultsDiscrete("0");
                listRecord.add(testRecord);
                recordList.setList(listRecord);
                return recordList;
            }
            //匹配规则统一小写+英文括号
            Map<String, StandardBasTestItem> basTestItemMap = commonData.getBasTestItemMap();
            Map<String, StandardBasTestItem> basTestMethodItemMap = commonData.getBasTestMethodItemMap();
            String itemFt = "";
            if (StrUtil.isNotBlank(itemTemp.getClassName())) {
                itemFt = itemTemp.getClassName()
                        .replace("（", "(")
                        .replace("）", ")").toLowerCase().trim();
            }
            if (StrUtil.isNotBlank(itemName)) {
                itemName = itemName.replace("（", "(")
                        .replace("）", ")")
                        .toLowerCase().trim();
            }
            String item = itemFt + ":" + itemName;
            StandardBasTestItem basTestItem = basTestMethodItemMap.get(item);
            if (Objects.isNull(basTestItem)) {
                basTestItem = basTestItemMap.get(itemName);
            }
            if (Objects.isNull(basTestItem)) {
                //字典匹配失败
                testRecord.setRemark(EtlStatus.ETL_ERROR_MATCH.getMessage());
                testRecord.setCleanStatus(EtlStatus.ETL_ERROR_MATCH.getCode());
                listRecord.add(testRecord);
                recordList.setList(listRecord);
                return recordList;
            }
            //进行标准化清洗
            testRecord.setItemNameComn(basTestItem.getItemNameCStandard());
            String unitComm = commonData.getBasTestUnitMap().get(itemTemp.getUnit());
            testRecord.setUnitComm(StrUtil.isNotBlank(unitComm)?unitComm:itemTemp.getUnit());
            String results = itemTemp.getResultValue();

            //优化上下限也移除去掉特殊符号
            if (StringUtils.isNotBlank(normalH)) {
                normalH = normalH.replace("[", "").replace("]", "").trim();
                normalH = normalH.replace("～", "-").trim();
                normalH = normalH.replace("--", "-").trim();
                normalH = normalH.replace("~", "-").trim();
                normalH = normalH.replace("度", "");
                //把上下限的包含单位过滤掉
                if (StringUtils.isNotBlank(itemTemp.getUnit()) && normalH.contains(itemTemp.getUnit())) {
                    normalH = normalH.replace(itemTemp.getUnit(), "");
                }
            }
            if (StringUtils.isNotBlank(normalL)) {
                normalL = normalL.replace("[", "").replace("]", "").trim();
                normalL = normalL.replace("～", "-").trim();
                normalL = normalL.replace("--", "-").trim();
                normalL = normalL.replace("~", "-").trim();
                normalL = normalL.replace("度", "");
                //把上下限的包含单位过滤掉
                if (StringUtils.isNotBlank(itemTemp.getUnit()) && normalL.contains(itemTemp.getUnit())) {
                    normalL = normalL.replace(itemTemp.getUnit(), "");
                }
            }

            if (StringUtils.isNotBlank(normalL) && normalL.contains(">") && StringUtils.isNotBlank(normalH) && (normalH.contains("<") || normalH.contains("＜"))) {
                normalL = normalL.replace(">", "");
                normalH = normalH.replace("<", "").replace("＜", "");
            } else {
                if (StringUtils.isNotBlank(normalL) && (normalL.contains("<") || normalL.contains("《") || normalL.contains("<=") || normalL.contains("≤") || normalL.contains("《=") || normalL.contains("＜"))) {
                    if (StringUtils.isNotBlank(normalH)) {
                        normalH = normalH.replace("<", "")
                                .replace("＜", "")
                                .replace("《", "")
                                .replace("≤", "")
                                .replace("<=", "")
                                .replace("《=", "");
                    } else {
                        normalH = normalL.replace("<", "")
                                .replace("＜", "")
                                .replace("《", "")
                                .replace("≤", "")
                                .replace("<=", "")
                                .replace("《=", "");
                    }
                    normalL = "";
                } else if (StringUtils.isNotBlank(normalL) && (normalL.contains(">") || normalL.contains("》") || normalL.contains("≥") || normalL.contains(">=") || normalL.contains("》=") || normalL.contains("＞"))) {
                    normalL = normalL.replace(">", "")
                            .replace("》", "")
                            .replace("≥", "")
                            .replace(">=", "")
                            .replace("＞", "")
                            .replace("》=", "");
                    normalH = "";
                }
                if (StringUtils.isNotBlank(normalH) && (normalH.contains("<") || normalH.contains("《") || normalH.contains("<=") || normalH.contains("≤") || normalH.contains("《=") || normalH.contains("＜"))) {
                    normalL = "";
                    normalH = normalH.replace("<", "")
                            .replace("＜", "")
                            .replace("《", "")
                            .replace("≤", "")
                            .replace("<=", "")
                            .replace("《=", "");

                } else if (StringUtils.isNotBlank(normalH) && (normalH.contains(">") || normalH.contains("》") || normalH.contains("≥") || normalH.contains(">=") || normalH.contains("》="))) {
                    if (StringUtils.isNotBlank(normalL)) {
                        normalL = normalL.replace(">", "")
                                .replace("》", "")
                                .replace("≥", "")
                                .replace(">=", "")
                                .replace("》=", "");
                    } else {
                        normalL = normalH.replace(">", "")
                                .replace("》", "")
                                .replace("≥", "")
                                .replace(">=", "")
                                .replace("》=", "");
                    }
                    normalH = "";
                }
                //处理c14
                if (results != null && (results.contains("DPM=") || results.contains("DOB=")) && (results.contains("(") || results.contains("（"))) {
                    String result = results;
                    String[] strings = result.split("[(（]");
                    if (strings[0].contains("=")) {
                        String[] s2 = strings[0].split("=");
                        if (s2.length == 2 && NumberUtil.isNumber(s2[1])) {
                            results = s2[1];
                        }
                        if (StrUtil.isNotBlank(results) && StrUtil.isNotBlank(strings[1])) {
                            String reference = strings[1].replace("(", "").replace("（", "").replace(")", "").replace("）", "");
                            if (reference.contains("<") || reference.contains(">")) {
                                String[] split = reference.split("[<>]");
                                if (split.length == 2 && NumberUtil.isNumber(split[0]) && NumberUtil.isNumber(split[1])) {
                                    normalL = split[0];
                                    normalH = split[1];
                                } else if (split.length == 2 && reference.contains("<") && reference.contains("正常")) {
                                    normalH = split[1];
                                    normalL = "";
                                } else if (split.length == 2 && reference.contains(">") && reference.contains("正常")) {
                                    normalL = split[1];
                                    normalH = "";
                                }
                            }
                        }
                    }
                }
            }
            if (StringUtils.isNotBlank(normalH) && (normalH.contains("<") || normalH.contains("＜") || normalH.contains(">"))) {
                normalH = normalH.replace("<", "").replace("＜", "").replace(">", "");
            }
            if (StringUtils.isNotBlank(normalL) && normalL.contains("-")
                    && normalL.split("-").length > 1) {
                String[] str2 = normalL.split("-");
                normalL = str2[0];
                normalH = str2[1];
            }
            if (StringUtils.isNotBlank(normalH) && normalH.contains("-")
                    && normalH.split("-").length > 1) {
                String[] str2 = normalH.split("-");
                normalL = str2[0];
                normalH = str2[1];
            }
            //保存清洗后上下限值
            //优化上下限也移除去掉特殊符号
            if (StringUtils.isNotBlank(normalH)) {
                normalH = normalH.replace(">", "")
                        .replace("》", "")
                        .replace("≥", "")
                        .replace(">=", "")
                        .replace("》=", "")
                        .replace("<", "")
                        .replace("＜", "")
                        .replace("《", "")
                        .replace("≤", "")
                        .replace("<=", "")
                        .replace("《=", "")
                        .replace("=", "")
                        .replace("＝", "")
                        .trim();
            }
            if (StringUtils.isNotBlank(normalL)) {
                normalL = normalL.replace(">", "")
                        .replace("》", "")
                        .replace("≥", "")
                        .replace(">=", "")
                        .replace("》=", "")
                        .replace("<", "")
                        .replace("＜", "")
                        .replace("《", "")
                        .replace("≤", "")
                        .replace("<=", "")
                        .replace("《=", "")
                        .replace("=", "")
                        .replace("＝", "")
                        .trim();
            }
            //特殊处理
            if ("白带清洁度".equals(itemName)) {
                if (StrUtil.isBlank(normalL) || StrUtil.isBlank(normalH)) {
                    //针对白带清洁度，赋默认上下限
                    normalL = "Ⅰ";
                    normalH = "Ⅱ";
                }
            }
            //去除括号
            results = results.replaceAll(EtlConst.KH_REGX, "");
            if (StrUtil.isNotBlank(results) && (results.contains("小于") || results.contains("<") || results.contains(">=") || results.contains("<=") ||
                    results.contains(">") || results.contains("大于") || results.contains("＜") || results.contains("《") ||
                    results.contains("〈") || results.contains("＞") || results.contains("》") || results.contains("〉") ||
                    results.contains("﹤") || results.contains("﹥"))) {
                results = results.replaceAll("小于", "")
                        .replaceAll("=", "")
                        .replaceAll("＝", "")
                        .replaceAll("<", "")
                        .replaceAll(">", "")
                        .replaceAll("＜", "")
                        .replaceAll("《", "")
                        .replaceAll("〈", "")
                        .replaceAll("＞", "")
                        .replaceAll("》", "")
                        .replaceAll("〉", "")
                        .replaceAll("﹤", "")
                        .replaceAll("﹥", "")
                        .replaceAll("大于", "");
            } else if (StrUtil.isNotBlank(results) && ((results.contains("+") || results.contains("-")) && results.contains("."))) {
                results = results.replaceAll("\\.+\\d*", "");

            } else if (StrUtil.isNotBlank(results) && (results.contains("↑") || results.contains("↓"))) {
                results = results.replace("↑", "");
                results = results.replace("↓", "");
            } else if (StrUtil.isNotBlank(results) && (StrUtil.isNotBlank(itemTemp.getUnit()) && results.contains(itemTemp.getUnit()))) {
                results = results.replace(itemTemp.getUnit(), "");
            } else if (StrUtil.isNotBlank(results)) {
                results = results
                        .replace("(此结果已复核确认)", "")
                        .replace("（此结果已复核确认）", "")
                        .replace("此结果已复核确认", "")
                        .replace("(已复核)", "")
                        .replace("(已复查)", "")
                        .replace("（已复核）", "")
                        .replace("（已复查）", "")
                        .replace("已复核", "")
                        .replace("已复查", "")
                        .trim();

                //清理优健康数值结果中夹带的中文V1.4.6.1
                if (EtlConst.itemNames.contains(testRecord.getItemNameComn()) && !NumberUtil.isNumber(results)) {
                    for (String s : EtlConst.itemNames.split(EtlConst.SPLIT_DOUHAO)) {
                        if (s.equals(testRecord.getItemNameComn())) {
                            String numStr = ReUtil.getGroup0(EtlConst.regx, results);
                            if (StrUtil.isNotBlank(numStr)) {
                                results = numStr.trim();
                            }
                            break;
                        }
                    }
                }
            }
            itemTemp.setResultValue(results);
            testRecord.setItemResults(results);
            testRecord.setNormalL(normalL);
            testRecord.setNormalH(normalH);
            resultsDiscreteProcess(testRecord);
            testRecord.setSwitchResult(testRecord.getItemResults());
            testRecord.setSwitchUnit(testRecord.getUnitComm());
            listRecord.add(testRecord);
            recordList.setList(listRecord);
        } catch (Exception e) {
            LOG.error("清洗检验异常，源数据入参[{}],清洗异常[{}]", JSON.toJSONString(itemTemp), e.getMessage(), e);
        }
        return recordList;
    }

    public void resultsDiscreteProcess(StandardTestRecord str) {
        str.setCleanStatus(EtlStatus.ETL_SUCCESS.getCode());
        str.setRemark(EtlStatus.ETL_SUCCESS.getMessage());
        if (NumberUtil.isNumber(str.getItemResults()) && NumberUtil.isNumber(str.getNormalL())
                && NumberUtil.isNumber(str.getNormalH())) {
            double value = Double.parseDouble(str.getItemResults());
            double normalL = Double.parseDouble(str.getNormalL());
            double normalH = Double.parseDouble(str.getNormalH());
            if (value > normalH) {
                str.setResultsDiscrete("2"); // 高
            } else if (value < normalL) {
                str.setResultsDiscrete("-2"); // 低
            } else if (value <= normalH && value >= normalH * 0.9) {
                str.setResultsDiscrete("1"); // 临界高
            } else if (value >= normalL && value <= normalL * 1.1) {
                str.setResultsDiscrete("-1"); // 临界低
            } else {
                str.setResultsDiscrete("0"); // 正常
            }
        } else if (NumberUtil.isNumber(str.getItemResults()) && (NumberUtil.isNumber(str.getNormalL())
                || NumberUtil.isNumber(str.getNormalH()))) {
            double value = Double.valueOf(str.getItemResults()).doubleValue();
            if (NumberUtil.isNumber(str.getNormalL())) {
                double normalL = Double.valueOf(str.getNormalL()).doubleValue();
                if (value > normalL) {
                    str.setResultsDiscrete(String.valueOf(0)); //正常
                } else if (value < normalL) {
                    str.setResultsDiscrete(String.valueOf(-2)); // 低
                } else {
                    str.setResultsDiscrete(String.valueOf(0)); // 正常
                }
            } else if (NumberUtil.isNumber(str.getNormalH())) {
                double normalH = Double.valueOf(str.getNormalH()).doubleValue();
                if (value < normalH) {
                    str.setResultsDiscrete(String.valueOf(0)); //正常
                } else if (value > normalH) {
                    str.setResultsDiscrete(String.valueOf(2)); // 高
                } else {
                    str.setResultsDiscrete(String.valueOf(0)); // 正常
                }
            }
        } else if (StringUtils.isNotBlank(str.getNormalL()) && NumberUtil.isNumber(str.getItemResults())
                && (str.getNormalL().contains("大于") || str.getNormalL().contains("小于"))) {
            double result = Double.valueOf(str.getItemResults()).doubleValue();
            String normalL = str.getNormalL().replace("大于", "").replace("小于", "").trim();
            if (NumberUtil.isNumber(normalL)) {
                str.setNormalL(normalL);
                double level = Double.valueOf(normalL).doubleValue();
                if (result > level) {
                    str.setResultsDiscrete("2"); //正常
                } else if (result < level) {
                    str.setResultsDiscrete("-2"); // 低
                } else {
                    str.setResultsDiscrete("0"); // 正常
                }
            }
        } else if (!NumberUtil.isNumber(str.getItemResults())) {
            if (str.getItemResults() == null && (str.getNormalL() == null || str.getNormalH() == null)) {
                str.setResultsDiscrete("0");
            } else {
                if (str.getNormalL() != null && str.getNormalH() != null && str.getItemResults() != null &&
                        NumberUtil.isNumber(str.getItemResults().replace("<", "").replace("＜", "").replace("《", "").trim())
                        && NumberUtil.isNumber(str.getNormalL()) && NumberUtil.isNumber(str.getNormalH())) {
                    str.setItemResults(str.getItemResults().replace("<", "").replace("＜", "").replace("《", "").trim());
                    str.setResultsDiscrete("0");
                } else if (str.getNormalL() != null && str.getNormalH() != null && str.getItemResults() != null && str.getItemResults().split("-").length > 1
                        && NumberUtil.isNumber(str.getNormalL()) && NumberUtil.isNumber(str.getNormalH())) {
                    str.setResultsDiscrete("0");
                    if (NumberUtil.isNumber(str.getItemResults().split("-")[0].trim()) && NumberUtil.isNumber(str.getItemResults().split("-")[1].trim())) {
                        int l = Integer.valueOf(str.getItemResults().split("-")[0].trim());
                        int h = Integer.valueOf(str.getItemResults().split("-")[1].trim());
                        if (((h - l) / 2 + l) > Double.valueOf(str.getNormalH()).doubleValue()) {
                            str.setItemResults(String.valueOf((h - l) / 2 + l));
                            str.setResultsDiscrete("2");
                        }
                        if (h < Double.valueOf(str.getNormalL()).doubleValue()) {
                            str.setItemResults(String.valueOf(h));
                            str.setResultsDiscrete("-2");
                        }
                    }
                } else if (NumberUtil.isNumber(replaceValue(str.getItemResults())) && NumberUtil.isNumber(replaceValue(str.getNormalL())) && NumberUtil.isNumber(replaceValue(str.getNormalH()))) {
                    double value = Double.valueOf(replaceValue(str.getItemResults())).doubleValue();
                    double normalL = Double.valueOf(replaceValue(str.getNormalL())).doubleValue();
                    double normalH = Double.valueOf(replaceValue(str.getNormalH())).doubleValue();
                    if (value < normalL) {
                        str.setResultsDiscrete("-2"); //低
                    } else if (value > normalH) {
                        str.setResultsDiscrete("2"); // 高
                    } else {
                        str.setResultsDiscrete("0"); // 正常
                    }
                    str.setItemResults(String.valueOf(value));
                } else {
                    if (resultDiscreteMap.get(str.getItemResults()) != null) {
                        str.setResultsDiscrete(resultDiscreteMap.get(str.getItemResults()));
                    } else {
                        if (str.getItemResults() != null && (str.getItemResults().equals(str.getNormalL()) || str.getItemResults().equals(str.getNormalH()))) {
                            str.setResultsDiscrete("0");
                        } else if (str.getNormalL() != null && str.getNormalH() != null && str.getItemResults() != null && NumberUtil.isNumber(str.getItemResults().replace(">", "").replace("》", "").trim())
                                && NumberUtil.isNumber(str.getNormalL()) && NumberUtil.isNumber(str.getNormalH()) && Double.valueOf(str.getNormalH()).doubleValue() < Double.valueOf(str.getItemResults().replace(">", "").replace("》", "").trim()).doubleValue()) {
                            str.setItemResults(str.getItemResults().replace(">", "").replace("》", "").trim());
                            str.setResultsDiscrete("2");
                        } else if (str.getItemResults() != null && (str.getItemResults().contains("阴") || (str.getItemResults().contains("阳")))) {
                            if (str.getItemResults().contains("阴")) {
                                str.setResultsDiscrete("0");
                            } else if (str.getItemResults().contains("阳")) {
                                str.setResultsDiscrete("2");
                            }
                        } else {
                            str.setItemResults(str.getItemResults().replace(">", "")
                                    .replace("》", "")
                                    .replace("≥", "")
                                    .replace(">=", "")
                                    .replace("》=", "")
                                    .replace("<", "")
                                    .replace("＜", "")
                                    .replace("《", "")
                                    .replace("≤", "")
                                    .replace("<=", "")
                                    .replace("《=", "")
                                    .replace("=", "")
                                    .trim());
                            //最后判断替换后的值是否数字
                            if (NumberUtil.isNumber(str.getItemResults()) && NumberUtil.isNumber(str.getNormalL()) && NumberUtil.isNumber(str.getNormalH())) {
                                double value = Double.valueOf(str.getItemResults()).doubleValue();
                                double normalL = Double.valueOf(str.getNormalL()).doubleValue();
                                double normalH = Double.valueOf(str.getNormalH()).doubleValue();
                                if (value >= normalH) {
                                    str.setResultsDiscrete("2"); // 高
                                } else if (value <= normalL) {
                                    str.setResultsDiscrete("-2"); // 低
                                } else {
                                    str.setResultsDiscrete("0"); // 正常
                                }
                            } else {
                                str.setResultsDiscrete("0");
                            }
                        }
                    }
                }
            }
        } else {
            str.setCleanStatus(EtlStatus.ETL_MISSING.getCode());
            str.setRemark(EtlStatus.ETL_MISSING.getMessage());
        }
    }

    //罗马数字转换
    public String replaceValue(String result) {
        if(result==null) {
            return "";
        }else if (NumberUtil.isNumber(result)) {
            return result;
        }else if(result.equals("Ⅰ") || result.equals("Ⅰ°") || result.equals("I") || result.equals("I°")) {
            return "1";
        }else if(result.equals("Ⅱ")|| result.equals("Ⅱ°")|| result.equals("II") || result.equals("II°")) {
            return "2";
        }else if(result.equals("Ⅲ")|| result.equals("Ⅲ°")|| result.equals("III") || result.equals("III°")) {
            return "3";
        }else if(result.equals("Ⅳ") || result.equals("Ⅳ°") || result.equals("IV") || result.equals("IV°")) {
            return "4";
        }else if(result.equals("Ⅴ") || result.equals("Ⅴ°") || result.equals("V") || result.equals("V°")) {
            return "5";
        }else if(result.equals("Ⅵ") || result.equals("Ⅵ°") || result.equals("VI") || result.equals("VI°")) {
            return "6";
        }else if(result.equals("Ⅶ") || result.equals("Ⅶ°") || result.equals("VII") || result.equals("VII°")) {
            return "7";
        }else if(result.equals("Ⅷ") || result.equals("Ⅷ°") || result.equals("VIII") || result.equals("VIII°")) {
            return "8";
        }else if(result.equals("Ⅸ") || result.equals("Ⅸ°") || result.equals("IX") || result.equals("IX°")) {
            return "9";
        }else {
            return "";
        }
    }
}


