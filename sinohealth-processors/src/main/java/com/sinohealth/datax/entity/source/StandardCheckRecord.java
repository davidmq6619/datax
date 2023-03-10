package com.sinohealth.datax.entity.source;

import com.sinohealth.datax.reflection.annotations.db_alias;

import java.io.Serializable;
import java.util.Date;

/**
 * @author mingqiang
 * @date 2022/8/22 - 11:20
 * @desc
 */

public class StandardCheckRecord implements Serializable {

    private String vid;
    @db_alias("class_name")
    private String className;
    @db_alias("item_name")
    private String itemName;
    @db_alias("item_name_comn")
    private String itemNameComn;
    @db_alias("item_id")
    private String itemId;
    @db_alias("item_results")
    private String itemResults;
    @db_alias("init_result")
    private String initResult;
    @db_alias("item_unit")
    private String itemUnit;
    @db_alias("normal_h")
    private String normalH;
    @db_alias("normal_l")
    private String normalL;
    @db_alias("clean_time")
    private Date cleanTime;
    @db_alias("unit_comm")
    private String unitComm;

    private String remark;
    @db_alias("clean_status")
    private Integer cleanStatus;
    @db_alias("image_describe")
    private String imageDescribe;
    @db_alias("image_diagnose")
    private String imageDiagnose;
    @db_alias("results_discrete")
    private String resultsDiscrete;




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

    public String getInitResult() {
        return initResult;
    }

    public void setInitResult(String initResult) {
        this.initResult = initResult;
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

    public String getImageDiagnose() {
        return imageDiagnose;
    }

    public void setImageDiagnose(String imageDiagnose) {
        this.imageDiagnose = imageDiagnose;
    }

    public String getResultsDiscrete() {
        return resultsDiscrete;
    }

    public void setResultsDiscrete(String resultsDiscrete) {
        this.resultsDiscrete = resultsDiscrete;
    }
}
