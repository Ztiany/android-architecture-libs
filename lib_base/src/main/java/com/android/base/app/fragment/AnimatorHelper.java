package com.android.base.app.fragment;

import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.android.base.R;

import androidx.fragment.app.FragmentTransaction;

public final class AnimatorHelper {

    private Animation noneAnim, noneAnimFixed;
    private Animation enterAnim, exitAnim, popEnterAnim, popExitAnim;

    private Context context;
    private FragmentAnimator fragmentAnimator;

    AnimatorHelper(Context context, FragmentAnimator fragmentAnimator) {
        this.context = context;
        notifyChanged(fragmentAnimator);
    }

    void notifyChanged(FragmentAnimator fragmentAnimator) {
        this.fragmentAnimator = fragmentAnimator;
        if (fragmentAnimator != null) {
            initEnterAnim();
            initExitAnim();
            initPopEnterAnim();
            initPopExitAnim();
        }
    }

    public Animation getNoneAnim() {
        if (noneAnim == null) {
            noneAnim = AnimationUtils.loadAnimation(context, R.anim.no_anim);
        }
        return noneAnim;
    }

    public Animation getNoneAnimFixed() {
        if (noneAnimFixed == null) {
            noneAnimFixed = new Animation() {
            };
        }
        return noneAnimFixed;
    }

    private Animation initEnterAnim() {
        if (fragmentAnimator.getEnter() == 0) {
            enterAnim = AnimationUtils.loadAnimation(context, R.anim.no_anim);
        } else {
            enterAnim = AnimationUtils.loadAnimation(context, fragmentAnimator.getEnter());
        }
        return enterAnim;
    }

    private Animation initExitAnim() {
        if (fragmentAnimator.getExit() == 0) {
            exitAnim = AnimationUtils.loadAnimation(context, R.anim.no_anim);
        } else {
            exitAnim = AnimationUtils.loadAnimation(context, fragmentAnimator.getExit());
        }
        return exitAnim;
    }

    private Animation initPopEnterAnim() {
        if (fragmentAnimator.getPopEnter() == 0) {
            popEnterAnim = AnimationUtils.loadAnimation(context, R.anim.no_anim);
        } else {
            popEnterAnim = AnimationUtils.loadAnimation(context, fragmentAnimator.getPopEnter());
        }
        return popEnterAnim;
    }

    private Animation initPopExitAnim() {
        if (fragmentAnimator.getPopExit() == 0) {
            popExitAnim = AnimationUtils.loadAnimation(context, R.anim.no_anim);
        } else {
            popExitAnim = AnimationUtils.loadAnimation(context, fragmentAnimator.getPopExit());
        }
        return popExitAnim;
    }

    Animation onCreateAnimation(BaseFragment baseFragment, int transit, boolean enter, int nextAnim) {
        if (transit == FragmentTransaction.TRANSIT_FRAGMENT_OPEN) {
            if (enter) {
                return enterAnim;
            } else {
                return popExitAnim;
            }
        } else if (transit == FragmentTransaction.TRANSIT_FRAGMENT_CLOSE) {
            if (enter) {
                return popEnterAnim;
            } else {
                return exitAnim;
            }
        }
        return null;
    }

}