package com.android.base.utils.android.views

import android.view.View
import android.widget.ListView

/** ListView 局部刷新 */
fun ListView.updateItemView(position: Int, updater: (itemView: View) -> Unit) {
    val firstPos = firstVisiblePosition
    val lastPos = lastVisiblePosition
    //可见才更新，不可见则在getView()时更新
    if (position in firstPos..lastPos) {
        val view = getChildAt(position - firstPos)
        updater(view)
    }
}