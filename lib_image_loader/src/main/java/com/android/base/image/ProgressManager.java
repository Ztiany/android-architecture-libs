package com.android.base.image;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.UiThread;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;

final class ProgressManager {

    private static volatile ProgressManager mProgressManager;
    private static final int DEFAULT_REFRESH_TIME = 200;

    private final Handler mHandler = new Handler(Looper.getMainLooper());
    private final Map<String, List<WeakReference<ProgressListener>>> mMultiResponseListeners;
    private final Map<String, WeakReference<ProgressListener>> mExclusiveResponseListeners;
    private final ResponseProgressInterceptor mInterceptor;
    private int mRefreshTime = DEFAULT_REFRESH_TIME;//进度刷新时间(单位ms)，避免高频率调用

    static ProgressManager getInstance() {
        if (mProgressManager == null) {
            synchronized (ProgressManager.class) {
                if (mProgressManager == null) {
                    mProgressManager = new ProgressManager();
                }
            }
        }
        return mProgressManager;
    }

    private ProgressManager() {
        mMultiResponseListeners = new HashMap<>();
        mExclusiveResponseListeners = new HashMap<>();
        this.mInterceptor = new ResponseProgressInterceptor();
    }

    OkHttpClient.Builder withProgress(OkHttpClient.Builder builder) {
        return builder.addNetworkInterceptor(mInterceptor);
    }

    @UiThread
    private void notifyResponseProgress(String url, ProgressInfo progressInfo) {
        //multi
        List<WeakReference<ProgressListener>> weakReferences = mMultiResponseListeners.get(url);
        if (isNotEmpty(weakReferences)) {
            for (WeakReference<ProgressListener> weakReference : weakReferences) {
                ProgressListener progressListener = weakReference.get();
                if (progressListener != null) {
                    progressListener.onProgress(url, progressInfo);
                }
            }
        }
        //Exclusive
        WeakReference<ProgressListener> listenerWeakReference = mExclusiveResponseListeners.get(url);
        if (listenerWeakReference != null) {
            ProgressListener progressListener = listenerWeakReference.get();
            if (progressListener != null) {
                progressListener.onProgress(url, progressInfo);
            }
        }
    }

    @UiThread
    private void notifyResponseError(String url, long id, Exception e) {
        //multi
        List<WeakReference<ProgressListener>> weakReferences = mMultiResponseListeners.get(url);
        if (isNotEmpty(weakReferences)) {
            for (WeakReference<ProgressListener> weakReference : weakReferences) {
                ProgressListener progressListener = weakReference.get();
                if (progressListener != null) {
                    progressListener.onError(id, url, e);
                }
            }
        }
        //Exclusive
        WeakReference<ProgressListener> listenerWeakReference = mExclusiveResponseListeners.get(url);
        if (listenerWeakReference != null) {
            ProgressListener progressListener = listenerWeakReference.get();
            if (progressListener != null) {
                progressListener.onError(id, url, e);
            }
        }
    }

    /**
     * 设置每次被调用的间隔时间,单位毫秒
     */
    void setRefreshTime(int refreshTime) {
        if (refreshTime < 0) {
            throw new IllegalArgumentException("the refreshTime must be >= 0");
        }
        mRefreshTime = refreshTime;
    }

    ///////////////////////////////////////////////////////////////////////////
    //listener
    ///////////////////////////////////////////////////////////////////////////
    @UiThread
    void addLoadListener(String url, ProgressListener listener) {
        //check
        if (TextUtils.isEmpty(url)) {
            throw new NullPointerException("url cannot be null");
        }
        if (listener == null) {
            throw new NullPointerException("ProgressListener cannot be null");
        }

        List<WeakReference<ProgressListener>> progressListeners = mMultiResponseListeners.get(url);
        //make list if need
        if (progressListeners == null) {
            progressListeners = new ArrayList<>();
            mMultiResponseListeners.put(url, progressListeners);
        }
        //add direct
        if (progressListeners.isEmpty()) {
            progressListeners.add(new WeakReference<>(listener));
        } else {
            //Prevent duplication
            boolean containers = false;
            ProgressListener reference;
            for (WeakReference<ProgressListener> progressListener : progressListeners) {
                reference = progressListener.get();
                if (reference == listener) {
                    containers = true;
                    break;
                }
            }
            if (!containers) {
                progressListeners.add(new WeakReference<>(listener));
            }
        }
    }

    @UiThread
    @SuppressWarnings("WeakerAccess")
    public void setListener(String url, ProgressListener progressListener) {
        if (TextUtils.isEmpty(url)) {
            throw new NullPointerException("url cannot be null");
        }
        if (progressListener == null) {
            mExclusiveResponseListeners.remove(url);
        } else {
            mExclusiveResponseListeners.put(url, new WeakReference<>(progressListener));
        }
    }

    @UiThread
    void removeListener(String url) {
        mMultiResponseListeners.remove(url);
    }

    private boolean isNotEmpty(List<WeakReference<ProgressListener>> weakReferences) {
        return weakReferences != null && !weakReferences.isEmpty();
    }

    ///////////////////////////////////////////////////////////////////////////
    //ResponseProgressInterceptor
    ///////////////////////////////////////////////////////////////////////////
    private class ResponseProgressInterceptor implements Interceptor {

        @NonNull
        @Override
        public Response intercept(@NonNull Chain chain) throws IOException {
            return wrapResponseBody(chain.proceed(chain.request()));
        }

        private Response wrapResponseBody(Response response) {
            if (response == null || response.body() == null) {
                return response;
            }
            final String key = response.request().url().toString();
            ProgressResponseBody progressResponseBody = new ProgressResponseBody(response.body(), mRefreshTime) {
                @Override
                void onProgress(ProgressInfo progressInfo) {
                    runOnUIThread(() -> notifyResponseProgress(key, progressInfo));
                }

                @Override
                protected void onError(long id, Exception e) {
                    runOnUIThread(() -> notifyResponseError(key, id, e));
                }
            };
            return response.newBuilder().body(progressResponseBody).build();
        }
    }

    private void runOnUIThread(Runnable runnable) {
        mHandler.post(runnable);
    }

}