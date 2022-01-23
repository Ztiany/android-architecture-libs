package com.android.sdk.net.core.host;

import com.android.sdk.net.NetContext;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HostFlagHolder {

    private final Map<Class<?>, String> mResultFlags = new ConcurrentHashMap<>();

    public String getFlag(Type type) {
        if ((type instanceof ParameterizedType)) {
            return getFlagFromClass((Class<?>) ((ParameterizedType) type).getRawType());
        }
        if (type instanceof Class) {
            return getFlagFromClass((Class<?>) type);
        }
        throw new IllegalArgumentException("the result [" + type + "] is not supported");
    }

    private String getFlagFromClass(Class<?> clazz) {
        String flag = mResultFlags.get(clazz);
        if (flag != null) {
            return flag;
        }

        flag = getFlagFromClassAnnotation(clazz);

        registerType(clazz, flag);

        return flag;
    }

    public void registerType(Class<?> clazz, String flag) {
        mResultFlags.put(clazz, flag);
    }

    private String getFlagFromClassAnnotation(Class<?> clazz) {
        if (!clazz.isAnnotationPresent(HostFlag.class)) {
            return NetContext.DEFAULT_FLAG;
        }
        HostFlag hostFlag = (HostFlag) clazz.getAnnotation(HostFlag.class);
        if (hostFlag == null) {
            return NetContext.DEFAULT_FLAG;
        }
        return hostFlag.value();
    }

}
