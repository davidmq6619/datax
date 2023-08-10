package com.sinohealth.datax.entity.zktarget;

import com.sinohealth.datax.entity.common.BasTestItem;
import com.sinohealth.datax.entity.source.BasMnTest;
import com.sinohealth.datax.entity.source.StandardMnTestRecord;
import com.sinohealth.datax.entity.source.StandardTestRecord;

import java.util.List;

/**
 * @author mingqiang
 * @date 2022/8/22 - 15:34
 * @desc
 */
public class StandardMnTestRecordList {

    public List<StandardMnTestRecord> list;

    public List<StandardMnTestRecord> getList() {
        return list;
    }

    public void setList(List<StandardMnTestRecord> list) {
        this.list = list;
    }
}
