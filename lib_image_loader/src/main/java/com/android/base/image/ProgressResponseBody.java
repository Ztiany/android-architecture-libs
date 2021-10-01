package com.android.base.image;

import android.os.SystemClock;

import java.io.IOException;

import androidx.annotation.NonNull;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

abstract class ProgressResponseBody extends ResponseBody {

    private final int mRefreshTime;
    private final ResponseBody mDelegate;
    private BufferedSource mBufferedSource;
    private final ProgressInfo mProgressInfo;

    ProgressResponseBody(ResponseBody responseBody, int refreshTime) {
        this.mDelegate = responseBody;
        this.mRefreshTime = refreshTime;
        this.mProgressInfo = new ProgressInfo(System.currentTimeMillis());
    }

    @Override
    public MediaType contentType() {
        return mDelegate.contentType();
    }

    @Override
    public long contentLength() {
        return mDelegate.contentLength();
    }

    @NonNull
    @Override
    public BufferedSource source() {
        if (mBufferedSource == null) {
            mBufferedSource = Okio.buffer(source(mDelegate.source()));
        }
        return mBufferedSource;
    }

    private Source source(Source source) {
        return new ForwardingSource(source) {

            private long mContentLength;
            private long totalBytesRead = 0L;
            private long lastRefreshTime = 0L;  //最后一次刷新的时间

            @Override
            public long read(@NonNull Buffer sink, long byteCount) throws IOException {
                long bytesRead;

                try {
                    bytesRead = super.read(sink, byteCount);
                } catch (IOException e) {
                    e.printStackTrace();
                    onError(mProgressInfo.getId(), e);
                    throw e;
                }

                if (mContentLength == 0) {
                    mContentLength = contentLength();
                    mProgressInfo.setContentLength(mContentLength);
                }

                // read() returns the number of bytes read, or -1 if this source is exhausted.
                totalBytesRead += bytesRead != -1 ? bytesRead : 0;

                long curTime = SystemClock.elapsedRealtime();
                long intervalTime = curTime - lastRefreshTime;

                if (intervalTime >= mRefreshTime || bytesRead == -1 || totalBytesRead == mContentLength) {

                    boolean finish = bytesRead == -1 && totalBytesRead == mContentLength;

                    mProgressInfo.setCurrentBytes(totalBytesRead);
                    mProgressInfo.setIntervalTime(intervalTime);
                    mProgressInfo.setFinish(finish);
                    mProgressInfo.setEachBytes(bytesRead);

                    onProgress(mProgressInfo);

                    lastRefreshTime = curTime;
                }
                return bytesRead;
            }
        };
    }

    protected abstract void onError(long id, Exception e);

    abstract void onProgress(ProgressInfo progressInfo);

}
