package com.android.sdk.cache;

import androidx.annotation.NonNull;

import java.lang.reflect.Type;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2020-03-20 17:26
 */
public abstract class BaseStorage implements Storage {

    @Override
    public void putEntity(@NonNull String key, Object entity, long cacheTime) {
        CommonImpl.putEntity(key, entity, cacheTime, this);
    }

    @Override
    public void putEntity(@NonNull String key, Object entity) {
        CommonImpl.putEntity(key, entity, 0, this);
    }

    @Override
    public <T> T getEntity(@NonNull String key, @NonNull Type type) {
        return CommonImpl.getEntity(key, type, this);
    }

    @NonNull
    @Override
    public <T> T getEntity(@NonNull String key, @NonNull Type type, @NonNull T defaultValue) {
        T entity = CommonImpl.getEntity(key, type, this);
        return entity == null ? defaultValue : entity;
    }

}
