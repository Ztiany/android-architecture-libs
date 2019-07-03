package com.android.base.permission;

import java.util.List;

class PermissionCallback {

    private boolean mIsDestroyed = false;

    private final OnPermissionDeniedListener mOnPermissionDeniedListener;
    private final OnAllPermissionGrantedListener mOnAllPermissionGrantedListener;

    PermissionCallback(OnPermissionDeniedListener onPermissionDeniedListener, OnAllPermissionGrantedListener onAllPermissionGrantedListener) {
        mOnPermissionDeniedListener = onPermissionDeniedListener;
        mOnAllPermissionGrantedListener = onAllPermissionGrantedListener;
    }

    void onPermissionDenied(List<String> strings) {
        if (mOnPermissionDeniedListener != null && !mIsDestroyed) {
            mOnPermissionDeniedListener.onPermissionDenied(strings);
        }
    }

    void onAllPermissionGranted() {
        if (mOnAllPermissionGrantedListener != null && !mIsDestroyed) {
            mOnAllPermissionGrantedListener.onAllPermissionGranted();
        }
    }

    void setDestroyed() {
        mIsDestroyed = true;
    }

}