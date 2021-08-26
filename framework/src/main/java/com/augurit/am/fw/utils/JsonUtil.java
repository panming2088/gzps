package com.augurit.am.fw.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;

import java.lang.reflect.Type;

/**
 * Created by ac on 2016-07-26.
 */
public final class JsonUtil {
    private static final Gson gson = new GsonBuilder().serializeNulls().setDateFormat(
            "yyyy-MM-dd HH:mm:ss").create();

    private JsonUtil() {
    }

    public static String getJson(Object object) throws JsonSyntaxException {
        return gson.toJson(object);
    }

    public static <T> T getObject(String jsonString, Class<T> classT)
            throws JsonSyntaxException {
        T returnObject = gson.fromJson(jsonString, classT);
        return returnObject;
    }

    public static <T> T getObject(String jsonString, Type type)
            throws JsonSyntaxException {
        T returnObject = gson.fromJson(jsonString, type);
        return returnObject;
    }

    public static <T> T getObject(JsonReader jsonString, Class<T> classT)
            throws JsonSyntaxException {
        T returnObject = gson.fromJson(jsonString, classT);
        return returnObject;
    }

    public static <T> T getObject(JsonReader jsonString, Type type)
            throws JsonSyntaxException {
        T returnObject = gson.fromJson(jsonString, type);
        return returnObject;
    }
}
