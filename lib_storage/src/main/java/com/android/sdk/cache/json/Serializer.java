package com.android.sdk.cache.json;

import java.lang.reflect.Type;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2020-03-20 17:19
 */
public interface Serializer {

    String toJson(Object entity);

    <T> T fromJson(String json, Type clazz);

}
