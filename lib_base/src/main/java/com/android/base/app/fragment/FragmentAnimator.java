package com.android.base.app.fragment;

import android.view.animation.Animation;

public interface FragmentAnimator {

    Animation makeOpenEnter();

    Animation makeOpenExit();

    Animation makeCloseEnter();

    Animation makeCloseExit();

}