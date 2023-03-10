package com.sinohealth.datax.entity.source;

import com.sinohealth.datax.reflection.annotations.db_alias;

import java.io.Serializable;
import java.util.Date;

/**
 * @author mingqiang
 * @date 2022/10/20 - 15:35
 * @desc 美年用户信息
 */

public class RegStdInfo implements Serializable {

    @db_alias("item_type")
    private String itemType;

    @db_alias("item_name")
    private String itemName;

    @db_alias("field_name")
    private String fieldName;

    @db_alias("std3_medical_name")
    private String std3MedicalName;

    @db_alias("result_num")
    private Integer resultNum;

    @db_alias("branch_num")
    private Integer branchNum;



    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getStd3MedicalName() {
        return std3MedicalName;
    }

    public void setStd3MedicalName(String std3MedicalName) {
        this.std3MedicalName = std3MedicalName;
    }

    public Integer getResultNum() {
        return resultNum;
    }

    public void setResultNum(Integer resultNum) {
        this.resultNum = resultNum;
    }

    public Integer getBranchNum() {
        return branchNum;
    }

    public void setBranchNum(Integer branchNum) {
        this.branchNum = branchNum;
    }
}
