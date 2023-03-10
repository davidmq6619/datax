package com.sinohealth.datax.entity.source;

import com.sinohealth.datax.reflection.annotations.db_alias;

import java.util.Date;


public class StandardCustomerRecord {

    public String vid;
    public String name;//用户姓名
    public String sex;//性别
    public String mobile;
    @db_alias("apply_time")
    public Date applyTime;//体检时间
    @db_alias("customer_csrq")
    public Date customerCsrq;//出生年月
    @db_alias("customer_sfzh")
    public String customerSfzh;
    public String age;
    @db_alias("clean_time")
    public Date cleanTime;
    @db_alias("clean_status")
    public Integer cleanStatus;
    public String remark;
    public String address;
    public String nation;
    @db_alias("marital_Status")
    public String maritalStatus;

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Date getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(Date applyTime) {
        this.applyTime = applyTime;
    }

    public Date getCustomerCsrq() {
        return customerCsrq;
    }

    public void setCustomerCsrq(Date customerCsrq) {
        this.customerCsrq = customerCsrq;
    }

    public Integer getCleanStatus() {
        return cleanStatus;
    }

    public void setCleanStatus(Integer cleanStatus) {
        this.cleanStatus = cleanStatus;
    }

    public String getCustomerSfzh() {
        return customerSfzh;
    }

    public void setCustomerSfzh(String customerSfzh) {
        this.customerSfzh = customerSfzh;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }
}
