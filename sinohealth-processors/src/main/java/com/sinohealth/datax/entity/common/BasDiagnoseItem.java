package com.sinohealth.datax.entity.common;

import com.sinohealth.datax.reflection.annotations.db_alias;

/**
 * <p>
 * 诊断内容字典
 * </p>
 *
 * @author Tony
 * @date 2018/8/7
 */
@db_alias("bas_diagnose_item")
public class BasDiagnoseItem {

	/**
	 * 编号
	 */
	private Integer				id;
	/**
	 * 诊断内容
	 */
	@db_alias("diagnose")
	private String				diagnose;
	/**
	 * 类型(0:正常;1:异常;2:消失)
	 */
	@db_alias("question_type")
	private Integer				questionType;
	/**
	 * 诊断内容(标准)
	 */
	@db_alias("diagnose_comm")
	private String				diagnoseComm;
	@db_alias("clean_status")
	private Integer				cleanStatus;				// 清洗状态
	@db_alias("del_flag")
	private Integer				delFlag;					// 删除标记

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDiagnose() {
		return diagnose;
	}

	public void setDiagnose(String diagnose) {
		this.diagnose = diagnose;
	}

	public Integer getQuestionType() {
		return questionType;
	}

	public void setQuestionType(Integer questionType) {
		this.questionType = questionType;
	}

	public String getDiagnoseComm() {
		return diagnoseComm;
	}

	public void setDiagnoseComm(String diagnoseComm) {
		this.diagnoseComm = diagnoseComm;
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

}
