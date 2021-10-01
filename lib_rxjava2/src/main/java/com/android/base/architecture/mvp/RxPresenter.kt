package com.android.base.architecture.mvp

import androidx.annotation.CallSuper
import com.android.base.rxjava.autodispose.AutoDisposeLifecycleScopeProviderEx
import com.uber.autodispose.lifecycle.CorrespondingEventsFunction
import com.uber.autodispose.lifecycle.LifecycleEndedException
import com.uber.autodispose.lifecycle.LifecycleScopes
import io.reactivex.CompletableSource
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject


/**
 * work with RxJava
 *
 * @author Ztiany
 * Date : 2016-10-19 12:17
 * Email: 1169654504@qq.com
 */
abstract class RxPresenter<V : IBaseView> : AbstractPresenter<V>(), AutoDisposeLifecycleScopeProviderEx<RxPresenter.LifecycleEvent> {

    private val lifecycleSubject = BehaviorSubject.create<LifecycleEvent>()

    enum class LifecycleEvent {
        START,
        DESTROY
    }

    @CallSuper
    override fun onStart() {
        lifecycleSubject.onNext(LifecycleEvent.START)
    }

    @CallSuper
    override fun onDestroy() {
        lifecycleSubject.onNext(LifecycleEvent.DESTROY)
        super@RxPresenter.onDestroy()
    }

    final override fun lifecycle(): Observable<LifecycleEvent> {
        return lifecycleSubject
    }

    final override fun correspondingEvents(): CorrespondingEventsFunction<LifecycleEvent> {
        return LIFECYCLE_CORRESPONDING_EVENTS
    }

    final override fun peekLifecycle(): LifecycleEvent? {
        return lifecycleSubject.value
    }

    final override fun requestScope(): CompletableSource {
        return LifecycleScopes.resolveScopeFromLifecycle<LifecycleEvent>(this)
    }

    companion object {
        internal val LIFECYCLE_CORRESPONDING_EVENTS: CorrespondingEventsFunction<LifecycleEvent> = CorrespondingEventsFunction {
            return@CorrespondingEventsFunction when (it) {
                LifecycleEvent.START -> LifecycleEvent.DESTROY
                else -> throw LifecycleEndedException("Cannot bind to LifecycleEvent  when outside of it.")
            }
        }
    }

}
