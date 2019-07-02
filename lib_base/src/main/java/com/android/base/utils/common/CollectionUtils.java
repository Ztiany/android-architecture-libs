package com.android.base.utils.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class CollectionUtils {

    private CollectionUtils() {
        throw new UnsupportedOperationException();
    }

    public static <T> ArrayList<T> toArrayList(List<T> list) {
        if (list == null) {
            return new ArrayList<>(0);
        }
        if (list instanceof ArrayList) {
            return (ArrayList<T>) list;
        }
        return new ArrayList<>(list);
    }

    public static <T> List<T> emptyIfNull(List<T> list) {
        if (list == null) {
            return Collections.emptyList();
        }
        return list;
    }

}
