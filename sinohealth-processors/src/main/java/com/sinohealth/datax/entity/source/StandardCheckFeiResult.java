package com.sinohealth.datax.entity.source;

import com.sinohealth.datax.reflection.annotations.db_alias;

/**
 * @author mingqiang
 * @date 2023/5/26 - 18:11
 * @desc
 */
public class StandardCheckFeiResult {

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

    /**
     *是否肺结节
     * 0否
     * 1是
     */
    @db_alias("fei_status")
    public String feiStatus;


    /**
     *直径
     */
    @db_alias("zhijing")
    public String zhijing;



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

    public String getFeiStatus() {
        return feiStatus;
    }

    public void setFeiStatus(String feiStatus) {
        this.feiStatus = feiStatus;
    }

    public String getZhijing() {
        return zhijing;
    }

    public void setZhijing(String zhijing) {
        this.zhijing = zhijing;
    }
}
