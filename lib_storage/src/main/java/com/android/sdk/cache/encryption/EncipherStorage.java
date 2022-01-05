package com.android.sdk.cache.encryption;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.sdk.cache.Storage;
import com.android.sdk.cache.BaseStorage;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2020-03-20 17:32
 */
public class EncipherStorage extends BaseStorage {

    private final Storage mStorage;
    private final Encipher mEncipher;

    public EncipherStorage(Storage storage, Encipher encipher) {
        mStorage = storage;
        mEncipher = encipher;
    }

    @Override
    public void putString(@NonNull String key, @Nullable String value) {
        mStorage.putString(key, mEncipher.encrypt(value));
    }

    @NonNull
    @Override
    public String getString(@NonNull String key, @NonNull String defaultValue) {
        return mEncipher.decrypt(mStorage.getString(key, defaultValue));
    }

    @Nullable
    @Override
    public String getString(@NonNull String key) {
        return mEncipher.decrypt(mStorage.getString(key));
    }

    @Override
    public void putLong(@NonNull String key, long value) {
        mStorage.putLong(key, value);
    }

    @Override
    public long getLong(@NonNull String key, long defaultValue) {
        return mStorage.getLong(key, defaultValue);
    }

    @Override
    public void putInt(@NonNull String key, int value) {
        mStorage.putInt(key, value);
    }

    @Override
    public int getInt(@NonNull String key, int defaultValue) {
        return mStorage.getInt(key, defaultValue);
    }

    @Override
    public void putBoolean(@NonNull String key, boolean value) {
        mStorage.putBoolean(key, value);
    }

    @Override
    public boolean getBoolean(@NonNull String key, boolean defaultValue) {
        return mStorage.getBoolean(key, defaultValue);
    }

    @Override
    public void remove(@NonNull String key) {
        mStorage.remove(key);
    }

    @Override
    public void clearAll() {
        mStorage.clearAll();
    }

}
