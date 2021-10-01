package com.android.base.architecture.mvp

import androidx.annotation.CallSuper
import java.lang.ref.WeakReference

abstract class AbstractPresenter<V : IBaseView> : IPresenter<V> {

    private var _view: WeakReference<V>? = null

    protected val view: V?
        get() = if (_view != null) {
            _view?.get()
        } else null

    protected val isViewAttached: Boolean
        get() = _view != null && _view?.get() != null

    final override fun bindView(view: V?) {
        if (view == null) {
            throw NullPointerException("Presenter bindView --> view is null")
        }
        if (_view != null) {
            throw UnsupportedOperationException("Presenter bindView --> the view already bind")
        }
        _view = WeakReference(view)
    }

    override fun onPostStart() {}

    override fun onPause() {
    }

    override fun onResume() {
    }

    @CallSuper
    override fun onDestroy() {
        if (_view != null) {
            _view?.clear()
            _view = null
        }
    }

}
