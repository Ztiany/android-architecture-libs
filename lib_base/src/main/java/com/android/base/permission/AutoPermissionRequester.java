package com.android.base.permission;


import android.content.Intent;

import com.android.base.utils.android.ActFragWrapper;

import java.util.Arrays;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import timber.log.Timber;

import static com.android.base.permission.PermissionCode.PERMISSION_REQUESTER_CODE;
import static com.android.base.permission.PermissionCode.REQUEST_PERMISSION_FOR_SETTING;

/**
 * <pre>
 *      1: 使用该类申请权限，当所有的权限都通过时回调权限获取成功，否则回调权限获取失败。
 *      2:不要同时调用requestPermission方法多次！！！以保证一个完整的流程。
 * 获取权限流程,以申请相机权限为例：
 *          1先检查自身是否有相机权限
 *          2如果有我们的app已经有了相机权限，则可以直接使用相机相关功能了
 *          3如果没有权限我们就需要请求权限了，但是还需要处理不再询问的设置
 *          &nbsp;   3.1如果shouldShowRequestPermissionRationale返回false，则说明接下来的对话框不包含”不再询问“选择框，我们可以直接申请权限
 *          &nbsp;   3.2如果shouldShowRequestPermissionRationale返回true，我们最好先弹出一个对话框来说明我们需要权限来做什么，让用户来选择是否继续授予权限，如果用户允许继续授予权限则继续申请权限
 *          4不管权限是否授予给我们的App，都可以在onRequestPermissionsResult的回调中获取结果，我们再问一次
 * </pre>
 *
 * @author Ztiany
 * Email: 1169654504@qq.com
 * Date : 2017-01-11 15:09
 */
public class AutoPermissionRequester {

    private final FragmentActivity mActivity;
    private String[] mPerms;
    private boolean mAskAgain = true;
    private boolean mShowTips = false;

    private OnAllPermissionGrantedListener mOnAllPermissionGrantedListener;
    private OnPermissionDeniedListener mOnPermissionDeniedListener;
    private IPermissionUIProvider mPermissionUIProvider;

    private ActFragWrapper mActFragWrapper;

    private PermissionCallback mPermissionCallback;

    private boolean mIsRequested;

    private AutoPermissionFragment.AutoPermissionFragmentCallback mAutoPermissionFragmentCallback;
    private EasyPermissions.PermissionCaller mPermissionCaller;

    private AutoPermissionRequester(FragmentActivity activity, LifecycleOwner lifecycleOwner) {
        mActivity = activity;
        if (mActivity == null) {
            throw new NullPointerException("activity is null.");
        }

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

    public static AutoPermissionRequester with(@NonNull Fragment fragment) {
        return new AutoPermissionRequester(fragment.getActivity(), fragment);
    }

    public static AutoPermissionRequester with(@NonNull FragmentActivity activity) {
        return new AutoPermissionRequester(activity, activity);
    }

    public AutoPermissionRequester permission(@NonNull String... permissions) {
        if (permissions.length == 0) {
            throw new IllegalArgumentException();
        }
        mPerms = permissions;
        return this;
    }

    public AutoPermissionRequester showTips(boolean showTips) {
        mShowTips = showTips;
        return this;
    }

    public AutoPermissionRequester askAgain(boolean askAgain) {
        mAskAgain = askAgain;
        return this;
    }

    public AutoPermissionRequester customUI(IPermissionUIProvider uiProvider) {
        mPermissionUIProvider = uiProvider;
        return this;
    }

    public AutoPermissionRequester onGranted(OnAllPermissionGrantedListener listener) {
        mOnAllPermissionGrantedListener = listener;
        return this;
    }

    public AutoPermissionRequester onDenied(OnPermissionDeniedListener listener) {
        mOnPermissionDeniedListener = listener;
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

        mActFragWrapper = ActFragWrapper.create(fragment);

        fragment.startRequest();
    }

    private AutoPermissionFragment.AutoPermissionFragmentCallback getCallback() {
        if (mAutoPermissionFragmentCallback == null) {
            mAutoPermissionFragmentCallback = new AutoPermissionFragment.AutoPermissionFragmentCallback() {
                @Override
                public void onReady() {
                    Timber.d("onReady() called");
                    requestPermission(mPerms);
                }

                @Override
                public void onActivityResult(int requestCode, int resultCode, Intent data) {
                    Timber.d("onActivityResult() called with: requestCode = [" + requestCode + "], resultCode = [" + resultCode + "], data = [" + data + "]");
                    AutoPermissionRequester.this.onActivityResult(requestCode, resultCode, data);
                }

                @Override
                public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
                    Timber.d("onRequestPermissionsResult() called with: requestCode = [" + requestCode + "], permissions = [" + Arrays.toString(permissions) + "], grantResults = [" + Arrays.toString(grantResults) + "]");
                    if (requestCode == PERMISSION_REQUESTER_CODE) {
                        AutoPermissionRequester.this.onRequestPermissionsResult(requestCode, permissions, grantResults);
                    }
                }
            };
        }
        return mAutoPermissionFragmentCallback;
    }

    private EasyPermissions.PermissionCaller getPermissionCaller() {
        if (mPermissionCaller == null) {
            mPermissionCaller = new PermissionCallerImpl(mPermissionCallback, mActFragWrapper, mAskAgain, mShowTips, mPermissionUIProvider);
        }
        return mPermissionCaller;
    }

    private void requestPermission(String... perms) {
        mPerms = perms;
        EasyPermissions.requestPermissions(getPermissionCaller(), PermissionCode.PERMISSION_REQUESTER_CODE, mPerms);
    }

    private void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, getPermissionCaller());
    }

    private void onActivityResult(int requestCode, @SuppressWarnings("unused") int resultCode, @SuppressWarnings("unused") Intent data) {
        if (requestCode == REQUEST_PERMISSION_FOR_SETTING) {//申请权限
            if (!EasyPermissions.hasPermissions(mActFragWrapper.getContext(), mPerms)) {//Setting界面回来之后，没有授予权限
                String[] filter = EasyPermissions.filter(mActFragWrapper.getContext(), mPerms);
                mPermissionCallback.onPermissionDenied(Arrays.asList(filter));//权限被拒绝

                if (mShowTips) {
                    getPermissionCaller().getPermissionUIProvider().showPermissionDeniedTip(mActFragWrapper.getContext(), filter);
                }

            } else {
                mPermissionCallback.onAllPermissionGranted();//所有权限被获取
            }
        }
    }

}
