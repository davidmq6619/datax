package com.sinohealth.datax.entity.source;

import com.sinohealth.datax.reflection.annotations.db_alias;

import java.util.Date;

@db_alias("view_yyqkb")
public class RegCustomerS {

    /**
     * 单号
     */
    private String vid;
    /**
     * 个人id
     */
    private String cid;
    /**
     * 单位代码
     */
    private String dwdm;
    /**
     * 时间
     */
    private Date yysj;
    private String yydjr;
    private Date yydjsj;
    private String jjzh;
    private String status;
    private String qtdjr;
    private Date qtdjsj;
    private Date tjsj;
    private Date tjzzsj;
    @db_alias("member_type")
    private String memberType;
    @db_alias("print_time")
    private Date printTime;
    @db_alias("cust_name")
    private String custName;
    @db_alias("cust_xb")
    private String custXb;
    @db_alias("cust_csrq")
    private Date custCsrq;
    @db_alias("cust_zy")
    private String custZy;
    @db_alias("cust_gzhy")
    private String custGzhy;
    @db_alias("bz_bm1")
    private String bzBm1;
    @db_alias("bz_bm2")
    private String bzBm2;
    @db_alias("bz_sfzhm")
    private String bzSfzhm;
    @db_alias("temp_bz")
    private String tempBz;

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getDwdm() {
        return dwdm;
    }

    public void setDwdm(String dwdm) {
        this.dwdm = dwdm;
    }

    public Date getYysj() {
        return yysj;
    }

    public void setYysj(Date yysj) {
        this.yysj = yysj;
    }


    public String getYydjr() {
        return yydjr;
    }

    public void setYydjr(String yydjr) {
        this.yydjr = yydjr;
    }

    public Date getYydjsj() {
        return yydjsj;
    }

    public void setYydjsj(Date yydjsj) {
        this.yydjsj = yydjsj;
    }

    public String getJjzh() {
        return jjzh;
    }

    public void setJjzh(String jjzh) {
        this.jjzh = jjzh;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getQtdjr() {
        return qtdjr;
    }

    public void setQtdjr(String qtdjr) {
        this.qtdjr = qtdjr;
    }

    public Date getQtdjsj() {
        return qtdjsj;
    }

    public void setQtdjsj(Date qtdjsj) {
        this.qtdjsj = qtdjsj;
    }

    public Date getTjsj() {
        return tjsj;
    }

    public void setTjsj(Date tjsj) {
        this.tjsj = tjsj;
    }

    public Date getTjzzsj() {
        return tjzzsj;
    }

    public void setTjzzsj(Date tjzzsj) {
        this.tjzzsj = tjzzsj;
    }

    public String getMemberType() {
        return memberType;
    }

    public void setMemberType(String memberType) {
        this.memberType = memberType;
    }

    public Date getPrintTime() {
        return printTime;
    }

    public void setPrintTime(Date printTime) {
        this.printTime = printTime;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getCustXb() {
        return custXb;
    }

    public void setCustXb(String custXb) {
        this.custXb = custXb;
    }

    public Date getCustCsrq() {
        return custCsrq;
    }

    public void setCustCsrq(Date custCsrq) {
        this.custCsrq = custCsrq;
    }

    public String getCustZy() {
        return custZy;
    }

    public void setCustZy(String custZy) {
        this.custZy = custZy;
    }

    public String getCustGzhy() {
        return custGzhy;
    }

    public void setCustGzhy(String custGzhy) {
        this.custGzhy = custGzhy;
    }

    public String getBzBm1() {
        return bzBm1;
    }

    public void setBzBm1(String bzBm1) {
        this.bzBm1 = bzBm1;
    }

    public String getBzBm2() {
        return bzBm2;
    }

    public void setBzBm2(String bzBm2) {
        this.bzBm2 = bzBm2;
    }

    public String getBzSfzhm() {
        return bzSfzhm;
    }

    public void setBzSfzhm(String bzSfzhm) {
        this.bzSfzhm = bzSfzhm;
    }

    public String getTempBz() {
        return tempBz;
    }

    public void setTempBz(String tempBz) {
        this.tempBz = tempBz;
    }
}
