package com.android.base.rxjava.thread;

import org.reactivestreams.Publisher;

import io.reactivex.Completable;
import io.reactivex.CompletableSource;
import io.reactivex.CompletableTransformer;
import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.Maybe;
import io.reactivex.MaybeSource;
import io.reactivex.MaybeTransformer;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.SingleTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2018-08-10 00:03
 */
public final class ThreadTransformer<T> implements ObservableTransformer<T, T>,
        FlowableTransformer<T, T>,
        SingleTransformer<T, T>,
        MaybeTransformer<T, T>,
        CompletableTransformer {

    public static <T> ThreadTransformer<T> newInstance(int type) {
        return new ThreadTransformer<>(type);
    }

    public static final int IO_UI = 1;
    public static final int COMPUTATION_UI = 2;
    public static final int NEW_THREAD_UI = 3;

    private final int mType;

    private ThreadTransformer(int type) {
        mType = type;
        if (!(mType == IO_UI || mType == COMPUTATION_UI || mType == NEW_THREAD_UI)) {
            throw new IllegalArgumentException("type error: " + type);
        }
    }

    @Override
    public CompletableSource apply(Completable upstream) {
        return upstream.subscribeOn(getScheduler()).observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Publisher<T> apply(Flowable<T> upstream) {
        return upstream.subscribeOn(getScheduler()).observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public MaybeSource<T> apply(Maybe<T> upstream) {
        return upstream.subscribeOn(getScheduler()).observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public ObservableSource<T> apply(Observable<T> upstream) {
        return upstream.subscribeOn(getScheduler()).observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public SingleSource<T> apply(Single<T> upstream) {
        return upstream.subscribeOn(getScheduler()).observeOn(AndroidSchedulers.mainThread());
    }

    private Scheduler getScheduler() {
        if (mType == IO_UI) {
            return Schedulers.io();
        } else if (mType == COMPUTATION_UI) {
            return Schedulers.computation();
        } else {
            return Schedulers.newThread();
        }
    }
}