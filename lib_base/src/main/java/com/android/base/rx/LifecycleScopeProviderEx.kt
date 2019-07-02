package com.android.base.rx

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import com.uber.autodispose.*
import com.uber.autodispose.lifecycle.LifecycleScopeProvider
import io.reactivex.*

/**
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2019-05-10 14:41
 */
interface LifecycleScopeProviderEx<T> : LifecycleScopeProvider<T> {

    fun <T> Flowable<T>.autoDispose(): FlowableSubscribeProxy<T> {
        return this.`as`(AutoDispose.autoDisposable(this@LifecycleScopeProviderEx))
    }

    fun <T> Observable<T>.autoDispose(): ObservableSubscribeProxy<T> {
        return this.`as`(AutoDispose.autoDisposable(this@LifecycleScopeProviderEx))
    }

    fun Completable.autoDispose(): CompletableSubscribeProxy {
        return this.`as`(AutoDispose.autoDisposable<Any>(this@LifecycleScopeProviderEx))
    }

    fun <T> Maybe<T>.autoDispose(): MaybeSubscribeProxy<T> {
        return this.`as`(AutoDispose.autoDisposable(this@LifecycleScopeProviderEx))
    }

    fun <T> Single<T>.autoDispose(): SingleSubscribeProxy<T> {
        return this.`as`(AutoDispose.autoDisposable(this@LifecycleScopeProviderEx))
    }

}

interface LifecycleOwnerEx : LifecycleOwner {

    fun <T> Flowable<T>.autoDispose(): FlowableSubscribeProxy<T> {
        return this.bindLifecycle(this@LifecycleOwnerEx)
    }

    fun <T> Observable<T>.autoDispose(): ObservableSubscribeProxy<T> {
        return this.bindLifecycle(this@LifecycleOwnerEx)
    }

    fun Completable.autoDispose(): CompletableSubscribeProxy {
        return this.bindLifecycle(this@LifecycleOwnerEx)
    }

    fun <T> Maybe<T>.autoDispose(): MaybeSubscribeProxy<T> {
        return this.bindLifecycle(this@LifecycleOwnerEx)
    }

    fun <T> Single<T>.autoDispose(): SingleSubscribeProxy<T> {
        return this.bindLifecycle(this@LifecycleOwnerEx)
    }
    
    fun <T> Flowable<T>.autoDispose(event: Lifecycle.Event): FlowableSubscribeProxy<T> {
        return this.bindLifecycle(this@LifecycleOwnerEx, event)
    }

    fun <T> Observable<T>.autoDispose(event: Lifecycle.Event): ObservableSubscribeProxy<T> {
        return this.bindLifecycle(this@LifecycleOwnerEx, event)
    }

    fun Completable.autoDispose(event: Lifecycle.Event): CompletableSubscribeProxy {
        return this.bindLifecycle(this@LifecycleOwnerEx, event)
    }

    fun <T> Maybe<T>.autoDispose(event: Lifecycle.Event): MaybeSubscribeProxy<T> {
        return this.bindLifecycle(this@LifecycleOwnerEx, event)
    }

    fun <T> Single<T>.autoDispose(event: Lifecycle.Event): SingleSubscribeProxy<T> {
        return this.bindLifecycle(this@LifecycleOwnerEx, event)
    }

}