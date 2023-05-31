package com.sinohealth.datax.processors;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import com.sinohealth.datax.common.CommonData;
import com.sinohealth.datax.common.Processor;
import com.sinohealth.datax.entity.source.CheckFeiResult;
import com.sinohealth.datax.entity.source.CheckResultMsS;
import com.sinohealth.datax.entity.source.StandardCheckFeiResult;
import com.sinohealth.datax.entity.zktarget.CheckResultMsEtl;
import com.sinohealth.datax.entity.zktarget.CheckResultMsEtlList;
import com.sinohealth.datax.entity.zktarget.StandardCheckFeiResultList;
import com.sinohealth.datax.utils.TextTrimUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Title:
 * @Package com.sinohealth.datax.processors
 * @Copyright: Copyright (C) 2022 SinoHealth Co. Ltd, All Rights Reserved
 * @Author heqiang 2022/5/18 18:04
 * @Description: 影像学描述清洗过程
 */
public class CheckResultMsEtlProcessor implements Processor<CheckFeiResult, StandardCheckFeiResultList> {

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
    public static final String regx2 = "([1-9]\\d*\\.?\\d*[cm]m)|(0\\.\\d*[1-9][cm]m)";

    /**
     * 提取4x10mm,10mm,4mmx10mm
     */
    public static String regx3 = "((([1-9]\\d*\\.?\\d*)|(0\\.\\d*[1-9]))[×Xx*])?(([1-9]\\d*\\.?\\d*)|(0\\.\\d*[1-9]))[cm]m";

    public static final String regxKey = "未见.*?[。,，；;:：]";

    public static String regexFeiF = "[。，；,;]边界清[。，；,;]";

    private static String regexGl = "(([1-9]\\d*\\.?\\d*)|(0\\.\\d*[1-9]))[cm]m(([1-9]\\d*\\.?\\d*)|(0\\.\\d*[1-9]))[cm]m";
    /**
     * 排除无效关键字后面的内容
     */
    public static String feiNotValid = "附见";

    public static String FeiNormal = "未见,无";

    public final static String Str_Split = "[,，、]";

    public final static String FeiNotValidStart = "[，。；：](.(?![，。；：]))*?";

    public final static String FeiNotValidEnd = ".*?[。；，：]";

    public static String FeiParam = "斜裂,水平裂,上叶,中叶,下叶,叶间裂,舌段,尖段,后段,前段,上段,外侧段,内侧段,背段,内侧底段,内基底段,外基底段,前基底段,后基底段,外侧底段,前底段,内前底段,后底段,尖后段,外S侧底段,外s侧底段";

    private String feiJJParam = "毛玻璃,磨玻璃,团块,结节,占位,肿块,粒状阴影,结片灶";

    private String feiJJParamAndZhijing = "高密度影,密度增高影,密度影,致密影";

    private String medicalParts = "";

    @Override
    public StandardCheckFeiResultList dataProcess(CheckFeiResult checkFeiResult, StandardCheckFeiResultList listResult, CommonData commonData) {
        List<StandardCheckFeiResult> list = new ArrayList<>();
        String result = checkFeiResult.getResult();
        result = result.replaceFirst(feiNotValid + ".*", "");
        result = result.replace("(", "")
                .replace(")", "")
                .replace("（", "")
                .replace("）", "");
        result = result.toLowerCase()
                .replace(",", "，")
                .replace(";", "；")
                .replace(":", "：");
        //命中的语句
        List<Map<String, String>> hits2 = new ArrayList<>();
        /**
         * regexGl
         */
        List<String> listStr = ReUtil.findAll(regexGl, result, 0);
        String replaceRR = "@@RR";
        HashMap<String, String> hashMap = new HashMap<>();
        for (int i = 0; i < listStr.size(); i++) {
            String glStr = listStr.get(i);
            String stri = replaceRR + i;
            hashMap.put(stri, glStr);
            result = result.replace(glStr, stri);
        }
        result = result.replaceAll("薄壁", "。")
                .replaceAll("mm3", "")
                .replaceAll("mm2", "")
                .replaceAll("mm²", "")
                .replaceAll("mm³", "")
                .replaceAll("cm3", "")
                .replaceAll("cm2", "")
                .replaceAll("cm²", "")
                .replaceAll("cm³", "")
                .replaceAll("，mm", "mm")
                .replaceAll("，cm", "cm")
                .replaceAll("㎜", "mm");
        Iterator<Map.Entry<String, String>> iterator = hashMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();
            String key = entry.getKey();
            String value = entry.getValue();
            result = result.replace(key, value);
        }
        result = result.replaceAll(regexFeiF, "。");
        //过滤掉
        String[] splitStrings = TextTrimUtils.splitSignsToArrByParam(result, ";；。");
        for (String str1 : splitStrings) {

            /**
             * 甲状腺前置处理
             * 甲状腺部位有效语句
             */
            /*String tempResult = "";
            Integer tempSize = result.length();
            String parts = MedicalParts();
            for (String s : parts.split(Health100ETLContants.Str_Split)) {
                String regxJzxVaild = "甲状腺.*?"+s;
                List<String> listVaild = ReUtil.findAll(regxJzxVaild, result, 0);
                for (String s1 : listVaild) {
                    if(s1.length() <= tempSize){
                        tempSize = s1.length();
                        tempResult = s1;
                    }
                }
            }
            if(StrUtil.isNotBlank(tempResult)){
                result = tempResult;
            }*/
            /**
             * 判断过滤掉无效语句，未见异常
             */
            if (!str1.startsWith("，")) {
                str1 = "，" + str1;
            }
            if (!str1.endsWith("，")) {
                str1 = str1 + "，";
            }
            for (String s : FeiNormal.split(Str_Split)) {
                String regxFei = FeiNotValidStart + s + FeiNotValidEnd;
                str1 = str1.replaceAll(regxFei, "");
            }
            /**
             * 判断是否肺的有效语句
             */
            Boolean flag = false;
            if (str1.contains("肺") || str1.contains("气管")) {
                flag = true;
            }
            if (!flag) {
                String[] strings = FeiParam.split(",");
                for (String s : strings) {
                    if (str1.contains(s)) {
                        flag = true;
                        break;
                    }
                }
            }
            if (flag) {
                boolean jjFlag = false;
                boolean jjFlagZhijing = false;
                for (String s : feiJJParam.split(Str_Split)) {
                    if (str1.contains(s)) {
                        jjFlag = true;
                        break;
                    }
                }
                if (!jjFlag) {
                    for (String s : feiJJParamAndZhijing.split(Str_Split)) {
                        if (str1.contains(s)) {
                            jjFlagZhijing = true;
                            break;
                        }
                    }
                }
                boolean flag7 = ReUtil.contains(regx, str1) && (str1.contains("cm") || str1.contains("mm"));
                if (jjFlag || jjFlagZhijing || flag7) {
                    if (jjFlag) {
                        Map<String, String> map = new HashMap<>();
                        map.put("str", str1);
                        if (flag7) {
                            map.put("zhijing", "true");
                        } else {
                            map.put("zhijing", "false");
                        }
                        hits2.add(map);
                    } else if (jjFlagZhijing && flag7) {
                        Map<String, String> map = new HashMap<>();
                        map.put("str", str1);
                        map.put("zhijing", "true");
                        hits2.add(map);
                    }
                }
            }
        }
        StandardCheckFeiResult pcr3 = new StandardCheckFeiResult();
        pcr3.setClassName(checkFeiResult.getClassName());
        pcr3.setMemberId(checkFeiResult.getMemberId());
        pcr3.setResult(checkFeiResult.getResult());
        if (!hits2.isEmpty()) {
            buildResultByItemNameCommA(pcr3, itemNameCommB, "1");
        }

        //按优先级取出最高的那句话：（结节 = 磨玻璃 = 毛玻璃 = 团块) > 高密度影&&目标语句有结节直径 > 直径大小
        Map<String, List<Map<String, String>>> zhijingMap = hits2.stream().collect(Collectors.groupingBy(x -> x.get("zhijing")));
        List<Map<String, String>> zhijingtrue = zhijingMap.get("true");
        if (zhijingtrue != null && !zhijingtrue.isEmpty()) {
            //目标语句有结节直径的语句
            List<String> hits5 = zhijingtrue.stream().map(y -> y.get("str")).collect(Collectors.toList());
            //添加最大结节直径
            double zhijingMM = zhijingOrderMM(hits5);
            if (zhijingMM > 0) {
                buildResultByItemNameCommA(pcr3, itemNameCommC, String.valueOf(zhijingMM));
            }
        }
        list.add(pcr3);
        listResult.setList(list);
        return listResult;
    }

    public StandardCheckFeiResult buildResultByItemNameCommA(StandardCheckFeiResult pcr3, String itemnameComm, String result) {
        if (itemnameComm.equals(itemNameCommC)) {
            pcr3.setZhijing(result);
        } else {
            pcr3.setFeiStatus(result);
        }
        return pcr3;
    }

    public static double zhijingOrderMM(List<String> hits) {
        HashMap<String, String> hashMap = new HashMap<>();
        double zhijing = 0;
        List<String> listS = new ArrayList<>();
        for (String hit : hits) {
            String[] tempResultList = hit.split("[,，、]");
            for (String s : tempResultList) {
                if (s.contains("cm") || s.contains("mm")) {
                    for (String s1 : ReUtil.findAll(regx3, s, 0)) {
                        List<String> numList = ReUtil.findAll(regx, s1, 0);
                        listS.addAll(numList);
                        for (String s2 : numList) {
                            hashMap.put(s2, s1);
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
                    zhijing = tempValue.multiply(new BigDecimal(10)).doubleValue();
                } else if (hitStr.contains("mm")) {
                    zhijing = tempValue.doubleValue();
                }
            }
        }
        return zhijing;
    }
}
