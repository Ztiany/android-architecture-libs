package com.android.sdk.cache.disklru;

import android.content.Context;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.sdk.cache.BaseStorage;

import java.io.File;
import java.io.IOException;

@SuppressWarnings("WeakerAccess,unused")
public class DiskLruStorageImpl extends BaseStorage {

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
        File parentFile = mDir.getParentFile();
        if (parentFile != null) {
            @SuppressWarnings("unused")
            boolean mkdirs = parentFile.mkdirs();
        }
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
    public void putString(@NonNull String key, @Nullable String value) {
        if (value == null) {
            getDiskLruCacheHelper().remove(key);
            return;
        }
        getDiskLruCacheHelper().put(key, value);
    }

    @NonNull
    @Override
    public String getString(@NonNull String key, @NonNull String defaultValue) {
        String result = getDiskLruCacheHelper().getAsString(key);
        if (TextUtils.isEmpty(result)) {
            result = defaultValue;
        }
        return result;
    }

    @Nullable
    @Override
    public String getString(@NonNull String key) {
        return getDiskLruCacheHelper().getAsString(key);
    }

    @Override
    public void putLong(@NonNull String key, long value) {
        getDiskLruCacheHelper().put(key, String.valueOf(value));
    }

    @Override
    public long getLong(@NonNull String key, long defaultValue) {
        String strLong = getDiskLruCacheHelper().getAsString(key);
        if (TextUtils.isEmpty(strLong)) {
            return defaultValue;
        }
        return Long.parseLong(strLong);
    }

    @Override
    public void putInt(@NonNull String key, int value) {
        getDiskLruCacheHelper().put(key, String.valueOf(value));
    }

    @Override
    public int getInt(@NonNull String key, int defaultValue) {
        String strInt = getDiskLruCacheHelper().getAsString(key);
        if (TextUtils.isEmpty(strInt)) {
            return defaultValue;
        }
        return Integer.parseInt(strInt);
    }

    @Override
    public void putBoolean(@NonNull String key, boolean value) {
        int bool = value ? 1 : 0;
        getDiskLruCacheHelper().put(key, String.valueOf(bool));
    }

    @Override
    public boolean getBoolean(@NonNull String key, boolean defaultValue) {
        String strInt = getDiskLruCacheHelper().getAsString(key);
        if (TextUtils.isEmpty(strInt)) {
            return defaultValue;
        }
        return Integer.parseInt(strInt) == 1;
    }

    @Override
    public void remove(@NonNull String key) {
        getDiskLruCacheHelper().remove(key);
    }

    @Override
    public void clearAll() {
        try {
            getDiskLruCacheHelper().delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}