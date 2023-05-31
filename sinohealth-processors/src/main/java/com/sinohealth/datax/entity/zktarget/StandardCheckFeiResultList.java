package com.sinohealth.datax.entity.zktarget;

import com.sinohealth.datax.entity.source.StandardCheckFeiResult;
import com.sinohealth.datax.entity.source.StandardCheckRecord;

import java.util.List;

/**
 * @author mingqiang
 * @date 2023/5/26 - 18:14
 * @desc
 */
public class StandardCheckFeiResultList {
    public List<StandardCheckFeiResult> list;

    public List<StandardCheckFeiResult> getList() {
        return list;
    }

    public void setList(List<StandardCheckFeiResult> list) {
        this.list = list;
    }
}
