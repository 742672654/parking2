package com.example.parking.util;


import java.io.IOException;
import java.text.SimpleDateFormat;

import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.codehaus.jackson.type.TypeReference;


public final class JsonUtil2 {

    private static ObjectMapper MAPPER;

    static {
        MAPPER = generateMapper(Inclusion.ALWAYS);
    }

    private JsonUtil2() {
    }


    public static <T> T fromJson(String json, Class<T> clazz){
        try{

            return clazz.equals(String.class) ? (T) json : MAPPER.readValue(json, clazz);
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    public static <T> T fromJson(String json, TypeReference<?> typeReference) throws IOException {
        return (T) (typeReference.getType().equals(String.class) ? json : MAPPER.readValue(json, typeReference));
    }


    public static <T> String toJson(T src) throws IOException {
        return src instanceof String ? (String) src : MAPPER.writeValueAsString(src);
    }


    public static <T> String toJson(T src, Inclusion inclusion) throws IOException {
        if (src instanceof String) {
            return (String) src;
        } else {
            ObjectMapper customMapper = generateMapper(inclusion);

            return customMapper.writeValueAsString(src);
        }
    }


    public static <T> String toJson(T src, ObjectMapper mapper) throws IOException {
        if (null != mapper) {
            if (src instanceof String) {
                return (String) src;
            } else {
                return mapper.writeValueAsString(src);
            }
        } else {
            return null;
        }
    }
    
	


    public static ObjectMapper mapper() {
        return MAPPER;
    }


    public static ObjectMapper generateMapper(Inclusion inclusion) {

        ObjectMapper customMapper = new ObjectMapper();

        // 设置输出时包含属性的风格
        customMapper.setSerializationInclusion(inclusion);

        // 设置输入时忽略在JSON字符串中存在但Java对象实际没有的属�?
        customMapper.configure(Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        // 禁止使用int代表Enum的order()來反序列化Enum,非常危險
        customMapper.configure(Feature.FAIL_ON_NUMBERS_FOR_ENUMS, true);

        // �?��日期格式都统�?��以下样式
        customMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

        return customMapper;
    }
}