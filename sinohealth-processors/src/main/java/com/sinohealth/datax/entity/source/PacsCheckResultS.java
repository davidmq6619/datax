package com.sinohealth.datax.entity.source;

import com.sinohealth.datax.reflection.annotations.db_alias;

import java.util.Date;

/**
 * @author Tony
 * @date 2018/8/7
 **/
@db_alias("jj_comm_jc_result")
public class PacsCheckResultS {

    public String				vid;
    public String				cid;
    @db_alias("item_id")
    public String				itemId;
    @db_alias("field_comment")
    public String				fieldComment;
    @db_alias("field_results")
    public String				fieldResults;
    @db_alias("zcz_xx")
    public String				zczXx;
    @db_alias("zcz_sx")
    public String				zczSx;
    public String				dw;
    @db_alias("init_results")
    public String				initResults;
    @db_alias("in_factory")
    public String				inFactory;
    @db_alias("op_datetime")
    public String				opDatetime;
    public Date                yysj;

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getFieldComment() {
        return fieldComment;
    }

    public void setFieldComment(String fieldComment) {
        this.fieldComment = fieldComment;
    }

    public String getFieldResults() {
        return fieldResults;
    }

    public void setFieldResults(String fieldResults) {
        this.fieldResults = fieldResults;
    }

    public String getZczXx() {
        return zczXx;
    }

    public void setZczXx(String zczXx) {
        this.zczXx = zczXx;
    }

    public String getZczSx() {
        return zczSx;
    }

    public void setZczSx(String zczSx) {
        this.zczSx = zczSx;
    }

    public String getDw() {
        return dw;
    }

    public void setDw(String dw) {
        this.dw = dw;
    }

    public String getInitResults() {
        return initResults;
    }

    public void setInitResults(String initResults) {
        this.initResults = initResults;
    }

    public String getInFactory() {
        return inFactory;
    }

    public void setInFactory(String inFactory) {
        this.inFactory = inFactory;
    }


    public String getOpDatetime() {
        return opDatetime;
    }

    public void setOpDatetime(String opDatetime) {
        this.opDatetime = opDatetime;
    }

    public Date getYysj() {
        return yysj;
    }

    public void setYysj(Date yysj) {
        this.yysj = yysj;
    }
}
