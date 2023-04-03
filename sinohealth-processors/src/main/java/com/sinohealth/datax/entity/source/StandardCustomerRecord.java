package com.sinohealth.datax.entity.source;

import com.sinohealth.datax.reflection.annotations.db_alias;

import java.util.Date;


public class StandardCustomerRecord {

    private String vid;
    private String name;//用户姓名
    private String sex;//性别
    private String mobile;
    @db_alias("apply_time")
    private Date applyTime;//体检时间
    @db_alias("customer_csrq")
    private Date customerCsrq;//出生年月
    @db_alias("customer_sfzh")
    private String customerSfzh;
    private String age;
    @db_alias("clean_time")
    private Date cleanTime;
    @db_alias("clean_status")
    private Integer cleanStatus;
    private String remark;
    private String address;
    private String nation;
    @db_alias("marital_Status")
    private String maritalStatus;
    @db_alias("org_id")
    private String orgId;
    @db_alias("store_id")
    private String storeId;
    @db_alias("package_name")
    private String packageName;
    @db_alias("package_price")
    private String packagePrice;
    @db_alias("report_time")
    private String reportTime;
    @db_alias("is_group")
    private String isGroup;

    public String getIsGroup() {
        return isGroup;
    }

    public void setIsGroup(String isGroup) {
        this.isGroup = isGroup;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getPackagePrice() {
        return packagePrice;
    }

    public void setPackagePrice(String packagePrice) {
        this.packagePrice = packagePrice;
    }

    public String getReportTime() {
        return reportTime;
    }

    public void setReportTime(String reportTime) {
        this.reportTime = reportTime;
    }

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
