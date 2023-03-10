package com.sinohealth.datax.entity.common;

import com.sinohealth.datax.reflection.annotations.db_alias;

/**
 * <p>
 * 年龄段疾病比例
 * </p>
 *
 * @author Tony
 * @date 2018/8/7
 */
public class BasCheckItemInit {
    /**
     * 编号
     */
    public Integer id;
    /**
     * 初始化结果
     */
    @db_alias("init_result")
    public String initResult;

    @db_alias("init_result_comm")
    public String initResultComm;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    @Override
    public String toString() {
        return "BasDiseaseAgeRatio{" + ", id=" + id + ", initResult=" + initResult + ", initResultComm="
                + initResultComm + "}";
    }

}
