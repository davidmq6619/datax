package com.sinohealth.datax.entity.source;

import com.sinohealth.datax.reflection.annotations.db_alias;

import java.io.Serializable;

/**
 * 检验结果Result
 *
 * @author mingqiang
 * @version 1766517
 */
public class BasTestItemTemp implements Serializable {

    public Long id;

    public String vid;

    @db_alias("class_name")
    public String className;                // 大类
    @db_alias("item_name")
    public String itemName;                // 小类
    @db_alias("item_code")
    public String itemCode;
    @db_alias("result_value")
    public String resultValue;                // 结果

    public String unit;                    // 单位

    public String reference;                    // 上下限

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

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

    public String getResultValue() {
        return resultValue;
    }

    public void setResultValue(String resultValue) {
        this.resultValue = resultValue;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }
}