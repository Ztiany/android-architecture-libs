package com.android.sdk.cache;

import android.content.Context;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2018-11-01 17:35
 */
public class MMKVStorageFactoryImpl implements StorageFactory {

    @Override
    public Builder newBuilder(Context context) {
        return new MMKVStorageBuilder(context);
    }

    class MMKVStorageBuilder extends Builder {

        MMKVStorageBuilder(Context context) {
            super(context);
        }

        @Override
        public Storage build() {
            return new MMKVStorageImpl(context, storageId, multiProcess);
        }

    }

}
