package com.sinohealth;

import com.alibaba.datax.plugin.rdbms.util.DBUtil;
import com.alibaba.datax.plugin.rdbms.util.DataBaseType;
import com.sinohealth.datax.entity.common.BasTestItem;
import com.sinohealth.datax.entity.common.BasTestResultDiscrete;
import com.sinohealth.datax.entity.common.StandardBasTestItem;
import com.sinohealth.datax.plugin.rdbms.init.BizDataIniter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * @author Tony
 * @date 2018/7/27
 **/
public class Test4 {

    public static void main(String[] args) {
        BizDataIniter bizDataIniter = new BizDataIniter(DataBaseType.MySql,
                "jdbc:mysql://192.168.16.50:3000/health100_source?rewriteBatchedStatements=true&useUnicode=true&characterEncoding=utf-8",
                "root","BigData2017!");
        Map<String, StandardBasTestItem> testItemMap = bizDataIniter.findBasTestItemMap();
        Map<String, String> testUnitMap = bizDataIniter.findBasTestUnitMap();
        Map<String, List<BasTestResultDiscrete>> testResultDiscreteMap = bizDataIniter.findTestResultDiscreteMap();

        System.out.println();


//        String sql = "select * from bas_test_item where clean_status=0 and del_flag=0";
//        Connection connection = DBUtil.getConnection(DataBaseType.MySql,
//                "jdbc:mysql://192.168.16.50:3000/health100_source?rewriteBatchedStatements=true&useUnicode=true&characterEncoding=utf-8",
//                "root","BigData2017!");
//        PreparedStatement preparedStatement = null;
//        try {
//            connection.setAutoCommit(false);
//            preparedStatement = connection.prepareStatement(sql);
//
//            ResultSet rs = preparedStatement.executeQuery();
//            List<BasTestItem> basTestItemList = DBHelper.parseResultList(BasTestItem.class, rs);
//            connection.commit();
//            System.out.println();
//        } catch (SQLException e) {
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            DBUtil.closeDBResources(preparedStatement, null);
//        }


    }
}
