package com.sinohealth.datax.processors;


import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.sinohealth.datax.common.CommonData;
import com.sinohealth.datax.common.Processor;
import com.sinohealth.datax.entity.common.BasCheckItem;
import com.sinohealth.datax.entity.common.BasItemAlias;
import com.sinohealth.datax.entity.common.StandardBasTestItem;
import com.sinohealth.datax.entity.source.*;
import com.sinohealth.datax.entity.zktarget.StandardRegStdInfoList;
import com.sinohealth.datax.utils.EtlConst;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * @author mingqiang
 * @date 2022/08/29
 * 获取用户基本信息
 **/
public class RegEtlDic implements Processor<RegStdInfo, StandardRegStdInfoList> {
    public static final Logger LOG = LoggerFactory.getLogger(RegEtlDic.class);

    @Override
    public StandardRegStdInfoList dataProcess(RegStdInfo stdInfo, StandardRegStdInfoList list, CommonData commonData) {

        //stdInfo.setItemName("左侧关节正侧位DR片");
        //stdInfo.setFieldName("X光片号");
        ArrayList<StandardRegStdInfo> recordList = new ArrayList<>();
        String zkItemName = stdInfo.getItemName();
        String detailItemName = stdInfo.getFieldName();
        StandardRegStdInfo standardMnStdInfo = new StandardRegStdInfo();
        standardMnStdInfo.setItemType(stdInfo.getItemType());
        standardMnStdInfo.setItemName(stdInfo.getItemName());
        standardMnStdInfo.setFieldName(stdInfo.getFieldName());
        standardMnStdInfo.setStd3MedicalName(stdInfo.getStd3MedicalName());
        standardMnStdInfo.setResultNum(stdInfo.getResultNum());
        standardMnStdInfo.setBranchNum(stdInfo.getBranchNum());
        try {
            //匹配规则统一小写+英文括号
            Map<String, StandardBasTestItem> basTestItemMap = commonData.getBasTestItemMap();
            Map<String, StandardBasTestItem> basTestMethodItemMap = commonData.getBasTestMethodItemMap();
            Map<String, BasCheckItem> basCheckItemMap = commonData.getBasCheckItemMap();
            List<BasItemAlias> itemAliasList = commonData.getBasItemAliasList();
            String itemFt = "";
            String itemName = "";
            if (StrUtil.isNotBlank(detailItemName) && !detailItemName.contains("X光") &&
                    !detailItemName.contains("报告") && !detailItemName.contains("详") &&
                    !detailItemName.contains("结果") && !detailItemName.contains("备注") &&
                    !detailItemName.contains("附件") && !detailItemName.contains("提示") &&
                    !detailItemName.contains("结论") && !detailItemName.contains("其他")
            ) {
                if (StrUtil.isNotBlank(zkItemName)) {
                    itemFt = zkItemName
                            .replace("（", "(")
                            .replace("）", ")").toLowerCase().trim();
                }
                if (StrUtil.isNotBlank(detailItemName)) {
                    itemName = detailItemName.replace("（", "(")
                            .replace("）", ")")
                            .toLowerCase().trim();
                }
                String item = itemFt + ":" + itemName;
                StandardBasTestItem basTestItem = basTestMethodItemMap.get(item);
                if (Objects.isNull(basTestItem)) {
                    basTestItem = basTestItemMap.get(itemName);
                }
                if (Objects.nonNull(basTestItem)) {
                    standardMnStdInfo.setZkStdMedicalMame(basTestItem.getItemNameCStandard());
                }
                //检查项
                if (StrUtil.isBlank(standardMnStdInfo.getZkStdMedicalMame())) {
                    if (StrUtil.isNotBlank(detailItemName)) {
                        itemName = detailItemName.replace("（", "(")
                                .replace("）", ")").trim();
                    }
                    BasCheckItem basCheckItem = basCheckItemMap.get(itemName);
                    if (Objects.nonNull(basCheckItem)) {
                        standardMnStdInfo.setZkStdMedicalMame(basCheckItem.getItemNameStandard());
                    }
                }
                //使用大项处理
                if (StrUtil.isBlank(standardMnStdInfo.getZkStdMedicalMame())) {
                    String zkItemNameNew = detailItemName.replaceAll(EtlConst.KH_REGX, "");
                    //检验方法
                    String zkStd = "";
                    boolean flagA = false;
                    boolean flagB = false;
                    boolean flagC = false;
                    if (zkItemNameNew.startsWith("DR")) {
                        flagA = true;
                        zkItemNameNew = zkItemNameNew.substring(2);
                    }
                    if (zkItemNameNew.startsWith("CT")) {
                        flagB = true;
                        zkItemNameNew = zkItemNameNew.substring(2);
                    }
                    if (zkItemNameNew.startsWith("MR")) {
                        flagC = true;
                        zkItemNameNew = zkItemNameNew.substring(2);
                    }
                    if (zkItemNameNew.contains("正")) {
                        zkItemNameNew = zkItemNameNew.replace("正", "DR");
                    } else if (zkItemNameNew.contains("侧")) {
                        zkItemNameNew = zkItemNameNew.replace("侧", "DR");
                    } else if (zkItemNameNew.contains("片")) {
                        zkItemNameNew = zkItemNameNew.replace("片", "DR");
                    } else if (flagA) {
                        zkItemNameNew = zkItemNameNew + "DR";
                    } else if (flagB) {
                        zkItemNameNew = zkItemNameNew + "CT";
                    } else if (flagC) {
                        zkItemNameNew = zkItemNameNew + "MR";
                    }
                    //MR,CT,DR,核磁-》MR,彩超 E超 B超-》超声，红外-》TMT
                    if (zkItemNameNew.contains("心电图")) {
                        zkStd = "心电图";
                    } else if (zkItemNameNew.contains("胃镜")) {
                        zkStd = "胃镜";
                    } else if (zkItemNameNew.contains("肠镜")) {
                        zkStd = "肠镜";
                    } else if (zkItemNameNew.contains("MR")) {
                        zkStd = zkItemNameNew.substring(0, zkItemNameNew.indexOf("MR") + 2);
                    } else if (zkItemNameNew.contains("CT")) {
                        zkStd = zkItemNameNew.substring(0, zkItemNameNew.indexOf("CT") + 2);
                    } else if (zkItemNameNew.contains("核磁")) {
                        zkItemNameNew = zkItemNameNew.replace("核磁", "MR");
                        zkStd = zkItemNameNew.substring(0, zkItemNameNew.indexOf("MR") + 2);
                    } else if (zkItemNameNew.contains("磁共振")) {
                        zkItemNameNew = zkItemNameNew.replace("磁共振", "MR");
                        zkStd = zkItemNameNew.substring(0, zkItemNameNew.indexOf("MR") + 2);
                    } else if (zkItemNameNew.contains("彩超") || zkItemNameNew.contains("E超") || zkItemNameNew.contains("B超") || zkItemNameNew.contains("超声")) {
                        zkItemNameNew = zkItemNameNew.replace("彩超", "超声")
                                .replace("B超", "超声")
                                .replace("E超", "超声");
                        zkStd = zkItemNameNew.substring(0, zkItemNameNew.indexOf("超声") + 2);
                    } else if (zkItemNameNew.contains("红外")) {
                        zkItemNameNew = zkItemNameNew.replace("红外", "TMT");
                        zkStd = zkItemNameNew.substring(0, zkItemNameNew.indexOf("TMT") + 3);
                    } else if(zkItemNameNew.contains("热成像")){
                        zkItemNameNew = zkItemNameNew.replace("热成像", "TMT");
                        zkStd = zkItemNameNew.substring(0, zkItemNameNew.indexOf("TMT") + 3);
                    }else if (zkItemNameNew.contains("DR")) {
                        zkStd = zkItemNameNew.substring(0, zkItemNameNew.indexOf("DR") + 2);
                    }
                    if (StrUtil.isNotBlank(zkStd)) {
                        standardMnStdInfo.setZkStdMedicalMame(zkStd);
                    }
                }
                if (StrUtil.isBlank(standardMnStdInfo.getZkStdMedicalMame())) {
                    for (BasItemAlias basItemAlias : itemAliasList) {
                        if (stdInfo.getFieldName().equals(basItemAlias.getAliasName())) {
                            standardMnStdInfo.setZkItemName(basItemAlias.getName());
                            break;
                        }
                    }
                }
            }


            if (StrUtil.isNotBlank(zkItemName)) {
                if (StrUtil.isBlank(standardMnStdInfo.getZkItemName())) {
                    if (StrUtil.isNotBlank(zkItemName)) {
                        itemName = zkItemName.replace("（", "(")
                                .replace("）", ")").toLowerCase().trim();
                    }
                    StandardBasTestItem basTestItemT = basTestItemMap.get(itemName);
                    if (Objects.nonNull(basTestItemT)) {
                        standardMnStdInfo.setZkItemName(basTestItemT.getItemNameCStandard());
                    }
                }
                if (StrUtil.isBlank(standardMnStdInfo.getZkItemName())) {
                    if (StrUtil.isNotBlank(zkItemName)) {
                        itemName = zkItemName.replace("（", "(")
                                .replace("）", ")").trim();
                    }
                    BasCheckItem basCheckItem = basCheckItemMap.get(itemName);
                    if (Objects.nonNull(basCheckItem)) {
                        standardMnStdInfo.setZkItemName(basCheckItem.getItemNameStandard());
                    }
                }
                //使用大项处理
                if (StrUtil.isBlank(standardMnStdInfo.getZkItemName())) {
                    String zkItemNameNew = zkItemName.replaceAll(EtlConst.KH_REGX, "");
                    //检验方法
                    String zkStd = "";
                    boolean flagA = false;
                    boolean flagB = false;
                    boolean flagC = false;
                    if (zkItemNameNew.startsWith("DR")) {
                        flagA = true;
                        zkItemNameNew = zkItemNameNew.substring(2);
                    }
                    if (zkItemNameNew.startsWith("CT")) {
                        flagB = true;
                        zkItemNameNew = zkItemNameNew.substring(2);
                    }
                    if (zkItemNameNew.startsWith("MR")) {
                        flagC = true;
                        zkItemNameNew = zkItemNameNew.substring(2);
                    }
                    if (zkItemNameNew.contains("正")) {
                        zkItemNameNew = zkItemNameNew.replace("正", "DR");
                    } else if (zkItemNameNew.contains("侧")) {
                        zkItemNameNew = zkItemNameNew.replace("侧", "DR");
                    } else if (zkItemNameNew.contains("片")) {
                        zkItemNameNew = zkItemNameNew.replace("片", "DR");
                    } else if (flagA) {
                        zkItemNameNew = zkItemNameNew + "DR";
                    } else if (flagB) {
                        zkItemNameNew = zkItemNameNew + "CT";
                    } else if (flagC) {
                        zkItemNameNew = zkItemNameNew + "MR";
                    }
                    //MR,CT,DR,核磁-》MR,彩超 E超 B超-》超声，红外-》TMT
                    if (zkItemNameNew.contains("心电图")) {
                        zkStd = "心电图";
                    } else if (zkItemNameNew.contains("胃镜")) {
                        zkStd = "胃镜";
                    } else if (zkItemNameNew.contains("肠镜")) {
                        zkStd = "肠镜";
                    } else if (zkItemNameNew.contains("MR")) {
                        zkStd = zkItemNameNew.substring(0, zkItemNameNew.indexOf("MR") + 2);
                    } else if (zkItemNameNew.contains("CT")) {
                        zkStd = zkItemNameNew.substring(0, zkItemNameNew.indexOf("CT") + 2);
                    } else if (zkItemNameNew.contains("核磁")) {
                        zkItemNameNew = zkItemNameNew.replace("核磁", "MR");
                        zkStd = zkItemNameNew.substring(0, zkItemNameNew.indexOf("MR") + 2);
                    }else if (zkItemNameNew.contains("磁共振")) {
                        zkItemNameNew = zkItemNameNew.replace("磁共振", "MR");
                        zkStd = zkItemNameNew.substring(0, zkItemNameNew.indexOf("MR") + 2);
                    } else if (zkItemNameNew.contains("彩超") || zkItemNameNew.contains("E超") || zkItemNameNew.contains("B超") || zkItemNameNew.contains("超声")) {
                        zkItemNameNew = zkItemNameNew.replace("彩超", "超声")
                                .replace("B超", "超声")
                                .replace("E超", "超声");
                        zkStd = zkItemNameNew.substring(0, zkItemNameNew.indexOf("超声") + 2);
                    } else if (zkItemNameNew.contains("红外")) {
                        zkItemNameNew = zkItemNameNew.replace("红外", "TMT");
                        zkStd = zkItemNameNew.substring(0, zkItemNameNew.indexOf("TMT") + 3);
                    } else if(zkItemNameNew.contains("热成像")){
                        zkItemNameNew = zkItemNameNew.replace("热成像", "TMT");
                        zkStd = zkItemNameNew.substring(0, zkItemNameNew.indexOf("TMT") + 3);
                    }else if (zkItemNameNew.contains("DR")) {
                        zkStd = zkItemNameNew.substring(0, zkItemNameNew.indexOf("DR") + 2);
                    }
                    if (StrUtil.isNotBlank(zkStd)) {
                        standardMnStdInfo.setZkItemName(zkStd);
                    }
                    if (StrUtil.isBlank(standardMnStdInfo.getZkItemName())) {
                        for (BasItemAlias basItemAlias : itemAliasList) {
                            if (stdInfo.getItemName().equals(basItemAlias.getAliasName())) {
                                standardMnStdInfo.setZkItemName(basItemAlias.getName());
                                break;
                            }
                        }
                    }
                }
            }
            String zkItemName2 = standardMnStdInfo.getZkItemName();
            String zkStdMedicalMame2 = standardMnStdInfo.getZkStdMedicalMame();
            if(StrUtil.isNotBlank(zkItemName2) && zkItemName2.contains(EtlConst.WJX_KEY)){
                zkItemName2 = zkItemName2.substring(zkItemName2.indexOf(EtlConst.WJX_KEY)+1);
                standardMnStdInfo.setZkItemName(zkItemName2);
            }
            if(StrUtil.isNotBlank(zkStdMedicalMame2) && zkStdMedicalMame2.contains(EtlConst.WJX_KEY)){
                zkStdMedicalMame2 = zkStdMedicalMame2.substring(zkStdMedicalMame2.indexOf(EtlConst.WJX_KEY)+1);
                standardMnStdInfo.setZkStdMedicalMame(zkStdMedicalMame2);
            }
            recordList.add(standardMnStdInfo);
            list.setList(recordList);
        } catch (Exception e) {
            LOG.error("数据异常，入参数据{},数据异常{}", JSON.toJSONString(stdInfo),
                    e.getMessage(), e);
        }
        return list;
    }

    public static void main(String[] args) {
        String str ="【329、332】★妇科液基细胞学检测TCT";
        String s = str.substring(str.indexOf("★")+1);
        System.out.println(s);
    }
}
