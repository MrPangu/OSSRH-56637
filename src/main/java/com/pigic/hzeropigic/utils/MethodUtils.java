package com.pigic.hzeropigic.utils;

/**
 * @author guchang.pan@hand-china.com
 *
 */
public class MethodUtils {
    public static String getMethodSimpleName(){
        String method = new Throwable().getStackTrace()[1].getMethodName();
        return method;
    }
    public static String getMethodName(){
        StackTraceElement stackTraceElement = new Throwable().getStackTrace()[1];
        String className = stackTraceElement.getClassName();
        String methodName = stackTraceElement.getMethodName();
        return className+"."+methodName;
    }
}
