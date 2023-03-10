package com.sinohealth.datax.entity.zktarget;

import com.sinohealth.datax.entity.source.StandardCheckRecord;

import java.io.Serializable;
import java.util.List;

/**
 * @author mingqiang
 * @date 2022/8/22 - 15:34
 * @desc
 */
public class StandardCheckRecordList implements Serializable {

    public List<StandardCheckRecord> list;

    public List<StandardCheckRecord> getList() {
        return list;
    }

    public void setList(List<StandardCheckRecord> list) {
        this.list = list;
    }
}
