package com.android.base.foundation.common;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

/**
 * 包含了 Activity 和 Fragment 的相关操作
 */
public class ActFragWrapper {

    private Fragment mFragment;
    private Activity mActivity;

    private ActFragWrapper() {
    }

    public static ActFragWrapper create(Activity activity) {
        ActFragWrapper context = new ActFragWrapper();
        context.mActivity = activity;
        return context;
    }

    public static ActFragWrapper create(Fragment fragment) {
        ActFragWrapper context = new ActFragWrapper();
        context.mFragment = fragment;
        return context;
    }

    public Context getContext() {
        if (mActivity != null) {
            return mActivity;
        } else {
            return mFragment.getActivity();
        }
    }

    public Fragment getFragment() {
        return mFragment;
    }

    public void startActivityForResult(Intent intent, int requestCode, @Nullable Bundle options) {
        if (mActivity != null) {
            mActivity.startActivityForResult(intent, requestCode, options);
        } else {
            mFragment.startActivityForResult(intent, requestCode, options);
        }
    }

    public void startService(Intent intent) {
        if (mActivity != null) {
            mActivity.startService(intent);
        } else {
            FragmentActivity activity = mFragment.getActivity();
            if (activity != null) {
                activity.startService(intent);
            }
        }
    }

    public void stopService(Class<? extends Service> payPalServiceClass) {
        if (mActivity != null) {
            mActivity.stopService(new Intent(mActivity, payPalServiceClass));
        } else {
            FragmentActivity activity = mFragment.getActivity();
            if (activity != null) {
                activity.stopService(new Intent(activity, payPalServiceClass));
            }
        }
    }

}