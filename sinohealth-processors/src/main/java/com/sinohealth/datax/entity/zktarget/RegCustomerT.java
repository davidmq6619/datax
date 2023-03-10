package com.sinohealth.datax.entity.zktarget;

import com.sinohealth.datax.reflection.annotations.db_alias;

import java.util.Date;

/**
 * @author Tony
 * @date 2018/8/7
 **/
@db_alias("biz_reg_customer")
public class RegCustomerT {

    /**
     * 体检流水号
     */
    @db_alias("vid")
    public String				vid;
    /**
     * 体检案例编号
     */
    @db_alias("cid")
    public String				cid;
    /**
     * 姓名
     */
    @db_alias("name")
    public String				name;

    /**
     * 性别
     */
    @db_alias("sex")
    public String				sex;
    /**
     * 出生日期
     */
    @db_alias("birth_date")
    public Date birthDate;
    /**
     * 客户职业
     */
    @db_alias("job")
    public String				job;
    /**
     * 工作行业
     */
    @db_alias("job_industry")
    public String				jobIndustry;
    /**
     * 预约时间
     */
    @db_alias("book_time")
    public Date				bookTime;
    /**
     * 预约登记人
     */
    @db_alias("book_person")
    public String				bookPerson;
    /**
     * 预约登记时间
     */
    @db_alias("book_reg_time")
    public Date				bookRegTime;
    /**
     * 门店编号
     */
    @db_alias("shop_no")
    public String				shopNo;
    /**
     * 状态
     */
    @db_alias("status")
    public String				status;
    /**
     * 其他登记人
     */
    @db_alias("other_reg_person")
    public String				otherRegPerson;
    /**
     * 其他登记时间
     */
    @db_alias("other_reg_time")
    public Date				otherRegTime;
    /**
     * 体检时间
     */
    @db_alias("body_check_time")
    public Date				bodyCheckTime;
    /**
     * 体检组织时间
     */
    @db_alias("body_check_org_time")
    public Date				bodyCheckOrgTime;
    /**
     * 会员类型
     */
    @db_alias("member_type")
    public String				memberType;
    /**
     * 打印时间
     */
    @db_alias("print_time")
    public Date				printTime;
    @db_alias("age")
    public Integer				age;
    @db_alias("age_discrete")
    public Integer				ageDiscrete;
    /**
     * 清洗状态 0:完成 1:未识别 2:机器识别
     */
    @db_alias("clean_status")
    public Integer				cleanStatus;
    @db_alias("clean_date")
    public Date cleanDate;
    @db_alias("company_code")
    public String				companyCode;				// 单位编码
    @db_alias("department")
    public String				department;					//部门
    @db_alias("id_card")
    public String				idCard;						//身份证号

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

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getJobIndustry() {
        return jobIndustry;
    }

    public void setJobIndustry(String jobIndustry) {
        this.jobIndustry = jobIndustry;
    }

    public Date getBookTime() {
        return bookTime;
    }

    public void setBookTime(Date bookTime) {
        this.bookTime = bookTime;
    }

    public String getBookPerson() {
        return bookPerson;
    }

    public void setBookPerson(String bookPerson) {
        this.bookPerson = bookPerson;
    }

    public Date getBookRegTime() {
        return bookRegTime;
    }

    public void setBookRegTime(Date bookRegTime) {
        this.bookRegTime = bookRegTime;
    }

    public String getShopNo() {
        return shopNo;
    }

    public void setShopNo(String shopNo) {
        this.shopNo = shopNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOtherRegPerson() {
        return otherRegPerson;
    }

    public void setOtherRegPerson(String otherRegPerson) {
        this.otherRegPerson = otherRegPerson;
    }

    public Date getOtherRegTime() {
        return otherRegTime;
    }

    public void setOtherRegTime(Date otherRegTime) {
        this.otherRegTime = otherRegTime;
    }

    public Date getBodyCheckTime() {
        return bodyCheckTime;
    }

    public void setBodyCheckTime(Date bodyCheckTime) {
        this.bodyCheckTime = bodyCheckTime;
    }

    public Date getBodyCheckOrgTime() {
        return bodyCheckOrgTime;
    }

    public void setBodyCheckOrgTime(Date bodyCheckOrgTime) {
        this.bodyCheckOrgTime = bodyCheckOrgTime;
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

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getAgeDiscrete() {
        return ageDiscrete;
    }

    public void setAgeDiscrete(Integer ageDiscrete) {
        this.ageDiscrete = ageDiscrete;
    }

    public Integer getCleanStatus() {
        return cleanStatus;
    }

    public void setCleanStatus(Integer cleanStatus) {
        this.cleanStatus = cleanStatus;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public Date getCleanDate() {
        return cleanDate;
    }

    public void setCleanDate(Date cleanDate) {
        this.cleanDate = cleanDate;
    }
}
