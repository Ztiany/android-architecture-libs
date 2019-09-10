package com.android.sdk.net.kit

import com.android.sdk.net.core.Result
import com.github.dmstocking.optional.java.util.Optional
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single


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

fun <T : Result<E>, E> Single<T>.optionalExtractor(): Single<Optional<E>> {
    return this.compose(ResultHandlers.optionalExtractor<E>())
}

fun <T : Result<E>, E> Single<T>.resultExtractor(): Single<E> {
    return this.compose(ResultHandlers.resultExtractor<E>())
}

fun <E, T : Result<E>> Single<T>.resultChecker(): Single<Result<E>> {
    return (this.compose(ResultHandlers.resultChecker<E>()))
}

/**组合远程数据与本地数据，参考 [RxResultKit.concatMultiSource]*/
fun <T> concatMultiSource(
        remote: Flowable<Optional<T>>,
        local: Flowable<Optional<T>>,
        selector: Selector<T>,
        onNewData: (T?) -> Unit
): Flowable<Optional<T>> {
    return RxResultKit.concatMultiSource(remote, local, selector, Consumer { onNewData(it) })
}

/**组合远程数据与本地数据，参考 [RxResultKit.concatMultiSource]*/
fun <T> concatMultiSource(
        remote: Flowable<Optional<T>>,
        local: Flowable<Optional<T>>,
        onNewData: (T?) -> Unit
): Flowable<Optional<T>> {
    return RxResultKit.concatMultiSource(remote, local, { t1, t2 -> t1 != t2 }, { onNewData(it) })
}

/**组合远程数据与本地数据，参考 [RxResultKit.combineMultiSource]*/
fun <T> combineMultiSource(
        remote: Flowable<Optional<T>>,
        local: Flowable<Optional<T>>,
        selector: Selector<T>,
        onNewData: (T?) -> Unit
): Flowable<CombinedResult<T>> {
    return RxResultKit.combineMultiSource(remote, local, selector, Consumer { onNewData(it) })
}

/**组合远程数据与本地数据，参考 [RxResultKit.combineMultiSource]*/
fun <T> combineMultiSource(
        remote: Flowable<Optional<T>>,
        local: Flowable<Optional<T>>,
        onNewData: (T?) -> Unit
): Flowable<CombinedResult<T>> {
    return RxResultKit.combineMultiSource(remote, local, { t1, t2 -> t1 != t2 }, { onNewData(it) })
}
