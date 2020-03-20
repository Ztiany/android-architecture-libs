package com.android.sdk.cache;

import com.github.dmstocking.optional.java.util.Optional;

import java.lang.reflect.Type;

import androidx.annotation.NonNull;
import io.reactivex.Flowable;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2020-03-20 17:26
 */
abstract class BaseStorage implements Storage {

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

    @Override
    public <T> Flowable<T> flowable(@NonNull String key, @NonNull Type type) {
        return CommonImpl.flowableEntity(key, type, this);
    }

    @Override
    public <T> Flowable<Optional<T>> flowableOptional(@NonNull String key, @NonNull Type type) {
        return CommonImpl.flowableOptionalEntity(key, type, this);
    }

}
