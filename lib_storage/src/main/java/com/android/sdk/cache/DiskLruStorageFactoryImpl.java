package com.android.sdk.cache;

import android.content.Context;

import timber.log.Timber;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2018-11-09 11:20
 */
public class DiskLruStorageFactoryImpl implements StorageFactory {

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
                Timber.d("DiskLruStorage was initialized, but do not support multi process");
            }
            super.enableMultiProcess(multiProcess);
            return this;
        }

        @Override
        public Storage build() {
            DiskLruStorageImpl diskLruStorage = new DiskLruStorageImpl(context, storageId);
            if (encipher != null) {
                return new EncipherStorage(diskLruStorage, encipher);
            }
            return diskLruStorage;
        }

    }

}