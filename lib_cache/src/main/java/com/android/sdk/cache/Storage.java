package com.android.sdk.cache;


import com.github.dmstocking.optional.java.util.Optional;

import java.lang.reflect.Type;

import androidx.annotation.Nullable;
import io.reactivex.Flowable;

/**
 * 缓存接口
 *
 * @author Ztiany
 * Date : 2016-10-24 21:59
 */
public interface Storage {

    void putEntity(String key, @Nullable Object entity, long cacheTime);

    void putEntity(String key, @Nullable Object entity);

    /**
     * @param key  缓存的 key
     * @param type 缓存实体类型，如果是泛型类型，请使用 {@link TypeFlag}标识
     * @param <T>  缓存实体类型
     * @return 缓存
     */
    @Nullable
    <T> T getEntity(String key, Type type);

    /**
     * 如果没有获取到缓存，那么 Flowable 将不会发送任何数据，默认在调用线程加载缓存。
     *
     * @param key  缓存的 key
     * @param type 缓存实体类型，如果是泛型类型，请使用 {@link TypeFlag}标识
     * @param <T>  缓存实体类型
     * @return 缓存
     */
    <T> Flowable<T> flowable(String key, Type type);

    /**
     * 默认在调用线程加载缓存。
     *
     * @param key  缓存的 key
     * @param type 缓存实体类型，如果是泛型类型，请使用 {@link TypeFlag}标识
     * @param <T>  缓存实体类型
     * @return 缓存
     */
    <T> Flowable<Optional<T>> flowableOptional(String key, Type type);

    void putString(String key, String value);

    String getString(String key, String defaultValue);

    @Nullable
    String getString(String key);

    void putLong(String key, long value);

    long getLong(String key, long defaultValue);

    void putInt(String key, int value);

    int getInt(String key, int defaultValue);

    void putBoolean(String key, boolean value);

    boolean getBoolean(String key, boolean defaultValue);

    void remove(String key);

    void clearAll();

}
