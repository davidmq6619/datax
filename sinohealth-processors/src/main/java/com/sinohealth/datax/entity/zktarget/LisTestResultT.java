package com.sinohealth.datax.entity.zktarget;

import com.sinohealth.datax.reflection.annotations.db_alias;

import java.util.Date;

/**
 * @author Tony
 * @date 2018/7/27
 **/
@db_alias("lis_test_result")
public class LisTestResultT {

    public String cid;
    public String vid;
    @db_alias("item_id")
    public String itemId;
    @db_alias("item_ft")
    public String itemFt;
    @db_alias("item_name")
    public String itemName;
    public String results;
    public String unit;
    @db_alias("normal_l")
    public String normalL;
    @db_alias("normal_h")
    public String normalH;
    @db_alias("big_category")
    public String bigCategory;
    @db_alias("small_category")
    public String smallCategory;
    @db_alias("results_discrete")
    public String resultsDiscrete;
    @db_alias("unit_comm")
    public String unitComm;
    @db_alias("items_name_comm")
    public String itemsNameComm;
    @db_alias("group_id")
    public Integer groupId;
    @db_alias("book_time")
    public Date bookTime;
    @db_alias("clean_status")
    public String cleanStatus;
    @db_alias("create_time")
    public Date createTime;
    @db_alias("classify")
    public String classify;
    @db_alias("system")
    public String system;
    @db_alias("is_special")
    public Integer isSpecial;

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

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemFt() {
        return itemFt;
    }

    public void setItemFt(String itemFt) {
        this.itemFt = itemFt;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
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

    public String getResultsDiscrete() {
        return resultsDiscrete;
    }

    public void setResultsDiscrete(String resultsDiscrete) {
        this.resultsDiscrete = resultsDiscrete;
    }

    public String getUnitComm() {
        return unitComm;
    }

    public void setUnitComm(String unitComm) {
        this.unitComm = unitComm;
    }

    public String getItemsNameComm() {
        return itemsNameComm;
    }

    public void setItemsNameComm(String itemsNameComm) {
        this.itemsNameComm = itemsNameComm;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public Date getBookTime() {
        return bookTime;
    }

    public void setBookTime(Date bookTime) {
        this.bookTime = bookTime;
    }

    public String getCleanStatus() {
        return cleanStatus;
    }

    public void setCleanStatus(String cleanStatus) {
        this.cleanStatus = cleanStatus;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
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