package com.sinohealth.datax.utils;

/**
 * @author mingqiang
 * @date 2022/9/13 - 11:20
 * @desc 清洗状态值
 */
public enum EtlStatus {
    ETL_DATA(0,"源数据"),
    ETL_SUCCESS(1,"清洗成功"),
    ETL_ERROR_MATCH(2,"字典匹配失败"),
    ETL_DATA_ERROR(3,"脏数据"),
    ETL_MISSING(4,"清洗未命中"),
    ETL_ERROR_DISPERSE(5,"清洗离散型失败"),
    ETL_ERROR(6,"清洗失败"),
    ETL_NOT_DOING(7,"检验或检查未做"),
    ETL_SUSPICIOUS(8,"结果可疑，待复检"),
    ETL_SUCCESS_NORMAL(9, "清洗,无异常");

    private Integer code;
    private String message;

    EtlStatus(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
