package com.android.sdk.cache;


import com.github.dmstocking.optional.java.util.Optional;

import java.lang.reflect.Type;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.reactivex.Flowable;

/**
 * 缓存接口
 *
 * @author Ztiany
 * Date : 2016-10-24 21:59
 */
public interface Storage {

    void putEntity(@NonNull String key, @Nullable Object entity, long cacheTime);

    void putEntity(@NonNull String key, @Nullable Object entity);

    /**
     * @param key  缓存的 key
     * @param type 缓存实体类型，如果是泛型类型，请使用 {@link TypeFlag}标识
     * @param <T>  缓存实体类型
     * @return 缓存
     */
    @Nullable
    <T> T getEntity(@NonNull String key, @NonNull Type type);

    /**
     * 如果没有获取到缓存，那么 Flowable 将不会发送任何数据，默认在调用线程加载缓存。
     *
     * @param key  缓存的 key
     * @param type 缓存实体类型，如果是泛型类型，请使用 {@link TypeFlag}标识
     * @param <T>  缓存实体类型
     * @return 缓存
     */
    <T> Flowable<T> flowable(@NonNull String key, @NonNull Type type);

    /**
     * 默认在调用线程加载缓存。
     *
     * @param key  缓存的 key
     * @param type 缓存实体类型，如果是泛型类型，请使用 {@link TypeFlag}标识
     * @param <T>  缓存实体类型
     * @return 缓存
     */
    <T> Flowable<Optional<T>> flowableOptional(@NonNull String key, @NonNull Type type);

    void putString(@NonNull String key, @Nullable String value);

    @NonNull
    String getString(@NonNull String key, @NonNull String defaultValue);

    @Nullable
    String getString(@NonNull String key);

    void putLong(@NonNull String key, long value);

    long getLong(@NonNull String key, long defaultValue);

    void putInt(@NonNull String key, int value);

    int getInt(@NonNull String key, int defaultValue);

    void putBoolean(@NonNull String key, boolean value);

    boolean getBoolean(@NonNull String key, boolean defaultValue);

    void remove(@NonNull String key);

    void clearAll();

}
