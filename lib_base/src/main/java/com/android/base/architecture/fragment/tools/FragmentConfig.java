package com.android.base.architecture.fragment.tools;

import com.android.base.architecture.fragment.animator.FragmentAnimator;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2019-03-05 15:25
 */
public class FragmentConfig {

    private static final int INVALIDATE_ID = -1;
    private static int sDefaultContainerId = INVALIDATE_ID;
    private static FragmentAnimator sFragmentAnimator = null;

    public static void setDefaultContainerId(int defaultContainerId) {
        sDefaultContainerId = defaultContainerId;
    }

    public static int defaultContainerId() {
        if (sDefaultContainerId == INVALIDATE_ID) {
            throw new IllegalStateException("sDefaultContainerId has not set");
        }
        return sDefaultContainerId;
    }

    public static void setDefaultFragmentAnimator(FragmentAnimator animator) {
        sFragmentAnimator = animator;
    }

    public static FragmentAnimator defaultFragmentAnimator() {
        return sFragmentAnimator;
    }

}