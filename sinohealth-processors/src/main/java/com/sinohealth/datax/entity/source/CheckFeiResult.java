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
@db_alias("eshlyy_user_report_info")
public class CheckFeiResult {

    /**
     *体检编号
     */
    @db_alias("member_id")
    public String memberId;

    /**
     *大项
     */
    @db_alias("class_name")
    public String className;

    /**
     *结果
     */
    @db_alias("result")
    public String result;

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

}
