package com.android.base.utils.android.views

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator

/**
 * 关闭默认局部刷新动画，对性能要求高的地方试用，比如一些公屏等刷新频繁的
 */
fun RecyclerView.closeDefaultAnimator() {
    itemAnimator?.let {
        it.addDuration = 0
        it.changeDuration = 0
        it.moveDuration = 0
        it.removeDuration = 0
        if (it is SimpleItemAnimator) {
            it.supportsChangeAnimations = false
        }
    }
}