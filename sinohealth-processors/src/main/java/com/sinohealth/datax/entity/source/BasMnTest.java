package com.sinohealth.datax.entity.source;

import com.sinohealth.datax.reflection.annotations.db_alias;

import java.io.Serializable;
import java.util.Date;

/**
 * 检验结果Result
 *
 * @author mingqiang
 * @desc 检验数据
 */
public class BasMnTest implements Serializable {

    public String vid;
    @db_alias("item_ft")
    public String itemFt;                // 大类
    @db_alias("item_name")
    public String itemName;                // 小类

    @db_alias("results")
    public String results;                // 结果

    public String unit;                    // 单位

    @db_alias("normal_l")
    public String normalL;                    // 上下限

    @db_alias("normal_h")
    public String normalH;

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

    public String getResults() {
        return results;
    }

    public void setResults(String results) {
        this.results = results;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
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