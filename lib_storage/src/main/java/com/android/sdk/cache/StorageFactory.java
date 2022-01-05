package com.android.sdk.cache;

import android.content.Context;

import com.android.sdk.cache.encryption.Encipher;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2018-11-01 17:32
 */
public interface StorageFactory {

    StorageFactory.Builder newBuilder(Context context);

    abstract class Builder {

        protected Context context;
        protected String storageId;
        protected boolean multiProcess;
        protected Encipher encipher;

        protected Builder(Context context) {
            this.context = context;
        }

        /**
         * 是否允许跨进程访问存储
         */
        public Builder enableMultiProcess(boolean multiProcess) {
            this.multiProcess = multiProcess;
            return this;
        }

        /**
         * @param storageId 存储标识
         */
        public Builder storageId(String storageId) {
            this.storageId = storageId;
            return this;
        }

        public Builder encipher(Encipher encipher) {
            this.encipher = encipher;
            return this;
        }

        public abstract Storage build();
    }

}