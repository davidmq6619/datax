package com.sinohealth.datax.entity.source;

import com.sinohealth.datax.reflection.annotations.db_alias;

/**
 * @author mingqiang
 * @date 2023/5/26 - 18:11
 * @desc
 */
public class StandardCheckJzxResult {
    /**
     *体检编号
     */
    @db_alias("vid")
    public String vid;

    /**
     *大项
     */
    @db_alias("category")
    public String category;


    @db_alias("item_name")
    public String itemname;


    @db_alias("result")
    public String result;


    @db_alias("trace_data")
    public String traceData;

    @db_alias("disease_type")
    public String diseaseType;

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getItemname() {
        return itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getTraceData() {
        return traceData;
    }

    public void setTraceData(String traceData) {
        this.traceData = traceData;
    }

    public String getDiseaseType() {
        return diseaseType;
    }

    public void setDiseaseType(String diseaseType) {
        this.diseaseType = diseaseType;
    }
}
