package com.android.base.widget.shape

import androidx.annotation.IntDef
import com.android.base.widget.shape.Direction.Companion.BOTTOM
import com.android.base.widget.shape.Direction.Companion.LEFT
import com.android.base.widget.shape.Direction.Companion.RIGHT
import com.android.base.widget.shape.Direction.Companion.TOP

@IntDef(TOP, BOTTOM, LEFT, RIGHT)
@Retention(AnnotationRetention.SOURCE)
annotation class Direction {
    companion object {
        const val TOP = 1
        const val BOTTOM = 2
        const val LEFT = 3
        const val RIGHT = 4
    }
}