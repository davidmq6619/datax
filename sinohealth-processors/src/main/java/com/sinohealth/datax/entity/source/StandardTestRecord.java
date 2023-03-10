package com.sinohealth.datax.entity.source;

import com.sinohealth.datax.reflection.annotations.db_alias;

import java.io.Serializable;
import java.util.Date;

/**
 * @author mingqiang
 * @date 2022/8/22 - 15:16
 * @desc
 */

public class StandardTestRecord implements Serializable {

    public String vid;
    @db_alias("class_name")
    public String className;
    @db_alias("item_name")
    public String itemName;
    @db_alias("item_name_comn")
    public String itemNameComn;
    @db_alias("item_id")
    public String itemId;
    @db_alias("item_results")
    public String itemResults;
    @db_alias("item_unit")
    public String itemUnit;
    @db_alias("normal_h")
    public String normalH;
    @db_alias("normal_l")
    public String normalL;
    @db_alias("results_discrete")
    public String resultsDiscrete;
    @db_alias("clean_time")
    public Date cleanTime;
    @db_alias("unit_comm")
    public String unitComm;
    @db_alias("clean_status")
    public Integer cleanStatus;

    public String remark;


    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemNameComn() {
        return itemNameComn;
    }

    public void setItemNameComn(String itemNameComn) {
        this.itemNameComn = itemNameComn;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemResults() {
        return itemResults;
    }

    public void setItemResults(String itemResults) {
        this.itemResults = itemResults;
    }

    public String getItemUnit() {
        return itemUnit;
    }

    public void setItemUnit(String itemUnit) {
        this.itemUnit = itemUnit;
    }

    public String getNormalH() {
        return normalH;
    }

    public void setNormalH(String normalH) {
        this.normalH = normalH;
    }

    public String getNormalL() {
        return normalL;
    }

    public void setNormalL(String normalL) {
        this.normalL = normalL;
    }

    public String getResultsDiscrete() {
        return resultsDiscrete;
    }

    public void setResultsDiscrete(String resultsDiscrete) {
        this.resultsDiscrete = resultsDiscrete;
    }

    public Date getCleanTime() {
        return cleanTime;
    }

    public void setCleanTime(Date cleanTime) {
        this.cleanTime = cleanTime;
    }

    public String getUnitComm() {
        return unitComm;
    }

    public void setUnitComm(String unitComm) {
        this.unitComm = unitComm;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Integer getCleanStatus() {
        return cleanStatus;
    }

    public void setCleanStatus(Integer cleanStatus) {
        this.cleanStatus = cleanStatus;
    }
}
