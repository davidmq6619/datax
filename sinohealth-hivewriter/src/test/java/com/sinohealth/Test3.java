package com.sinohealth;

import cn.hutool.core.date.DateUtil;
import com.alibaba.datax.plugin.rdbms.util.DBUtil;
import com.alibaba.datax.plugin.rdbms.util.DataBaseType;
import com.google.common.collect.Maps;
import com.sinohealth.datax.entity.common.BasTestItem;
import com.sinohealth.datax.entity.zktarget.LisTestResultT;
import com.sinohealth.datax.reflection.Reflector;
import com.sinohealth.datax.utils.DBHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * @author Tony
 * @date 2018/7/27
 **/
public class Test3 {

    public static void main(String[] args) throws Exception {

        Map<String, BasTestItem> basTestItemMap = Maps.newHashMap();
        String sql = "select * from bas_test_item where  testitem_ft = '血钙' and testitem_name_c = '血钙' and clean_status=0 and del_flag=0";
        Connection connection = DBUtil.getConnection(DataBaseType.MySql,
                "jdbc:mysql://192.168.16.50:3000/health100_source?rewriteBatchedStatements=true&useUnicode=true&characterEncoding=utf-8",
                "root","BigData2017!");
        PreparedStatement preparedStatement = null;
        try {
            connection.setAutoCommit(false);
            preparedStatement = connection.prepareStatement(sql);

            ResultSet rs = preparedStatement.executeQuery();
            List<BasTestItem> basTestItemList = DBHelper.parseResultList(BasTestItem.class, rs);
            connection.commit();
            for (BasTestItem item : basTestItemList) {
                basTestItemMap.put(item.getTestitemFt() + "~" + item.getTestitemNameC(), item);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeDBResources(preparedStatement, null);
        }





        LisTestResultT obj = new LisTestResultT();
        obj.setCreateTime(new Date());
        obj.setBookTime(new Date());
        obj.setUnitComm("unit comm");
        obj.setUnit("unit");
        obj.setResultsDiscrete("1");
        obj.setResults("2");
        obj.setItemName("a");
        obj.setItemsNameComm("b");
        obj.setItemFt("1");
        obj.setBigCategory("big");
        obj.setSmallCategory("small");
        obj.setCleanStatus("1");
        obj.setGroupId(1);
        obj.setNormalH("2");
        obj.setNormalL("1");
        obj.setVid("1");
        obj.setCid("2");
        obj.setItemId("2");

        String[] columns = new String[]{"cid","vid","item_id","item_ft","item_name","results","unit","normal_l","normal_h","big_category","small_category","items_name_comm","results_discrete","unit_comm","group_id","book_time","clean_status","create_time"};

        Reflector reflector = Reflector.forClass(LisTestResultT.class);
        reflector.getTableName();

        for (String column : columns) {
            System.out.println(column + ":" + reflector.getFieldName(column));
            String fieldName = reflector.getFieldName(column);
            Object value = reflector.getGetInvoker(fieldName).invoke(obj, null);
            System.out.println(value.toString());

        }




    }

}
