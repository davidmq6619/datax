package com.sinohealth.datax.entity.zktarget;

import com.sinohealth.datax.entity.source.StandardCheckRecord;
import com.sinohealth.datax.entity.source.StandardMnCheckRecord;

import java.io.Serializable;
import java.util.List;

/**
 * @author mingqiang
 * @date 2022/8/22 - 15:34
 * @desc
 */
public class StandardMnCheckRecordList implements Serializable {

    public List<StandardMnCheckRecord> list;

    public List<StandardMnCheckRecord> getList() {
        return list;
    }

    public void setList(List<StandardMnCheckRecord> list) {
        this.list = list;
    }
}
