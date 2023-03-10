package com.sinohealth.datax.entity.zktarget;

import com.sinohealth.datax.reflection.annotations.db_alias;

/**
 * @author Tony
 * @date 2018/7/27
 **/
@db_alias("test_result")
public class Test {

    public String vid;
    @db_alias("item_ft")
    public String itemFt;

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public String getItemFt() {
        return itemFt;
    }

    public void setItemFt(String itemFt) {
        this.itemFt = itemFt;
    }
}