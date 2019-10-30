package com.android.base.utils.debug

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import com.android.base.R
import com.android.base.app.fragment.BaseFragment
import com.android.base.utils.android.views.visibleOrGone
import kotlinx.android.synthetic.main.base_debug_environment.*

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2019-08-13 19:17
 */
class EnvironmentConfigFragment : BaseFragment() {

    companion object {
        private const val SHOW_TITLE = "show_title"

        fun newInstance(showTitle: Boolean) = EnvironmentConfigFragment().apply {
            arguments = Bundle().apply {
                putBoolean(SHOW_TITLE, showTitle)
            }
        }

    }

    override fun provideLayout() = R.layout.base_debug_environment

    override fun onViewPrepared(view: View, savedInstanceState: Bundle?) {
        super.onViewPrepared(view, savedInstanceState)

        baseToolbarDebug.visibleOrGone(arguments?.getBoolean(SHOW_TITLE, false) ?: false)

        val allCategory = EnvironmentContext.allCategory()
        allCategory.forEach { (category, list) ->
            val environmentItemLayout = EnvironmentItemLayout(requireContext())
            environmentItemLayout.bindEnvironmentList(category, list)
            baseLlDebugHostContent.addView(environmentItemLayout, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))
        }
    }

}

