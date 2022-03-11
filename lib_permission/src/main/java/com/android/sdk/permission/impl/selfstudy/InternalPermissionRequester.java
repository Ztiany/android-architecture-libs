package com.android.sdk.permission.impl.selfstudy;


import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;

import com.android.sdk.permission.api.PermissionRequest;
import com.android.sdk.permission.utils.HostWrapper;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import timber.log.Timber;

/**
 * @author Ztiany
 * Date : 2017-01-11 15:09
 */
public class InternalPermissionRequester implements PermissionRequest {

    private final FragmentActivity mActivity;
    private String[] mPermission;
    private boolean mAskAgain = true;
    private boolean mShowTips = false;

    private OnAllPermissionGrantedListener mOnAllPermissionGrantedListener;
    private OnPermissionDeniedListener mOnPermissionDeniedListener;
    private IPermissionUIProvider mPermissionUIProvider;

    private HostWrapper mHostWrapper;

    private PermissionCallback mPermissionCallback;

    private boolean mIsRequested;

    private AutoPermissionFragment.AutoPermissionFragmentCallback mAutoPermissionFragmentCallback;
    private EasyPermissions.PermissionCaller mPermissionCaller;

    public InternalPermissionRequester(FragmentActivity activity, LifecycleOwner lifecycleOwner, String[] permission) {
        mActivity = activity;

        if (mActivity == null) {
            throw new NullPointerException("activity is null.");
        }

        mPermission = permission;

        DefaultLifecycleObserver observer = new DefaultLifecycleObserver() {
            @Override
            public void onDestroy(@NonNull LifecycleOwner owner) {
                mOnAllPermissionGrantedListener = null;
                mOnPermissionDeniedListener = null;
                mPermissionUIProvider = null;
                if (mPermissionCallback != null) {
                    mPermissionCallback.setDestroyed();
                }
            }
        };

        lifecycleOwner.getLifecycle().addObserver(observer);
    }

    public InternalPermissionRequester permission(@NonNull String... permissions) {
        if (permissions.length == 0) {
            throw new IllegalArgumentException();
        }
        mPermission = permissions;
        return this;
    }

    @NotNull
    @Override
    public InternalPermissionRequester showTips(boolean showTips) {
        mShowTips = showTips;
        return this;
    }

    @NotNull
    @Override
    public InternalPermissionRequester askAgain(boolean askAgain) {
        mAskAgain = askAgain;
        return this;
    }

    @NotNull
    @Override
    public InternalPermissionRequester customUI(@NonNull IPermissionUIProvider uiProvider) {
        mPermissionUIProvider = uiProvider;
        return this;
    }

    public void request() {
        if (mIsRequested) {
            throw new UnsupportedOperationException("AutoPermissionRequester instance just can be used by once.");
        }
        mIsRequested = true;
        mPermissionCallback = new PermissionCallback(mOnPermissionDeniedListener, mOnAllPermissionGrantedListener);
        startRequest();
    }

    private void startRequest() {
        FragmentManager supportFragmentManager = mActivity.getSupportFragmentManager();
        AutoPermissionFragment fragment = (AutoPermissionFragment) supportFragmentManager.findFragmentByTag(AutoPermissionFragment.class.getName());

        Lifecycle.State currentState = mActivity.getLifecycle().getCurrentState();

        if (currentState.isAtLeast(Lifecycle.State.RESUMED)) {
            startRequestInternal(supportFragmentManager, fragment);
        } else {
            DefaultLifecycleObserver lifecycleObserver = new DefaultLifecycleObserver() {
                @Override
                public void onResume(@NonNull LifecycleOwner owner) {
                    mActivity.getLifecycle().removeObserver(this);
                    Timber.d("startRequest 2");
                    startRequestInternal(supportFragmentManager, fragment);
                }
            };
            mActivity.getLifecycle().addObserver(lifecycleObserver);
        }
    }

    private void startRequestInternal(FragmentManager supportFragmentManager, AutoPermissionFragment fragment) {
        if (fragment == null) {
            fragment = AutoPermissionFragment.newInstance();
            supportFragmentManager.beginTransaction()
                    .add(fragment, AutoPermissionFragment.class.getName())
                    .commitAllowingStateLoss();
        }

        fragment.setRequester(getCallback());

        mHostWrapper = HostWrapper.create(fragment);

        fragment.startRequest();
    }

    private AutoPermissionFragment.AutoPermissionFragmentCallback getCallback() {
        if (mAutoPermissionFragmentCallback == null) {
            mAutoPermissionFragmentCallback = new AutoPermissionFragment.AutoPermissionFragmentCallback() {
                @Override
                public void onReady() {
                    Timber.d("onReady() called");
                    requestPermission(mPermission);
                }

                @Override
                public void onActivityResult(int requestCode, int resultCode, Intent data) {
                    Timber.d("onActivityResult() called with: requestCode = [" + requestCode + "], resultCode = [" + resultCode + "], data = [" + data + "]");
                    InternalPermissionRequester.this.onActivityResult(requestCode, resultCode, data);
                }

                @Override
                public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
                    Timber.d("onRequestPermissionsResult() called with: requestCode = [" + requestCode + "], permissions = [" + Arrays.toString(permissions) + "], grantResults = [" + Arrays.toString(grantResults) + "]");
                    if (requestCode == PermissionCode.PERMISSION_REQUESTER_CODE) {
                        InternalPermissionRequester.this.onRequestPermissionsResult(requestCode, permissions, grantResults);
                    }
                }
            };
        }
        return mAutoPermissionFragmentCallback;
    }

    private EasyPermissions.PermissionCaller getPermissionCaller() {
        if (mPermissionCaller == null) {
            mPermissionCaller = new PermissionCallerImpl(mPermissionCallback, mHostWrapper, mAskAgain, mShowTips, mPermissionUIProvider);
        }
        return mPermissionCaller;
    }

    private void requestPermission(String... perms) {
        mPermission = perms;
        EasyPermissions.requestPermissions(getPermissionCaller(), PermissionCode.PERMISSION_REQUESTER_CODE, mPermission);
    }

    private void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, getPermissionCaller());
    }

    private void onActivityResult(int requestCode, @SuppressWarnings("unused") int resultCode, @SuppressWarnings("unused") Intent data) {
        if (requestCode == PermissionCode.REQUEST_PERMISSION_FOR_SETTING) {//申请权限
            if (!EasyPermissions.hasPermissions(mHostWrapper.getContext(), mPermission)) {//Setting界面回来之后，没有授予权限
                String[] filter = EasyPermissions.filter(mHostWrapper.getContext(), mPermission);
                mPermissionCallback.onPermissionDenied(Arrays.asList(filter));//权限被拒绝

                if (mShowTips) {
                    getPermissionCaller().getPermissionUIProvider().showPermissionDeniedTip(mHostWrapper.getContext(), filter);
                }

            } else {
                mPermissionCallback.onAllPermissionGranted();//所有权限被获取
            }
        }
    }

    @NotNull
    @Override
    public PermissionRequest onGranted(@NotNull Function1<? super List<String>, Unit> granted) {
        mOnAllPermissionGrantedListener = () -> granted.invoke(Collections.emptyList());
        return this;
    }

    @NotNull
    @Override
    public PermissionRequest onDenied(@NotNull Function1<? super List<String>, Unit> denied) {
        mOnPermissionDeniedListener = denied::invoke;
        return this;
    }

    @Override
    public void start() {
        request();
    }

}