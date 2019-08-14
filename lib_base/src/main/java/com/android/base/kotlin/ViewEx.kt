package com.android.base.kotlin

import android.graphics.drawable.Drawable
import android.support.annotation.ColorRes
import android.support.annotation.DrawableRes
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewCompat
import android.support.v4.widget.NestedScrollView
import android.view.View
import android.view.View.FOCUS_DOWN
import android.view.View.FOCUS_UP
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.ScrollView
import com.android.base.rx.subscribeIgnoreError
import com.android.base.utils.android.ViewUtils
import com.android.base.utils.android.compat.AndroidVersion.atLeast
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit

fun View.visibleOrGone(visible: Boolean) {
    if (visible) {
        this.visibility = View.VISIBLE
    } else {
        this.visibility = View.GONE
    }
}

fun View.visibleOrInvisible(visible: Boolean) {
    if (visible) {
        this.visibility = View.VISIBLE
    } else {
        this.visibility = View.INVISIBLE
    }
}

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun View.isVisible() = this.visibility == View.VISIBLE
fun View.isInvisible() = this.visibility == View.INVISIBLE
fun View.isGone() = this.visibility == View.GONE

fun View.realContext() = ViewUtils.getRealContext(this)

inline fun View.doOnLayoutAvailable(crossinline block: () -> Unit) {
    //isLaidOut 方法作用：如果 view 已经通过至少一个布局，则返回true，因为它最后一次附加到窗口或从窗口分离。
    ViewCompat.isLaidOut(this).yes {
        block()
    }.otherwise {
        addOnLayoutChangeListener(object : View.OnLayoutChangeListener {
            override fun onLayoutChange(v: View?, left: Int, top: Int, right: Int, bottom: Int, oldLeft: Int, oldTop: Int, oldRight: Int, oldBottom: Int) {
                removeOnLayoutChangeListener(this)
                block()
            }
        })
    }
}

inline fun <T : View> T.onGlobalLayoutOnce(crossinline action: T.() -> Unit) {
    val t: T = this
    t.viewTreeObserver
            .addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    action.invoke(t)
                    if (atLeast(16)) {
                        viewTreeObserver.removeOnGlobalLayoutListener(this)
                    } else {
                        @Suppress("DEPRECATION")
                        viewTreeObserver.removeGlobalOnLayoutListener(this)
                    }
                }
            })
}

fun View.setPaddingAll(padding: Int) {
    this.setPadding(padding, padding, padding, padding)
}

fun View.setPaddingLeft(padding: Int) {
    this.setPadding(padding, paddingTop, paddingRight, paddingBottom)
}

fun View.setPaddRight(padding: Int) {
    this.setPadding(paddingLeft, paddingTop, padding, paddingBottom)
}

fun View.setPaddingTop(padding: Int) {
    this.setPadding(paddingLeft, padding, paddingRight, paddingBottom)
}

fun View.setPaddingBottom(padding: Int) {
    this.setPadding(paddingLeft, paddingTop, paddingRight, padding)
}

fun View.colorFromId(@ColorRes colorRes: Int): Int {
    val safeContext = context ?: return 0
    return ContextCompat.getColor(safeContext, colorRes)
}

fun View.drawableFromId(@DrawableRes colorRes: Int): Drawable? {
    val safeContext = context ?: return null
    return ContextCompat.getDrawable(safeContext, colorRes)
}

fun View.setWidth(width: Int) {
    val params = layoutParams ?: ViewGroup.LayoutParams(0, 0)
    params.width = width
    layoutParams = params
}

fun View.setHeight(height: Int) {
    val params = layoutParams ?: ViewGroup.LayoutParams(0, 0)
    params.height = height
    layoutParams = params
}

fun View.setSize(width: Int, height: Int) {
    val params = layoutParams ?: ViewGroup.LayoutParams(0, 0)
    params.width = width
    params.height = height
    layoutParams = params
}

fun View.onDebouncedClick(onClick: (View) -> Unit) {
    onClickObservable(500)
            .subscribeIgnoreError { onClick(this) }
}

fun View.onDebouncedClick(milliseconds: Long, onClick: (View) -> Unit) {
    onClickObservable(milliseconds)
            .subscribeIgnoreError { onClick(this) }
}

fun View.onClickObservable(): Observable<Any> {
    return onClickObservable(500)
}

fun View.onClickObservable(milliseconds: Long): Observable<Any> {
    return RxView.clicks(this)
            .throttleFirst(milliseconds, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
}

inline val ViewGroup.views get() = (0 until childCount).map { getChildAt(it) }

fun ScrollView.scrollToBottom() {
    fullScroll(FOCUS_DOWN)
}

fun ScrollView.scrollToTop() {
    fullScroll(FOCUS_UP)
}

fun NestedScrollView.scrollToBottom() {
    fullScroll(FOCUS_DOWN)
}

fun NestedScrollView.scrollToTop() {
    fullScroll(FOCUS_UP)
}