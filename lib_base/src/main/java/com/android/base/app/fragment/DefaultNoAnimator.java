package com.android.base.app.fragment;


import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.android.base.R;
import com.android.base.utils.BaseUtils;

public class DefaultNoAnimator implements FragmentAnimator {

    @Override
    public Animation makeOpenEnter() {
        return AnimationUtils.loadAnimation(BaseUtils.getAppContext(), R.anim.base_no_anim);
    }

    @Override
    public Animation makeOpenExit() {
        return AnimationUtils.loadAnimation(BaseUtils.getAppContext(), R.anim.base_no_anim);
    }

    @Override
    public Animation makeCloseEnter() {
        return AnimationUtils.loadAnimation(BaseUtils.getAppContext(), R.anim.base_no_anim);
    }

    @Override
    public Animation makeCloseExit() {
        return AnimationUtils.loadAnimation(BaseUtils.getAppContext(), R.anim.base_no_anim);
    }

}