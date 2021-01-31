package com.pedromassango.doubleclick

import android.view.View

/**
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2020-10-14 12:28
 */
fun View.setOnDoubleClickListener(onSingleClicked: ((View) -> Unit)? = null, onDoubleClicked: (View) -> Unit) {
    setOnClickListener(object : DoubleClickListener() {
        override fun onDoubleClick(view: View) {
            onDoubleClicked(view)
        }

        override fun onSingleClick(view: View) {
            onSingleClicked?.invoke(view)
        }
    })
}