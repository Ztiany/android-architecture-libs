package com.android.sdk.net.kit;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.android.sdk.net.NetContext;
import com.android.sdk.net.exception.ApiErrorException;
import com.android.sdk.net.exception.NetworkErrorException;
import com.github.dmstocking.optional.java.util.Optional;

import org.reactivestreams.Publisher;

import java.io.IOException;

import io.reactivex.Flowable;
import io.reactivex.flowables.ConnectableFlowable;
import io.reactivex.functions.Function;
import retrofit2.HttpException;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2019-05-15 19:17
 */
public class RxResultKit {

    /**
     * <pre>
     * 1. 如果网络不可用，直接返回缓存，如果没有缓存，报错没有网络连接
     * 2. 如果存在网络
     *      2.1 如果没有缓存，则从网络获取
     *      2.1 如果有缓存，则先返回缓存，然后从网络获取
     *      2.1 对比缓存与网络数据，如果没有更新，则忽略
     *      2.1 如果有更新，则更新缓存，并返回网络数据
     * </pre>
     *
     * @param remote    网络数据源
     * @param local     本地数据源
     * @param onNewData 当有更新时，返回新的数据，可以在这里存储
     * @param <T>       数据类型
     * @param selector  比较器，返回当 true 表示两者相等，参数顺序为 (local, remote)
     * @return 组合后的Observable
     * </T>
     */
    public static <T> Flowable<Optional<T>> composeMultiSource(
            Flowable<Optional<T>> remote,
            Flowable<Optional<T>> local,
            Selector<T> selector,
            Consumer<T> onNewData) {

        //没有网络
        if (!NetContext.get().connected()) {
            return local.flatMap((Function<Optional<T>, Publisher<Optional<T>>>) tOptional -> {
                if (tOptional.isPresent()) {
                    return Flowable.just(tOptional);
                } else {
                    return Flowable.error(new NetworkErrorException());
                }
            });
        }

        //有网络
        ConnectableFlowable<Optional<T>> sharedLocal = local.replay();
        sharedLocal.connect();

        //组合数据
        Flowable<Optional<T>> complexRemote = sharedLocal
                .flatMap((Function<Optional<T>, Publisher<Optional<T>>>) localData -> {
                    //没有缓存
                    if (!localData.isPresent()) {
                        return remote.doOnNext(tOptional -> onNewData.accept(tOptional.orElse(null)));
                    }
                    /*有缓存时网络错误，不触发错误，只有在过期时返回新的数据*/
                    return remote
                            .onErrorResumeNext(onErrorResumeFunction(onNewData))
                            .filter(remoteData -> selector.test(localData.get(), remoteData.orElse(null)))
                            .doOnNext(newData -> onNewData.accept(newData.orElse(null)));
                });

        return Flowable.concat(sharedLocal.filter(Optional::isPresent), complexRemote);
    }

    public static <T> Flowable<Optional<T>> selectLocalOrRemote(Flowable<Optional<T>> remote, @Nullable T local, Selector<T> selector, Consumer<T> onNewData) {
        //没有网络没有缓存
        if (!NetContext.get().connected() && local == null) {
            return Flowable.error(new NetworkErrorException());
        }
        //有缓存
        if (local != null) {
            return Flowable.concat(
                    Flowable.just(Optional.of(local)),
                    /*有缓存时网络错误，不触发错误，只有在过期时返回新的数据*/
                    remote.onErrorResumeNext(onErrorResumeFunction(onNewData))
                            .filter(tOptional -> selector.test(local, tOptional.orElse(null)))
                            .doOnNext(tOptional -> onNewData.accept(tOptional.orElse(null))));
        } else {
            return remote;
        }
    }

    @NonNull
    private static <T> Function<Throwable, Publisher<? extends Optional<T>>> onErrorResumeFunction(Consumer<T> onNewData) {
        return throwable -> {
            if (isNetworkError(throwable)) {
                return Flowable.never();
            } else {
                if (throwable instanceof ApiErrorException) {
                    onNewData.accept(null);
                }
                return Flowable.error(throwable);
            }
        };
    }

    private static boolean isNetworkError(Throwable exception) {
        return exception instanceof IOException
                || exception instanceof HttpException
                || exception instanceof NetworkErrorException;
    }

}