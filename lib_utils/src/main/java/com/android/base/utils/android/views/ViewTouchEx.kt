package com.android.base.utils.android.views

import android.annotation.SuppressLint
import android.graphics.Rect
import android.view.MotionEvent
import android.view.TouchDelegate
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.ViewGroupUtils

/**
 * @see [LayoutDSL](https://github.com/wisdomtl/Layout_DSL/blob/master/app/src/main/java/taylor/com/dsl/Layout.kt)
 */
@SuppressLint("RestrictedApi")
fun View.expand(dx: Int, dy: Int) {

    val parentView = (parent as? ViewGroup) ?: return

    if (parentView.touchDelegate == null) {
        parentView.touchDelegate = MultiTouchDelegate(delegateView = this)
    }

    post {
        val rect = Rect()
        ViewGroupUtils.getDescendantRect(parentView, this, rect)
        rect.inset(-dx, -dy)
        (parentView.touchDelegate as? MultiTouchDelegate)?.delegateViewMap?.put(this, rect)
    }

}

private class MultiTouchDelegate(
    bound: Rect? = null,
    delegateView: View
) : TouchDelegate(bound, delegateView) {

    val delegateViewMap = mutableMapOf<View, Rect>()
    private var delegateView: View? = null

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x.toInt()
        val y = event.y.toInt()
        var handled = false

        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                delegateView = findDelegateViewUnder(x, y)
            }
            MotionEvent.ACTION_CANCEL -> {
                delegateView = null
            }
        }
        delegateView?.let {
            event.setLocation(it.width / 2f, it.height / 2f)
            handled = it.dispatchTouchEvent(event)
        }
        return handled
    }

    private fun findDelegateViewUnder(x: Int, y: Int): View? {
        delegateViewMap.forEach { entry -> if (entry.value.contains(x, y)) return entry.key }
        return null
    }

}