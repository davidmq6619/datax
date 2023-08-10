package com.sinohealth.datax.entity.source;

import com.sinohealth.datax.reflection.annotations.db_alias;

import java.io.Serializable;
import java.util.Date;

/**
 * @author mingqiang
 * @date 2022/8/22 - 15:16
 * @desc
 */

public class StandardMnTestRecord implements Serializable {

    public String vid;

    @db_alias("item_ft")
    public String itemFt;                // 大类

    @db_alias("item_name")
    public String itemName;                // 小类

    @db_alias("results")
    public String results;                // 结果

    public String unit;                    // 单位

    @db_alias("normal_l")
    public String normalL;                    // 上下限

    @db_alias("normal_h")
    public String normalH;

    @db_alias("results_discrete")
    private String resultsDiscrete;

    @db_alias("clean_time")
    private Date cleanTime;

    @db_alias("unit_comm")
    private String unitComm;

    @db_alias("clean_status")
    private Integer cleanStatus;

    private String remark;

    @db_alias("item_name_comm")
    private String itemNameComm;

    public String getItemNameComm() {
        return itemNameComm;
    }

    public void setItemNameComm(String itemNameComm) {
        this.itemNameComm = itemNameComm;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public String getItemFt() {
        return itemFt;
    }

    public void setItemFt(String itemFt) {
        this.itemFt = itemFt;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getResults() {
        return results;
    }

    public void setResults(String results) {
        this.results = results;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getNormalL() {
        return normalL;
    }

    public void setNormalL(String normalL) {
        this.normalL = normalL;
    }

    public String getNormalH() {
        return normalH;
    }

    public void setNormalH(String normalH) {
        this.normalH = normalH;
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

    public Integer getCleanStatus() {
        return cleanStatus;
    }

    public void setCleanStatus(Integer cleanStatus) {
        this.cleanStatus = cleanStatus;
    }
}
