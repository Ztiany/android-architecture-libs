package com.android.sdk.cache;

import android.text.TextUtils;

import java.lang.reflect.Type;

import timber.log.Timber;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2018-11-01 17:11
 */
final class CommonImpl {

    static void putEntity(String key, Object entity, long cacheTime, Storage storage) {
        if (entity == null) {
            storage.remove(key);
            return;
        }
        CacheEntity cacheEntity = new CacheEntity(StorageContext.provideSerializer().toJson(entity), cacheTime);
        storage.putString(key, StorageContext.provideSerializer().toJson(cacheEntity));
    }

    private static String getCacheEntity(String key, Storage storage) {
        String cacheStr = storage.getString(key, "");
        if (TextUtils.isEmpty(cacheStr)) {
            return null;
        }

        CacheEntity cacheEntity = StorageContext.provideSerializer().fromJson(cacheStr, CacheEntity.class);

        if (cacheEntity == null) {
            return null;
        }

        if (cacheEntity.mCacheTime == 0) {
            return cacheEntity.mJsonData;
        }

        if (System.currentTimeMillis() - cacheEntity.mStoreTime < cacheEntity.mCacheTime) {
            return cacheEntity.mJsonData;
        } else {
            storage.remove(key);
        }
        return null;
    }

    static <T> T getEntity(String key, Type clazz, Storage storage) {
        String cacheEntity = getCacheEntity(key, storage);
        Timber.d("cacheEntity = %s", cacheEntity);
        if (cacheEntity != null) {
            return StorageContext.provideSerializer().fromJson(cacheEntity, clazz);
        }
        return null;
    }

}