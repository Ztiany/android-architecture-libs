package com.android.base.permission;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2018-04-16 17:48
 */
public class AutoPermissionFragment extends Fragment {

    static AutoPermissionFragment newInstance() {
        return new AutoPermissionFragment();
    }

    private final Handler mHandler = new Handler();
    private final Runnable mRunnable = this::startChecked;

    private AutoPermissionFragmentCallback mRequester;
    private boolean mIsActivityReady = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mIsActivityReady = true;
    }

    @Override
    public void onResume() {
        super.onResume();
        mIsActivityReady = true;
    }

    @Override
    public void onPause() {
        super.onPause();
        mHandler.removeCallbacks(mRunnable);
        mIsActivityReady = false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (mRequester != null) {
            mRequester.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
        mRequester = null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mRequester != null) {
            mRequester.onActivityResult(requestCode, resultCode, data);
        }
    }

    void startRequest() {
        startChecked();
    }

    private void startChecked() {
        if (mIsActivityReady) {
            if (mRequester != null) {
                mRequester.onReady();
            }
            mHandler.removeCallbacks(mRunnable);
        } else {
            mHandler.post(mRunnable);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(mRunnable);
    }

    void setRequester(AutoPermissionFragmentCallback requester) {
        mRequester = requester;
    }

    interface AutoPermissionFragmentCallback{
        void onReady();

        void onActivityResult(int requestCode, int resultCode, Intent data);

        void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults);
    }

}
