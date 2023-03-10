package com.sinohealth.datax.entity.common;

import com.sinohealth.datax.reflection.annotations.db_alias;

/**
 * 检验结果Result
 *
 * @author ld
 * @version 2017-07-05
 */
public class BasTestItem {

    public Integer id;
    @db_alias("testitem_name_c")
    public String testitemNameC;
    @db_alias("testitem_ft")
    public String testitemFt;
    @db_alias("big_category")
    public String bigCategory;                // 大类
    @db_alias("small_category")
    public String smallCategory;                // 小类
    @db_alias("item_name_comm")
    public String itemNameComm;                // 中文标准名称
    @db_alias("clean_status")
    public Integer cleanStatus;                // 清洗状态
    @db_alias("del_flag")
    public Integer delFlag;                    // 删除标记
    @db_alias("group_id")
    public Integer groupId;                    // 行号
    public Integer items;                        // 单项个数
    @db_alias("classify")
    public String classify;
    @db_alias("system")
    public String system;
    @db_alias("is_special")
    public Integer isSpecial;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTestitemNameC() {
        return testitemNameC;
    }

    public void setTestitemNameC(String testitemNameC) {
        this.testitemNameC = testitemNameC;
    }

    public String getTestitemFt() {
        return testitemFt;
    }

    public void setTestitemFt(String testitemFt) {
        this.testitemFt = testitemFt;
    }

    public String getBigCategory() {
        return bigCategory;
    }

    public void setBigCategory(String bigCategory) {
        this.bigCategory = bigCategory;
    }

    public String getSmallCategory() {
        return smallCategory;
    }

    public void setSmallCategory(String smallCategory) {
        this.smallCategory = smallCategory;
    }

    public String getItemNameComm() {
        return itemNameComm;
    }

    public void setItemNameComm(String itemNameComm) {
        this.itemNameComm = itemNameComm;
    }

    public Integer getCleanStatus() {
        return cleanStatus;
    }

    public void setCleanStatus(Integer cleanStatus) {
        this.cleanStatus = cleanStatus;
    }

    public Integer getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public Integer getItems() {
        return items;
    }

    public void setItems(Integer items) {
        this.items = items;
    }

    public String getClassify() {
        return classify;
    }

    public void setClassify(String classify) {
        this.classify = classify;
    }

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public Integer getIsSpecial() {
        return isSpecial;
    }

    public void setIsSpecial(Integer isSpecial) {
        this.isSpecial = isSpecial;
    }
}