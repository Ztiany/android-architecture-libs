package com.android.base.utils.common;

import java.util.Collection;
import java.util.Map;

/**
 * 对象检查工具
 */
public class Checker {

    private Checker() {

    }

    public static boolean isEmpty(Collection<?> data) {
        return data == null || data.size() == 0;
    }

    public static boolean notEmpty(Collection<?> data) {
        return !isEmpty(data);
    }

    public static boolean isNull(Object o) {
        return o == null;
    }

    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.size() == 0;
    }

    public static boolean notEmpty(Map<?, ?> map) {
        return !isEmpty(map);
    }

    public static <T> boolean isEmpty(T[] t) {
        return t == null || t.length == 0;
    }

    public static <T> boolean notEmpty(T[] t) {
        return !isEmpty(t);
    }

    public static <T> T requireNonNull(T obj) {
        if (obj == null)
            throw new NullPointerException();
        return obj;
    }

    public static <T> T requireNonNull(T obj, String message) {
        if (obj == null)
            throw new NullPointerException(message);
        return obj;
    }

    public static boolean nonNull(Object obj) {
        return obj != null;
    }

}