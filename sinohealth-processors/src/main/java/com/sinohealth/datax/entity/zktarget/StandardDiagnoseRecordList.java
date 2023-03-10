package com.sinohealth.datax.entity.zktarget;

import com.sinohealth.datax.entity.source.StandardDiagnoseRecord;

import java.io.Serializable;
import java.util.List;

/**
 * @author mingqiang
 * @date 2022/8/22 - 15:34
 * @desc
 */
public class StandardDiagnoseRecordList implements Serializable {

    public List<StandardDiagnoseRecord> list;

    public List<StandardDiagnoseRecord> getList() {
        return list;
    }

    public void setList(List<StandardDiagnoseRecord> list) {
        this.list = list;
    }
}
