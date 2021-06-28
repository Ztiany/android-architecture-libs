package com.android.base.app.fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.base.utils.android.views.removeFromTree

/**
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2020-03-05 12:44
 */
class ReusableView {

    var reuseTheView = false

    private var layoutView: View? = null
    private var cachedView: View? = null

    private var previousParams: Any? = null

    private fun createFragmentLayoutCached(
        providedLayout: Any?,
        inflater: LayoutInflater,
        container: ViewGroup?
    ): View? {

        var view = cachedView

        return if (view == null || providedLayout != previousParams) {
            view = createFragmentLayout(providedLayout, inflater, container)
            cachedView = view
            view
        } else {
            view.removeFromTree()
            view
        }

    }

    private fun createFragmentLayout(
        providedLayout: Any?,
        inflater: LayoutInflater,
        container: ViewGroup?
    ): View? {

        previousParams = providedLayout

        return when (providedLayout) {
            null -> null
            is Int -> inflater.inflate(providedLayout, container, false)
            is View -> providedLayout
            else -> throw IllegalArgumentException("Here you should provide  a  layout id  or a View")
        }
    }

    fun createView(provideLayout: Any?, inflater: LayoutInflater, container: ViewGroup?): View? {
        return if (reuseTheView) {
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

    fun destroyView() {
        if (!reuseTheView) {
            layoutView = null
            cachedView = null
            previousParams = null
        }
    }

}