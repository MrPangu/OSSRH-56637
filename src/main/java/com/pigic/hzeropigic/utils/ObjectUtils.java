package com.pigic.hzeropigic.utils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @author guchang.pan@hand-china.com
 * @Date: 2019/1/31 16:52
 */
@SuppressWarnings("all")
public class ObjectUtils {

    public static Map<String, Object> objectNotNullFieldToMap(Object obj) throws Exception {
        if(obj == null){
            return null;
        }

        Map<String, Object> map = new HashMap<String, Object>();

        Field[] declaredFields = obj.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            field.setAccessible(true);
            Object objtmp=null;
            if((objtmp=field.get(obj))!=null){
            map.put(field.getName(), objtmp);
            }
        }
        return map;
    }
    public static Map<String, Object> objectFullToMap(Object obj) throws Exception {
        if(obj == null){
            return null;
        }

        Map<String, Object> map = new HashMap<String, Object>();

        Field[] declaredFields = obj.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            field.setAccessible(true);
            map.put(field.getName(), field.get(obj));
        }
        return map;
    }
}
