package com.sinohealth.datax.common;

import com.sinohealth.datax.entity.common.*;
import com.sinohealth.datax.entity.source.BasTestItemTemp;

import java.util.List;
import java.util.Map;

/**
 * @author Tony
 * @date 2018/7/27
 **/
public class CommonData {

    public DBProperty dbProperty;

    public Map<String, StandardBasTestItem> basTestItemMap;
    public Map<String, StandardBasTestItem> basTestMethodItemMap;
    public Map<String, String> basTestUnitMap;
    public Map<String, BasCheckItem> basCheckItemMap;
    public List<BasItemAlias> basItemAliasList;
    public Map<String, BasTestItemTemp> bastestItemTemp;
    public List<KeywordsLevelDto> keywordsLevelList;
    public List<BasInspectionKeyword> basInspectionKeywordList;
    public Map<String, String> resultDiscreteMap;

    public DBProperty getDbProperty() {
        return dbProperty;
    }

    public void setDbProperty(DBProperty dbProperty) {
        this.dbProperty = dbProperty;
    }

    public Map<String, StandardBasTestItem> getBasTestItemMap() {
        return basTestItemMap;
    }

    public void setBasTestItemMap(Map<String, StandardBasTestItem> basTestItemMap) {
        this.basTestItemMap = basTestItemMap;
    }

    public Map<String, String> getBasTestUnitMap() {
        return basTestUnitMap;
    }

    public void setBasTestUnitMap(Map<String, String> basTestUnitMap) {
        this.basTestUnitMap = basTestUnitMap;
    }

    public Map<String, BasCheckItem> getBasCheckItemMap() {
        return basCheckItemMap;
    }

    public void setBasCheckItemMap(Map<String, BasCheckItem> basCheckItemMap) {
        this.basCheckItemMap = basCheckItemMap;
    }

    public List<BasItemAlias> getBasItemAliasList() {
        return basItemAliasList;
    }

    public void setBasItemAliasList(List<BasItemAlias> basItemAliasList) {
        this.basItemAliasList = basItemAliasList;
    }

    public Map<String, BasTestItemTemp> getBastestItemTemp() {
        return bastestItemTemp;
    }

    public void setBastestItemTemp(Map<String, BasTestItemTemp> bastestItemTemp) {
        this.bastestItemTemp = bastestItemTemp;
    }

    public List<KeywordsLevelDto> getKeywordsLevelList() {
        return keywordsLevelList;
    }

    public void setKeywordsLevelList(List<KeywordsLevelDto> keywordsLevelList) {
        this.keywordsLevelList = keywordsLevelList;
    }

    public List<BasInspectionKeyword> getBasInspectionKeywordList() {
        return basInspectionKeywordList;
    }

    public void setBasInspectionKeywordList(List<BasInspectionKeyword> basInspectionKeywordList) {
        this.basInspectionKeywordList = basInspectionKeywordList;
    }

    public Map<String, String> getResultDiscreteMap() {
        return resultDiscreteMap;
    }

    public void setResultDiscreteMap(Map<String, String> resultDiscreteMap) {
        this.resultDiscreteMap = resultDiscreteMap;
    }

    public Map<String, StandardBasTestItem> getBasTestMethodItemMap() {
        return basTestMethodItemMap;
    }

    public void setBasTestMethodItemMap(Map<String, StandardBasTestItem> basTestMethodItemMap) {
        this.basTestMethodItemMap = basTestMethodItemMap;
    }
}
