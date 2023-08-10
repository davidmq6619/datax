package com.sinohealth.datax.utils;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ReUtil;
import com.sinohealth.datax.entity.source.CheckResultMsS;
import com.sinohealth.datax.entity.source.RegCheck;
import com.sinohealth.datax.entity.source.StandardCheckRecord;
import com.sinohealth.datax.entity.source.StandardMnCheckRecord;
import com.sinohealth.datax.entity.zktarget.CheckResultMsEtl;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author mingqiang
 * @date 2022/8/30 - 15:14
 * @desc
 */
public class EtlFeiUtils {
    /**
     * 肺CT的检测方式别名
     */
    public static final List<String> feictMethod = Arrays.asList("CT胸部检查",
            "CT胸部",
            "CT检查(胸部)-不出片",
            "低剂量胸部螺旋CT扫描",
            "C（胸部）",
            "低剂量胸部螺旋CT扫描(哺乳,备孕,孕者禁)",
            "低剂量胸部CT扫描",
            "CT检查(胸部)",
            "胸片升级肺部CT（不出片）",
            "胸部CT（送（不含片））",
            "胸部CT(不含片)",
            "低剂量螺旋胸部CT",
            "CT-胸部",
            "胸部CT（64排）",
            "CT-胸部（不出片）",
            "低剂量胸部CT扫描(不含胶片）",
            "胸部CT(体验）",
            "低剂量胸部CT扫描（不出片）",
            "低剂量胸部CT扫描（团）",
            "胸部DR免费升级CT",
            "胸部CT+三维重建",
            "胸部CT（64排）【不含胶片】",
            "胸部CT平扫",
            "螺旋CT胸部平扫",
            "低剂量胸部CT扫描（Z）",
            "CT检查(升级胸部)",
            "低剂量螺旋CT（胸部）",
            "CT（胸部）",
            "胸部CT低剂量筛查+三维重建",
            "胸部螺旋CT平扫(不出片)",
            "胸部螺旋CT断层扫描",
            "胸部CT（低剂量）",
            "胸部CT（不出片）",
            "胸部（肺部）CT",
            "胸部CT（送（不含片）",
            "优惠胸部CT80(不出片)",
            "优惠胸部CT100(出片)",
            "C(胸部)",
            "胸部螺旋CT平扫",
            "胸部CT（16排）",
            "胸部CT",
            "低剂量螺旋CT胸部平扫",
            "胸部CT(团检单价)",
            "胸部CT（团单）",
            "CT肺部扫描（不含片）",
            "CT肺部平扫",
            "CT-肺部（含片）",
            "肺部CT(不含出片)",
            "肺CT",
            "低剂量肺部CT扫描(哺乳,备孕,孕者禁)",
            "CT-肺部(不含片)",
            "CT肺部",
            "CT检查（肺部）不含片",
            "CT检查（肺部）",
            "CT肺部(ZS)",
            "CT检查（肺）",
            "CT检查（肺部）【不含片】",
            "双肺CT平扫",
            "CT肺部（团单）",
            "肺部CT",
            "肺部CT平扫+增强+三维成像",
            "胸部（肺部）CT",
            "肺CT(不出片)",
            "肺部CT平扫",
            "CT(肺部)",
            "胸部低剂量CT",
            "胸部低剂量CT检查");

    public static final List<String> feiMethod = Arrays.asList(
            "结节", "磨玻璃", "毛玻璃", "团块", "高密度影", "肿块", "占位", "肿瘤", "癌", "Ca", "软组织", "灶", "囊肿", "瘤", "肿物", "包块"
    );

    /**
     * 是否做肺CT 0未做 1已做
     */
    public static final String itemNameCommA = "肺部检查";

    /**
     * 是否有肺结节 0否 1是
     */
    public static final String itemNameCommB = "肺结节";

    /**
     * 最大结节直径 统一单位cm
     */
    public static final String itemNameCommC = "肺结节最大直径";
    /**
     * 结节直径提取正则
     */
    public static final String regx = "([1-9]\\d*\\.?\\d*)|(0\\.\\d*[1-9])";

    public static final String regxKey = "未见.*?[。,，；;:：]";

    public static final String regx3 = "((([1-9]\\d*\\.?\\\\d*)|(0\\.\\d*[1-9]))[×Xx*])?(([1-9]\\d*\\.?\\d*)|(0\\.\\d*[1-9]))[cm]m";

    public static void etl(RegCheck checkResultMsS, List<StandardMnCheckRecord> list) {
        String result = checkResultMsS.getResults()
                .replace("(","")
                .replace(")","")
                .replace("（","")
                .replace("）","");
        boolean flagFei = false;
        result = result.toLowerCase();
        //命中的语句
        List<Map<String, String>> hits2 = new ArrayList<>();
        List<String> resultList = ReUtil.findAll(regxKey, result, 0);
        Collections.sort(resultList, Comparator.comparingInt(String::length).reversed());
        for (String s : resultList) {
            String s1 = s.substring(0, s.length() - 1);
            result = result.replaceAll(s1,"");
        }
        result = result.replaceAll("薄壁","。")
                .replaceAll("mm3","")
                .replaceAll("mm2","")
                .replaceAll("mm²","")
                .replaceAll("mm³","")
                .replaceAll("cm3","")
                .replaceAll("cm2","")
                .replaceAll("cm²","")
                .replaceAll("cm³","")
                .replaceAll("，mm","mm")
                .replaceAll("，cm","cm")
                .replaceAll("㎜","mm");
        String[] paramKey = TextTrimUtils.splitSignsToArrByParam(result, ",， ;；。：:");
        boolean flagKey = false;
        for (String s : paramKey) {
            if("边界清".equals(s)){
                flagKey = true;
            }
        }
        if(flagKey){
            result = result.replaceAll("边界清","。");
        }
        String[] splitStrings = TextTrimUtils.splitSignsToArrByParam(result, ";；。");
        for (String str1 : splitStrings) {
            if (str1.contains("肺") || str1.contains("胸膜") || str1.contains("气管")) {
                if (str1.contains("毛玻璃") || str1.contains("磨玻璃") || str1.contains("团块") || str1.contains("结节") || str1.contains("占位") || str1.contains("肿块") || str1.contains("高密度影") || str1.contains("粒状阴影")|| str1.contains("结片灶")) {
                    flagFei = true;
                    boolean flagNormal = false;
                    boolean flag7 = ReUtil.contains(regx, str1) && (str1.contains("cm") || str1.contains("CM") || str1.contains("Cm") || str1.contains("cM") || str1.contains("mM") || str1.contains("MM") || str1.contains("mm") || str1.contains("Mm"));
                    for (String s : EtlConst.NORMAL_LIST) {
                        if (str1.contains(s) && !flag7) {
                            flagNormal = true;
                            break;
                        }
                    }
                    if (flagNormal) {
                        if(!str1.contains("密度影")){
                            continue;
                        }
                    }

                    if (str1.contains("结节状钙化") || str1.contains("钙化结节影")) {
                        if (flag7) {
                            Map<String, String> map = new HashMap<>();
                            map.put("str", str1);
                            map.put("zhijing", "true");
                            hits2.add(map);
                        }
                    } else if ((str1.contains("高密度影") || str1.contains("密度增高影")) && flag7) {
                        Map<String, String> map = new HashMap<>();
                        map.put("str", str1);
                        map.put("zhijing", "true");
                        hits2.add(map);
                    } else if (str1.contains("毛玻璃") || str1.contains("磨玻璃") || str1.contains("团块")
                            || str1.contains("结节") || str1.contains("占位") || str1.contains("肿块") || str1.contains("粒状阴影") || str1.contains("结片灶")) {
                        Map<String, String> map = new HashMap<>();
                        map.put("str", str1);
                        if (flag7) {
                            map.put("zhijing", "true");
                        } else {
                            map.put("zhijing", "false");
                        }
                        hits2.add(map);
                    }
                }
            }
        }
        if (flagFei) {
            if (!hits2.isEmpty()) {
                list.add(buildResultByItemNameCommA(checkResultMsS, itemNameCommB, "1"));
            }
        }
        //按优先级取出最高的那句话：（结节 = 磨玻璃 = 毛玻璃 = 团块) > 高密度影&&目标语句有结节直径 > 直径大小
        Map<String, List<Map<String, String>>> zhijingMap = hits2.stream().collect(Collectors.groupingBy(x -> x.get("zhijing")));
        String finalHit = "";
        List<Map<String, String>> zhijingtrue = zhijingMap.get("true");
        if (zhijingtrue != null && !zhijingtrue.isEmpty()) {
            //目标语句有结节直径的语句
            List<String> hits5 = zhijingtrue.stream().map(y -> y.get("str")).collect(Collectors.toList());
            //添加最大结节直径
            list.add(buildResultByItemNameCommA(checkResultMsS, itemNameCommC, zhijingOrderMM(hits5)));
        }
        //定位上述条件在哪一句中 end
    }

    //构建etl结果
    public static StandardMnCheckRecord buildResultByItemNameCommA(RegCheck checkResultMsS, String itemnameComm, String result) {
        StandardMnCheckRecord etl = new StandardMnCheckRecord();
        etl.setCleanTime(new Date());
        etl.setImageDescribe(checkResultMsS.getResults());
        etl.setVid(checkResultMsS.getVid());
        etl.setItemFt(checkResultMsS.getItemFt());
        etl.setResults(result);
        etl.setItemName(itemnameComm);
        etl.setItemNameComn(itemnameComm);
        etl.setCleanStatus(EtlStatus.ETL_SUCCESS.getCode());
        etl.setResultsDiscrete(2);
        if ("0".equals(result)) {
            etl.setRemark(EtlStatus.ETL_SUCCESS_NORMAL.getMessage());
            etl.setCleanStatus(EtlStatus.ETL_SUCCESS_NORMAL.getCode());
            etl.setResultsDiscrete(0);
        }
        if (itemnameComm.equals(itemNameCommC)) {
            if ( checkResultMsS.getResults() !=null && checkResultMsS.getResults().toLowerCase().contains("mm")) {
                etl.setUnit("mm");
            }
        }
        return etl;
    }

    public static String zhijingOrderMM(List<String> hits) {
        HashMap<String, String> hashMap = new HashMap<>();
        String cur_zhijing = "0";
        List<String> listS = new ArrayList<>();
        for (String hit : hits) {
            String tempResult = hit.toLowerCase();
            String[] tempResultList = tempResult.split("[,，、]");
            for (String s : tempResultList) {
                if (s.contains("cm") || s.contains("mm")) {
                    for (String s1 : ReUtil.findAll(regx3, s, 0)) {
                        List<String> numList = ReUtil.findAll(regx, s1, 0);
                        listS.addAll(numList);
                        for (String s2 : numList) {
                            hashMap.put(s2,s1);
                        }
                    }
                }
            }
        }
        if (!listS.isEmpty()) {
            listS.sort(Comparator.comparing(Double::valueOf));
            String s = listS.get(listS.size() - 1);
            String hitStr = hashMap.get(s);
            if (NumberUtil.isNumber(s)) {
                BigDecimal tempValue = new BigDecimal(s);
                if (hitStr.contains("cm")) {
                    cur_zhijing = tempValue.multiply(new BigDecimal(10)).toString();
                } else if (hitStr.contains("mm")) {
                    cur_zhijing = tempValue.toString();
                }
            }
        }
        return cur_zhijing;
    }
}
