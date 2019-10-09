package com.android.base.app.fragment;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.android.base.R;
import com.android.base.utils.BaseUtils;


public class DefaultHorizontalAnimator implements FragmentAnimator {

    @Override
    public Animation makeOpenEnter() {
        return AnimationUtils.loadAnimation(BaseUtils.getAppContext(), R.anim.h_fragment_open_enter);
    }

    @Override
    public Animation makeOpenExit() {
        return AnimationUtils.loadAnimation(BaseUtils.getAppContext(), R.anim.h_fragment_open_exit);
    }

    @Override
    public Animation makeCloseEnter() {
        return AnimationUtils.loadAnimation(BaseUtils.getAppContext(), R.anim.h_fragment_close_enter);
    }

    @Override
    public Animation makeCloseExit() {
        return AnimationUtils.loadAnimation(BaseUtils.getAppContext(), R.anim.h_fragment_close_exit);
    }

}