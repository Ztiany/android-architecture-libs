package com.android.base.app.fragment.animator

import android.view.animation.*
import com.android.base.R
import com.android.base.utils.BaseUtils

interface FragmentAnimator {
    fun makeOpenEnter(): Animation?
    fun makeOpenExit(): Animation?
    fun makeCloseEnter(): Animation?
    fun makeCloseExit(): Animation?
}

class DefaultVerticalAnimator : FragmentAnimator {
    override fun makeOpenEnter(): Animation? {
        return AnimationUtils.loadAnimation(BaseUtils.getAppContext(), R.anim.v_fragment_open_enter)
    }

    override fun makeOpenExit(): Animation? {
        return AnimationUtils.loadAnimation(BaseUtils.getAppContext(), R.anim.v_fragment_open_exit)
    }

    override fun makeCloseEnter(): Animation? {
        return AnimationUtils.loadAnimation(BaseUtils.getAppContext(), R.anim.v_fragment_close_enter)
    }

    override fun makeCloseExit(): Animation? {
        return AnimationUtils.loadAnimation(BaseUtils.getAppContext(), R.anim.v_fragment_close_exit)
    }
}

class DefaultHorizontalAnimator : FragmentAnimator {
    override fun makeOpenEnter(): Animation? {
        return AnimationUtils.loadAnimation(BaseUtils.getAppContext(), R.anim.h_fragment_open_enter)
    }

    override fun makeOpenExit(): Animation? {
        return AnimationUtils.loadAnimation(BaseUtils.getAppContext(), R.anim.h_fragment_open_exit)
    }

    override fun makeCloseEnter(): Animation? {
        return AnimationUtils.loadAnimation(BaseUtils.getAppContext(), R.anim.h_fragment_close_enter)
    }

    override fun makeCloseExit(): Animation? {
        return AnimationUtils.loadAnimation(BaseUtils.getAppContext(), R.anim.h_fragment_close_exit)
    }
}

class DefaultNoAnimator : FragmentAnimator {
    override fun makeOpenEnter(): Animation? {
        return AnimationUtils.loadAnimation(BaseUtils.getAppContext(), R.anim.base_no_anim)
    }

    override fun makeOpenExit(): Animation? {
        return AnimationUtils.loadAnimation(BaseUtils.getAppContext(), R.anim.base_no_anim)
    }

    override fun makeCloseEnter(): Animation? {
        return AnimationUtils.loadAnimation(BaseUtils.getAppContext(), R.anim.base_no_anim)
    }

    override fun makeCloseExit(): Animation? {
        return AnimationUtils.loadAnimation(BaseUtils.getAppContext(), R.anim.base_no_anim)
    }
}


class DefaultScaleAnimator : FragmentAnimator {
    override fun makeOpenEnter(): Animation? {
        return makeOpenCloseAnimation(1.125f, 1.0f, 0f, 1f)
    }

    override fun makeOpenExit(): Animation? {
        return makeOpenCloseAnimation(1.0f, .975f, 1f, 0f)
    }

    override fun makeCloseEnter(): Animation? {
        return makeOpenCloseAnimation(.975f, 1.0f, 0f, 1f)
    }

    override fun makeCloseExit(): Animation? {
        return makeOpenCloseAnimation(1.0f, 1.075f, 1f, 0f)
    }

    companion object {
        private val DECELERATE_QUINT: Interpolator = DecelerateInterpolator(2.5f)
        private val DECELERATE_CUBIC: Interpolator = DecelerateInterpolator(1.5f)
        private const val ANIM_DUR = 220
        private fun makeOpenCloseAnimation(startScale: Float, endScale: Float, startAlpha: Float, endAlpha: Float): Animation {
            val set = AnimationSet(false)
            val scale = ScaleAnimation(startScale, endScale, startScale, endScale, Animation.RELATIVE_TO_SELF, .5f, Animation.RELATIVE_TO_SELF, .5f)
            scale.interpolator = DECELERATE_QUINT
            scale.duration = ANIM_DUR.toLong()
            set.addAnimation(scale)
            val alpha = AlphaAnimation(startAlpha, endAlpha)
            alpha.interpolator = DECELERATE_CUBIC
            alpha.duration = ANIM_DUR.toLong()
            set.addAnimation(alpha)
            return set
        }
    }
}
