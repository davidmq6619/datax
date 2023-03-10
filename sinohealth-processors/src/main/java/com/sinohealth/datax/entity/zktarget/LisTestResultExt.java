package com.sinohealth.datax.entity.zktarget;

import com.sinohealth.datax.reflection.annotations.db_alias;

@db_alias("lis_test_result_ext")
public class LisTestResultExt {

    public java.lang.Integer id;//remark:编号;length:10; not null,default:null
	public java.lang.String cid;//remark:体检案例编号;length:12
	public java.lang.String vid;//remark:体检编号;length:12
	@db_alias("item_id")
	public java.lang.String itemId;//remark:检验项目编号;length:6
	@db_alias("item_ft")
	public java.lang.String itemFt;//remark:检验项分类;length:50
	@db_alias("big_category")
	public java.lang.String bigCategory;//remark:;length:50
	@db_alias("small_category")
	public java.lang.String smallCategory;//remark:;length:50
	@db_alias("item_name")
	public java.lang.String itemName;//remark:检验项目名称;length:150
	@db_alias("items_name_comm")
	public java.lang.String itemsNameComm;//remark:检验项标准名称;length:45
	public java.lang.String unit;//remark:检验单位;length:50
	@db_alias("unit_comm")
	public java.lang.String unitComm;//remark:检验单位(标准);length:50
	public java.lang.String results;//remark:检验结果;length:400
	@db_alias("results_discrete")
	public java.lang.String resultsDiscrete;//remark:检验结果(离散化);length:50
	@db_alias("normal_l")
	public java.lang.String normalL;//remark:参考值下限;length:40
	@db_alias("normal_h")
	public java.lang.String normalH;//remark:参考值上限;length:40
	@db_alias("create_time")
	public java.util.Date createTime;//remark:操作时间;length:19
	@db_alias("group_id")
	public java.lang.Integer groupId;//remark:;length:10
	@db_alias("clean_status")
	public java.lang.String cleanStatus;//remark:清洗状态：0：确认后数据、1：未能识别数据、2：机器识别数据;length:1
	@db_alias("book_time")
	public java.util.Date bookTime;//remark:预约时间;length:19
	public java.lang.String cid1;//remark:体检案例编号;length:12
	public java.lang.String vid1;//remark:体检编号;length:12
	@db_alias("item_id1")
	public java.lang.String itemId1;//remark:检验项目编号;length:6
	@db_alias("item_ft1")
	public java.lang.String itemFt1;//remark:检验项分类;length:50
	@db_alias("big_category1")
	public java.lang.String bigCategory1;//remark:;length:50
	@db_alias("small_category1")
	public java.lang.String smallCategory1;//remark:;length:50
	@db_alias("item_name1")
	public java.lang.String itemName1;//remark:检验项目名称;length:150
	@db_alias("items_name_comm1")
	public java.lang.String itemsNameComm1;//remark:检验项标准名称;length:45
	public java.lang.String unit1;//remark:检验单位;length:50
	@db_alias("unit_comm1")
	public java.lang.String unitComm1;//remark:检验单位(标准);length:50
	public java.lang.String results1;//remark:检验结果;length:400
	@db_alias("results_discrete1")
	public java.lang.String resultsDiscrete1;//remark:检验结果(离散化);length:50
	@db_alias("normal_l1")
	public java.lang.String normalL1;//remark:参考值下限;length:40
	@db_alias("normal_h1")
	public java.lang.String normalH1;//remark:参考值上限;length:40
	@db_alias("create_time1")
	public java.util.Date createTime1;//remark:操作时间;length:19
	@db_alias("group_id1")
	public java.lang.Integer groupId1;//remark:;length:10
	@db_alias("clean_status1")
	public java.lang.String cleanStatus1;//remark:清洗状态：0：确认后数据、1：未能识别数据、2：机器识别数据;length:1
	@db_alias("book_time1")
	public java.util.Date bookTime1;//remark:预约时间;length:19

	public LisTestResultExt() {
	}

	public void setId(java.lang.Integer id) {
		this.id = id;
	}

	public java.lang.Integer getId() {
		return id;
	}

	public void setCid(java.lang.String cid) {
		this.cid = cid;
	}

	public java.lang.String getCid() {
		return cid;
	}

	public void setVid(java.lang.String vid) {
		this.vid = vid;
	}

	public java.lang.String getVid() {
		return vid;
	}

	public void setItemId(java.lang.String itemId) {
		this.itemId = itemId;
	}

	public java.lang.String getItemId() {
		return itemId;
	}

	public void setItemFt(java.lang.String itemFt) {
		this.itemFt = itemFt;
	}

	public java.lang.String getItemFt() {
		return itemFt;
	}

	public void setBigCategory(java.lang.String bigCategory) {
		this.bigCategory = bigCategory;
	}

	public java.lang.String getBigCategory() {
		return bigCategory;
	}

	public void setSmallCategory(java.lang.String smallCategory) {
		this.smallCategory = smallCategory;
	}

	public java.lang.String getSmallCategory() {
		return smallCategory;
	}

	public void setItemName(java.lang.String itemName) {
		this.itemName = itemName;
	}

	public java.lang.String getItemName() {
		return itemName;
	}

	public void setItemsNameComm(java.lang.String itemsNameComm) {
		this.itemsNameComm = itemsNameComm;
	}

	public java.lang.String getItemsNameComm() {
		return itemsNameComm;
	}

	public void setUnit(java.lang.String unit) {
		this.unit = unit;
	}

	public java.lang.String getUnit() {
		return unit;
	}

	public void setUnitComm(java.lang.String unitComm) {
		this.unitComm = unitComm;
	}

	public java.lang.String getUnitComm() {
		return unitComm;
	}

	public void setResults(java.lang.String results) {
		this.results = results;
	}

	public java.lang.String getResults() {
		return results;
	}

	public void setResultsDiscrete(java.lang.String resultsDiscrete) {
		this.resultsDiscrete = resultsDiscrete;
	}

	public java.lang.String getResultsDiscrete() {
		return resultsDiscrete;
	}

	public void setNormalL(java.lang.String normalL) {
		this.normalL = normalL;
	}

	public java.lang.String getNormalL() {
		return normalL;
	}

	public void setNormalH(java.lang.String normalH) {
		this.normalH = normalH;
	}

	public java.lang.String getNormalH() {
		return normalH;
	}

	public void setCreateTime(java.util.Date createTime) {
		this.createTime = createTime;
	}

	public java.util.Date getCreateTime() {
		return createTime;
	}

	public void setGroupId(java.lang.Integer groupId) {
		this.groupId = groupId;
	}

	public java.lang.Integer getGroupId() {
		return groupId;
	}

	public void setCleanStatus(java.lang.String cleanStatus) {
		this.cleanStatus = cleanStatus;
	}

	public java.lang.String getCleanStatus() {
		return cleanStatus;
	}

	public void setBookTime(java.util.Date bookTime) {
		this.bookTime = bookTime;
	}

	public java.util.Date getBookTime() {
		return bookTime;
	}

	public void setCid1(java.lang.String cid1) {
		this.cid1 = cid1;
	}

	public java.lang.String getCid1() {
		return cid1;
	}

	public void setVid1(java.lang.String vid1) {
		this.vid1 = vid1;
	}

	public java.lang.String getVid1() {
		return vid1;
	}

	public void setItemId1(java.lang.String itemId1) {
		this.itemId1 = itemId1;
	}

	public java.lang.String getItemId1() {
		return itemId1;
	}

	public void setItemFt1(java.lang.String itemFt1) {
		this.itemFt1 = itemFt1;
	}

	public java.lang.String getItemFt1() {
		return itemFt1;
	}

	public void setBigCategory1(java.lang.String bigCategory1) {
		this.bigCategory1 = bigCategory1;
	}

	public java.lang.String getBigCategory1() {
		return bigCategory1;
	}

	public void setSmallCategory1(java.lang.String smallCategory1) {
		this.smallCategory1 = smallCategory1;
	}

	public java.lang.String getSmallCategory1() {
		return smallCategory1;
	}

	public void setItemName1(java.lang.String itemName1) {
		this.itemName1 = itemName1;
	}

	public java.lang.String getItemName1() {
		return itemName1;
	}

	public void setItemsNameComm1(java.lang.String itemsNameComm1) {
		this.itemsNameComm1 = itemsNameComm1;
	}

	public java.lang.String getItemsNameComm1() {
		return itemsNameComm1;
	}

	public void setUnit1(java.lang.String unit1) {
		this.unit1 = unit1;
	}

	public java.lang.String getUnit1() {
		return unit1;
	}

	public void setUnitComm1(java.lang.String unitComm1) {
		this.unitComm1 = unitComm1;
	}

	public java.lang.String getUnitComm1() {
		return unitComm1;
	}

	public void setResults1(java.lang.String results1) {
		this.results1 = results1;
	}

	public java.lang.String getResults1() {
		return results1;
	}

	public void setResultsDiscrete1(java.lang.String resultsDiscrete1) {
		this.resultsDiscrete1 = resultsDiscrete1;
	}

	public java.lang.String getResultsDiscrete1() {
		return resultsDiscrete1;
	}

	public void setNormalL1(java.lang.String normalL1) {
		this.normalL1 = normalL1;
	}

	public java.lang.String getNormalL1() {
		return normalL1;
	}

	public void setNormalH1(java.lang.String normalH1) {
		this.normalH1 = normalH1;
	}

	public java.lang.String getNormalH1() {
		return normalH1;
	}

	public void setCreateTime1(java.util.Date createTime1) {
		this.createTime1 = createTime1;
	}

	public java.util.Date getCreateTime1() {
		return createTime1;
	}

	public void setGroupId1(java.lang.Integer groupId1) {
		this.groupId1 = groupId1;
	}

	public java.lang.Integer getGroupId1() {
		return groupId1;
	}

	public void setCleanStatus1(java.lang.String cleanStatus1) {
		this.cleanStatus1 = cleanStatus1;
	}

	public java.lang.String getCleanStatus1() {
		return cleanStatus1;
	}

	public void setBookTime1(java.util.Date bookTime1) {
		this.bookTime1 = bookTime1;
	}

	public java.util.Date getBookTime1() {
		return bookTime1;
	}
}