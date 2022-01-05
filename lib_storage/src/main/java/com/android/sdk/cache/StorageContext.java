package com.android.sdk.cache;

import com.android.sdk.cache.json.JsonSerializer;
import com.android.sdk.cache.json.Serializer;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2020-03-20 17:48
 */
public class StorageContext {

    private static Serializer sSerializer;

    public static void setSerializer(Serializer serializer) {
        sSerializer = serializer;
    }

    public static Serializer provideSerializer() {
        if (sSerializer != null) {
            return sSerializer;
        }
        return Holder.JSON_SERIALIZER;
    }

    private static class Holder {
        private static final Serializer JSON_SERIALIZER = new JsonSerializer();
    }

}
