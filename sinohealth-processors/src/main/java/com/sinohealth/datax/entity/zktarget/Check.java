package com.sinohealth.datax.entity.zktarget;

import com.sinohealth.datax.reflection.annotations.db_alias;

/**
 * @author Tony
 * @date 2018/8/7
 **/
@db_alias("check_result")
public class Check {

    /**
     * 体检编号
     */
    public String				vid;
    /**
     * 初始化结果
     */
    @db_alias("init_results")
    public String				initResults;

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public String getInitResults() {
        return initResults;
    }

    public void setInitResults(String initResults) {
        this.initResults = initResults;
    }
}
