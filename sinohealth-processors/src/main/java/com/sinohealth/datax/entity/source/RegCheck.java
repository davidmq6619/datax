package com.sinohealth.datax.entity.source;

import com.sinohealth.datax.reflection.annotations.db_alias;

import java.io.Serializable;

/**
 * @author mingqiang
 * @date 2022/10/20 - 15:41
 * @desc 美年检查
 */
public class RegCheck implements Serializable {

    private String vid;

    @db_alias("item_ft")
    private String itemFt;

    @db_alias("item_name")
    private String itemName;

    private String unit;

    private String results;

    @db_alias("normal_l")
    private String normalL;

    @db_alias("normal_h")
    private String normalH;


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

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getResults() {
        return results;
    }

    public void setResults(String results) {
        this.results = results;
    }

    public String getNormalL() {
        return normalL;
    }

    public void setNormalL(String normalL) {
        this.normalL = normalL;
    }

    public String getNormalH() {
        return normalH;
    }

    public void setNormalH(String normalH) {
        this.normalH = normalH;
    }

}
