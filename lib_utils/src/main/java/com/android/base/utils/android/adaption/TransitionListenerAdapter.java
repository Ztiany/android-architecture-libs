package com.android.base.utils.android.adaption;

import android.os.Build;
import android.transition.Transition;

import androidx.annotation.RequiresApi;


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
