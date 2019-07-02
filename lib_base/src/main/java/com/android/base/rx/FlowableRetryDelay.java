package com.android.base.rx;

import android.support.annotation.Nullable;

import org.reactivestreams.Publisher;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import timber.log.Timber;

public class FlowableRetryDelay implements Function<Flowable<Throwable>, Publisher<?>> {

    private final int mMaxRetries;
    private final long mRetryDelayMillis;
    @Nullable
    private RetryChecker mRetryChecker;
    private int mRetryCount = 0;

    @SuppressWarnings("unused")
    public FlowableRetryDelay(final int maxRetries, final long retryDelayMillis) {
        this(maxRetries, retryDelayMillis, null);
    }

    public FlowableRetryDelay(final int maxRetries, final long retryDelayMillis, @Nullable RetryChecker retryChecker) {
        mMaxRetries = maxRetries;
        mRetryDelayMillis = retryDelayMillis;
        mRetryChecker = retryChecker != null ? retryChecker : throwable -> true;
    }

    @Override
    public Publisher<?> apply(Flowable<Throwable> throwableFlowable) {
        return throwableFlowable.flatMap((Function<Throwable, Publisher<?>>) throwable -> {
            if (mRetryChecker != null && !mRetryChecker.verify(throwable)) {
                return Flowable.error(throwable);
            }
            mRetryCount++;
            Timber.i(new Date() + " 自动重试" + (mRetryCount + 1) + "次，在" + Thread.currentThread() + "线程");
            if (mRetryCount <= mMaxRetries) {
                return Flowable.timer(mRetryDelayMillis, TimeUnit.MILLISECONDS);
            }
            return Flowable.error(throwable);
        });
    }

}