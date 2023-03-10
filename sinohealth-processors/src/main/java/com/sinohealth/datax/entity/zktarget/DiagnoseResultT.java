package com.sinohealth.datax.entity.zktarget;

import com.sinohealth.datax.reflection.annotations.db_alias;

import java.util.Date;

/**
 * <p>
 * 客户诊断内容
 * </p>
 *
 * @author Tony
 * @date 2018/8/7
 */
@db_alias("biz_diagnose_result")
public class DiagnoseResultT {

	/**
	 * 编号
	 */
	public Long						id;
	/**
	 * 体检流水号
	 */
	public String						vid;
	/**
	 * 项目编号
	 */
	@db_alias("item_id")
	public String						itemId;
	/**
	 * 诊断内容
	 */
	@db_alias("diagnose_result")
	public String						diagnoseResult;
	/**
	 * 项目描述
	 */
	@db_alias("item_ms")
	public String						itemMs;
	/**
	 * 项目描述贮备
	 */
	@db_alias("item_ms_bak")
	public String						itemMsBak;
	/**
	 * 模版内容
	 */
	@db_alias("temp_content")
	public String						tempContent;
	/**
	 * 是否患病
	 */
	@db_alias("is_ill")
	public String						isIll;
	/**
	 * 诊断建议
	 */
	@db_alias("diagnose_suggest")
	public String						diagnoseSuggest;
	/**
	 * 医学解释
	 */
	@db_alias("medical_explain")
	public String						medicalExplain;
	/**
	 * 原因后果
	 */
	@db_alias("cause_effect")
	public String						causeEffect;
	/**
	 * 医学建议
	 */
	public String						suggest;
	/**
	 * 登记时间
	 */
	@db_alias("check_time")
	public Date						checkTime;
	/**
	 * 检查部位
	 */
	public String						position;
	/**
	 * 地区编码
	 */
	@db_alias("in_factory")
	public String						inFactory;
	/**
	 * 操作医生
	 */
	public String						doctor;
	/**
	 * 结果
	 */
	public String						results;
	/**
	 * 范围
	 */
	@db_alias("normal_range")
	public String						normalRange;
	/**
	 * 单项报告标题
	 */
	@db_alias("item_title")
	public String						itemTitle;
	/**
	 * 疾病名称
	 */
	@db_alias("ill_name")
	public String						illName;
	/**
	 * 清洗状态 0:完成 1:未识别 2:机器识别
	 */
	@db_alias("clean_status")
	public Integer						cleanStatus;
	/**
	 * 预约时间
	 */
	@db_alias("book_time")
	public Date						bookTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getDiagnoseResult() {
		return diagnoseResult;
	}

	public void setDiagnoseResult(String diagnoseResult) {
		this.diagnoseResult = diagnoseResult;
	}

	public String getItemMs() {
		return itemMs;
	}

	public void setItemMs(String itemMs) {
		this.itemMs = itemMs;
	}

	public String getItemMsBak() {
		return itemMsBak;
	}

	public void setItemMsBak(String itemMsBak) {
		this.itemMsBak = itemMsBak;
	}

	public String getTempContent() {
		return tempContent;
	}

	public void setTempContent(String tempContent) {
		this.tempContent = tempContent;
	}

	public String getIsIll() {
		return isIll;
	}

	public void setIsIll(String isIll) {
		this.isIll = isIll;
	}

	public String getDiagnoseSuggest() {
		return diagnoseSuggest;
	}

	public void setDiagnoseSuggest(String diagnoseSuggest) {
		this.diagnoseSuggest = diagnoseSuggest;
	}

	public String getMedicalExplain() {
		return medicalExplain;
	}

	public void setMedicalExplain(String medicalExplain) {
		this.medicalExplain = medicalExplain;
	}

	public String getCauseEffect() {
		return causeEffect;
	}

	public void setCauseEffect(String causeEffect) {
		this.causeEffect = causeEffect;
	}

	public String getSuggest() {
		return suggest;
	}

	public void setSuggest(String suggest) {
		this.suggest = suggest;
	}

	public Date getCheckTime() {
		return checkTime;
	}

	public void setCheckTime(Date checkTime) {
		this.checkTime = checkTime;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getInFactory() {
		return inFactory;
	}

	public void setInFactory(String inFactory) {
		this.inFactory = inFactory;
	}

	public String getDoctor() {
		return doctor;
	}

	public void setDoctor(String doctor) {
		this.doctor = doctor;
	}

	public String getResults() {
		return results;
	}

	public void setResults(String results) {
		this.results = results;
	}

	public String getNormalRange() {
		return normalRange;
	}

	public void setNormalRange(String normalRange) {
		this.normalRange = normalRange;
	}

	public String getItemTitle() {
		return itemTitle;
	}

	public void setItemTitle(String itemTitle) {
		this.itemTitle = itemTitle;
	}

	public String getIllName() {
		return illName;
	}

	public void setIllName(String illName) {
		this.illName = illName;
	}

	public Integer getCleanStatus() {
		return cleanStatus;
	}

	public void setCleanStatus(Integer cleanStatus) {
		this.cleanStatus = cleanStatus;
	}

	public Date getBookTime() {
		return bookTime;
	}

	public void setBookTime(Date bookTime) {
		this.bookTime = bookTime;
	}

}
