package com.pigic.hzeropigic.utils;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.beanutils.converters.BigDecimalConverter;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.commons.beanutils.converters.IntegerConverter;
import org.apache.commons.beanutils.converters.StringConverter;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cglib.beans.BeanCopier;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/*
Bean转换
 * @date: 2019/5/13
 * @author: 潘顾昌 <guchang.pan@hand-china.com>
 * @version: 0.0.1
 * @copyright Copyright (c) 2019, Hand
 */
public class BeanUtilsEx {
    
    private static Map<String, Object> frameCache = new HashMap<>(1);
    private final static  Logger logger = LoggerFactory.getLogger(BeanUtilsEx.class);

    static {
        BeanConverter converter = new BeanConverter();

        // 注册一个日期类
        ConvertUtils.register(converter, Date.class);
        ConvertUtils.register(new DateConverter(null), Date.class); 
        
        // 注册一个BigDecimal类型
        ConvertUtils.register(converter, BigDecimal.class);
        ConvertUtils.register(new BigDecimalConverter(null), BigDecimal.class);
        
        // 注册String类型,主要是String转Date
        ConvertUtils.register(converter, String.class);
        ConvertUtils.register(new StringConverter(null), String.class);

        ConvertUtils.register(converter, Map.class);
        
        ConvertUtils.register(new IntegerConverter(null), Integer.class);
    }

    /**
     * <pre>
     * 类型转换
     * </pre>
     * 
     * @param inObj
     *            传入对象
     * @param clz
     *            类型
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T convert(Object inObj, Class<T> clz) {
        return (T) new BeanConverter().convert(clz, inObj);

    }

    /**
     * Json转成Map
     * 
     * @param jsonStr
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> json2Map(String jsonStr) {

        Map<String, Object> paramMap = new HashMap<String, Object>();

        if (!StringUtils.isEmpty(jsonStr) && !"{}".equals(jsonStr)) {

            paramMap = JSONUtilsEx.deserialize(jsonStr, HashMap.class);

            for (String key : paramMap.keySet()) {
                if ("null".equals(paramMap.get(key))) {
                    paramMap.put(key, null);
                }
            }
        }

        return paramMap;
    }

    /**
     * 属性名，属性类型不一定要完全一致，速度慢
     * 
     * @param srcObject
     * @param destObject
     * @throws Exception
     */
    public static void copyProperties(Object srcObject, Object destObject) throws Exception {
        org.springframework.beans.BeanUtils.copyProperties(srcObject, destObject);
    }

    /**
     * 属性类型必须完全一致,速度一般
     */
    public static <T> T copyPropertiesUnConvert(Object srcObject, Class<T> destClz) throws Exception {
        T result = destClz.newInstance();

        copyPropertiesUnConvert(srcObject, result);

        return result;
    }

    public static void copyPropertiesUnConvert(Object srcObject, Object destObject) throws Exception {
        org.springframework.beans.BeanUtils.copyProperties(srcObject, destObject);
    }

    private static Map<String, BeanCopier> bcMap = new ConcurrentHashMap<String, BeanCopier>();

    /**
     * 属性名,属性类型必须完全一致,速度最快
     */
    public static <T> T copyPropertiesWithCglib(Object srcObject, Class<T> destClz) throws Exception {

        T result = destClz.newInstance();

        copyPropertiesWithCglib(srcObject, result);

        return result;
    }

    public static void copyPropertiesWithCglib(Object srcObject, Object destObject) throws Exception {

        Class<? extends Object> srcClz = srcObject.getClass();
        Class<? extends Object> descClz = destObject.getClass();

        if (bcMap.get(srcClz.getName() + "#" + descClz.getName()) == null) {
            bcMap.put(srcClz.getName() + "#" + descClz.getName(), BeanCopier.create(srcClz, descClz, false));
        }

        bcMap.get(srcClz.getName() + "#" + descClz.getName()).copy(srcObject, destObject, null);
    }

    private static class BeanConverter implements Converter {

        @SuppressWarnings({ "rawtypes", "unchecked" })
        public Object convert(Class type, Object value) {

            if (value == null) {
                return null;
            } else if (value.getClass().equals(type) || type.isAssignableFrom(value.getClass())) {

                // 如果是同类型,或者需要转成的类为传入类的基类,则直接返回
                return value;
            } else {

                if (type.equals(String.class)) {

                    if (value instanceof Date) {

                        // 日期型转字符型
                        return DateUtilsEx.formatToString((Date) value, DateUtilsEx.DATE_FORMAT_SEC);
                    } else {
                        return String.valueOf(value);
                    }

                } else if (type.equals(Date.class)) {

                    // 转化为日期型
                    if (value instanceof Long) {
                        return new Date(((Long) value).longValue());
                    } else if (value instanceof String) {

                        int lengthb = ((String) value).getBytes().length;
                        try {
                            if (lengthb == DateUtilsEx.DATE_FORMAT_DAY.getBytes().length) {
                                return DateUtilsEx.formatToDate(value.toString(), DateUtilsEx.DATE_FORMAT_DAY);
                            } else if (lengthb == DateUtilsEx.DATE_FORMAT_SEC.getBytes().length) {
                                return DateUtilsEx.formatToDate(String.valueOf(value), DateUtilsEx.DATE_FORMAT_SEC);
                            } else if (lengthb == 0) {
                                return null;
                            }
                        } catch (ParseException e) {
                            throw new RuntimeException("类型转化出现异常:" + e);
                        }
                    }
                } else if (type.equals(BigDecimal.class)) {

                    // 转化为BigDecimal型
                    if (StringUtils.isBlank(String.valueOf(value))) {
                        return null;
                    } else if (BeanUtilsEx.isSimpleObject(value)) {
                        return new BigDecimal(String.valueOf(value));
                    }
                } else if (type.equals(Long.class)) {

                    // 转化为Long型
                    if (StringUtils.isBlank(String.valueOf(value))) {
                        return null;
                    } else if (BeanUtilsEx.isSimpleObject(value)) {
                        return new Long(String.valueOf(value));
                    }
                } else if (type.equals(Double.class)) {

                    // 转化为Double型
                    if (value == null || StringUtils.isBlank(String.valueOf(value))) {
                        return null;
                    } else if (BeanUtilsEx.isSimpleObject(value)) {
                        return new Double(String.valueOf(value));
                    }
                } else if (type.equals(Integer.class)) {

                    // 转化为Integer型
                    if (BeanUtilsEx.isSimpleObject(value)) {
                        if (value instanceof String && StringUtils.isBlank((String) value)) {
                            return null;
                        } else {
                            return new Integer(String.valueOf(value));
                        }
                    }
                } else if (type.equals(Boolean.class)) {

                    // 转化为Boolean型
                    if (StringUtils.isBlank(String.valueOf(value))) {
                        return Boolean.FALSE;
                    } else if ("1".equals(value) || "true".equals(value)) {
                        return Boolean.TRUE;
                    } else {
                        return Boolean.FALSE;
                    }
                } else if (type.equals(Map.class)) {

                    // 转化为Map型
                    if (!BeanUtilsEx.isSimpleObject(value)) {
                        return bean2map(value);
                    }
                } else {

                    // 由Map 转化为 pojo bean
                    if (value instanceof Map) {
                        return map2bean((Map<String, Object>) value, type);
                    }

                }
                throw new RuntimeException("无法将对象" + value + "转化为" + type.getName() + "类型!");
            }
        }

        /**
         * Map转化为POJO对象
         * 
         * @param srcMap
         * @param type
         * @return
         */
        @SuppressWarnings({ "rawtypes", "unchecked" })
        private Object map2bean(Map<String, Object> srcMap, Class type) {

            Map<String, Class> propertyMap = getClassPropertyType(type);

            Object destObject;

            try {
                destObject = type.newInstance();
            } catch (Exception e) {
                throw new RuntimeException("转型时无法实例化" + type.getName());
            }

            for (String propertyName : propertyMap.keySet()) {

                // TODO toLower是为了兼容js里所有参数都最小化
                if (srcMap.containsKey(propertyName) || srcMap.containsKey(propertyName.toLowerCase())) {

                    Object propertyValue = srcMap.containsKey(propertyName) ? srcMap.get(propertyName)
                            : srcMap.get(propertyName.toLowerCase());

                    // TODO 这里要靠json来区别null和字符串的""
                    // 如果值为空的话,不反射入POJO
                    if (propertyValue != null) {

                        propertyValue = BeanUtilsEx.convert(propertyValue, propertyMap.get(propertyName));

                        try {
                            if (propertyValue != null) {
                                BeanUtils.setProperty(destObject, propertyName, propertyValue);
                            }
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }

            return destObject;
        }

        
        
        /**
         * POJO转化为Map
         * 
         * @param value
         * @return
         */
        @SuppressWarnings("unchecked")
        private Map<String, Object> bean2map(Object value) {

            Map<String, Object> result = new HashMap<String, Object>();

            Map<String, String> propertyMap;
            try {
                propertyMap = BeanUtils.describe(value);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            // 去除bean对象中的class属性信息
            propertyMap.remove("class");

            for (String propertyName : propertyMap.keySet()) {
                result.put(propertyName, propertyMap.get(propertyName));
            }

            return result;
        }
    }

    /**
     * 判断对象是否是基本型:String,BigDecimal,Long等
     * 
     * @param o
     * @return
     */
    public static boolean isSimpleObject(Object o) {
        return o == null ? false : isSimpleClz(o.getClass());
    }

    
    
    public static boolean isSimpleClz(Class clz) {
        return clz.equals(String.class) || clz.equals(Long.class) || clz.equals(BigDecimal.class)
                || clz.equals(Integer.class) || clz.equals(Double.class) || clz.equals(Double.TYPE)
                || clz.equals(Integer.TYPE) || clz.equals(Long.TYPE) || clz.equals(Float.class)
                || clz.equals(Float.TYPE);
    }
    
    
    public static Map<String, Class> getClassPropertyType(Class clz) {
        Map<String, Class> result = (Map) frameCache.get("ClassUtilsEx.getClassPropertyType" + clz.getName());
        if (result == null) {
            result = new HashMap();
            if (clz.getSuperclass() != null) {
                ((Map) result).putAll(getClassPropertyType(clz.getSuperclass()));
            }

            Field[] var2 = clz.getDeclaredFields();
            int var3 = var2.length;

            for (int var4 = 0; var4 < var3; ++var4) {
                Field field = var2[var4];
                ((Map) result).put(field.getName(), field.getType());
            }

            frameCache.put("ClassUtilsEx.getClassPropertyType" + clz.getName(), result);
        }

        return (Map) result;
    }

}
