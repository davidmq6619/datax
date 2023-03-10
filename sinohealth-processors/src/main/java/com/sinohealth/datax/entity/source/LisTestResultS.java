package com.sinohealth.datax.entity.source;

import com.sinohealth.datax.reflection.annotations.db_alias;

import java.util.Date;

/**
 * @author Tony
 * @date 2018/7/27
 **/
@db_alias("lis_test_result")
public class LisTestResultS {

    private String cid; // cid
    private String vid; // vid
    @db_alias("testitem_id")
    private String testitemId; // testitem_id
    @db_alias("testitem_name_c")
    private String testitemNameC; // 检验项目名称
    private String results; // 检验结果
    private String unit; // unit
    @db_alias("normal_l")
    private String normalL; // normal_l
    @db_alias("normal_h")
    private String normalH; // normal_h
    @db_alias("testitem_ft")
    private String testitemFt; // 检验项分类
    private Date yysj;

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public String getTestitemId() {
        return testitemId;
    }

    public void setTestitemId(String testitemId) {
        this.testitemId = testitemId;
    }

    public String getTestitemNameC() {
        return testitemNameC;
    }

    public void setTestitemNameC(String testitemNameC) {
        this.testitemNameC = testitemNameC;
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

    public String getTestitemFt() {
        return testitemFt;
    }

    public void setTestitemFt(String testitemFt) {
        this.testitemFt = testitemFt;
    }

    public Date getYysj() {
        return yysj;
    }

    public void setYysj(Date yysj) {
        this.yysj = yysj;
    }
}
