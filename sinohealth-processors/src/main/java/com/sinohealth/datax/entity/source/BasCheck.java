package com.sinohealth.datax.entity.source;

import com.sinohealth.datax.reflection.annotations.db_alias;

import java.io.Serializable;
import java.util.Date;

/**
 * @author mingqiang
 * @date 2022/10/20 - 15:41
 * @desc 医院检查实体
 */
public class BasCheck implements Serializable {

    private String vid;

    @db_alias("class_name")
    private String className;

    @db_alias("item_name")
    private String itemName;

    private String unit;
    @db_alias("result_value")
    private String results;

    @db_alias("reference")
    private String reference;

    @db_alias("image_describe")
    private String imageDescribe;

    @db_alias("image_diagnose")
    private String imageDiagnose;

    @db_alias("report_time")
    private Date reportTime;

    private String summary;


    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Date getReportTime() {
        return reportTime;
    }

    public void setReportTime(Date reportTime) {
        this.reportTime = reportTime;
    }

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

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getResults() {
        return results;
    }

    public void setResults(String results) {
        this.results = results;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
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
}
