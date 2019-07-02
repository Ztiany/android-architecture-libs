package com.android.base.permission;   // Callback

import java.util.List;

class PermissionCallback {

    private final OnPermissionDeniedListener mOnPermissionDeniedListener;
    private final OnAllPermissionGrantedListener mOnAllPermissionGrantedListener;

    PermissionCallback(OnPermissionDeniedListener onPermissionDeniedListener, OnAllPermissionGrantedListener onAllPermissionGrantedListener) {
        mOnPermissionDeniedListener = onPermissionDeniedListener;
        mOnAllPermissionGrantedListener = onAllPermissionGrantedListener;
    }

    void onPermissionDenied(List<String> strings) {
        if (mOnPermissionDeniedListener != null) {
            mOnPermissionDeniedListener.onPermissionDenied(strings);
        }
    }

    void onAllPermissionGranted() {
        if (mOnAllPermissionGrantedListener != null) {
            mOnAllPermissionGrantedListener.onAllPermissionGranted();
        }
    }

}