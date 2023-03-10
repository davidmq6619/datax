package com.sinohealth.datax.entity.zktarget;

import com.sinohealth.datax.reflection.annotations.db_alias;

import java.util.Date;

/**
 * <p>
 * 客户诊断内容标准化
 * </p>
 *
 * @author Tony
 * @date 2018/8/7
 */
@db_alias("biz_diagnose_result_detail")
public class DiagnoseResultDetailT {

	/**
	 * 编号
	 */
	public Integer				id;
	@db_alias("result_id")
	public Long				resultId;
	/**
	 * 体检编号
	 */
	public String				vid;
	/**
	 * 诊断内容
	 */
	@db_alias("diagnose_result")
	public String				diagnoseResult;
	/**
	 * 诊断内容标准化
	 */
	@db_alias("diagnose_result_comm")
	public String				diagnoseResultComm;
	@db_alias("question_type")
	public Integer				questionType;
	/**
	 * 登记时间
	 */
	@db_alias("check_time")
	public Date				checkTime;
	/**
	 * 预约时间
	 */
	@db_alias("book_time")
	public Date bookTime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Long getResultId() {
		return resultId;
	}

	public void setResultId(Long resultId) {
		this.resultId = resultId;
	}

	public String getVid() {
		return vid;
	}

	public void setVid(String vid) {
		this.vid = vid;
	}

	public String getDiagnoseResult() {
		return diagnoseResult;
	}

	public void setDiagnoseResult(String diagnoseResult) {
		this.diagnoseResult = diagnoseResult;
	}

	public String getDiagnoseResultComm() {
		return diagnoseResultComm;
	}

	public void setDiagnoseResultComm(String diagnoseResultComm) {
		this.diagnoseResultComm = diagnoseResultComm;
	}

	public Integer getQuestionType() {
		return questionType;
	}

	public void setQuestionType(Integer questionType) {
		this.questionType = questionType;
	}

	public Date getCheckTime() {
		return checkTime;
	}

	public void setCheckTime(Date checkTime) {
		this.checkTime = checkTime;
	}

	public Date getBookTime() {
		return bookTime;
	}

	public void setBookTime(Date bookTime) {
		this.bookTime = bookTime;
	}
}
