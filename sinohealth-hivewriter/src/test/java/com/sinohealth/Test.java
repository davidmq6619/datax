package com.sinohealth;

import com.alibaba.datax.common.element.Column;
import com.alibaba.datax.common.element.DateColumn;
import com.alibaba.datax.common.element.LongColumn;
import com.alibaba.datax.common.element.StringColumn;
import com.alibaba.datax.core.transport.record.DefaultRecord;
import com.sinohealth.datax.entity.source.LisTestResultS;
import com.sinohealth.datax.reflection.factory.DefaultObjectFactory;
import com.sinohealth.datax.reflection.factory.ObjectFactory;
import com.sinohealth.datax.utils.ReflectUtil;
import org.apache.commons.beanutils.BeanUtils;

import java.util.Date;

/**
 * @author Tony
 * @date 2018/7/27
 **/
public class Test {

    public static void main(String[] args) throws Exception {

        String claszSName = "com.sinohealth.Demo";
        Class clazzS = Class.forName(claszSName);

        DefaultRecord record = new DefaultRecord();
        Column id = new LongColumn("1", "id");
        Column name = new StringColumn("tony", "name");
        Column otherName = new StringColumn("tony2", "other_name");
        Column birth = new DateColumn(new Date().getTime(), "birth");
        Column age = new LongColumn("10", "age");
        record.addColumn(id);
        record.addColumn(name);
        record.addColumn(otherName);
        record.addColumn(birth);
        record.addColumn(age);

        Object obj = ReflectUtil.transform(clazzS, record);

        // 值拷贝
        ObjectFactory objectFactory = new DefaultObjectFactory();
        Object targetObj = objectFactory.create(null);

        BeanUtils.copyProperties(targetObj, obj);

        System.out.println();

        LisTestResultS lisTestResultS = new LisTestResultS();
        System.out.println(lisTestResultS.getClass().getName());

    }



}
