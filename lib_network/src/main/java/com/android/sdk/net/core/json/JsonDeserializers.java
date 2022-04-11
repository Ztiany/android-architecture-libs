package com.android.sdk.net.core.json;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import kotlin.Unit;
import timber.log.Timber;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2018-11-13 14:57
 */
class JsonDeserializers {

    static class PrimitiveIntegerJsonDeserializer implements JsonDeserializer<Integer> {

        @Override
        public Integer deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            try {
                return json.getAsInt();
            } catch (Exception e) {
                Timber.e(e, "IntegerJsonDeserializer-deserialize-error:%s", (json != null ? json.toString() : ""));
                return 0;
            }
        }

    }

    static class PrimitiveFloatJsonDeserializer implements JsonDeserializer<Float> {

        @Override
        public Float deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            try {
                return json.getAsFloat();
            } catch (Exception e) {
                Timber.e(e, "FloatJsonDeserializer-deserialize-error:%s", (json != null ? json.toString() : ""));
                return 0F;
            }
        }

    }

    static class PrimitiveDoubleJsonDeserializer implements JsonDeserializer<Double> {

        @Override
        public Double deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            try {
                return json.getAsDouble();
            } catch (Exception e) {
                Timber.e(e, "DoubleJsonDeserializer-deserialize-error:%s", (json != null ? json.toString() : ""));
                return 0D;
            }
        }

    }

    static class IntegerJsonDeserializer implements JsonDeserializer<Integer> {

        @Override
        public Integer deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            try {
                return json.getAsInt();
            } catch (Exception e) {
                Timber.e(e, "IntegerJsonDeserializer-deserialize-error:%s", (json != null ? json.toString() : ""));
                return null;
            }
        }

    }

    static class FloatJsonDeserializer implements JsonDeserializer<Float> {

        @Override
        public Float deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            try {
                return json.getAsFloat();
            } catch (Exception e) {
                Timber.e(e, "FloatJsonDeserializer-deserialize-error:%s", (json != null ? json.toString() : ""));
                return null;
            }
        }

    }

    static class DoubleJsonDeserializer implements JsonDeserializer<Double> {

        @Override
        public Double deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            try {
                return json.getAsDouble();
            } catch (Exception e) {
                Timber.e(e, "DoubleJsonDeserializer-deserialize-error:%s", (json != null ? json.toString() : ""));
                return null;
            }
        }

    }

    static class StringJsonDeserializer implements JsonDeserializer<String> {

        @Override
        public String deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            try {
                return json.getAsString();
            } catch (Exception e) {

                Timber.e(e, "StringJsonDeserializer-deserialize-error:%s", (json != null ? json.toString() : ""));
                return null;
            }
        }
    }

    static class VoidJsonDeserializer implements JsonDeserializer<Void> {

        @Override
        public Void deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return null;
        }

    }

    static class UnitJsonDeserializer implements JsonDeserializer<Unit> {

        @Override
        public Unit deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return Unit.INSTANCE;
        }

    }

}
