package com.android.sdk.net.rxjava2

import com.android.sdk.net.core.result.Result
import com.github.dmstocking.optional.java.util.Optional
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single


fun <T : Result<E>, E> Observable<T>.optionalExtractor(): Observable<Optional<E>> {
    return this.compose(ResultHandlers.optionalExtractor())
}

fun <T : Result<E>, E> Observable<T>.resultExtractor(): Observable<E> {
    return this.compose(ResultHandlers.resultExtractor())
}

fun <E, T : Result<E>> Observable<T>.resultChecker(): Observable<Result<E>> {
    return (this.compose(ResultHandlers.resultChecker()))
}

fun <T : Result<E>, E> Flowable<T>.optionalExtractor(): Flowable<Optional<E>> {
    return this.compose(ResultHandlers.optionalExtractor())
}

fun <T : Result<E>, E> Flowable<T>.resultExtractor(): Flowable<E> {
    return this.compose(ResultHandlers.resultExtractor())
}

fun <E, T : Result<E>> Flowable<T>.resultChecker(): Flowable<Result<E>> {
    return (this.compose(ResultHandlers.resultChecker()))
}

fun <T : Result<E>, E> Single<T>.optionalExtractor(): Single<Optional<E>> {
    return this.compose(ResultHandlers.optionalExtractor())
}

fun <T : Result<E>, E> Single<T>.resultExtractor(): Single<E> {
    return this.compose(ResultHandlers.resultExtractor())
}

fun <E, T : Result<E>> Single<T>.resultChecker(): Single<Result<E>> {
    return (this.compose(ResultHandlers.resultChecker()))
}
