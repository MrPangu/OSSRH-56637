package com.pigic.hzeropigic.utils;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.beanutils.converters.SqlDateConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/*
 * Map与对象转换
 * @date: 2019/5/23
 * @author guchang.pan@hand-china.com
 * @version: 0.0.1
 * @copyright Copyright (c) 2019, Hand
 */
public class MapToObjectConvert {

    static {
        ConvertUtils.register(new SqlDateConverter(null), java.sql.Date.class);
        ConvertUtils.register(new Converter() {
            @Override
            public Object convert(Class type, Object value) {
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try{
                    return simpleDateFormat.parse(value.toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }, Date.class);
    }

    public static Object mapToObject(Map<String, Object> map, Class<?> beanClass) throws Exception {
        if (map == null) {
            return null;
        }

        Object obj = beanClass.newInstance();

        BeanUtils.populate(obj, map);

        return obj;
    }

    public static Map<?, ?> objectToMap(Object obj) {
        if(obj == null) {
            return null;
        }

        return new org.apache.commons.beanutils.BeanMap(obj);
    }
}
