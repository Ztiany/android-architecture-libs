package com.android.base.permission;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2018-04-16 17:48
 */
public class AndPermissionFragment extends Fragment {

    static AndPermissionFragment newInstance() {
        return new AndPermissionFragment();
    }

    private final Handler mHandler = new Handler();
    private final Runnable mRunnable = this::startChecked;

    private AndPermissionRequester mRequester;
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mRequester != null) {
            mRequester.onActivityResult(requestCode, resultCode, data);
        }
    }

    void setRequester(AndPermissionRequester requester) {
        mRequester = requester;
    }

    void startAsk() {
        startChecked();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(mRunnable);
    }

    private void startChecked() {
        if (mIsActivityReady) {
            if (mRequester != null) {
                mRequester.onAutoPermissionFragmentReady(this);
            }
            mHandler.removeCallbacks(mRunnable);
        } else {
            mHandler.post(mRunnable);
        }
    }

}