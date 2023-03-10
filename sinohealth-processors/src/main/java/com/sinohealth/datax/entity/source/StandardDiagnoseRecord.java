package com.sinohealth.datax.entity.source;

import com.sinohealth.datax.reflection.annotations.db_alias;

import java.io.Serializable;
import java.util.Date;

/**
 * @author mingqiang
 * @date 2022/9/5 - 14:20
 * @desc 诊断结果
 */
public class StandardDiagnoseRecord implements Serializable {

    private String vid;

    @db_alias("class_name")
    private String className;


    @db_alias("item_id")
    private String itemId;

    @db_alias("item_name")
    private String itemName;

    @db_alias("item_results")
    private String itemResults;

    @db_alias("clean_status")
    private Integer cleanStatus;

    private String remark;

    @db_alias("clean_time")
    private Date cleanTime;

    @db_alias("image_diagnose")
    private String imageDiagnose;




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

    public String getItemResults() {
        return itemResults;
    }

    public void setItemResults(String itemResults) {
        this.itemResults = itemResults;
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

    public String getImageDiagnose() {
        return imageDiagnose;
    }

    public void setImageDiagnose(String imageDiagnose) {
        this.imageDiagnose = imageDiagnose;
    }

    public Date getCleanTime() {
        return cleanTime;
    }

    public void setCleanTime(Date cleanTime) {
        this.cleanTime = cleanTime;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }
}
