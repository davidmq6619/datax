package com.sinohealth.datax.entity.common;

import java.io.Serializable;
import java.util.List;

/**
 * @author mingqiang
 * @date 2022/8/30 - 9:53
 * @desc 关键字等级
 */
public class KeywordsLevelDto implements Serializable {
    public Integer level;
    public List<String> keywords;

    public List<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }
}
