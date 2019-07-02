package com.android.sdk.net.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Modifier;

import kotlin.Unit;

import static com.android.sdk.net.gson.JsonDeserializers.DoubleJsonDeserializer;
import static com.android.sdk.net.gson.JsonDeserializers.FloatJsonDeserializer;
import static com.android.sdk.net.gson.JsonDeserializers.IntegerJsonDeserializer;
import static com.android.sdk.net.gson.JsonDeserializers.StringJsonDeserializer;
import static com.android.sdk.net.gson.JsonDeserializers.UnitJsonDeserializer;
import static com.android.sdk.net.gson.JsonDeserializers.VoidJsonDeserializer;

/**
 * @author Ztiany
 * Date: 2018-08-15
 */
public class GsonUtils {

    private final static Gson GSON = new GsonBuilder()
            .excludeFieldsWithModifiers(Modifier.TRANSIENT)
            .excludeFieldsWithModifiers(Modifier.STATIC)
            /*容错处理*/
            .registerTypeAdapter(int.class, new IntegerJsonDeserializer())
            .registerTypeAdapter(Integer.class, new IntegerJsonDeserializer())
            .registerTypeAdapter(double.class, new DoubleJsonDeserializer())
            .registerTypeAdapter(Double.class, new DoubleJsonDeserializer())
            .registerTypeAdapter(float.class, new FloatJsonDeserializer())
            .registerTypeAdapter(Float.class, new FloatJsonDeserializer())
            .registerTypeAdapter(String.class, new StringJsonDeserializer())
            .registerTypeAdapter(Void.class, new VoidJsonDeserializer())
            .registerTypeAdapter(Unit.class, new UnitJsonDeserializer())
            /*根据注解反序列化抽象类或接口*/
            .registerTypeAdapterFactory(new AutoGenTypeAdapterFactory())
            .create();

    public static Gson gson() {
        return GSON;
    }

}
