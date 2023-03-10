package com.sinohealth.datax.entity.zktarget;

import com.sinohealth.datax.reflection.annotations.db_alias;

import java.util.Date;

/**
 * @Title:
 * @Package com.sinohealth.datax.entity.target
 * @Copyright: Copyright (C) 2022 SinoHealth Co. Ltd, All Rights Reserved
 * @Author heqiang 2022/5/18 17:57
 * @Description: 影像学描述字段清洗结果
 */
@db_alias("pacs_check_result")
public class CheckResultMsEtl {

    /**
     *关联的检查ID
     */
    @db_alias("check_id")
    public Integer checkId;

    /**
     * 体检编号
     */
    public String				vid;

    /**
     * 项目标准名称
     */
    @db_alias("item_name_comm")
    public String				itemNameComm;

    /**
     * 检查结果
     */
    public String				results;

    /**
     * 初始化结果
     */
    @db_alias("init_result")
    public String				initResult;

    @db_alias("init_result_comm")
    public String				initResultComm;

    /**
     * 预约时间
     */
    @db_alias("book_time")
    public Date bookTime;

    /**
     * 创建时间
     */
    @db_alias("create_time")
    public Date createTime;
    /**
     * 清洗状态 0:完成 1:未识别 2:机器识别
     */
    @db_alias("clean_status")
    public Integer				cleanStatus;


    public Integer getCheckId() {
        return checkId;
    }

    public void setCheckId(Integer checkId) {
        this.checkId = checkId;
    }

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public String getItemNameComm() {
        return itemNameComm;
    }

    public void setItemNameComm(String itemNameComm) {
        this.itemNameComm = itemNameComm;
    }

    public String getResults() {
        return results;
    }

    public void setResults(String results) {
        this.results = results;
    }

    public String getInitResult() {
        return initResult;
    }

    public void setInitResult(String initResult) {
        this.initResult = initResult;
    }

    public String getInitResultComm() {
        return initResultComm;
    }

    public void setInitResultComm(String initResultComm) {
        this.initResultComm = initResultComm;
    }

    public Date getBookTime() {
        return bookTime;
    }

    public void setBookTime(Date bookTime) {
        this.bookTime = bookTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getCleanStatus() {
        return cleanStatus;
    }

    public void setCleanStatus(Integer cleanStatus) {
        this.cleanStatus = cleanStatus;
    }
}
