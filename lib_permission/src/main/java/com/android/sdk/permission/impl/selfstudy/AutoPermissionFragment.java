package com.android.sdk.permission.impl.selfstudy;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import timber.log.Timber;

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

    private AutoPermissionFragmentCallback mCallback;
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
        mIsActivityReady = false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Timber.d("onRequestPermissionsResult() called " + mCallback);

        AutoPermissionFragmentCallback callback = getCallback();
        if (callback != null) {
            callback.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Timber.d("onActivityResult() called " + mCallback);

        super.onActivityResult(requestCode, resultCode, data);
        AutoPermissionFragmentCallback callback = getCallback();
        if (callback != null) {
            callback.onActivityResult(requestCode, resultCode, data);
        }
    }

    void startRequest() {
        Timber.d("startRequest() called ");
        mHandler.removeCallbacks(mRunnable);
        startChecked();
    }

    private void startChecked() {
        Timber.d("startChecked() called " + mCallback);
        if (mIsActivityReady) {
            AutoPermissionFragmentCallback callback = getCallback();
            if (callback != null) {
                callback.onReady();
            }
            mHandler.removeCallbacks(mRunnable);
        } else {
            mHandler.postDelayed(mRunnable, 300);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(mRunnable);
        mCallback = null;
    }

    void setRequester(AutoPermissionFragmentCallback requester) {
        Timber.d("setRequester() called with: requester = [" + requester + "]");
        mCallback = requester;
    }

    private AutoPermissionFragmentCallback getCallback() {
        return mCallback;
    }

    interface AutoPermissionFragmentCallback {
        void onReady();

        void onActivityResult(int requestCode, int resultCode, Intent data);

        void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults);
    }

}
