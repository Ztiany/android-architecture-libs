package com.android.sdk.cache;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Modifier;
import java.lang.reflect.Type;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2018-11-01 16:38
 */
final class JsonUtils {

    private static final String TAG = JsonUtils.class.getSimpleName();

    private final static Gson GSON = new GsonBuilder()
            .excludeFieldsWithModifiers(Modifier.TRANSIENT)
            .excludeFieldsWithModifiers(Modifier.STATIC)
            .create();

    static String toJson(Object entity) {
        if (entity == null) {
            return "";
        }
        try {
            return GSON.toJson(entity);
        } catch (Exception e) {
            Log.e(TAG, "JsonSerializer toJson error with: entity = " + entity, e);
        }
        return "";
    }

    @SuppressWarnings("unchecked")
    static <T> T fromJson(String json, Type clazz) {
        try {
            if (clazz == String.class) {
                return (T) json;
            } else {
                return GSON.fromJson(json, clazz);
            }
        } catch (Exception e) {
            Log.e(TAG, "JsonSerializer fromJson error with: json = " + json + " class = " + clazz, e);
        }
        return null;
    }

}
