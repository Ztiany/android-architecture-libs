package com.android.sdk.cache;

class CacheEntity {

    String mJsonData;//数据
    long mCacheTime;//有效时间
    long mStoreTime;//存储时间戳

    CacheEntity(String jsonData, long cacheTime) {
        mJsonData = jsonData;
        mCacheTime = cacheTime;
        mStoreTime = System.currentTimeMillis();
    }

}