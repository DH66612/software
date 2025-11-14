package com.echo.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

public class jsonUtils {//JSON工具类，对象转json字符串
    private static final ObjectMapper mapper = new ObjectMapper();//创建JSON映射器实例

    public static String toJson(Object obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            e.printStackTrace();
            return "{}";//返回空JSON
        }
    }
}