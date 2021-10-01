package com.android.base.rxjava.rxkit

import com.android.base.rxjava.retrial.FlowableRetryDelay
import com.android.base.rxjava.retrial.ObservableRetryDelay
import com.android.base.rxjava.retrial.RetryChecker
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import timber.log.Timber
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/** RxJava2的一个重要的设计理念是：不吃掉任何一个异常。产生的问题是，当RxJava2“downStream”取消订阅后，“upStream”仍有可能抛出异常，这时由于已经取消订阅，
 * “downStream”无法处理异常，此时的异常无人处理，便会导致程序崩溃,解决方案：在Application设置RxJavaPlugin的ErrorHandler。
 * refer: [RxJava2使用过程中遇到的坑](https://github.com/qqiabc521/blog/issues/3) */
fun setupRxJavaErrorHandler() {
    RxKit.setupRxJavaErrorHandler()
}

operator fun CompositeDisposable?.plusAssign(disposable: Disposable) {
    this?.add(disposable)
}

fun Disposable.addTo(compositeDisposable: CompositeDisposable?) {
    compositeDisposable?.add(this)
}

fun Disposable?.disposeChecked() {
    RxKit.disposeChecked(this)
}

fun newCompositeIfDisposed(cd: CompositeDisposable?): CompositeDisposable {
    return RxKit.newCompositeIfUnsubscribed(cd)
}

fun autoCompositeDisposable(): ReadWriteProperty<Any, CompositeDisposable> {
    return CompositeDisposableDelegate()
}

internal class CompositeDisposableDelegate : ReadWriteProperty<Any, CompositeDisposable> {

    private lateinit var compositeDisposable: CompositeDisposable

    override fun getValue(thisRef: Any, property: KProperty<*>): CompositeDisposable {
        if (!::compositeDisposable.isInitialized) {
            compositeDisposable = CompositeDisposable()
        }
        compositeDisposable = newCompositeIfDisposed(compositeDisposable)
        return compositeDisposable
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: CompositeDisposable) {
        compositeDisposable = value
    }

}

fun <T> Single<T>.observeOnUI(): Single<T> = this.observeOn(AndroidSchedulers.mainThread())
fun <T> Maybe<T>.observeOnUI(): Maybe<T> = this.observeOn(AndroidSchedulers.mainThread())
fun <T> Observable<T>.observeOnUI(): Observable<T> = this.observeOn(AndroidSchedulers.mainThread())
fun <T> Flowable<T>.observeOnUI(): Flowable<T> = this.observeOn(AndroidSchedulers.mainThread())
fun Completable.observeOnUI(): Completable = this.observeOn(AndroidSchedulers.mainThread())

fun <T> Observable<T>.subscribeIgnoreError(action: (T) -> Unit): Disposable = this.subscribe(
    {
        action(it)
    },
    {
        Timber.e(it, "Kotlin Extends ignoreError: ")
    }
)

fun <T> Flowable<T>.subscribeIgnoreError(action: (T) -> Unit): Disposable = this.subscribe(
    {
        action(it)
    },
    {
        Timber.e(it, "Kotlin Extends ignoreError: ")
    }
)

fun <T> Single<T>.subscribeIgnoreError(action: (T) -> Unit): Disposable = this.subscribe(
    {
        action(it)
    },
    {
        Timber.e(it, "Kotlin Extends ignoreError: ")
    }
)

fun <T> Maybe<T>.subscribeIgnoreError(action: (T) -> Unit): Disposable = this.subscribe(
    {
        action(it)
    },
    {
        Timber.e(it, "Kotlin Extends ignoreError: ")
    }
)

fun Completable.subscribeIgnoreError(action: () -> Unit): Disposable = this.subscribe(
    {
        action()
    },
    {
        Timber.e(it, "Kotlin Extends ignoreError: ")
    }
)

inline fun <reified A : Any> RxBus.toObservable(): Flowable<A> = this.toObservable(A::class.java)
inline fun <reified A : Any> RxBus.toObservable(tag: String): Flowable<A> =
    this.toObservable(tag, A::class.java)

fun <T> Observable<T>.io2UI(): Observable<T> = this.compose(RxKit.io2UI())
fun <T> Observable<T>.newThread2UI(): Observable<T> = this.compose(RxKit.newThread2UI())
fun <T> Observable<T>.computation2UI(): Observable<T> = this.compose(RxKit.computation2UI())

fun <T> Flowable<T>.io2UI(): Flowable<T> = this.compose(RxKit.io2UI())
fun <T> Flowable<T>.newThread2UI(): Flowable<T> = this.compose(RxKit.newThread2UI())
fun <T> Flowable<T>.computation2UI(): Flowable<T> = this.compose(RxKit.computation2UI())

fun <T> Single<T>.io2UI(): Single<T> = this.compose(RxKit.io2UI())
fun <T> Single<T>.newThread2UI(): Single<T> = this.compose(RxKit.newThread2UI())
fun <T> Single<T>.computation2UI(): Single<T> = this.compose(RxKit.computation2UI())

fun <T> Maybe<T>.io2UI(): Maybe<T> = this.compose(RxKit.io2UI())
fun <T> Maybe<T>.newThread2UI(): Maybe<T> = this.compose(RxKit.newThread2UI())
fun <T> Maybe<T>.computation2UI(): Maybe<T> = this.compose(RxKit.computation2UI())

fun Completable.io2UI(): Completable = this.compose(RxKit.io2UI<Any>())
fun Completable.newThread2UI(): Completable = this.compose(RxKit.newThread2UI<Any>())
fun Completable.computation2UI(): Completable = this.compose(RxKit.computation2UI<Any>())

fun <T> Observable<T>.subscribed(): Disposable =
    this.subscribe(RxKit.logResultHandler(), RxKit.logErrorHandler())

fun <T> Flowable<T>.subscribed(): Disposable =
    this.subscribe(RxKit.logResultHandler(), RxKit.logErrorHandler())

fun <T> Single<T>.subscribed(): Disposable =
    this.subscribe(RxKit.logResultHandler(), RxKit.logErrorHandler())

fun <T> Maybe<T>.subscribed(): Disposable =
    this.subscribe(RxKit.logResultHandler(), RxKit.logErrorHandler())

fun Completable.subscribed(): Disposable =
    this.subscribe(RxKit.logCompletedHandler(), RxKit.logErrorHandler())


fun <T> Flowable<T>.retryWhen(
    maxRetries: Int,
    retryDelayMillis: Long,
    retryChecker: RetryChecker? = null
): Flowable<T> {
    return this.retryWhen(
        FlowableRetryDelay(
            maxRetries,
            retryDelayMillis,
            retryChecker
        )
    )
}

fun <T> Observable<T>.retryWhen(
    maxRetries: Int,
    retryDelayMillis: Long,
    retryChecker: RetryChecker? = null
): Observable<T> {
    return this.retryWhen(
        ObservableRetryDelay(
            maxRetries,
            retryDelayMillis,
            retryChecker
        )
    )
}

fun Completable.retryWhen(
    maxRetries: Int,
    retryDelayMillis: Long,
    retryChecker: RetryChecker? = null
): Completable {
    return this.retryWhen(
        FlowableRetryDelay(
            maxRetries,
            retryDelayMillis,
            retryChecker
        )
    )
}

fun <T> Single<T>.retryWhen(
    maxRetries: Int,
    retryDelayMillis: Long,
    retryChecker: RetryChecker? = null
): Single<T> {
    return this.retryWhen(
        FlowableRetryDelay(
            maxRetries,
            retryDelayMillis,
            retryChecker
        )
    )
}

fun <T> Maybe<T>.retryWhen(
    maxRetries: Int,
    retryDelayMillis: Long,
    retryChecker: RetryChecker? = null
): Maybe<T> {
    return this.retryWhen(
        FlowableRetryDelay(
            maxRetries,
            retryDelayMillis,
            retryChecker
        )
    )
}
