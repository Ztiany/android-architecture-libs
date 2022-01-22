package com.android.base.utils.coroutines

import android.os.Build
import android.view.View
import android.view.View.OnAttachStateChangeListener
import kotlinx.coroutines.*

class AutoDisposableJob(
        private val view: View,
        private val wrapped: Job
) : Job by wrapped, OnAttachStateChangeListener {

    override fun onViewAttachedToWindow(v: View?) = Unit

    override fun onViewDetachedFromWindow(v: View?) {
        cancel()
        view.removeOnAttachStateChangeListener(this)
    }

    private fun isViewAttached() =
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && view.isAttachedToWindow || view.windowToken != null

    init {
        if (isViewAttached()) {
            view.addOnAttachStateChangeListener(this)
        } else {
            cancel()
        }

        invokeOnCompletion {
            view.post {
                view.removeOnAttachStateChangeListener(this)
            }
        }
    }
}

fun Job.asAutoDisposable(view: View) = AutoDisposableJob(view, this)
