package com.android.base.rxjava.retrial;


import androidx.annotation.Nullable;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import timber.log.Timber;

public class ObservableRetryDelay implements Function<Observable<Throwable>, ObservableSource<?>> {

    private final int mMaxRetries;
    private final long mRetryDelayMillis;
    @Nullable private RetryChecker mRetryChecker;
    private int mRetryCount = 0;

    @SuppressWarnings("unused")
    public ObservableRetryDelay(final int maxRetries, final long retryDelayMillis) {
        this(maxRetries, retryDelayMillis, null);
    }

    public ObservableRetryDelay(final int maxRetries, final long retryDelayMillis, @Nullable RetryChecker retryChecker) {
        mMaxRetries = maxRetries;
        mRetryDelayMillis = retryDelayMillis;
        mRetryChecker = retryChecker != null ? retryChecker : throwable -> true;
    }

    @Override
    public ObservableSource<?> apply(Observable<Throwable> throwableObservable) {
        return throwableObservable.flatMap((Function<Throwable, ObservableSource<?>>) throwable -> {
            if (mRetryChecker != null && !mRetryChecker.verify(throwable)) {
                return Observable.error(throwable);
            }
            mRetryCount++;
            Timber.i(new Date().toLocaleString() + " 自动重试" + (mRetryCount) + "次，在" + Thread.currentThread() + "线程");
            if (mRetryCount <= mMaxRetries) {
                return Observable.timer(mRetryDelayMillis, TimeUnit.MILLISECONDS);
            }
            return Observable.error(throwable);
        });
    }

}