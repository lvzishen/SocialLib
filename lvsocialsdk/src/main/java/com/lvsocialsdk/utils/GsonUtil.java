package com.lvsocialsdk.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Gson工具类
 * <p/>
 * Created by lvzishen on 16/12/2.
 */
public final class GsonUtil {
    private static Gson gson;

    static {
        GsonBuilder builder = new GsonBuilder();
        gson = builder.create();
    }

    public static <T> T fromJson(String json, Class<T> clazz) {
        return gson.fromJson(json, clazz);
    }

    public static String toJson(Object src) {
        return gson.toJson(src);
    }
}
