package com.android.sdk.cache;

import android.content.Context;
import android.text.TextUtils;

import com.github.dmstocking.optional.java.util.Optional;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;

import androidx.annotation.NonNull;
import io.reactivex.Flowable;

@SuppressWarnings("WeakerAccess,unused")
public class DiskLruStorageImpl implements Storage {

    private DiskLruCacheHelper mDiskLruCacheHelper;
    private static final int CACHE_SIZE = 50 * 1024 * 1024;//50M
    private final File mDir;
    private final int mSize;

    /**
     * @param context   上下文
     * @param cachePath 缓存文件
     */
    public DiskLruStorageImpl(@NonNull Context context, @NonNull String cachePath) {
        this(context, cachePath, CACHE_SIZE);
    }

    /**
     * @param context   上下文
     * @param cachePath 缓存文件
     * @param cacheSize 缓存大小，字节数
     */
    public DiskLruStorageImpl(@NonNull Context context, @NonNull String cachePath, int cacheSize) {
        mDir = context.getDir(cachePath, Context.MODE_PRIVATE);
        mSize = cacheSize;
        @SuppressWarnings("unused")
        boolean mkdirs = mDir.getParentFile().mkdirs();
    }

    private DiskLruCacheHelper getDiskLruCacheHelper() {
        if (mDiskLruCacheHelper == null || mDiskLruCacheHelper.isClosed()) {
            try {
                mDiskLruCacheHelper = new DiskLruCacheHelper(null, mDir, mSize);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return mDiskLruCacheHelper;
    }

    @Override
    public void putString(String key, String value) {
        if (value == null) {
            getDiskLruCacheHelper().remove(key);
            return;
        }
        getDiskLruCacheHelper().put(buildKey(key), value);
    }

    @Override
    public String getString(String key, String defaultValue) {
        String result = getDiskLruCacheHelper().getAsString(buildKey(key));
        if (TextUtils.isEmpty(result)) {
            result = defaultValue;
        }
        return result;
    }

    @Override
    public String getString(String key) {
        return getDiskLruCacheHelper().getAsString(buildKey(key));
    }

    @Override
    public void putLong(String key, long value) {
        getDiskLruCacheHelper().put(buildKey(key), String.valueOf(value));
    }

    @Override
    public long getLong(String key, long defaultValue) {
        String strLong = getDiskLruCacheHelper().getAsString(buildKey(key));
        if (TextUtils.isEmpty(strLong)) {
            return defaultValue;
        }
        return Long.parseLong(strLong);
    }

    @Override
    public void putInt(String key, int value) {
        getDiskLruCacheHelper().put(buildKey(key), String.valueOf(value));
    }

    @Override
    public int getInt(String key, int defaultValue) {
        String strInt = getDiskLruCacheHelper().getAsString(buildKey(key));
        if (TextUtils.isEmpty(strInt)) {
            return defaultValue;
        }
        return Integer.parseInt(strInt);
    }

    @Override
    public void putBoolean(String key, boolean value) {
        int bool = value ? 1 : 0;
        getDiskLruCacheHelper().put(buildKey(key), String.valueOf(bool));
    }

    @Override
    public boolean getBoolean(String key, boolean defaultValue) {
        String strInt = getDiskLruCacheHelper().getAsString(buildKey(key));
        if (TextUtils.isEmpty(strInt)) {
            return defaultValue;
        }
        return Integer.parseInt(strInt) == 1;
    }

    @Override
    public void remove(String key) {
        getDiskLruCacheHelper().remove(buildKey(key));
    }

    @Override
    public void clearAll() {
        try {
            getDiskLruCacheHelper().delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String buildKey(String originKey) {
        return originKey;
    }

    @Override
    public void putEntity(String key, Object entity, long cacheTime) {
        CommonImpl.putEntity(key, entity, cacheTime, this);
    }

    @Override
    public void putEntity(String key, Object entity) {
        CommonImpl.putEntity(key, entity, 0, this);
    }

    @Override
    public <T> T getEntity(String key, Type type) {
        return CommonImpl.getEntity(key, type, this);
    }

    @Override
    public <T> Flowable<T> flowable(String key, Type type) {
        return CommonImpl.flowableEntity(key, type, this);
    }

    @Override
    public <T> Flowable<Optional<T>> flowableOptional(String key, Type type) {
        return CommonImpl.flowableOptionalEntity(key, type, this);
    }

}