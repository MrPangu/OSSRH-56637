package com.pigic.hzeropigic.utils;

import org.hzero.core.message.MessageAccessor;
import org.hzero.export.annotation.ExcelColumn;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

public class CommonUtils {
    public static String FIELD_WREHOUSE_IDS = "warehouseIds";
    public static String FIELD_WHARA_IDS = "whareaIds";
    public static String FIELD_OWNER_IDS = "ownerIds";

    public static <T> T nvl(T... args){
        for(int i = 0; i < args.length; ++i) {
            if (args[i] != null) {
                return args[i];
            }
        }
        return null;
    }

    /**
     * 获取消息--没变量
     *
     * @param code 消息代码
     * @return
     */
    public static String getMessage(String code) {
        String result = null;
        try {
            result = MessageAccessor.getMessage(code).getDesc();
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获取消息--有变量
     *
     * @param code 消息代码
     * @param obj 类名
     * @param propertyName 字段名
     * @return
     */
    public static String getMessage(String code ,Object obj, String propertyName, String lang) {
        String result = null;
        try {
            String[] params = new String[]{CommonUtils.getDesc(obj,propertyName,lang)};
            result = MessageAccessor.getMessage(code,params).getDesc();
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 通过属性取得属性的描述注解
     *
     * @param field
     * @return
     */
    public static String getDesc(Field field, String lang) {
        String result = null;
        try {
            field.setAccessible(true);
            Annotation[] annotation = field.getAnnotations();
            for (Annotation tag : annotation) {
                if (tag instanceof ExcelColumn) {
                    if ("zh_CN".equals(lang)) {
                        result = ((ExcelColumn) tag).zh();
                    }else{
                        result = ((ExcelColumn) tag).en();
                    }
                    break;
                }
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 通过对象和属性名称取得属性的描述注解
     *
     * @param obj
     * @param propertyName
     * @return
     */
    public static String getDesc(Object obj, String propertyName, String lang) {
        String result = null;
        try {
            Field[] fields = obj.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                if (field.getName().equals(propertyName)) {
                    String desc = getDesc(field,lang);
                    if (desc != null && !desc.isEmpty()) {
                        result = desc;
                        break;
                    }
                }
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * @Description: 对象值转换时忽略空值
     * @author guchang.pan@hand-china.com
     *
     */
    public static void copyPropertiesIgnoreNull(Object src, Object target){
        BeanUtils.copyProperties(src, target, getNullPropertyNames(src));
    }

    public static String[] getNullPropertyNames (Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<String>();
        for(java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null){ emptyNames.add(pd.getName());}
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

}
