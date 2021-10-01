package com.android.base.architecture.fragment.animator;

import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.fragment.app.FragmentTransaction;

import com.android.base.R;

public final class FragmentAnimatorHelper {

    private final Context context;

    private FragmentAnimator fragmentAnimator;

    public FragmentAnimatorHelper(Context context, FragmentAnimator fragmentAnimator) {
        this.context = context;
        this.fragmentAnimator = fragmentAnimator;
    }

    public void changeAnimation(FragmentAnimator fragmentAnimator) {
        this.fragmentAnimator = fragmentAnimator;
    }

    public Animation getNoneAnim() {
        return AnimationUtils.loadAnimation(context, R.anim.base_no_anim);
    }

    public Animation getNoneAnimFixed() {
        return new Animation() {
        };
    }

    private Animation initEnterAnim() {
        if (fragmentAnimator == null) {
            return AnimationUtils.loadAnimation(context, R.anim.base_no_anim);
        } else {
            Animation animation = fragmentAnimator.makeOpenEnter();
            if (animation == null) {
                return AnimationUtils.loadAnimation(context, R.anim.base_no_anim);
            } else {
                return animation;
            }
        }
    }

    private Animation initExitAnim() {
        if (fragmentAnimator == null) {
            return AnimationUtils.loadAnimation(context, R.anim.base_no_anim);
        } else {
            Animation animation = fragmentAnimator.makeOpenExit();
            if (animation == null) {
                return AnimationUtils.loadAnimation(context, R.anim.base_no_anim);
            } else {
                return animation;
            }
        }
    }

    private Animation initPopEnterAnim() {
        if (fragmentAnimator == null) {
            return AnimationUtils.loadAnimation(context, R.anim.base_no_anim);
        } else {
            Animation animation = fragmentAnimator.makeCloseEnter();
            if (animation == null) {
                return AnimationUtils.loadAnimation(context, R.anim.base_no_anim);
            } else {
                return animation;
            }
        }
    }

    private Animation initPopExitAnim() {
        if (fragmentAnimator == null) {
            return AnimationUtils.loadAnimation(context, R.anim.base_no_anim);
        } else {
            Animation animation = fragmentAnimator.makeCloseExit();
            if (animation == null) {
                return AnimationUtils.loadAnimation(context, R.anim.base_no_anim);
            } else {
                return animation;
            }
        }
    }

    public Animation onCreateAnimation(int transit, boolean enter) {
        if (transit == FragmentTransaction.TRANSIT_FRAGMENT_OPEN) {
            if (enter) {
                return initEnterAnim();
            } else {
                return initExitAnim();
            }
        } else if (transit == FragmentTransaction.TRANSIT_FRAGMENT_CLOSE) {
            if (enter) {
                return initPopEnterAnim();
            } else {
                return initPopExitAnim();
            }
        }
        return null;
    }

}