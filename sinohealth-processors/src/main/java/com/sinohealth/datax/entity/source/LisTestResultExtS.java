package com.sinohealth.datax.entity.source;

import com.sinohealth.datax.reflection.annotations.db_alias;

import java.util.Date;

/**
 * @author Tony
 * @date 2018/7/27
 **/
@db_alias("lis_test_result")
public class LisTestResultExtS {

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

    private String cid1; // cid
    private String vid1; // vid
    @db_alias("testitem_id1")
    private String testitemId1; // testitem_id
    @db_alias("testitem_name_c1")
    private String testitemNameC1; // 检验项目名称
    private String results1; // 检验结果
    private String unit1; // unit
    @db_alias("normal_l1")
    private String normalL1; // normal_l
    @db_alias("normal_h1")
    private String normalH1; // normal_h
    @db_alias("testitem_ft1")
    private String testitemFt1; // 检验项分类
    private Date yysj1;

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


    public String getCid1() {
        return cid1;
    }

    public void setCid1(String cid1) {
        this.cid1 = cid1;
    }

    public String getVid1() {
        return vid1;
    }

    public void setVid1(String vid1) {
        this.vid1 = vid1;
    }

    public String getTestitemId1() {
        return testitemId1;
    }

    public void setTestitemId1(String testitemId1) {
        this.testitemId1 = testitemId1;
    }

    public String getTestitemNameC1() {
        return testitemNameC1;
    }

    public void setTestitemNameC1(String testitemNameC1) {
        this.testitemNameC1 = testitemNameC1;
    }

    public String getResults1() {
        return results1;
    }

    public void setResults1(String results1) {
        this.results1 = results1;
    }

    public String getUnit1() {
        return unit1;
    }

    public void setUnit1(String unit1) {
        this.unit1 = unit1;
    }

    public String getNormalL1() {
        return normalL1;
    }

    public void setNormalL1(String normalL1) {
        this.normalL1 = normalL1;
    }

    public String getNormalH1() {
        return normalH1;
    }

    public void setNormalH1(String normalH1) {
        this.normalH1 = normalH1;
    }

    public String getTestitemFt1() {
        return testitemFt1;
    }

    public void setTestitemFt1(String testitemFt1) {
        this.testitemFt1 = testitemFt1;
    }

    public Date getYysj1() {
        return yysj1;
    }

    public void setYysj1(Date yysj1) {
        this.yysj1 = yysj1;
    }
}
