package com.android.sdk.permission.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import timber.log.Timber;

public class HostWrapper {

    private Fragment mFragment;
    private Context mContext;

    private HostWrapper() {
    }

    public static HostWrapper create(Context context) {
        HostWrapper hostWrapper = new HostWrapper();
        hostWrapper.mContext = context;
        return hostWrapper;
    }

    public static HostWrapper create(Fragment fragment) {
        HostWrapper context = new HostWrapper();
        context.mFragment = fragment;
        return context;
    }

    public Context getContext() {
        if (mContext != null) {
            return mContext;
        } else {
            return mFragment.getActivity();
        }
    }

    public Fragment getFragment() {
        return mFragment;
    }

    public void startActivityForResult(Intent intent, int requestCode, @Nullable Bundle options) {
        if (mContext != null) {
            if (mContext instanceof Activity) {
                ((Activity) mContext).startActivityForResult(intent, requestCode, options);
            } else {
                Timber.d("context is not an Activity");
            }
        } else {
            mFragment.startActivityForResult(intent, requestCode, options);
        }
    }

}