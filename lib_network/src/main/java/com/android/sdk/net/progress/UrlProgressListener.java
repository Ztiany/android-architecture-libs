package com.android.sdk.net.progress;

public interface UrlProgressListener {

    void onProgress(String url, long contentLength, long currentBytes, float percent, boolean isFinish);

    void onError(String url, Exception e);
}
