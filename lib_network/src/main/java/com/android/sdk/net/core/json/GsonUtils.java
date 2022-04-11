package com.android.sdk.net.core.json;

import static com.android.sdk.net.core.json.JsonDeserializers.DoubleJsonDeserializer;
import static com.android.sdk.net.core.json.JsonDeserializers.FloatJsonDeserializer;
import static com.android.sdk.net.core.json.JsonDeserializers.IntegerJsonDeserializer;
import static com.android.sdk.net.core.json.JsonDeserializers.StringJsonDeserializer;
import static com.android.sdk.net.core.json.JsonDeserializers.UnitJsonDeserializer;
import static com.android.sdk.net.core.json.JsonDeserializers.VoidJsonDeserializer;

import com.android.sdk.net.core.json.JsonDeserializers.PrimitiveDoubleJsonDeserializer;
import com.android.sdk.net.core.json.JsonDeserializers.PrimitiveFloatJsonDeserializer;
import com.android.sdk.net.core.json.JsonDeserializers.PrimitiveIntegerJsonDeserializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Modifier;

import kotlin.Unit;

/**
 * @author Ztiany
 * Date: 2018-08-15
 */
public class GsonUtils {

    private final static Gson GSON = new GsonBuilder()
            .excludeFieldsWithModifiers(Modifier.TRANSIENT)
            .excludeFieldsWithModifiers(Modifier.STATIC)
            /*容错处理*/
            .registerTypeAdapter(int.class, new PrimitiveIntegerJsonDeserializer())
            .registerTypeAdapter(float.class, new PrimitiveFloatJsonDeserializer())
            .registerTypeAdapter(double.class, new PrimitiveDoubleJsonDeserializer())
            .registerTypeAdapter(Integer.class, new IntegerJsonDeserializer())
            .registerTypeAdapter(Float.class, new FloatJsonDeserializer())
            .registerTypeAdapter(Double.class, new DoubleJsonDeserializer())
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
