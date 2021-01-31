package me.ztiany.widget.clicks

import android.view.View

/**
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2021-01-04 15:15
 */
abstract class SingleClickListener(private val offset: Int = 300) : View.OnClickListener {

    private var time = System.currentTimeMillis()

    final override fun onClick(v: View) {
        val currentTimeMillis = System.currentTimeMillis()
        if (offset <= currentTimeMillis - time) {
            onSingleClick(v)
            time = System.currentTimeMillis()
        }
    }

    abstract fun onSingleClick(view: View)

}