package com.sinohealth.datax.entity.source;

import com.sinohealth.datax.reflection.annotations.db_alias;

import java.io.Serializable;

/**
 * @author mingqiang
 * @date 2022/10/20 - 15:35
 * @desc 美年用户信息
 */

public class RegStdMnInfo implements Serializable {

    @db_alias("item_code")
    private String itemCode;

    @db_alias("item_name")
    private String itemName;

    @db_alias("detail_item_code")
    private String detailItemCode;

    @db_alias("detail_item_name")
    private String detailItemName;


    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
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
}
