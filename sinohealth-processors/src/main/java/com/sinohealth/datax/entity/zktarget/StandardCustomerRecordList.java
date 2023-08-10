package com.sinohealth.datax.entity.zktarget;

import com.sinohealth.datax.entity.source.RegMnCustomer;
import com.sinohealth.datax.entity.source.StandardCustomerRecord;

import java.util.List;

/**
 * @author mingqiang
 * @date 2022/8/22 - 15:34
 * @desc
 */
public class StandardCustomerRecordList {

    public List<RegMnCustomer> list;

    public List<RegMnCustomer> getList() {
        return list;
    }

    public void setList(List<RegMnCustomer> list) {
        this.list = list;
    }
}
