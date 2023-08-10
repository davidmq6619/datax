package com.sinohealth;

import com.alibaba.datax.plugin.rdbms.util.DBUtil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

/**
 * @author Tony
 * @date 2018/7/27
 **/
public class Test2 {

    public static void main(String[] args) {
        String url = "jdbc:mysql://192.168.16.5:8066/Health100?rewriteBatchedStatements=true&useUnicode=true&characterEncoding=utf-8";
        String user = "root";
        String password = "123456";
        Connection connection;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(url, user, password);

            PreparedStatement pre = null;
            ResultSet res = null;
            String sql = "select * from bas_test_item limit 10";

            pre = connection.prepareStatement(sql);
            res = pre.executeQuery();
            //调用将结果集转换成实体对象方法
//            List<BasTestItem> list = DBUtil.toEntity(res, BasTestItem.class);
            //循环遍历结果
//            for(int i=0;i<list.size();i++){
//
//            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }

}
