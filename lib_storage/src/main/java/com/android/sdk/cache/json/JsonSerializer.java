package com.android.sdk.cache.json;

import android.net.Uri;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;

import java.lang.reflect.Modifier;
import java.lang.reflect.Type;

import timber.log.Timber;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2020-03-20 17:23
 */
public class JsonSerializer implements Serializer {

    private final Gson GSON = new GsonBuilder()
            .excludeFieldsWithModifiers(Modifier.TRANSIENT)
            .excludeFieldsWithModifiers(Modifier.STATIC)
            .registerTypeAdapter(Uri.class, new UriInOut())
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
            Timber.e(e, "JsonSerializer fromJson error with: json %s, class= %s ", json, clazz.toString());
        }
        return null;
    }

    private static class UriInOut implements JsonDeserializer<Uri>, com.google.gson.JsonSerializer<Uri> {
        @Override
        public Uri deserialize(final JsonElement src, final Type srcType, final JsonDeserializationContext context) throws JsonParseException {
            try {
                return Uri.parse(src.getAsString());
            } catch (Exception e) {
                return Uri.EMPTY;
            }
        }

        @Override
        public JsonElement serialize(Uri src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.toString());
        }
    }

}