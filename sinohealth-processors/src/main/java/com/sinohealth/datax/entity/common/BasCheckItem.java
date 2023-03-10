package com.sinohealth.datax.entity.common;

import com.sinohealth.datax.reflection.annotations.db_alias;

/**
 * @author mingqiang
 * @date 2022/08/29
 **/
public class BasCheckItem {

    public Long id;
    @db_alias("item_no")
    public String itemNo;
    @db_alias("item_name")
    public String itemName;
    @db_alias("item_name_standard")
    public String itemNameStandard;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getItemNo() {
        return itemNo;
    }

    public void setItemNo(String itemNo) {
        this.itemNo = itemNo;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemNameStandard() {
        return itemNameStandard;
    }

    public void setItemNameStandard(String itemNameStandard) {
        this.itemNameStandard = itemNameStandard;
    }
}
