package com.sinohealth.datax.entity.source;

import com.sinohealth.datax.reflection.annotations.db_alias;

import java.io.Serializable;
import java.util.Date;

/**
 * @author mingqiang
 * @date 2022/8/22 - 11:20
 * @desc
 */

public class StandardMnCheckRecord implements Serializable {

    private String vid;

    @db_alias("item_ft")
    private String itemFt;

    @db_alias("item_name")
    private String itemName;

    @db_alias("item_name_comn")
    private String itemNameComn;

    private String results;

    @db_alias("unit")
    private String unit;

    @db_alias("normal_h")
    private String normalH;

    @db_alias("normal_l")
    private String normalL;

    @db_alias("results_discrete")
    private Integer resultsDiscrete;

    @db_alias("clean_time")
    private Date cleanTime;

    private String remark;
    @db_alias("clean_status")
    private Integer cleanStatus;
    @db_alias("image_describe")
    private String imageDescribe;

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
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

    public String getItemNameComn() {
        return itemNameComn;
    }

    public void setItemNameComn(String itemNameComn) {
        this.itemNameComn = itemNameComn;
    }

    public String getResults() {
        return results;
    }

    public void setResults(String results) {
        this.results = results;
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

    public Date getCleanTime() {
        return cleanTime;
    }

    public void setCleanTime(Date cleanTime) {
        this.cleanTime = cleanTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getCleanStatus() {
        return cleanStatus;
    }

    public void setCleanStatus(Integer cleanStatus) {
        this.cleanStatus = cleanStatus;
    }

    public String getImageDescribe() {
        return imageDescribe;
    }

    public void setImageDescribe(String imageDescribe) {
        this.imageDescribe = imageDescribe;
    }

    public Integer getResultsDiscrete() {
        return resultsDiscrete;
    }

    public void setResultsDiscrete(Integer resultsDiscrete) {
        this.resultsDiscrete = resultsDiscrete;
    }
}
