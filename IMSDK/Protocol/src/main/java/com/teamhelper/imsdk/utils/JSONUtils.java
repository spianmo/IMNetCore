package com.teamhelper.imsdk.utils;


import com.google.gson.Gson;

import java.lang.reflect.Type;

/**
 * @Description: JSON工具类
 * @Author: ZhangLuo
 * @WeChat: ISV-ZhangLuo
 * @Email: zhangluo.chn@gmail.com
 */
public class JSONUtils {

    private final static Gson GSON = new Gson();

    /**
     * 对象转字符串
     *
     * @param data
     * @return
     */
    public static String toString(Object data) {
        if (data == null) {
            return null;
        }
        return GSON.toJson(data);
    }

    /**
     * JSON字符串转对象
     *
     * @param jsonStr
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T parseObject(String jsonStr, Class<T> clazz) {
        return GSON.fromJson(jsonStr, clazz);
    }

    /**
     * JSON字符串转带泛型对象
     *
     * @param jsonStr
     * @param type
     * @param <T>
     * @return
     */
    public static <T> T parseObject(String jsonStr, Type type) {
        return GSON.fromJson(jsonStr, type);
    }
}
