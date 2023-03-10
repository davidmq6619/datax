package com.sinohealth.datax.entity.common;

import com.sinohealth.datax.reflection.annotations.db_alias;

import java.io.Serializable;

/**
 * @author mingqiang
 * @date 2022/8/30 - 11:17
 * @desc 诊断
 */
public class BasInspectionKeyword implements Serializable {
    public Long id;
    //一级分类（8项）
    public String category1;

    //二级分类
    public String category2;

    //显示名称
    @db_alias("show_name")
    public String showName;

    //代表疾病
    public String priority;

    //分类
    public String classify;

    //检测类别/方法
    public String method;

    //部位
    public String part;

    //别名
    @db_alias("alias_name")
    public String aliasName;

    //关键词（名词）
    public String keyword;

    //结果（形容词）
    public String result;

    //疾病/异常体征
    @db_alias("disease_signs")
    public String diseaseSigns;

    //注释
    public String notes;

    //联合编号
    @db_alias("union_code")
    public String unionCode;

    //关联度
    @db_alias("union_degree")
    public String unionDegree;

    //性别
    public String sex;

    //适用人群
    @db_alias("applicable_people")
    public String applicablePeople;

    //复查周期
    @db_alias("recheck_cycle")
    public String recheckCycle;

    //备注
    public String remark;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategory1() {
        return category1;
    }

    public void setCategory1(String category1) {
        this.category1 = category1;
    }

    public String getCategory2() {
        return category2;
    }

    public void setCategory2(String category2) {
        this.category2 = category2;
    }

    public String getShowName() {
        return showName;
    }

    public void setShowName(String showName) {
        this.showName = showName;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getClassify() {
        return classify;
    }

    public void setClassify(String classify) {
        this.classify = classify;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getPart() {
        return part;
    }

    public void setPart(String part) {
        this.part = part;
    }

    public String getAliasName() {
        return aliasName;
    }

    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getDiseaseSigns() {
        return diseaseSigns;
    }

    public void setDiseaseSigns(String diseaseSigns) {
        this.diseaseSigns = diseaseSigns;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getUnionCode() {
        return unionCode;
    }

    public void setUnionCode(String unionCode) {
        this.unionCode = unionCode;
    }

    public String getUnionDegree() {
        return unionDegree;
    }

    public void setUnionDegree(String unionDegree) {
        this.unionDegree = unionDegree;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getApplicablePeople() {
        return applicablePeople;
    }

    public void setApplicablePeople(String applicablePeople) {
        this.applicablePeople = applicablePeople;
    }

    public String getRecheckCycle() {
        return recheckCycle;
    }

    public void setRecheckCycle(String recheckCycle) {
        this.recheckCycle = recheckCycle;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
