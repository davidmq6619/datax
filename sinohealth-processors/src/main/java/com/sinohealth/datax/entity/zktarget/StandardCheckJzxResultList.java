package com.sinohealth.datax.entity.zktarget;

import com.sinohealth.datax.entity.source.StandardCheckFeiResult;
import com.sinohealth.datax.entity.source.StandardCheckJzxResult;

import java.util.List;

/**
 * @author mingqiang
 * @date 2023/5/26 - 18:14
 * @desc
 */
public class StandardCheckJzxResultList {
    public List<StandardCheckJzxResult> list;

    public List<StandardCheckJzxResult> getList() {
        return list;
    }

    public void setList(List<StandardCheckJzxResult> list) {
        this.list = list;
    }
}
