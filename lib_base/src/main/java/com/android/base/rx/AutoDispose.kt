@file:JvmName("AutoDisposeUtils")

package com.android.base.rx

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import com.uber.autodispose.*
import com.uber.autodispose.AutoDispose.autoDisposable
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import io.reactivex.*
import io.reactivex.disposables.Disposable
import timber.log.Timber

fun <T> Flowable<T>.bindLifecycle(lifecycleOwner: LifecycleOwner): FlowableSubscribeProxy<T> {
    return this.`as`(autoDisposable(AndroidLifecycleScopeProvider.from(lifecycleOwner)))
}

fun <T> Flowable<T>.bindLifecycle(lifecycleOwner: LifecycleOwner, event: Lifecycle.Event): FlowableSubscribeProxy<T> {
    return this.`as`(autoDisposable(AndroidLifecycleScopeProvider.from(lifecycleOwner, event)))
}

fun <T> Observable<T>.bindLifecycle(lifecycleOwner: LifecycleOwner): ObservableSubscribeProxy<T> {
    return this.`as`(autoDisposable(AndroidLifecycleScopeProvider.from(lifecycleOwner)))
}

fun <T> Observable<T>.bindLifecycle(lifecycleOwner: LifecycleOwner, event: Lifecycle.Event): ObservableSubscribeProxy<T> {
    return this.`as`(autoDisposable(AndroidLifecycleScopeProvider.from(lifecycleOwner, event)))
}

fun Completable.bindLifecycle(lifecycleOwner: LifecycleOwner): CompletableSubscribeProxy {
    return this.`as`(autoDisposable<Any>(AndroidLifecycleScopeProvider.from(lifecycleOwner)))
}

fun Completable.bindLifecycle(lifecycleOwner: LifecycleOwner, event: Lifecycle.Event): CompletableSubscribeProxy {
    return this.`as`(autoDisposable<Any>(AndroidLifecycleScopeProvider.from(lifecycleOwner, event)))
}

fun <T> Maybe<T>.bindLifecycle(lifecycleOwner: LifecycleOwner): MaybeSubscribeProxy<T> {
    return this.`as`(autoDisposable(AndroidLifecycleScopeProvider.from(lifecycleOwner)))
}

fun <T> Maybe<T>.bindLifecycle(lifecycleOwner: LifecycleOwner, event: Lifecycle.Event): MaybeSubscribeProxy<T> {
    return this.`as`(autoDisposable(AndroidLifecycleScopeProvider.from(lifecycleOwner, event)))
}

fun <T> Single<T>.bindLifecycle(lifecycleOwner: LifecycleOwner): SingleSubscribeProxy<T> {
    return this.`as`(autoDisposable(AndroidLifecycleScopeProvider.from(lifecycleOwner)))
}

fun <T> Single<T>.bindLifecycle(lifecycleOwner: LifecycleOwner, event: Lifecycle.Event): SingleSubscribeProxy<T> {
    return this.`as`(autoDisposable(AndroidLifecycleScopeProvider.from(lifecycleOwner, event)))
}

fun <T> ObservableSubscribeProxy<T>.subscribed(): Disposable = this.subscribe(RxKit.logResultHandler(), RxKit.logErrorHandler())
fun <T> FlowableSubscribeProxy<T>.subscribed(): Disposable = this.subscribe(RxKit.logResultHandler(), RxKit.logErrorHandler())
fun <T> SingleSubscribeProxy<T>.subscribed(): Disposable = this.subscribe(RxKit.logResultHandler(), RxKit.logErrorHandler())
fun <T> MaybeSubscribeProxy<T>.subscribed(): Disposable = this.subscribe(RxKit.logResultHandler(), RxKit.logErrorHandler())
fun CompletableSubscribeProxy.subscribed(): Disposable = this.subscribe(RxKit.logCompletedHandler(), RxKit.logErrorHandler())

fun <T> ObservableSubscribeProxy<T>.subscribeIgnoreError(action: (T) -> Unit): Disposable = this.subscribe(
        {
            action(it)
        },
        {
            Timber.e(it, "Kotlin Extends ignoreError: ")
        }
)

fun <T> FlowableSubscribeProxy<T>.subscribeIgnoreError(action: (T) -> Unit): Disposable = this.subscribe(
        {
            action(it)
        },
        {
            Timber.e(it, "Kotlin Extends ignoreError: ")
        }
)

fun <T> SingleSubscribeProxy<T>.subscribeIgnoreError(action: (T) -> Unit): Disposable = this.subscribe(
        {
            action(it)
        },
        {
            Timber.e(it, "Kotlin Extends ignoreError: ")
        }
)

fun <T> MaybeSubscribeProxy<T>.subscribeIgnoreError(action: (T) -> Unit): Disposable = this.subscribe(
        {
            action(it)
        },
        {
            Timber.e(it, "Kotlin Extends ignoreError: ")
        }
)

fun CompletableSubscribeProxy.subscribeIgnoreError(action: () -> Unit): Disposable = this.subscribe(
        {
            action()
        },
        {
            Timber.e(it, "Kotlin Extends ignoreError: ")
        }
)