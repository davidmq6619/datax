package com.sinohealth.datax.utils;

import cn.hutool.core.util.StrUtil;
import com.alibaba.datax.common.element.*;
import com.sinohealth.datax.reflection.Reflector;
import com.sinohealth.datax.reflection.factory.DefaultObjectFactory;
import com.sinohealth.datax.reflection.factory.ObjectFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Date;


/**
 * @author Tony
 * @date 2018/7/27
 **/
public class ReflectUtil {

    private static Logger LOG = LoggerFactory.getLogger(ReflectUtil.class);

    public static <T> T transform(Class<T> clazz, Record record) {
        try {
            Reflector reflector = Reflector.forClass(clazz);
            ObjectFactory objectFactory = new DefaultObjectFactory();
            T obj = objectFactory.create(clazz);
            for (int i = 0; i < record.getColumnNumber(); i++) {
                String columnName = record.getColumn(i).getColumnName();
                String fieldName = reflector.getFieldName(columnName);
                if (reflector.hasSetter(fieldName)) {
                    Column columnValue = record.getColumn(i);
                    if (columnValue != null) {
                        try {
                            Object fieldValue = null;
                            Class<?> fieldType = reflector.getSetterType(fieldName);
                            if (fieldType.equals(Short.class) || fieldType.equals(short.class)) {
                                fieldValue = Short.valueOf(columnValue.asString());
                            } else if (fieldType.equals(Byte.class) || fieldType.equals(byte.class)) {
                                fieldValue = columnValue.asBytes();
                            } else if (fieldType.equals(Integer.class) || fieldType.equals(int.class)) {
                                fieldValue = Integer.valueOf(columnValue.asString());
                            } else if (fieldType.equals(Long.class) || fieldType.equals(long.class)) {
                                fieldValue = columnValue.asLong();
                            } else if (fieldType.equals(Float.class) || fieldType.equals(float.class)) {
                                fieldValue = Float.valueOf(columnValue.asString());
                            } else if (fieldType.equals(Double.class) || fieldType.equals(double.class)) {
                                fieldValue = columnValue.asDouble();
                            } else if (fieldType.equals(Boolean.class) || fieldType.equals(boolean.class)) {
                                fieldValue = columnValue.asBoolean();
                            } else if (fieldType.equals(Date.class)) {
                                fieldValue = columnValue.asDate();
                            } else if (fieldType.equals(String.class)) {
                                fieldValue = columnValue.asString();
                            }
                            reflector.getSetInvoker(fieldName).invoke(obj, new Object[]{fieldValue});
                        } catch (Exception e) {
                            throw new Exception("DaoHelper.parseResultObj faild,columname:" + columnName, e);
                        }
                    }
                }
            }

            return obj;
        } catch (Exception ex) {
            LOG.error("Record transform to SourceEntity occurs error!!!");
        }

        return null;
    }

    public static Record getRecord(Record record, Object object) throws Exception {
        Class<?> clazz = object.getClass();
        Reflector reflector = Reflector.forClass(clazz);
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            String filedName = field.getName();
            String columnName = reflector.getColumn(filedName);
            // String
            if (field.getGenericType().toString().equals(
                    "class java.lang.String")) {
                Method m = object.getClass().getMethod(
                        "get" + getMethodName(field.getName()));
                String val = (String) m.invoke(object);
                record.addColumn(new StringColumn(val, columnName));
            }
            // Integer
            if (field.getGenericType().toString().equals(
                    "class java.lang.Integer")) {
                Method m = object.getClass().getMethod(
                        "get" + getMethodName(field.getName()));
                Integer val = (Integer) m.invoke(object);
                record.addColumn(new LongColumn(val, columnName));

            }
            // Long
            if (field.getGenericType().toString().equals(
                    "class java.lang.Long")) {
                Method m = object.getClass().getMethod(
                        "get" + getMethodName(field.getName()));
                String val = m.invoke(object).toString();
                record.addColumn(new LongColumn(val, columnName));

            }
            // Double
            if (field.getGenericType().toString().equals(
                    "class java.lang.Double")) {
                Method m = object.getClass().getMethod(
                        "get" + getMethodName(field.getName()));
                String val = m.invoke(object).toString();
                record.addColumn(new DoubleColumn(val, columnName));

            }
            // Boolean
            if (field.getGenericType().toString().equals(
                    "class java.lang.Boolean")) {
                Method m = object.getClass().getMethod(
                        field.getName());
                Boolean val = (Boolean) m.invoke(object);
                record.addColumn(new BoolColumn(val, columnName));

            }
            // boolean
            if (field.getGenericType().toString().equals("boolean")) {
                Method m = object.getClass().getMethod(
                        field.getName());
                Boolean val = (Boolean) m.invoke(object);
                record.addColumn(new BoolColumn(val, columnName));

            }
            // Date
            if (field.getGenericType().toString().equals(
                    "class java.util.Date")) {
                Method m = object.getClass().getMethod(
                        "get" + getMethodName(field.getName()));
                Date val = (Date) m.invoke(object);
                if(val == null){
                    val = new Date();
                }
                    record.addColumn(new DateColumn(val.getTime(), columnName));
            }
        }

        return record;

    }

    // 把一个字符串的第一个字母大写、效率是最高的、
    private static String getMethodName(String fieldName) throws Exception {
        byte[] items = fieldName.getBytes();
        items[0] = (byte) ((char) items[0] - 'a' + 'A');
        return new String(items);
    }

}

