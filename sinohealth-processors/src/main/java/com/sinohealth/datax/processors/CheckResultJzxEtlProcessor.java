package com.sinohealth.datax.processors;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.toolkit.CollectionUtils;
import com.sinohealth.datax.common.CommonData;
import com.sinohealth.datax.common.Health100ETLContants;
import com.sinohealth.datax.common.Processor;
import com.sinohealth.datax.entity.common.KeywordsLevelDto;
import com.sinohealth.datax.entity.source.CheckFeiResult;
import com.sinohealth.datax.entity.source.CheckJzxResult;
import com.sinohealth.datax.entity.source.StandardCheckFeiResult;
import com.sinohealth.datax.entity.source.StandardCheckJzxResult;
import com.sinohealth.datax.entity.zktarget.StandardCheckFeiResultList;
import com.sinohealth.datax.entity.zktarget.StandardCheckJzxResultList;
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
public class CheckResultJzxEtlProcessor implements Processor<CheckJzxResult, StandardCheckJzxResultList> {

    private final static String REGX_LEVEL = "TI[-—]RADS\\d+[A-Z]?[级|类]";
    /**
     * 结节直径提取正则
     */
    public static final String regx = "([1-9]\\d*\\.?\\d*)|(0\\.\\d*[1-9])";

    public static final String jzxReplace = "（:@@,(:@@,）:@@,):@@,..:.,[:@@,]:@@";

    //替换成见字的字符
    public static final String jzxJian = "探及,扫及";

    public static final String jzxNotSplit = "甲状旁腺.*?[。],甲状腺被膜.*?[。],CM/S";

    public static final String jzxNotSplitJian = "CDFI.*?见,内可见,未见,内见";

    /**
     * 部位关键字
     */
    public static final String medicalParts = "眼,耳,鼻,口唇,颊,颚,牙,唾液腺,咽,喉,食管,支气管,气管,脑,颅,骨,椎动脉,颈,甲状旁腺,垂体,松果体,生殖腺,肺,心,纵膈,升主动脉,降主动脉,主动脉弓,冠状动脉,主动脉,脊椎,脊柱,胃,肠,阑尾,肝,胆,脾,胰,肾,输尿管,膀胱,尿道,子宫,附件,卵巢,输卵管,盆腔,会阴,肛,颌下";

    /**
     * 甲状腺含有该字符则不进行断句
     */
    public static final String jzxNoSplit = "数个,多个,多发,数枚,多枚,两个,2个,二个,均可见,均见,各见,双叶内见";

    /**
     * JzxNotValid
     */
    public static final String jzxNotValid = "峡部厚约.*?，@@峡部.*?厚约.*?，@@峡部.*?约.*?，@@峡部.*?大小约.*?，";

    public static final String jzxYesValid = "各见,可见,结节,扫及,探及";

    public static final String jzxFirstJian = ".*?见";

    public static final String jzxRegx = "((([1-9]\\d*\\.?\\d*)|(0\\.\\d*[1-9]))[×Xx*])?((([1-9]\\d*\\.?\\d*)|(0\\.\\d*[1-9]))[×Xx*])?(([1-9]\\d*\\.?\\d*)|(0\\.\\d*[1-9]))[CM]M";

    public static final String alias1 = "低回声结节:低回声,低回声区:低回声,低回声:低回声,弱回声结节:低回声,弱回声:低回声,弱回声区:低回声,高回声结节:高回声,高回声:高回声,高回声区:高回声,稍强回声结节:高回声,强回声结节:高回声,强回声区:高回声,强回声:高回声,等回声:等回声,等回声结节:等回声,等回声区:等回声,呈等回声:等回声,无回声结节:无回声,无回声区:无回声,无回声:无回声,混合回声区:混合回声,混合回声结节:混合回声,无回声:无回声,混合回声:混合回声,混合性回声:混合回声,不均质回声:不均质回声,回声不一:不均质回声,海绵样:不均质回声,低合回声:低回声";
    public static final String alias2 = "囊实:囊实性结节,实性:实性结节,囊性:囊性结节,液暗区:囊性结节,液性暗区:囊性结节";


    @Override
    public StandardCheckJzxResultList dataProcess(CheckJzxResult checkJzxResult, StandardCheckJzxResultList listResult, CommonData commonData) {
        List<StandardCheckJzxResult> listJzx = new ArrayList<>();
        String result = checkJzxResult.getResult();
        String traceData = checkJzxResult.getResult();
        String itemName = checkJzxResult.getItemName();
        String category = checkJzxResult.getCategory();
        String vid = checkJzxResult.getVid();
        for (String s : jzxReplace.split(Health100ETLContants.SPLIT_DOUHAO)) {
            String[] split = s.split(Health100ETLContants.SPLIT_MAOHAO);
            if (split.length == 2) {
                String n1 = split[0];
                String n2 = split[1];
                String s2 = n2.replace(Health100ETLContants.MANY_REGX_SPLIT, "");
                result = result.replace(n1, s2);
            }
        }
        //等级提取
        result = result.toUpperCase();
        /**
         * 甲状腺前置处理有效语句
         */
        String[] arrayNotSplit = jzxNotSplit.split(Health100ETLContants.Str_Split);
        for (String s : arrayNotSplit) {
            result = result.replaceAll(s, "");
        }
        /**
         * 替换成见
         */
        for (String s : jzxJian.split(",")) {
            result = result.replaceAll(s, Health100ETLContants.SPLIT_JIAN);
        }
        /**
         * 甲状腺前置处理
         * 甲状腺部位有效语句
         */
        String tempResult = "";
        Integer tempSize = result.length();
        String parts = medicalParts;
        for (String s : parts.split(Health100ETLContants.Str_Split)) {
            String regxJzxVaild = "甲状腺.*?" + s;
            List<String> listVaild = ReUtil.findAll(regxJzxVaild, result, 0);
            for (String s1 : listVaild) {
                if (s1.length() <= tempSize) {
                    tempSize = s1.length();
                    tempResult = s1;
                }
            }
        }
        if (StrUtil.isNotBlank(tempResult)) {
            result = tempResult;
        }

        /***
         * 断句切割，数个、多个、多发、数枚、多枚、两个、2个、二个、均可见、均见、各见；含有这些字符则不进行断句
         */
        Boolean notSplit = false;
        String[] jzxArr = jzxNoSplit.split(Health100ETLContants.Str_Split);
        for (String s : jzxArr) {
            int indexOf = result.indexOf(s);
            if (indexOf > 0) {
                String notSplitResult = result.substring(0, indexOf);
                String splitResult = result.substring(indexOf);
                String newResult = splitResult.replaceAll("[;；。]", "，");
                result = notSplitResult + newResult;
                notSplit = true;
            }
        }
        /***
         * 排除第一个见之前的数据
         */
        result = result.replaceFirst(jzxFirstJian, "");

        /**
         * 甲状腺前置处理有效语句
         */
        String[] arrayNotSplitJian = jzxNotSplitJian.split(Health100ETLContants.Str_Split);
        for (String s : arrayNotSplitJian) {
            result = result.replaceAll(s, "");
        }
        /***
         * 断句切割掉无效语句
         * 1.第一个见字前面的内容
         * 2.“峡部厚约……，”、“峡部……厚约……，”
         */
        String[] yesValidList = jzxYesValid.split(Health100ETLContants.Str_Split);
        for (String s : jzxNotValid.split(Health100ETLContants.MANY_REGX_SPLIT)) {
            List<String> regexList = ReUtil.findAll(s, result, 0);
            for (String c : regexList) {
                boolean flag = false;
                for (String str : yesValidList) {
                    if (c.contains(str)) {
                        flag = true;
                        break;
                    }
                }
                if (flag) {
                    int indexOf = c.indexOf(Health100ETLContants.SPLIT_JIAN);
                    if (indexOf > 0) {
                        c = c.substring(0, indexOf);
                    }
                }
                result = result.replace(c, "");
            }
        }
        result = result.replaceAll(REGX_LEVEL, "**********");
        List<Map<String, Object>> tempList = new ArrayList<>();
        String[] resultArr;
        if (notSplit) {
            resultArr = result.split("[；。]");
        } else {
            resultArr = result.split("[；。见]");
        }
        for (String s : resultArr) {
            for (KeywordsLevelDto keyword : commonData.keywordsLevelList) {
                for (String str3 : keyword.getKeywords()) {
                    if (s.contains(str3)) {
                        boolean zhijingFlag = false;
                        for (String s1 : ReUtil.findAll(jzxRegx, s, 0)) {
                            List<String> list = ReUtil.findAll(regx, s1, 0);
                            list.sort(Comparator.comparing(Double::valueOf));
                            String zhijing = list.get(list.size() - 1);
                            if (NumberUtil.isNumber(zhijing)) {
                                Double zhijingNum = Double.valueOf(zhijing);
                                if (s1.contains("CM")) {
                                    BigDecimal bigDecimal1 = new BigDecimal(zhijingNum);
                                    BigDecimal bigDecimal2 = new BigDecimal(10);
                                    zhijingNum = bigDecimal1.multiply(bigDecimal2).doubleValue();
                                }
                                Map<String, Object> mapBy = new HashMap<>();
                                mapBy.put(Health100ETLContants.JZX_LEVEL, keyword.getLevel());
                                mapBy.put(Health100ETLContants.JZX_RESULT, s);
                                mapBy.put(Health100ETLContants.JZX_ZHIJING, zhijingNum);
                                tempList.add(mapBy);
                                zhijingFlag = true;
                            }
                        }
                        if (zhijingFlag) {
                            break;
                        }
                    }
                }
            }
        }
        double zhijingNum = 0.0;
        String str = "";
        if (CollectionUtils.isNotEmpty(tempList)) {
            //先判断1类中是否有大于10mm
            Map<Double, String> allMap = new HashMap<>();
            Map<Double, String> allMapLevel = new HashMap<>();
            double tempNum = 0.0;
            double tempNumLevel = 0.0;
            for (Map<String, Object> map : tempList) {
                String level = String.valueOf(map.get(Health100ETLContants.JZX_LEVEL));
                String resultStr = String.valueOf(map.get(Health100ETLContants.JZX_RESULT));
                String zhijingStr = String.valueOf(map.get(Health100ETLContants.JZX_ZHIJING));
                double zhijing = new BigDecimal(zhijingStr).doubleValue();
                if (zhijing > tempNum) {
                    tempNum = zhijing;
                    allMap.clear();
                    allMap.put(tempNum, resultStr);
                }
                if ("1".equals(level)) {
                    if (zhijing > tempNumLevel) {
                        tempNumLevel = zhijing;
                        allMapLevel.clear();
                        allMapLevel.put(tempNumLevel, resultStr);
                    }
                }
            }
            Iterator<Double> iteratorLevel = allMapLevel.keySet().iterator();
            Iterator<Double> iterator = allMap.keySet().iterator();
            boolean flagLevel = false;
            while (iteratorLevel.hasNext()) {
                zhijingNum = iteratorLevel.next();
                if (zhijingNum >= 10) {
                    flagLevel = true;
                    str = allMapLevel.get(zhijingNum);
                }
            }
            if (!flagLevel) {
                while (iterator.hasNext()) {
                    zhijingNum = iterator.next();
                    str = allMap.get(zhijingNum);
                }
            }
        }
        if (StrUtil.isNotBlank(str)) {
            //匹配超声特性
            String str3 = "";
            for (String str2 : alias1.split(Health100ETLContants.SPLIT_DOUHAO)) {
                if (str.contains(str2.split(Health100ETLContants.SPLIT_MAOHAO)[0])) {
                    str3 = str2.split(Health100ETLContants.SPLIT_MAOHAO)[1];
                    break;
                }
            }
            //匹配结节类型
            String str4 = "";
            for (String str2 : alias2.split(Health100ETLContants.SPLIT_DOUHAO)) {
                if (str.contains(str2.split(Health100ETLContants.SPLIT_MAOHAO)[0])) {
                    str4 = str2.split(Health100ETLContants.SPLIT_MAOHAO)[1];
                    break;
                }
            }
            //超声类型
            if (StrUtil.isNotBlank(str3)) {
                StandardCheckJzxResult jzxResult = new StandardCheckJzxResult();
                jzxResult.setCategory(category);
                jzxResult.setDiseaseType(Health100ETLContants.CHAOSHENGTEXING);
                jzxResult.setItemname(itemName);
                jzxResult.setVid(vid);
                jzxResult.setTraceData(traceData);
                jzxResult.setResult(str3);
                listJzx.add(jzxResult);
            }
            //结节类型
            if (StrUtil.isNotBlank(str4)) {
                StandardCheckJzxResult jzxResult = new StandardCheckJzxResult();
                jzxResult.setCategory(category);
                jzxResult.setDiseaseType(Health100ETLContants.JIEJIELEIXING);
                jzxResult.setItemname(itemName);
                jzxResult.setVid(vid);
                jzxResult.setTraceData(traceData);
                jzxResult.setResult(str4);
                listJzx.add(jzxResult);
            }

            //最大结节直径
            if (StrUtil.isNotBlank(str)) {
                StandardCheckJzxResult jzxResult = new StandardCheckJzxResult();
                jzxResult.setCategory(category);
                jzxResult.setDiseaseType(Health100ETLContants.ZHIJING);
                jzxResult.setItemname(itemName);
                jzxResult.setVid(vid);
                jzxResult.setTraceData(traceData);
                jzxResult.setResult(String.valueOf(zhijingNum));
                listJzx.add(jzxResult);
            }
        }
        listResult.setList(listJzx);
        return listResult;
    }
}
