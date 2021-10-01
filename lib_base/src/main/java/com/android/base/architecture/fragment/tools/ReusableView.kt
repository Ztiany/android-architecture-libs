package com.android.base.architecture.fragment.tools

import android.view.View
import android.view.ViewGroup

/**
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2020-03-05 12:44
 */
class ReusableView {

    private var layoutView: View? = null

    private var cachedView: View? = null

    internal var reuseTheView: Boolean = false

    private fun createFragmentLayoutCached(factory: () -> View): View? {
        if (cachedView == null) {
            val layout = createFragmentLayout(factory)
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

    private fun createFragmentLayout(factory: () -> View): View {
        return factory()
    }

    fun createView(factory: () -> View): View? {
        return if (reuseTheView) {
            createFragmentLayoutCached(factory)
        } else {
            createFragmentLayout(factory)
        }
    }

    fun isNotTheSameView(view: View): Boolean {
        if (layoutView !== view) {
            layoutView = view
            return true
        }
        return false
    }

    fun destroyView(): Boolean {
        if (!reuseTheView) {
            cachedView = null
            layoutView = null
        }
        return !reuseTheView
    }

}