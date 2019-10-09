package com.android.base.app.fragment;


import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.ScaleAnimation;


public class DefaultScaleAnimator implements FragmentAnimator {

    private static final Interpolator DECELERATE_QUINT = new DecelerateInterpolator(2.5f);
    private static final Interpolator DECELERATE_CUBIC = new DecelerateInterpolator(1.5f);

    private static final int ANIM_DUR = 220;

    @Override
    public Animation makeOpenEnter() {
        return makeOpenCloseAnimation(1.125f, 1.0f, 0, 1);
    }

    @Override
    public Animation makeOpenExit() {
        return makeOpenCloseAnimation(1.0f, .975f, 1, 0);
    }

    @Override
    public Animation makeCloseEnter() {
        return makeOpenCloseAnimation(.975f, 1.0f, 0, 1);
    }

    @Override
    public Animation makeCloseExit() {
        return makeOpenCloseAnimation(1.0f, 1.075f, 1, 0);
    }

    private static Animation makeOpenCloseAnimation(float startScale, float endScale, float startAlpha, float endAlpha) {
        AnimationSet set = new AnimationSet(false);

        ScaleAnimation scale = new ScaleAnimation(startScale, endScale, startScale, endScale,
                Animation.RELATIVE_TO_SELF, .5f, Animation.RELATIVE_TO_SELF, .5f);
        scale.setInterpolator(DECELERATE_QUINT);
        scale.setDuration(ANIM_DUR);
        set.addAnimation(scale);

        AlphaAnimation alpha = new AlphaAnimation(startAlpha, endAlpha);
        alpha.setInterpolator(DECELERATE_CUBIC);
        alpha.setDuration(ANIM_DUR);
        set.addAnimation(alpha);

        return set;
    }

}