package com.sinohealth.datax.utils;

import cn.hutool.core.util.StrUtil;
import com.sinohealth.datax.reflection.Reflector;
import com.sinohealth.datax.reflection.factory.DefaultObjectFactory;
import com.sinohealth.datax.reflection.factory.ObjectFactory;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Tony
 * @date 2018/7/27
 **/
public class DBHelper {

    public static <T> T parseResultObj(Class<T> clazz, ResultSet rs) {
        T obj = null;
        try {
            if (rs != null && !rs.wasNull()) {
                ResultSetMetaData md = rs.getMetaData();
                int columnCount = md.getColumnCount();
                Reflector reflector = Reflector.forClass(clazz);
                while (rs.next()) {
                    ObjectFactory objectFactory = new DefaultObjectFactory();
                    obj = objectFactory.create(clazz);
                    for (int i = 1; i <= columnCount; i++) {
                        String columnName = md.getColumnLabel(i);
                        if (StrUtil.isBlank(columnName)) {
                            columnName = md.getColumnName(i);
                        }
                        String propertyName = reflector.getFieldName(columnName);
                        if (reflector.hasSetter(propertyName)) {
                            Object columnValue = rs.getObject(i);
                            if (columnValue != null) {
                                try {
                                    Class<?> fieldType = reflector.getSetterType(propertyName);
                                    if (fieldType.equals(Short.class) || fieldType.equals(short.class)) {
                                        columnValue = rs.getShort(i);
                                    } else if (fieldType.equals(Byte.class) || fieldType.equals(byte.class)) {
                                        columnValue = rs.getByte(i);
                                    } else if (fieldType.equals(Integer.class) || fieldType.equals(int.class)) {
                                        columnValue = rs.getInt(i);
                                    } else if (fieldType.equals(Long.class) || fieldType.equals(long.class)) {
                                        columnValue = rs.getLong(i);
                                    } else if (fieldType.equals(Float.class) || fieldType.equals(float.class)) {
                                        columnValue = rs.getFloat(i);
                                    } else if (fieldType.equals(Double.class) || fieldType.equals(double.class)) {
                                        columnValue = rs.getDouble(i);
                                    } else if (fieldType.equals(Boolean.class) || fieldType.equals(boolean.class)) {
                                        columnValue = rs.getBoolean(i);
                                    } else if (fieldType.equals(Date.class)) {
                                        columnValue = rs.getTimestamp(i);
                                    } else if (fieldType.equals(String.class)) {
                                        columnValue = rs.getString(i);
                                    }

                                    reflector.getSetInvoker(propertyName).invoke(obj, new Object[]{columnValue});
                                } catch (Exception e) {
                                    throw new Exception("DaoHelper.parseResultList failed,columnName:" + columnName, e);
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return obj;
    }

    public static <T> List<T> parseResultList(Class<T> clazz, ResultSet rs) {
        List<T> array = new ArrayList<T>();
        try {
            if (rs != null && !rs.wasNull()) {
                ResultSetMetaData md = rs.getMetaData();
                int columnCount = md.getColumnCount();
                Reflector reflector = Reflector.forClass(clazz);
                while (rs.next()) {
                    ObjectFactory objectFactory = new DefaultObjectFactory();
                    T obj = objectFactory.create(clazz);
                    for (int i = 1; i <= columnCount; i++) {
                        String columnName = md.getColumnLabel(i);
                        if (StrUtil.isBlank(columnName)) {
                            columnName = md.getColumnName(i);
                        }
                        String propertyName = reflector.getFieldName(columnName);
                        if (reflector.hasSetter(propertyName)) {
                            Object columnValue = rs.getObject(i);
                            if (columnValue != null) {
                                try {
                                    Class<?> fieldType = reflector.getSetterType(propertyName);
                                    if (fieldType.equals(Short.class) || fieldType.equals(short.class)) {
                                        columnValue = rs.getShort(i);
                                    } else if (fieldType.equals(Byte.class) || fieldType.equals(byte.class)) {
                                        columnValue = rs.getByte(i);
                                    } else if (fieldType.equals(Integer.class) || fieldType.equals(int.class)) {
                                        columnValue = rs.getInt(i);
                                    } else if (fieldType.equals(Long.class) || fieldType.equals(long.class)) {
                                        columnValue = rs.getLong(i);
                                    } else if (fieldType.equals(Float.class) || fieldType.equals(float.class)) {
                                        columnValue = rs.getFloat(i);
                                    } else if (fieldType.equals(Double.class) || fieldType.equals(double.class)) {
                                        columnValue = rs.getDouble(i);
                                    } else if (fieldType.equals(Boolean.class) || fieldType.equals(boolean.class)) {
                                        columnValue = rs.getBoolean(i);
                                    } else if (fieldType.equals(Date.class)) {
                                        columnValue = rs.getTimestamp(i);
                                    } else if (fieldType.equals(String.class)) {
                                        columnValue = rs.getString(i);
                                    }

                                    reflector.getSetInvoker(propertyName).invoke(obj, new Object[]{columnValue});
                                } catch (Exception e) {
                                    throw new Exception("DaoHelper.parseResultList failed,columnName:" + columnName, e);
                                }
                            }
                        }
                    }
                    array.add(obj);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return array;
    }


}
