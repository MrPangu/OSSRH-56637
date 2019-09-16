package com.pigic.hzeropigic.utils;


import org.apache.commons.lang.StringUtils;

import java.util.Map;
import java.util.Objects;

/**
 * @ClassName UtilsHelper
 * @Description TODO
 * @Author 38148
 * @Date 2019/5/29 19:50
 * @Email dianzhang.zhou@hand-china.com
 **/
public class UtilsHelper {


    /**
     * Object Convert String
     * @param object
     * @return
     */
    public static String ObjectToString(Object object){
        if(Objects.isNull(object)) {
            return "";
        }
        else{
            return object.toString();
        }
    }
    
    
    /**
     * 处理请求结果
     *
     * @param jsonStr
     * @return
     * @author lvpeng
     * @throws Exception 
     * @date  2019年5月29日
     */
    public static Object deserialize(String jsonStr) throws Exception {
        if (!StringUtils.isBlank(jsonStr) && !jsonStr.contains("<html>")) {
            Map<String, ?> responseMap = (Map)JSONUtilsEx.deserialize(jsonStr, Map.class);
            String code = String.valueOf(responseMap.get("code"));
            String text = String.valueOf(responseMap.get("text"));
            if (!"200".equals(code)) {
                throw new Exception(text);
            } else {
                return responseMap.get("data");
            }
        }else{
            throw new Exception("平台系统访问异常");
        }
    }


}