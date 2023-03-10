package com.sinohealth.datax.utils;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ReUtil;
import com.sinohealth.datax.entity.source.CheckResultMsS;
import com.sinohealth.datax.entity.source.StandardCheckRecord;
import com.sinohealth.datax.entity.zktarget.CheckResultMsEtl;

import java.util.*;

/**
 * @author mingqiang
 * @date 2022/8/30 - 15:14
 * @desc 获取乳腺
 */
public class EtlRuUtils {
    /**
     * 肺CT的检测方式别名
     */
    public static final List<String> ruxianMethod = Arrays.asList(
            "乳腺钼钯",
            "乳腺超声",
            "乳腺数字化摄影",
            "彩超：乳腺",
            "乳腺MR平扫+DWI+乳腺动态增强",
            "胸部低剂量CT",
            "乳腺彩超"
    );
    public static final List<String> ruMethod = Arrays.asList(
            "结节",
            "肿块",
            "灶",
            "影",
            "簇+钙化",
            "囊肿",
            "腺瘤",
            "瘤",
            "回声区",
            "回声团",
            "回声灶",
            "肿物",
            "包块",
            "占位",
            "团块",
            "癌",
            "Ca",
            "低回声",
            "混合回声"
    );

    /**
     * 是否有肺结节 0否 1是
     */
    public static final String itemNameCommB = "乳腺结节";
    /**
     * 结节直径提取正则
     */
    public static final String regx = "([1-9]\\d*\\.?\\d*)|(0\\.\\d*[1-9])";

    public static void etl(StandardCheckRecord checkResultMsS, List<StandardCheckRecord> list) {
        //取出描述，抛弃附件后的字
        String result = checkResultMsS.getItemResults();
        result= result.replaceAll(EtlConst.REGX_SENSITIVE, "******");
        /*if(!result.endsWith("。")){
            result = result + "。";
        }
        result = result + checkResultMsS.getImageDiagnose();*/
        List<String> hitStrs = new ArrayList<>();
        String[] splitStrings = TextUtils.splitSignsToArrByParam(result, ";；。");
        boolean flag2 = false;
        for (String str1 : splitStrings) {
            boolean flagRu = false;
            for (String s : EtlConst.RU_DATA_KEY) {
                if (str1.contains(s)) {
                    flagRu = true;
                    break;
                }
            }
            boolean flagNormal = false;
            for (String s : EtlConst.NORMAL_LIST) {
                if(str1.contains(s)){
                    flagNormal = true;
                    break;
                }
            }
            if (flagRu ||flagNormal) {
                continue;
            }
            if (str1.contains("乳")) {
                for (String ru : ruMethod) {
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
            list.add(buildResultByItemNameCommA(checkResultMsS, itemNameCommB, "1"));
        } else {
            list.add(buildResultByItemNameCommA(checkResultMsS, itemNameCommB, "0"));
            return;
        }
    }

    //构建etl结果
    public static StandardCheckRecord buildResultByItemNameCommA(StandardCheckRecord checkResultMsS, String itemnameComm, String result) {
        StandardCheckRecord etl = new StandardCheckRecord();
        etl.setCleanTime(new Date());
        etl.setVid(checkResultMsS.getVid());
        etl.setInitResult(itemnameComm);
        etl.setClassName(checkResultMsS.getClassName());
        etl.setItemResults(result);
        etl.setItemName(checkResultMsS.getItemName());
        etl.setItemNameComn(itemnameComm);
        etl.setImageDiagnose(checkResultMsS.getImageDiagnose());
        etl.setImageDescribe(checkResultMsS.getItemResults());
        if ("1".equals(result)) {
            etl.setCleanStatus(EtlStatus.ETL_SUCCESS.getCode());
        } else {
            etl.setCleanStatus(EtlStatus.ETL_SUCCESS_NORMAL.getCode());
            etl.setRemark(EtlStatus.ETL_SUCCESS_NORMAL.getMessage());
        }
        return etl;
    }

    public static String zhijingOrder(List<String> hits) {
        //毫米
        double cur_zhijing = 0.00;
        String cur_hit = "";
        for (String hit : hits) {
            List<String> listS = new ArrayList<>();
            listS.addAll(ReUtil.findAll(regx, hit, 0));
            if (!listS.isEmpty()) {
                listS.sort(Comparator.comparing(Double::valueOf));
                String s = listS.get(listS.size() - 1);

                if (NumberUtil.isNumber(s)) {
                    double tempValue = Double.valueOf(s);
                    if (hit.contains("cm") || hit.contains("CM") || hit.contains("Cm") || hit.contains("cM")) {
                        tempValue = tempValue * 10;
                    } else if (hit.contains("mm") || hit.contains("MM") || hit.contains("Mm") || hit.contains("mM")) {
                        tempValue = tempValue;
                    }
                    if (cur_zhijing == 0.00 || tempValue > cur_zhijing) {
                        cur_zhijing = tempValue;
                        cur_hit = hit;
                    }
                }

            }


        }
        return cur_hit;
    }

    public static double zhijingOrderCM(String hit) {
        //cm
        double cur_zhijing = 0;

        List<String> listS = new ArrayList<>();
        listS.addAll(ReUtil.findAll(regx, hit, 0));
        if (!listS.isEmpty()) {
            listS.sort((x1, x2) -> Double.valueOf(x1).compareTo(Double.valueOf(x2)));
            String s = listS.get(listS.size() - 1);

            if (NumberUtil.isNumber(s)) {
                double tempValue = Double.valueOf(s);
                if (hit.contains("cm") || hit.contains("CM") || hit.contains("Cm") || hit.contains("cM")) {
                    cur_zhijing = tempValue;
                } else if (hit.contains("mm") || hit.contains("MM") || hit.contains("Mm") || hit.contains("mM")) {
                    cur_zhijing = tempValue / 10;
                }
            }
        }

        return cur_zhijing;
    }

    public static void main(String[] args) {
        String str = "两侧胸廓对称。右肺上叶可见小结节影。两侧肺门不大。纵隔窗示心影及大血管形态正常，纵隔内未见肿块及明显肿大淋巴结。无胸腔积液及胸膜增厚。右胸膜下见结节影。";
        CheckResultMsS checkResultMsS = new CheckResultMsS();
        checkResultMsS.setId(1);
        checkResultMsS.setBookTime(new Date());
        checkResultMsS.setInitResult("胸部（肺部）CT@#刘杰 审核者:李明");
        checkResultMsS.setItemNameComm("描述");
        checkResultMsS.setResults(str);
        checkResultMsS.setVid("1111");
        List<CheckResultMsEtl> list = new ArrayList<>();
        System.out.println("1");
    }

}
