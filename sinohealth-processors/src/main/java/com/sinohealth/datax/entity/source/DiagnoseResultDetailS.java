package com.sinohealth.datax.entity.source;

import com.sinohealth.datax.reflection.annotations.db_alias;

import java.util.Date;

/**StandardDiagnoseRecord
 * @author Tony
 * @date 2018/8/9
 **/
@db_alias("biz_diagnose_result")
public class DiagnoseResultDetailS {

    public Long id;
    public String vid;
    @db_alias("diagnose_result")
    public String diagnoseResult;
    @db_alias("check_time")
    public Date checkTime;
    @db_alias("book_time")
    public Date bookTime;

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

    public String getDiagnoseResult() {
        return diagnoseResult;
    }

    public void setDiagnoseResult(String diagnoseResult) {
        this.diagnoseResult = diagnoseResult;
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
