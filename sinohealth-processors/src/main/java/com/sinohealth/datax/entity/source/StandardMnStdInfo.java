package com.sinohealth.datax.entity.source;

import com.sinohealth.datax.reflection.annotations.db_alias;


public class StandardMnStdInfo {

    @db_alias("item_code")
    private String itemCode;

    @db_alias("item_name")
    private String itemName;

    @db_alias("detail_item_code")
    private String detailItemCode;

    @db_alias("detail_item_name")
    private String detailItemName;

    @db_alias("zk_std_medical_name")
    private String zkStdMedicalMame;

    @db_alias("zk_item_name")
    private String zkItemName;

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getDetailItemCode() {
        return detailItemCode;
    }

    public void setDetailItemCode(String detailItemCode) {
        this.detailItemCode = detailItemCode;
    }

    public String getDetailItemName() {
        return detailItemName;
    }

    public void setDetailItemName(String detailItemName) {
        this.detailItemName = detailItemName;
    }

    public String getZkStdMedicalMame() {
        return zkStdMedicalMame;
    }

    public void setZkStdMedicalMame(String zkStdMedicalMame) {
        this.zkStdMedicalMame = zkStdMedicalMame;
    }

    public String getZkItemName() {
        return zkItemName;
    }

    public void setZkItemName(String zkItemName) {
        this.zkItemName = zkItemName;
    }
}
