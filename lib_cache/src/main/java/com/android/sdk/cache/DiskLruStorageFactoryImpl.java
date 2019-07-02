package com.android.sdk.cache;

import android.content.Context;
import android.util.Log;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2018-11-09 11:20
 */
public class DiskLruStorageFactoryImpl implements StorageFactory {

    private static final String TAG = DiskLruStorageFactoryImpl.class.getSimpleName();

    @Override
    public Builder newBuilder(Context context) {
        return new DiskLruStorageBuilder(context);
    }

    class DiskLruStorageBuilder extends Builder {

        DiskLruStorageBuilder(Context context) {
            super(context);
        }

        @Override
        public Builder enableMultiProcess(boolean multiProcess) {
            if (multiProcess) {
                Log.d(TAG, "DiskLruStorage was initialized, but do not support multi process");
            }
            super.enableMultiProcess(multiProcess);
            return this;
        }

        @Override
        public Storage build() {
            return new DiskLruStorageImpl(context, storageId);
        }

    }

}