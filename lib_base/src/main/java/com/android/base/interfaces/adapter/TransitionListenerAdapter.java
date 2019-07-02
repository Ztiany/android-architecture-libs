package com.android.base.interfaces.adapter;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.transition.Transition;


@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public interface TransitionListenerAdapter extends Transition.TransitionListener {

    @Override
    default void onTransitionStart(Transition transition) {
    }

    @Override
    default void onTransitionEnd(Transition transition) {
    }

    @Override
    default void onTransitionCancel(Transition transition) {
    }

    @Override
    default void onTransitionPause(Transition transition) {
    }

    @Override
    default void onTransitionResume(Transition transition) {
    }

}
