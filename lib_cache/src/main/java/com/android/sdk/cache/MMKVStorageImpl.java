package com.android.sdk.cache;

import android.content.Context;
import android.util.Log;

import com.github.dmstocking.optional.java.util.Optional;
import com.tencent.mmkv.MMKV;

import java.lang.reflect.Type;
import java.util.concurrent.atomic.AtomicBoolean;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.reactivex.Flowable;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2018-11-01 11:25
 */
@SuppressWarnings("WeakerAccess,unused")
public class MMKVStorageImpl implements Storage {

    private static final String TAG = MMKVStorageImpl.class.getSimpleName();

    private static final AtomicBoolean INITIALIZED = new AtomicBoolean(false);

    private final MMKV mMmkv;

    public MMKVStorageImpl(Context context, String mmkvId) {
        this(context, mmkvId, false);
    }

    public MMKVStorageImpl(Context context, String mmkvId, boolean multiProcess) {

        if (INITIALIZED.compareAndSet(false, true)) {
            String rootDir = MMKV.initialize(context.getApplicationContext());
            Log.d(TAG, "MMKV initialized and rootDir is: " + rootDir);
        }

        int mode = multiProcess ? MMKV.MULTI_PROCESS_MODE : MMKV.SINGLE_PROCESS_MODE;
        mMmkv = MMKV.mmkvWithID(mmkvId, mode);
    }

    @Override
    public void putString(@NonNull String key, @Nullable String value) {
        try {
            if (value == null) {
                remove(key);
                return;
            }
            mMmkv.encode(key, value);
        } catch (Error error) {
            error.printStackTrace();
        }
    }

    @NonNull
    @Override
    public String getString(@NonNull String key, @NonNull String defaultValue) {
        String result = null;
        try {
            result = mMmkv.decodeString(key, defaultValue);
        } catch (Error error) {
            error.printStackTrace();
        }
        return result == null ? defaultValue : result;
    }

    @Nullable
    @Override
    public String getString(@NonNull String key) {
        try {
            return mMmkv.decodeString(key);
        } catch (Error error) {
            error.printStackTrace();
        }
        return null;
    }

    @Override
    public void putLong(@NonNull String key, long value) {
        try {
            mMmkv.encode(key, value);
        } catch (Error error) {
            error.printStackTrace();
        }
    }

    @Override
    public long getLong(@NonNull String key, long defaultValue) {
        try {
            return mMmkv.decodeLong(key, defaultValue);
        } catch (Error error) {
            error.printStackTrace();
        }
        return defaultValue;
    }

    @Override
    public void putInt(@NonNull String key, int value) {
        try {
            mMmkv.encode(key, value);
        } catch (Error error) {
            error.printStackTrace();
        }
    }

    @Override
    public int getInt(@NonNull String key, int defaultValue) {
        try {
            return mMmkv.decodeInt(key, defaultValue);
        } catch (Error error) {
            error.printStackTrace();
        }
        return defaultValue;
    }

    @Override
    public void putBoolean(@NonNull String key, boolean value) {
        try {
            mMmkv.encode(key, value);
        } catch (Error error) {
            error.printStackTrace();
        }
    }

    @Override
    public boolean getBoolean(@NonNull String key, boolean defaultValue) {
        try {
            return mMmkv.decodeBool(key, defaultValue);
        } catch (Error error) {
            error.printStackTrace();
        }
        return defaultValue;
    }

    @Override
    public void remove(@NonNull String key) {
        try {
            mMmkv.removeValueForKey(key);
        } catch (Error error) {
            error.printStackTrace();
        }
    }

    @Override
    public void clearAll() {
        mMmkv.clear();
    }

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