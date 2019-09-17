package com.android.sdk.net.progress;

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

class ProgressResponseBody extends ResponseBody {

    private final int mRefreshTime;
    private final ResponseBody mDelegate;
    private final ProgressListener mProgressListener;
    private BufferedSource mBufferedSource;

    ProgressResponseBody(ResponseBody responseBody, int refreshTime, ProgressListener progressListener) {
        this.mDelegate = responseBody;
        mProgressListener = progressListener;
        this.mRefreshTime = refreshTime;
    }

    @Override
    public MediaType contentType() {
        return mDelegate.contentType();
    }

    @Override
    public long contentLength() {
        return mDelegate.contentLength();
    }

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
                    mProgressListener.onLoadFail(e);
                    throw e;
                }
                if (mContentLength == 0) {
                    mContentLength = contentLength();
                }
                // read() returns the number of bytes read, or -1 if this source is exhausted.
                totalBytesRead += bytesRead != -1 ? bytesRead : 0;
                long curTime = SystemClock.elapsedRealtime();

                if (curTime - lastRefreshTime >= mRefreshTime || bytesRead == -1 || totalBytesRead == mContentLength) {
                    boolean finish = bytesRead == -1 && totalBytesRead == mContentLength;
                    mProgressListener.onProgress(mContentLength, totalBytesRead, totalBytesRead * 1.0F / mContentLength, finish);
                    lastRefreshTime = curTime;
                }
                return bytesRead;
            }
        };
    }
}
