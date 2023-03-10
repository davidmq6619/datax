package com.sinohealth.datax.processors;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ReUtil;
import com.sinohealth.datax.common.CommonData;
import com.sinohealth.datax.common.Processor;
import com.sinohealth.datax.entity.source.CheckResultMsS;
import com.sinohealth.datax.entity.zktarget.CheckResultMsEtl;
import com.sinohealth.datax.entity.zktarget.CheckResultMsEtlList;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Title:
 * @Package com.sinohealth.datax.processors
 * @Copyright: Copyright (C) 2022 SinoHealth Co. Ltd, All Rights Reserved
 * @Author heqiang 2022/5/18 18:04
 * @Description: 影像学描述清洗过程
 */
public class CheckResultMsEtlProcessor implements Processor<CheckResultMsS, CheckResultMsEtlList> {
    /**
     *肺CT的检测方式别名
     */
    public static final List<String> feictMethod= Arrays.asList("CT胸部检查",
            "CT胸部",
            "CT检查(胸部)-不出片",
            "低剂量胸部螺旋CT扫描",
            "C（胸部）",
            "低剂量胸部螺旋CT扫描(哺乳、备孕、孕者禁)",
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
            "低剂量肺部CT扫描(哺乳、备孕、孕者禁)",
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
            "胸部低剂量CT检查");

    /**
     *是否做肺CT 0未做 1已做
     */
    public  static final String itemNameCommA="肺CT";

    /**
     *是否有肺结节 0否 1是
     */
    public  static final String itemNameCommB="肺结节诊断";

    /**
     *最大结节直径 统一单位cm
     */
    public  static final String itemNameCommC="最大结节直径";
    /**
     * 结节直径提取正则
     */
    public  static final String regx = "([1-9]\\d*\\.?\\d*)|(0\\.\\d*[1-9])";

    @Override
    public CheckResultMsEtlList dataProcess(CheckResultMsS checkResultMsS, CheckResultMsEtlList o, CommonData commonData) {
        CheckResultMsEtlList ms=new CheckResultMsEtlList();
         boolean flag=false;
         String initResultComm="";
         if(1==1){
             CheckResultMsEtlList etlList = new CheckResultMsEtlList();
             List<CheckResultMsEtl> list = new ArrayList<>();
             for (int i = 0; i < 10; i++) {
                 CheckResultMsEtl msEtl = new CheckResultMsEtl();
                 msEtl.setVid(checkResultMsS.getVid());
                 msEtl.setResults(i+"");
                 list.add(msEtl);
             }
             etlList.setList(list);
             return etlList;
         }
        for(int i=0;i<feictMethod.size();i++){
             flag=checkResultMsS.getInitResult().contains(feictMethod.get(i));
         if(flag){
             initResultComm=feictMethod.get(i);
             break;
         }
         }
         if(flag){
             List<CheckResultMsEtl> list=new ArrayList<>();
             //匹配上肺部描述，开始清洗
             list.add(buildResultByItemNameCommA(checkResultMsS,initResultComm,itemNameCommA,"1"));
             etl(checkResultMsS,list,initResultComm);
             ms.setList(list);
         }else{
              //该描述不是肺CT的描述  直接忽略
             ms.setList(new ArrayList<>());
         }
        return ms;
    }

    public static void etl(CheckResultMsS checkResultMsS,List<CheckResultMsEtl> list,String initResultComm) {
        //取出描述，抛弃附件后的字
        String result = checkResultMsS.getResults();
        if (result.contains("附件")) {
            result = result.substring(0, result.indexOf("附件"));
        }
        //先判断整段符不符合要求 肺、胸膜
        boolean flag = result.contains("肺") || result.contains("胸膜");
        //”结节"、“磨玻璃”、““毛玻璃”、“高密度影”、“团块”
        boolean flag2 = result.contains("结节") || result.contains("磨玻璃") || result.contains("毛玻璃") || result.contains("高密度影") || result.contains("团块");
        if (!flag || !flag2) {
            //有一个条件不满足就跳过
            list.add(buildResultByItemNameCommA(checkResultMsS, initResultComm, itemNameCommB, "0"));
            return;
        }
        //定位上述条件在哪一句中 start
        //断句符标准化
        result = result.replaceAll(";", "；");
        //描述根据句号断句
        String[] juhaoSplit = result.split("。");
        //命中的语句
        List<String> hitStrs = new ArrayList<>();
        for (String juhao : juhaoSplit) {
            //先判断整句符不符合要求 肺、胸膜
            boolean flag3 = juhao.contains("肺") || juhao.contains("胸膜");
            //”结节"、“磨玻璃”、““毛玻璃”、“高密度影”、“团块”
            boolean flag4 = juhao.contains("结节") || juhao.contains("磨玻璃") || juhao.contains("毛玻璃") || juhao.contains("高密度影") || juhao.contains("团块");
            if (!flag3 || !flag4) {
                //有一个条件不满足就跳过
                continue;
            }
            //根据分号再断句
            String[] fenhaoSplit = juhao.split("；");
            for (String fenhao : fenhaoSplit) {
                //判断符不符合要求 肺、胸膜
                boolean flag5 = fenhao.contains("肺") || fenhao.contains("胸膜");
                //”结节"、“磨玻璃”、““毛玻璃”、“高密度影”、“团块”
                boolean flag6 = fenhao.contains("结节") || fenhao.contains("磨玻璃") || fenhao.contains("毛玻璃") || fenhao.contains("高密度影") || fenhao.contains("团块");
                if (flag5 && flag6) {
                    hitStrs.add(fenhao);
                }
            }
        }
        if (hitStrs.isEmpty()) {
            list.add(buildResultByItemNameCommA(checkResultMsS, initResultComm, itemNameCommB, "0"));
            return;
        }
        //按优先级取出最高的那句话：（结节 = 磨玻璃 = 毛玻璃 = 团块) > 高密度影&&目标语句有结节直径 > 直径大小

        //第1层筛选结果 （结节 = 磨玻璃 = 毛玻璃 = 团块)
        List<Map<String,String>> hits2 = new ArrayList<>();
        for (String hit : hitStrs) {
            //目标语句有结节直径的语句
            boolean flag7 = ReUtil.contains(regx, hit) && (hit.contains("cm") || hit.contains("CM") || hit.contains("Cm") || hit.contains("cM") || hit.contains("mM") || hit.contains("MM") || hit.contains("mm") || hit.contains("Mm"));

            if (hit.contains("结节状钙化")) {
                if(flag7) {
                    Map<String,String> map=new HashMap<>();
                    map.put("str",hit);
                    map.put("zhijing","true");
                    hits2.add(map);
                }
            } else if (hit.contains("毛玻璃")||hit.contains("磨玻璃")||hit.contains("团块")||hit.contains("结节")) {
                Map<String,String> map=new HashMap<>();
                map.put("str",hit);
                if(flag7) {
                    map.put("zhijing", "true");
                }else{
                    map.put("zhijing", "false");
                }
                hits2.add(map);
            } else if (hit.contains("高密度影")&&flag7) {
                Map<String,String> map=new HashMap<>();
                map.put("str",hit);
                map.put("zhijing","true");
                hits2.add(map);
            }
        }

        if(hits2.isEmpty()){
            list.add(buildResultByItemNameCommA(checkResultMsS,initResultComm,itemNameCommB,"0"));
            return ;
        }else{
            list.add(buildResultByItemNameCommA(checkResultMsS,initResultComm,itemNameCommB,"1"));
        }

        Map<String, List<Map<String, String>>> zhijingMap = hits2.stream().collect(Collectors.groupingBy(x -> x.get("zhijing")));
         String finalHit ="";
        List<Map<String, String>>  zhijingtrue= zhijingMap.get("true");
        if(zhijingtrue!=null&&!zhijingtrue.isEmpty()) {
            //目标语句有结节直径的语句
            List<String> hits5 = zhijingtrue.stream().map(y -> y.get("str")).collect(Collectors.toList());
            if (hits5.size() == 1) {
                finalHit = hits5.get(0);
            } else {
                finalHit = zhijingOrder(hits5);

            }
            //添加最大结节直径
            list.add(buildResultByItemNameCommA(checkResultMsS,initResultComm,itemNameCommC,String.valueOf(zhijingOrderCM(finalHit))));

        }


//定位上述条件在哪一句中 end

    }
    //构建etl结果
    public static CheckResultMsEtl buildResultByItemNameCommA(CheckResultMsS checkResultMsS,String initResultComm,String itemnameComm,String result){
        CheckResultMsEtl etl=new CheckResultMsEtl();
        etl.setBookTime(checkResultMsS.getBookTime());
        etl.setCreateTime(new Date());
        etl.setCleanStatus(0);
        etl.setCheckId(checkResultMsS.getId());
        etl.setVid(checkResultMsS.getVid());
        etl.setInitResult(checkResultMsS.getInitResult());
        etl.setInitResultComm(initResultComm);
        etl.setResults(result);
        etl.setItemNameComm(itemnameComm);
        return etl;
    }
public static String zhijingOrder(List<String> hits){
    //毫米
    double cur_zhijing=0.00;
    String cur_hit="";
    for (String hit : hits) {
        List<String> listS = new ArrayList<>();
        listS.addAll(ReUtil.findAll(regx, hit, 0));
        if (!listS.isEmpty()) {
            listS.sort((x1, x2) -> Double.valueOf(x1).compareTo(Double.valueOf(x2)));
            String s = listS.get(listS.size() - 1);

            if (NumberUtil.isNumber(s)) {
                double tempValue = Double.valueOf(s);
                if (hit.contains("cm") || hit.contains("CM") || hit.contains("Cm") || hit.contains("cM")) {
                    tempValue = tempValue * 10;
                }else if(hit.contains("mm") || hit.contains("MM") || hit.contains("Mm") || hit.contains("mM")){
                    tempValue=tempValue;
                }
                if(cur_zhijing==0.00||tempValue>cur_zhijing) {
                    cur_zhijing=tempValue;
                    cur_hit=hit;
                }
            }

        }


    }
    return cur_hit;
}

    public static double zhijingOrderCM(String hit){
        //cm
        double cur_zhijing=0;

            List<String> listS = new ArrayList<>();
            listS.addAll(ReUtil.findAll(regx, hit, 0));
            if (!listS.isEmpty()) {
                listS.sort((x1, x2) -> Double.valueOf(x1).compareTo(Double.valueOf(x2)));
                String s = listS.get(listS.size() - 1);

                if (NumberUtil.isNumber(s)) {
                    double tempValue = Double.valueOf(s);
                    if (hit.contains("cm") || hit.contains("CM") || hit.contains("Cm") || hit.contains("cM")) {
                        cur_zhijing = tempValue ;
                    }else if(hit.contains("mm") || hit.contains("MM") || hit.contains("Mm") || hit.contains("mM")){
                        cur_zhijing=tempValue/10;
                    }

                }

            }



        return cur_zhijing;
    }

    public static void main(String[] args) {
        String str="两侧胸廓对称。右肺上叶可见小结节影。两侧肺门不大。纵隔窗示心影及大血管形态正常，纵隔内未见肿块及明显肿大淋巴结。无胸腔积液及胸膜增厚。右胸膜下见结节影。";
        CheckResultMsS checkResultMsS=new CheckResultMsS();
        checkResultMsS.setId(1);
        checkResultMsS.setBookTime(new Date());
        checkResultMsS.setInitResult("胸部（肺部）CT@#刘杰 审核者:李明");
        checkResultMsS.setItemNameComm("描述");
        checkResultMsS.setResults(str);
        checkResultMsS.setVid("1111");
        List<CheckResultMsEtl> list=new ArrayList<>();
        etl(checkResultMsS,list,"胸部（肺部）CT");
        System.out.println("1");
    }
}
