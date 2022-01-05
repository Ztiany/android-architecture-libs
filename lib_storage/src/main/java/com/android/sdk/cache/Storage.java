package com.android.sdk.cache;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.reflect.Type;

/**
 * 缓存接口
 *
 * @author Ztiany
 * Date : 2016-10-24 21:59
 */
public interface Storage {

    ///////////////////////////////////////////////////////////////////////////
    // Putting
    ///////////////////////////////////////////////////////////////////////////
    void putInt(@NonNull String key, int value);

    void putLong(@NonNull String key, long value);

    void putBoolean(@NonNull String key, boolean value);

    void putString(@NonNull String key, @Nullable String value);

    void putEntity(@NonNull String key, @Nullable Object entity, long cacheTime);

    void putEntity(@NonNull String key, @Nullable Object entity);

    ///////////////////////////////////////////////////////////////////////////
    // Getting
    ///////////////////////////////////////////////////////////////////////////
    int getInt(@NonNull String key, int defaultValue);

    long getLong(@NonNull String key, long defaultValue);

    boolean getBoolean(@NonNull String key, boolean defaultValue);

    @Nullable
    String getString(@NonNull String key);

    @NonNull
    String getString(@NonNull String key, @NonNull String defaultValue);

    /**
     * @param key  缓存的 key
     * @param type 缓存实体类型，如果是泛型类型，请使用 {@link TypeFlag}标识
     * @param <T>  缓存实体类型
     * @return 缓存
     */
    @Nullable
    <T> T getEntity(@NonNull String key, @NonNull Type type);

    /**
     * @param key  缓存的 key
     * @param type 缓存实体类型，如果是泛型类型，请使用 {@link TypeFlag}标识
     * @param <T>  缓存实体类型
     * @return 缓存
     */
    @NonNull
    <T> T getEntity(@NonNull String key, @NonNull Type type, @NonNull T defaultValue);

    ///////////////////////////////////////////////////////////////////////////
    // remove
    ///////////////////////////////////////////////////////////////////////////

    void remove(@NonNull String key);

    void clearAll();

}