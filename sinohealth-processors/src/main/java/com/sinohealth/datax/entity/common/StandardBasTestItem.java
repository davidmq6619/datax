package com.sinohealth.datax.entity.common;

import com.sinohealth.datax.reflection.annotations.db_alias;

import java.io.Serializable;

/**
 * @author mingqiang
 * @date 2022/8/26 - 9:49
 * @desc
 */
public class StandardBasTestItem implements Serializable {

    public Long id;

    @db_alias("item_name")
    public String itemName;

    @db_alias("item_name_c_standard")
    public String itemNameCStandard;

    @db_alias("alias_ids")
    public String aliasIds;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemNameCStandard() {
        return itemNameCStandard;
    }

    public void setItemNameCStandard(String itemNameCStandard) {
        this.itemNameCStandard = itemNameCStandard;
    }

    public String getAliasIds() {
        return aliasIds;
    }

    public void setAliasIds(String aliasIds) {
        this.aliasIds = aliasIds;
    }
}
