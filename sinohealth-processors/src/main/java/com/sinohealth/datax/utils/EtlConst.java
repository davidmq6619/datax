package com.sinohealth.datax.utils;

import com.google.common.collect.Lists;
import com.sinohealth.datax.entity.common.BasTestItem;
import com.sinohealth.datax.entity.source.BasTestItemTemp;

import java.util.Arrays;
import java.util.List;

/**
 * @author mingqiang
 * @date 2022/8/30 - 14:59
 * @desc 检查常量
 */
public interface EtlConst {
    String NULL_STR = "";
    String SPLIT_OTHER = "&";
    String SPLIT_FENHAO = ";";
    String SPLIT_DOUHAO = ",";
    String SPLIT_MAOHAO = ":";
    String SPLIT_JUHAO = "。";
    String SPLIT_JIAN = "见";
    String SPLIT_FENHAO2 = "；";
    String TIJIANMIAOSHU = "体检描述";
    String MIAOSHU = "描述";
    String XIAOJIE = "小结";
    String ITEM_THYROID = "甲状腺";
    String INIT_RESULT_1 = "甲状腺彩超";
    String INIT_RESULT_BALLCHECK_1 = "甲状腺彩超（团检）";
    String INIT_RESULT_2 = "乳腺彩超";
    public final static String INIT_RESULT_RU = "乳腺结节";
    String JIEJIE = "结节";
    String QU = "区";
    String ZHIJING = "最大结节直径";
    String CHAOSHENGTEXING = "结节超声特性";
    String JIEJIELEIXING = "结节类型";
    String JZXJB = "甲状腺疾病";
    String JZXJB_RESULT = "有甲状腺结节";
    String RADSLEVEL = "TI-RADS分级";
    String BI_RADSLEVEL = "BI-RADS分级";
    String LU_RADSLEVEL = "LU-RADS分级";
    String BIFIX = "BI-RADS";
    //2.4 加特殊化检查项目处理【幽⻔螺杆菌呼⽓试验】
    String HELICOBACTER_PYLORI_BREATH = "幽门螺杆菌呼气试验";
    String INIT_RESULT_HELICOBACTER_PYLORI_BREATH = "幽门螺杆菌呼气试验";
    String TJ_SUMMARY = "体检汇总";
    //通过编码提取甲状腺信息
    String HNYGXKYY_433 = "433";
    String INIT_RESULT_FEI = "肺部检查";
    String TMSZYY_JZX = "MS_00001";
    //处理肺结节(Se4:IM120)
    String KH_REGX = "[（(].*?[）)]";
    //1.**2.**
    String FG_REGX ="\\d*[.、]+\\B";
    String KH_REGX2 = "（[^）]*）";
    String FEI_KEY="肺";
    String XIONG_KEY = "胸膜";
    String QI_KEY = "气管";
    String RU_KEY = "乳";
    String CLASS_KEY = "@#";
    String BAO_GAO = "报告";
    String WJX_KEY = "★";
    /**
     * 结节直径提取正则
     */
    String regx = "([1-9]\\d*\\.?\\d*)|(0\\.\\d*[1-9])";

    String REGX_LEVEL = "TI[-|—]RADS.*[分|级|类]";
    String REGX_SENSITIVE = "CDFI.*?未见|CDFI.*?可见";
    String BI_REGX_LEVEL = "BI-RADS\\w+[级|类]";
    String TI_REGX_LEVEL = "TI-RADS\\w+[级|类]";
    String LU_REGX_LEVEL = "LU-RADS\\w+[级|类]";


    String HP_YANG = "HP阳性";
    String HP_YANG_VAL = "阳性";
    String alias1 = "低回声结节:低回声,低回声区:低回声,低回声:低回声,弱回声结节:低回声,弱回声:低回声,弱回声区:低回声,高回声结节:高回声,高回声:高回声," +
            "高回声区:高回声,稍强回声结节:高回声,强回声结节:高回声,强回声区:高回声,强回声:高回声,等回声:等回声,等回声结节:等回声,等回声区:等回声,呈等回声:等回声,无回声结节:无回声," +
            "无回声区:无回声,无回声:无回声,混合回声区:混合回声,混合回声结节:混合回声,无回声:无回声,混合回声:混合回声,混合性回声:混合回声,不均质回声:混合回声";
    String alias2 = "囊实:囊实性结节,实性:实性结节,囊性:囊性结节";
    String keyLevelJson="[{\n" +
            "  \"level\":1,\n" +
            "  \"keywords\":[\"低回声\",\"弱回声\",\"高回声\",\"强回声\",\"回声减低区\"]\n" +
            "},{\n" +
            "  \"level\":2,\n" +
            "  \"keywords\":[\"等回声\",\"实性\",\"混合回声\",\"囊实\",\"无回声\",\"囊性\",\"不均质回声\"]\n" +
            "}]";
    List<String> ETL_NOT_DOING = Arrays.asList("未做", "拒绝", "拒做", "拒绝检查", "弃查", "弃检","拒检", "拒查", "未检出","未检");
    List<String> ETL_SUSPICIOUS = Arrays.asList("可疑", "待复检");
    List<String> NORMAL_LIST = Arrays.asList("未见异常","未见明显异常","没有", "未见","无异常","无明显异常","未发现", "未闻及","未触及","未显示");
    List<String> NEGATIVE_DATA = Arrays.asList("阴","阴性");
    List<String> POSITIVE_DATA = Arrays.asList("阳","阳性","+");
    List<String> RU_DATA_KEY = Arrays.asList("脑","肾","视","喉","鼻","垂体","甲状腺","皮肤","耳","肛");
    List<String> specialPartList = Lists.newArrayList("鼻 ", "扁桃体", "胆", "耳", "肺", "附睾", "附件", "腹",
             "肛门", "睾丸", "宫颈", "颌", "喉", "会阴", "脊柱", "甲状腺", "结肠", "口腔", "淋巴", "卵巢", "尿道", "膀胱", "盆腔", "脾", "前列腺",
            "肾", "食道", "输卵管", "输尿管", "外阴", "胃", "下肢", "血管", "咽", "眼", "腋", "胰", "阴唇", "阴道", "阴囊", "支气管", "子宫", "纵膈",
            "脊椎", "口唇", "颊", "颚", "牙", "舌","唾液腺", "食管", "脑","颅","椎动脉","胸","甲状腺","甲状旁腺","垂体","松果体","生殖腺","肺",
            "心","肠","阑尾","胆","肛");
    String itemNames="舒张压,收缩压,血糖(空腹),谷丙转氨酶,谷草转氨酶,γ-谷氨酰基转移酶,血糖(餐后2小时),低密度脂蛋白,血清甘油三酯,高密度脂蛋白,胰岛素（空腹）,血清尿酸,糖化血红蛋白,血红蛋白,血清白蛋白,血清肌酐,血小板计数,血清总胆固醇";

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
}
