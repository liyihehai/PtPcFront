package com.nnte.pf_uti_client.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.util.*;

/**
 * bean工具类
 */
public class BeanUtils {
    public interface IEntityMapByIdList<T> {
        Map<Object, T> getEntityMapByIdList(List<Object> idList);
    }

    /**
     * 对象属性拷贝，用于有继承关系的父子类之间进行属性拷贝
     */
    public static <T> void copyFromSrcObject(T _src, T _dim) throws Exception {
        Field[] fields = _src.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true); // 设置属性是可以访问的
            Object val = null;
            Field srcfield = null;
            try {
                srcfield = _src.getClass().getDeclaredField(field.getName());
            } catch (NoSuchFieldException e) {
                srcfield = _src.getClass().getSuperclass().getDeclaredField(field.getName());
            }
            if (srcfield != null) {
                if (!Modifier.isFinal(srcfield.getModifiers()) &&
                        !Modifier.isStatic(srcfield.getModifiers())) {
                    srcfield.setAccessible(true); // 设置属性是可以访问的
                    val = srcfield.get(_src);
                    field.set(_dim, val);
                }
            }
        }
    }

    /**
     * 按属性名称取得属性，含父类属性
     */
    public static Field getFieldByName(Class clazz, String field) {
        if (clazz == null)
            return null;
        Field f = null;
        try {
            f = clazz.getDeclaredField(field);
        } catch (NoSuchFieldException e) {
        }
        if (f == null) {
            Class parenClass = clazz.getSuperclass();
            if (parenClass != null)
                return getFieldByName(parenClass, field);
        }
        return f;
    }

    /**
     * 对象属性拷贝，用于任意对象之间进行属性拷贝，前提是
     * _src和_dim有相同的属性名称,另外_src属性不能是父级
     * 对象属性，_dim属性可以是父级对象属性
     */
    public static void copyFromSrc(Object _src, Object _dim) throws Exception {
        Field[] fields = _src.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true); // 设置属性是可以访问的
            Object val = null;
            Field dimfield = getFieldByName(_dim.getClass(), field.getName());
            if (dimfield != null) {
                if (!Modifier.isFinal(dimfield.getModifiers()) &&
                        !Modifier.isStatic(dimfield.getModifiers())) {
                    dimfield.setAccessible(true); // 设置属性是可以访问的
                    val = field.get(_src);
                    if (val != null && dimfield.getType().getName().indexOf("String") >= 0)
                        dimfield.set(_dim, val.toString());
                    else
                        dimfield.set(_dim, val);
                }
            }
        }
    }

    /**
     * 按属性名称取得属性的值，含父类属性
     */
    public static Object getFieldValueByName(Object srcObj, String field) throws Exception {
        Field fieldObj = getFieldByName(srcObj.getClass(), field);
        if (fieldObj != null) {
            fieldObj.setAccessible(true); // 设置属性是可以访问的
            return fieldObj.get(srcObj);
        }
        return null;
    }

    /**
     * 按属性名称设置属性的值，含父类属性
     */
    public static void setFieldValueByName(Object dimObj, String field, Object value) throws Exception {
        Field fieldObj = getFieldByName(dimObj.getClass(), field);
        if (fieldObj != null) {
            fieldObj.setAccessible(true); // 设置属性是可以访问的
            fieldObj.set(dimObj, value);
        }
    }

    public <T> void setCollectionName(Collection<?> collection, String idField, String nameField,
                                      IEntityMapByIdList<T> entityMapInterface, String entityNameField) throws Exception{
        if (collection == null || collection.size() < 0)
            return;
        Set<Object> idSet = new HashSet<>();
        for(Object item:collection){
            idSet.add(getFieldValueByName(item, idField));
        }
        List<Object> idList = new ArrayList<>(idSet);
        Map<Object, T> brandMap = entityMapInterface.getEntityMapByIdList(idList);
        if (brandMap == null || brandMap.size() <= 0)
            return;
        for(Object item:collection){
            Object id = getFieldValueByName(item, idField);
            if (id != null) {
                T entity = brandMap.get(id);
                if (entity != null) {
                    setFieldValueByName(item, nameField, getFieldValueByName(entity, entityNameField));
                }
            }
        }
    }

    public static <T> void setFeildValue(Object srcVal, Field field, T _dim) throws Exception {
        try {
            if (srcVal.getClass().equals(field.getType())) {
                field.set(_dim, srcVal);
            } else {
                if (field.getType().getName().indexOf("String") >= 0) {
                    field.set(_dim, srcVal.toString());
                } else if (field.getType().getName().indexOf("Integer") >= 0) {
                    field.set(_dim, Integer.valueOf(srcVal.toString()));
                } else if (field.getType().getName().indexOf("Long") >= 0) {
                    field.set(_dim, Long.valueOf(srcVal.toString()));
                } else if (field.getType().getName().indexOf("Double") >= 0) {
                    field.set(_dim, Double.valueOf(srcVal.toString()));
                } else if (field.getType().getName().indexOf("Float") >= 0) {
                    field.set(_dim, Float.valueOf(srcVal.toString()));
                } else if (field.getType().getName().indexOf("BigDecimal") >= 0) {
                    field.set(_dim, BigDecimal.valueOf(Double.valueOf(srcVal.toString())));
                } else if (field.getType().getName().indexOf("Date") >= 0) {
                    if (srcVal.getClass().getName().indexOf("String") >= 0) {
                        String fmt = DateUtils.getStringDateFmt(srcVal.toString());
                        if (fmt != null) {
                            Date dval = DateUtils.stringToDate(srcVal.toString(), fmt);
                            field.set(_dim, dval);
                        } else {
                            throw (new Exception("日期时间转换错误[" + field.getName() + "]"));
                        }
                    } else if (srcVal.getClass().getName().indexOf("Integer") >= 0 ||
                            srcVal.getClass().getName().indexOf("Long") >= 0) {
                        Long lDate = Long.valueOf(srcVal.toString());
                        field.set(_dim, new Date(lDate));
                    }
                }
            }
        } catch (Exception e) {
            throw new Exception(e);
        }
    }
}
