package com.pigic.hzeropigic.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author guchang.pan@hand-china.com
 *
 */
public class TempUtils {
    private static ThreadLocal<Map<String,Object>> container=new ThreadLocal<Map<String,Object>>(){
        @Override
        protected Map<String, Object> initialValue() {
            return new HashMap<>();
        }
    };

    public static Map<String,Object> getContainer(){
        return container.get();
    }
}
