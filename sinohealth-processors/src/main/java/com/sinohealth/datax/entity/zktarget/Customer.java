package com.sinohealth.datax.entity.zktarget;

import com.sinohealth.datax.reflection.annotations.db_alias;

@db_alias("customer")
public class Customer {

    @db_alias("vid")
    public String vid;
    @db_alias("jjzh")
    public String jjzh;
    @db_alias("temp_bz")
    public String tempBz;

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public String getJjzh() {
        return jjzh;
    }

    public void setJjzh(String jjzh) {
        this.jjzh = jjzh;
    }

    public String getTempBz() {
        return tempBz;
    }

    public void setTempBz(String tempBz) {
        this.tempBz = tempBz;
    }
}
