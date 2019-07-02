package com.android.sdk.net.kit

import com.android.sdk.net.core.Result
import com.github.dmstocking.optional.java.util.Optional
import io.reactivex.Flowable
import io.reactivex.Observable


fun <T : Result<E>, E> Observable<T>.optionalExtractor(): Observable<Optional<E>> {
    return this.compose(ResultHandlers.optionalExtractor<E>())
}

fun <T : Result<E>, E> Observable<T>.resultExtractor(): Observable<E> {
    return this.compose(ResultHandlers.resultExtractor<E>())
}

fun <E, T : Result<E>> Observable<T>.resultChecker(): Observable<Result<E>> {
    return (this.compose(ResultHandlers.resultChecker<E>()))
}

fun <T : Result<E>, E> Flowable<T>.optionalExtractor(): Flowable<Optional<E>> {
    return this.compose(ResultHandlers.optionalExtractor<E>())
}

fun <T : Result<E>, E> Flowable<T>.resultExtractor(): Flowable<E> {
    return this.compose(ResultHandlers.resultExtractor<E>())
}

fun <E, T : Result<E>> Flowable<T>.resultChecker(): Flowable<Result<E>> {
    return (this.compose(ResultHandlers.resultChecker<E>()))
}

/**组合远程数据与本地数据，参考 [RxResultKit.composeMultiSource]*/
fun <T> composeMultiSource(
        remote: Flowable<Optional<T>>,
        local: Flowable<Optional<T>>,
        selector: (local: T, remote: T?) -> Boolean,
        onNewData: (T?) -> Unit
): Flowable<Optional<T>> {
    return RxResultKit.composeMultiSource(remote, local, Selector(selector), Consumer { onNewData(it) })
}

/**组合远程数据与本地数据，参考 [RxResultKit.composeMultiSource]*/
fun <T> composeMultiSource(
        remote: Flowable<Optional<T>>,
        local: Flowable<Optional<T>>,
        onNewData: (T?) -> Unit
): Flowable<Optional<T>> {
    return RxResultKit.composeMultiSource(remote, local, { t1, t2 -> t1 != t2 }, { onNewData(it) })
}

fun <T> selectLocalOrRemote(remote: Flowable<Optional<T>>, local: T?, selector: (local: T, remote: T?) -> Boolean, onNewData: (T?) -> Unit): Flowable<Optional<T>> {
    return RxResultKit.selectLocalOrRemote(remote, local, selector, onNewData)
}

fun <T> selectLocalOrRemote(remote: Flowable<Optional<T>>, local: T?, onNewData: (T?) -> Unit): Flowable<Optional<T>> {
    return RxResultKit.selectLocalOrRemote(remote, local, { t1, t2 -> t1 != t2 }, { onNewData(it) })
}