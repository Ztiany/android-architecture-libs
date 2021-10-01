package com.android.sdk.cache;

import com.google.gson.internal.$Gson$Types;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * copy from gson, 为了不让调用者依赖 gson.
 *
 * @param <T> 类型标记
 */
public abstract class TypeFlag<T> {

    private final Class<? super T> rawType;
    private final Type type;

    @SuppressWarnings("unchecked")
    protected TypeFlag() {
        this.type = getSuperclassTypeParameter(getClass());
        this.rawType = (Class<? super T>) $Gson$Types.getRawType(type);
    }

    @SuppressWarnings("all")
    private static Type getSuperclassTypeParameter(Class<?> subclass) {
        Type superclass = subclass.getGenericSuperclass();
        if (superclass instanceof Class) {
            throw new RuntimeException("Missing type parameter.");
        }
        ParameterizedType parameterized = (ParameterizedType) superclass;
        return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);
    }

    public final Class<? super T> getRawType() {
        return rawType;
    }

    public final Type getType() {
        return type;
    }

}