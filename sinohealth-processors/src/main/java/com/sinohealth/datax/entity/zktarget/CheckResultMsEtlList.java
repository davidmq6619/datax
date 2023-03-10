package com.sinohealth.datax.entity.zktarget;

import java.util.List;

/**
 * @Title:
 * @Package com.sinohealth.datax.entity.target
 * @Copyright: Copyright (C) 2022 SinoHealth Co. Ltd, All Rights Reserved
 * @Author heqiang 2022/5/18 18:03
 * @Description: 洗后批量结果
 */
public class CheckResultMsEtlList {

    public List<CheckResultMsEtl> list;

    public List<CheckResultMsEtl> getList() {
        return list;
    }

    public void setList(List<CheckResultMsEtl> list) {
        this.list = list;
    }
}
