package com.android.base.architecture.fragment.animator

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

    private val decelerateQuint: Interpolator = DecelerateInterpolator(2.5f)
    private val decelerateCubic: Interpolator = DecelerateInterpolator(1.5f)

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

    private fun makeOpenCloseAnimation(startScale: Float, endScale: Float, startAlpha: Float, endAlpha: Float): Animation {
        val animDuration = 220
        val set = AnimationSet(false)
        val scale = ScaleAnimation(startScale, endScale, startScale, endScale, Animation.RELATIVE_TO_SELF, .5f, Animation.RELATIVE_TO_SELF, .5f)
        scale.interpolator = decelerateQuint
        scale.duration = animDuration.toLong()
        set.addAnimation(scale)
        val alpha = AlphaAnimation(startAlpha, endAlpha)
        alpha.interpolator = decelerateCubic
        alpha.duration = animDuration.toLong()
        set.addAnimation(alpha)
        return set
    }

}

/** recommended in androidx*/
class FragmentScaleAnimator : FragmentAnimator {

    private val decelerateQuint: Interpolator = DecelerateInterpolator(2.5f)
    private val decelerateCubic: Interpolator = DecelerateInterpolator(1.5f)

    override fun makeOpenEnter(): Animation? {
        return makeOpenCloseAnimation(1.125f, 1.0f, 0f, 1f)
    }

    override fun makeOpenExit(): Animation? {
        return makeOpenCloseAnimation(1.0f, 1.0f, 1f, 0f)
    }

    override fun makeCloseEnter(): Animation? {
        return makeOpenCloseAnimation(1.0f, 1.0f, 0f, 1f)
    }

    override fun makeCloseExit(): Animation? {
        return makeOpenCloseAnimation(1.0f, 1.075f, 1f, 0f)
    }

    private fun makeOpenCloseAnimation(startScale: Float, endScale: Float, startAlpha: Float, endAlpha: Float): Animation {
        val animDuration = 350

        val set = AnimationSet(false)
        val scale = ScaleAnimation(startScale, endScale, startScale, endScale, Animation.RELATIVE_TO_SELF, .5f, Animation.RELATIVE_TO_SELF, .5f)
        scale.interpolator = decelerateQuint
        scale.duration = animDuration.toLong()
        set.addAnimation(scale)
        val alpha = AlphaAnimation(startAlpha, endAlpha)
        alpha.interpolator = decelerateCubic
        alpha.duration = animDuration.toLong()
        set.addAnimation(alpha)

        return set
    }

}