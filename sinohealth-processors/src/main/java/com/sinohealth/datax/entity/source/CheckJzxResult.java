package com.sinohealth.datax.entity.source;

import com.sinohealth.datax.reflection.annotations.db_alias;

/**
 * @Title:
 * @Package com.sinohealth.datax.entity.source
 * @Copyright: Copyright (C) 2022 SinoHealth Co. Ltd, All Rights Reserved
 * @Author heqiang 2022/5/18 17:43
 * @Description: 影像检查结果描述字段数据
 */
@db_alias("mn_check_data_jj")
public class CheckJzxResult {

    /**
     *体检编号
     */
    @db_alias("vid")
    public String vid;

    /**
     *大项
     */
    @db_alias("category")
    public String category;

    /**
     *结果
     */
    @db_alias("item_name")
    public String itemName;

    /**
     *结果
     */
    @db_alias("result")
    public String result;

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
