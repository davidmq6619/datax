package com.sinohealth.datax.plugin.rdbms.init;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.datax.plugin.rdbms.util.DBUtil;
import com.alibaba.datax.plugin.rdbms.util.DataBaseType;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.sinohealth.datax.entity.common.*;
import com.sinohealth.datax.utils.DBHelper;
import com.sinohealth.datax.utils.EtlConst;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Tony
 * @date 2018/7/27
 **/
public class BizDataIniter {

    public static final Logger LOG = LoggerFactory.getLogger(BizDataIniter.class);

    public static final String PREFIX = "~";

    public DataBaseType dataBaseType;
    public String url;
    public String user;
    public String passwd;

    public BizDataIniter(DataBaseType dataBaseType, String url, String user, String passwd) {
        this.dataBaseType = dataBaseType;
        this.url = url;
        this.user = user;
        this.passwd = passwd;
    }

    public Map<String, StandardBasTestItem> findBasTestItemMap() {

        Map<String, StandardBasTestItem> basTestItemMap = Maps.newHashMap();
        String sql = "select * from standard_bas_test_item";
        Connection connection = DBUtil.getConnection(this.dataBaseType, this.url, this.user, this.passwd);
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        try {
            connection.setAutoCommit(false);
            preparedStatement = connection.prepareStatement(sql);
            rs = preparedStatement.executeQuery();
            List<StandardBasTestItem> basTestItemList = DBHelper.parseResultList(StandardBasTestItem.class, rs);
            connection.commit();
            for (StandardBasTestItem item : basTestItemList) {
                String testitemFt =  "";
                if(StrUtil.isNotBlank(item.getItemName())){
                    testitemFt = item.getItemName().replace("（", "(")
                                .replace("）",")").toLowerCase().trim();
                }
                basTestItemMap.put(testitemFt, item);
            }
            return basTestItemMap;
        } catch (Exception e) {
            LOG.error("findBasTestItemMap occur error!!", e);
            e.printStackTrace();
        } finally {
            DBUtil.closeDBResources(preparedStatement, connection);
        }
        return basTestItemMap;
    }

    public Map<String, StandardBasTestItem> findBasTestMethodItemMap() {

        Map<String, StandardBasTestItem> basTestItemMap = Maps.newHashMap();
        String sql = "select * from standard_bas_test_item";
        String sql2 = "select * from bas_item_alias";

        List<BasItemAlias> basCheckItemList = new ArrayList<>();
        Connection connection = DBUtil.getConnection(this.dataBaseType, this.url, this.user, this.passwd);
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        try {
            connection.setAutoCommit(false);
            preparedStatement = connection.prepareStatement(sql);
            rs = preparedStatement.executeQuery();
            List<StandardBasTestItem> basTestItemList = DBHelper.parseResultList(StandardBasTestItem.class, rs);
            preparedStatement = connection.prepareStatement(sql2);
            rs = preparedStatement.executeQuery();
            basCheckItemList = DBHelper.parseResultList(BasItemAlias.class, rs);
            connection.commit();
            for (StandardBasTestItem item : basTestItemList) {
                if (StrUtil.isBlank(item.getAliasIds())) {
                    String testitemFt =  "";
                    if(StrUtil.isNotBlank(item.getItemName())){
                        testitemFt = item.getItemName().replace("（", "(")
                                .replace("）",")").toLowerCase().trim();
                    }
                    basTestItemMap.put(testitemFt, item);
                } else {
                    String[] list = item.getAliasIds().split(",");
                    for (String s : list) {
                        for (BasItemAlias itemAlias : basCheckItemList) {
                            String alias = itemAlias.getAliasName().replace("（", "(")
                                    .replace("）",")").toLowerCase().trim();
                            if (itemAlias.getName().equals(s)) {
                                String itemName = item.getItemName().replace("（", "(")
                                        .replace("）",")").toLowerCase().trim();
                                basTestItemMap.put(alias + ":" + itemName, item);
                            }
                        }
                    }
                }
            }
            return basTestItemMap;
        } catch (Exception e) {
            LOG.error("findBasTestItemMap occur error!!", e);
            e.printStackTrace();
        } finally {
            DBUtil.closeDBResources(preparedStatement, connection);
        }
        return basTestItemMap;
    }

    public Map<String, String> findBasTestUnitMap() {
        Map<String, String> basTestItemUnitMap = Maps.newHashMap();
        String sql = "select * from standard_test_item_unit";
        Connection connection = DBUtil.getConnection(this.dataBaseType, this.url, this.user, this.passwd);
        PreparedStatement preparedStatement = null;
        try {
            connection.setAutoCommit(false);
            preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            List<BasTestItemUnit> basTestItemUnitList = DBHelper.parseResultList(BasTestItemUnit.class, rs);
            connection.commit();
            for (BasTestItemUnit unitItem : basTestItemUnitList) {
                String unit = StrUtil.isNotBlank(unitItem.getUnit()) ? unitItem.getUnit().trim() : "";
                String unitComm = StrUtil.isNotBlank(unitItem.getUnitComm()) ? unitItem.getUnitComm().trim() : "";
                basTestItemUnitMap.put(unit, unitComm);
            }
            return basTestItemUnitMap;
        } catch (Exception e) {
            LOG.error("findBasTestUnitMap occur error!!", e);
            e.printStackTrace();
        } finally {
            DBUtil.closeDBResources(preparedStatement, connection);
        }
        return basTestItemUnitMap;
    }

    public Map<String, BasCheckItem> findCheckItemList() {
        Map<String, BasCheckItem> checkItemMap = Maps.newHashMap();
        String sql = "select * from standard_bas_check_item";
        Connection connection = DBUtil.getConnection(this.dataBaseType, this.url, this.user, this.passwd);
        PreparedStatement preparedStatement = null;
        try {
            connection.setAutoCommit(false);
            preparedStatement = connection.prepareStatement(sql);

            ResultSet rs = preparedStatement.executeQuery();
            List<BasCheckItem> basCheckItemList = DBHelper.parseResultList(BasCheckItem.class, rs);
            connection.commit();
            for (BasCheckItem item : basCheckItemList) {
                String testitemFt = "";
                if(StrUtil.isNotBlank(item.getItemName())){
                    testitemFt = item.getItemName().replace("（", "(")
                            .replace("）",")").trim();
                }
                checkItemMap.put(testitemFt, item);
            }
            return checkItemMap;
        } catch (Exception e) {
            LOG.error("findCheckItemList occur error!!", e);
            e.printStackTrace();
        } finally {
            DBUtil.closeDBResources(preparedStatement, connection);
        }
        return checkItemMap;
    }

    public List<BasItemAlias> findBasItemAlias() {
        List<BasItemAlias> basCheckItemList = new ArrayList<>();
        List<BasItemAlias> basCheckItemList2;
        String sql = "select name,alias_name from bas_item_alias";
        String sql2 = "select  method as name, method as alias_name from bas_inspection_keyword GROUP BY name";
        Connection connection = DBUtil.getConnection(this.dataBaseType, this.url, this.user, this.passwd);
        PreparedStatement preparedStatement = null;
        try {
            connection.setAutoCommit(false);
            preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            basCheckItemList = DBHelper.parseResultList(BasItemAlias.class, rs);
            preparedStatement = connection.prepareStatement(sql2);
            ResultSet rs2 = preparedStatement.executeQuery();
            basCheckItemList2 = DBHelper.parseResultList(BasItemAlias.class, rs2);
            connection.commit();
            basCheckItemList.addAll(basCheckItemList2);
            return basCheckItemList;
        } catch (Exception e) {
            LOG.error("findCheckItemInit occur error!!", e);
            e.printStackTrace();
        } finally {
            DBUtil.closeDBResources(preparedStatement, connection);
        }

        return basCheckItemList;
    }

    public List<BasInspectionKeyword> findBasDiagnoseItemList() {
        Map<String, BasInspectionKeyword> basDiagnoseItemMap = Maps.newHashMap();
        String sql = "select * from bas_inspection_keyword";
        Connection connection = DBUtil.getConnection(this.dataBaseType, this.url, this.user, this.passwd);
        PreparedStatement preparedStatement = null;
        try {
            connection.setAutoCommit(false);
            preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            List<BasInspectionKeyword> basDiagnoseItemList = DBHelper.parseResultList(BasInspectionKeyword.class, rs);
            connection.commit();
            return basDiagnoseItemList;
        } catch (Exception e) {
            LOG.error("findBasDiagnoseItemList occur error!!", e);
            e.printStackTrace();
        } finally {
            DBUtil.closeDBResources(preparedStatement, connection);
        }
        return null;
    }

    public Map<String, String> findCheckItemInitMap() {
        Map<String, String> checkItemInitMap = new HashMap<>();
        List<BasCheckItemInit> basCheckItemList = new ArrayList<BasCheckItemInit>();
        String sql = "select id,init_result,init_result_comm from bas_check_item_init where init_result is not null and init_result_comm is not null";
        Connection connection = DBUtil.getConnection(this.dataBaseType, this.url, this.user, this.passwd);
        PreparedStatement preparedStatement = null;
        try {
            connection.setAutoCommit(false);
            preparedStatement = connection.prepareStatement(sql);

            ResultSet rs = preparedStatement.executeQuery();
            basCheckItemList = DBHelper.parseResultList(BasCheckItemInit.class, rs);
            connection.commit();

            if (CollectionUtil.isNotEmpty(basCheckItemList)) {
                basCheckItemList.forEach(o -> {
                    checkItemInitMap.put(o.getInitResult().trim(), o.getInitResultComm().trim());
                });
            }

            return checkItemInitMap;
        } catch (Exception e) {
            LOG.error("findCheckItemInitMap occur error!!", e);
            e.printStackTrace();
        } finally {
            DBUtil.closeDBResources(preparedStatement, connection);
        }

        return checkItemInitMap;
    }

    public List<KeywordsLevelDto> findEtlLevelJson() {
        return JSON.parseArray(EtlConst.keyLevelJson, KeywordsLevelDto.class);
    }
}
