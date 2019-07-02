package com.android.base.permission;

import android.arch.lifecycle.DefaultLifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.android.base.utils.android.ActFragWrapper;

import timber.log.Timber;

import static com.android.base.permission.PermissionCode.PERMISSION_REQUESTER_CODE;

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

    private OnAllPermissionGrantedListener mOnAllPermissionGrantedListener;
    private OnPermissionDeniedListener mOnPermissionDeniedListener;

    private PermissionCallback mPermissionCallback;

    private IPermissionUIProvider mPermissionUIProvider;
    private PermissionRequester mPermissionRequester;

    private AutoPermissionFragment.AutoPermissionFragmentCallback mAutoPermissionFragmentCallback;

    private AutoPermissionRequester(FragmentActivity activity, LifecycleOwner lifecycleOwner) {
        mActivity = activity;
        if (mActivity == null) {
            throw new NullPointerException();
        }

        DefaultLifecycleObserver observer = new DefaultLifecycleObserver() {
            @Override
            public void onDestroy(@NonNull LifecycleOwner owner) {
                mOnAllPermissionGrantedListener = null;
                mOnPermissionDeniedListener = null;
            }
        };
        lifecycleOwner.getLifecycle().addObserver(observer);
    }

    public static AutoPermissionRequester with(Fragment fragment) {
        return new AutoPermissionRequester(fragment.getActivity(), fragment);
    }

    public static AutoPermissionRequester with(FragmentActivity activity) {
        return new AutoPermissionRequester(activity, activity);
    }

    public AutoPermissionRequester permission(String... permissions) {
        if (permissions == null || permissions.length == 0) {
            throw new IllegalArgumentException();
        }
        mPerms = permissions;
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
        mPermissionCallback = new PermissionCallback(mOnPermissionDeniedListener, mOnAllPermissionGrantedListener);
        startRequest();
    }

    private void startRequest() {
        FragmentManager supportFragmentManager = mActivity.getSupportFragmentManager();
        AutoPermissionFragment fragment = (AutoPermissionFragment) supportFragmentManager.findFragmentByTag(AutoPermissionFragment.class.getName());

        if (fragment == null) {
            fragment = AutoPermissionFragment.newInstance();

            mPermissionRequester = new PermissionRequester(ActFragWrapper.create(fragment), mPermissionCallback, mAskAgain, mPermissionUIProvider);
            fragment.setRequester(getCallback());

            supportFragmentManager.beginTransaction()
                    .add(fragment, AutoPermissionFragment.class.getName())
                    .commitNowAllowingStateLoss();

        } else {
            fragment.setRequester(getCallback());
            mPermissionRequester = new PermissionRequester(ActFragWrapper.create(fragment), mPermissionCallback, mAskAgain, mPermissionUIProvider);
        }

        fragment.startRequest();
    }

    private AutoPermissionFragment.AutoPermissionFragmentCallback getCallback() {
        if (mAutoPermissionFragmentCallback == null) {
            mAutoPermissionFragmentCallback = new AutoPermissionFragment.AutoPermissionFragmentCallback() {
                @Override
                public void onReady() {
                    Timber.d("onReady() called");
                    if (mPermissionRequester != null) {
                        mPermissionRequester.requestPermission(PERMISSION_REQUESTER_CODE, mPerms);
                    }
                }

                @Override
                public void onActivityResult(int requestCode, int resultCode, Intent data) {
                    Timber.d("onActivityResult() called with: requestCode = [" + requestCode + "], resultCode = [" + resultCode + "], data = [" + data + "]");
                    if (mPermissionRequester != null) {
                        mPermissionRequester.onActivityResult(requestCode, resultCode, data);
                    }
                }

                @Override
                public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
                    Timber.d("onRequestPermissionsResult() called with: requestCode = [" + requestCode + "], permissions = [" + permissions + "], grantResults = [" + grantResults + "]");
                    if (requestCode == PERMISSION_REQUESTER_CODE && mPermissionRequester != null) {
                        mPermissionRequester.onRequestPermissionsResult(requestCode, permissions, grantResults);
                    }
                }
            };
        }
        return mAutoPermissionFragmentCallback;
    }

}
