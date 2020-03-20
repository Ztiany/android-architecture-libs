package com.android.sdk.cache;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Modifier;
import java.lang.reflect.Type;

import timber.log.Timber;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2020-03-20 17:23
 */
public class JsonSerializer implements Serializer {

    private final String TAG = JsonSerializer.class.getSimpleName();

    private final Gson GSON = new GsonBuilder()
            .excludeFieldsWithModifiers(Modifier.TRANSIENT)
            .excludeFieldsWithModifiers(Modifier.STATIC)
            .create();

    @Override
    public String toJson(Object entity) {
        if (entity == null) {
            return "";
        }
        try {
            return GSON.toJson(entity);
        } catch (Exception e) {
            Timber.e(e, "JsonSerializer toJson error with: entity = %s", entity.toString());
        }
        return "";
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T fromJson(String json, Type clazz) {
        try {
            if (clazz == String.class) {
                return (T) json;
            } else {
                return GSON.fromJson(json, clazz);
            }
        } catch (Exception e) {
            Timber.e(e, "JsonSerializer fromJson error with: json = " + json + " class = " + clazz);
        }
        return null;
    }

}