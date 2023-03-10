package com.sinohealth.datax.entity.source;

import com.sinohealth.datax.reflection.annotations.db_alias;

import java.util.Date;

/**
 * @Title:
 * @Package com.sinohealth.datax.entity.source
 * @Copyright: Copyright (C) 2022 SinoHealth Co. Ltd, All Rights Reserved
 * @Author heqiang 2022/5/18 17:43
 * @Description: 影像检查结果描述字段数据
 */
@db_alias("pacs_check_result")
public class CheckResultMsS {

    /**
     *主键
     */
    public Integer id;
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

    public Date getBookTime() {
        return bookTime;
    }

    public void setBookTime(Date bookTime) {
        this.bookTime = bookTime;
    }
}
