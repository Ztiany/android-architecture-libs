package com.android.base.app.fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2020-03-05 12:44
 */
internal class ReusingView {

    var cacheTheView = false

    private var layoutView: View? = null

    private var cachedView: View? = null

    private fun createFragmentLayoutCached(providedLayout: Any?, inflater: LayoutInflater, container: ViewGroup?): View? {
        if (cachedView == null) {
            val layout = createFragmentLayout(providedLayout, inflater, container)
            cachedView = layout
            return layout
        }

        cachedView?.run {
            val viewParent = parent
            if (viewParent != null && viewParent is ViewGroup) {
                viewParent.removeView(this)
            }
        }

        return cachedView
    }

    private fun createFragmentLayout(providedLayout: Any?, inflater: LayoutInflater, container: ViewGroup?): View? {
        return when (providedLayout) {
            null -> null
            is Int -> inflater.inflate(providedLayout, container, false)
            is View -> providedLayout
            else -> throw IllegalArgumentException("Here you should provide  a  layout id  or a View")
        }
    }

    fun createView(provideLayout: Any?, inflater: LayoutInflater, container: ViewGroup?): View? {
        return if (cacheTheView) {
            createFragmentLayoutCached(provideLayout, inflater, container)
        } else {
            createFragmentLayout(provideLayout, inflater, container)
        }
    }

    fun isNotTheSameView(view: View): Boolean {
        if (layoutView !== view) {
            layoutView = view
            return true
        }
        return false
    }

}