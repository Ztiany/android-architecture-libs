package com.android.sdk.net.progress;


public interface ProgressListener {

    void onProgress(long contentLength, long currentBytes, float percent, boolean isFinish);

    void onLoadFail(Exception e);

}
