package com.pigic.hzeropigic.utils;

import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/*
 * JSON处理类
 * @date: 2019/4/12
 * @author: 潘顾昌 <guchang.pan@hand-china.com>
 * @version: 0.0.1
 * @copyright Copyright (c) 2019, Hand
 */
public class JSONUtilsEx {
    private static ObjectMapper defaultMapper = new ObjectMapper();

    public static String serialize(Object obj) {
        return serialize(obj, defaultMapper);
    }

    public static String serialize(Object obj, ObjectMapper mapper) {
        StringWriter writer = null;

        try {
            writer = new StringWriter();
            mapper.writeValue(writer, obj);
            writer.close();
        } catch (Exception var4) {
            throw new RuntimeException("JSON序列化结果异常:" + var4.getMessage());
        }

        return writer.toString();
    }

    public static <T> T deserialize(String jsonStr, Class<T> clazz) {
        if (StringUtils.isBlank(jsonStr)) {
            return null;
        } else {
            try {
                return defaultMapper.readValue(jsonStr.replace("\n", ""), clazz);
            } catch (Exception var3) {
                throw new RuntimeException("JSON反序列化结果异常:" + var3.getMessage());
            }
        }
    }

    public static <T> List<T> deserializeList(String jsonStr, Class<T> clazz) {
        List<T> result = new ArrayList();
        List<Map> resultList = (List) deserialize(jsonStr, List.class);
        if (CollectionUtils.isNotEmpty(resultList)) {
            Iterator var4 = resultList.iterator();

            while (var4.hasNext()) {
                Map map = (Map) var4.next();
                result.add(BeanUtilsEx.convert(map, clazz));
            }
        }

        return result;
    }

    static {
        defaultMapper.setDateFormat(new SimpleDateFormat("yyyy.MM.dd HH:mm:ss"));
        defaultMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        defaultMapper.configure(Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
    }
}