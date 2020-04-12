package com.pigic.hzeropigic.utils;

import lombok.SneakyThrows;

import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;

/**
 * @author guchang.pan@hand-china.com
 * @Date: 2019/2/20 13:19
 */
public interface DateTimeUtils {

    public static DateTimeFormatter pattern1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static DateTimeFormatter pattern2 = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static DateTimeFormatter pattern3 = DateTimeFormatter.ofPattern("HH:mm:ss");

    public static String convertDateToString(LocalDateTime dateTime){
        return dateTime.format(pattern1);
    }

    public static String convertDateToString(LocalDate date){
        return date.format(pattern2);
    }

    public static String convertDateToString(LocalTime time){
        return time.format(pattern3);
    }

    public static LocalDateTime convertStringToDateTime(String dateTime){
        return LocalDateTime.parse(dateTime,pattern1);
    }

    public static LocalDate convertStringToDate(String date){
        return LocalDate.parse(date,pattern2);
    }

    public static LocalTime convertStringToTime(String time){
        return LocalTime.parse(time,pattern3);
    }

    public static String dateTostring(Temporal temporal, String formatter){
        DateTimeFormatter pattern = DateTimeFormatter.ofPattern(formatter);
        return pattern.format(temporal);
    }

    /**
     * 字符串转日期
     * @param temporal 待转换的字符串
     * @param oftype 转换格式"yyyy-MM-dd HH:mm:ss"
     * @param classtype 转换结果的类型
     * @param <T> 转换结果
     * @return
     */
    @SneakyThrows
    public static <T extends Temporal> T stringToDate(String temporal, String oftype, Class<T> classtype){
        DateTimeFormatter pattern = DateTimeFormatter.ofPattern(oftype);
        Method method = classtype.getDeclaredMethod("parse", CharSequence.class, DateTimeFormatter.class);
        Object result = method.invoke(null, temporal, pattern);
        return (T) result;
    }

}
