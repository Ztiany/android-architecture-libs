package com.android.base.permission;

import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.github.dmstocking.optional.java.util.function.Consumer;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.PermissionRequest;

import java.util.List;


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
public class AndPermissionRequester {

    private final FragmentActivity mActivity;
    private boolean mAskAgain = true;
    private boolean mShowReason = true;
    private boolean mShowTips = false;
    private IPermissionUIProvider mPermissionUIProvider;
    private Consumer<List<String>> mOnGranted;
    private Consumer<List<String>> mOnDenied;
    private static final int REQUEST_PERMISSION_FOR_SETTING = 999;
    private List<String> mDeniedPermission;
    private String[] mPerms;

    private AndPermissionRequester(FragmentActivity activity) {
        if (activity == null) {
            throw new NullPointerException("activity is null");
        }
        mActivity = activity;
    }

    public static AndPermissionRequester with(Fragment fragment) {
        return new AndPermissionRequester(fragment.getActivity());
    }

    public static AndPermissionRequester with(FragmentActivity activity) {
        return new AndPermissionRequester(activity);
    }

    public AndPermissionRequester permission(String... permissions) {
        if (permissions == null || permissions.length == 0) {
            throw new IllegalArgumentException();
        }
        mPerms = permissions;
        return this;
    }

    public AndPermissionRequester askAgain(boolean askAgain) {
        mAskAgain = askAgain;
        return this;
    }

    public AndPermissionRequester showReason(boolean showReason) {
        mShowReason = showReason;
        return this;
    }

    public AndPermissionRequester showDeniedTips(boolean showTips) {
        mShowTips = showTips;
        return this;
    }

    public AndPermissionRequester customUI(@NonNull IPermissionUIProvider uiProvider) {
        mPermissionUIProvider = uiProvider;
        return this;
    }

    public AndPermissionRequester onGranted(@NonNull Consumer<List<String>> onGranted) {
        this.mOnGranted = onGranted;
        return this;
    }

    public AndPermissionRequester onDenied(@NonNull Consumer<List<String>> onDenied) {
        this.mOnDenied = onDenied;
        return this;
    }

    public void request() {
        if (mPerms != null) {
            doPermissionRequest();
        } else {
            throw new IllegalStateException("no permission set");
        }
    }

    private void doPermissionRequest() {
        PermissionRequest permissionRequest = AndPermission.with(mActivity).runtime().permission(mPerms);

        if (mShowReason) {
            permissionRequest = permissionRequest.rationale((context, data, executor) ->
                    getPermissionUIProvider().showPermissionRationaleDialog(
                            mActivity,
                            data.toArray(new String[0]),
                            (dialog, which) -> executor.execute(),
                            (dialog, which) -> executor.cancel()));
        }

        permissionRequest
                .onGranted(data -> {
                    if (mOnGranted != null) {
                        mOnGranted.accept(data);
                    }
                })
                .onDenied(permissions -> {
                    if (mAskAgain) {
                        doAskAgain(permissions);
                    } else {
                        if (mOnDenied != null) {
                            mOnDenied.accept(permissions);
                        }
                    }
                })
                .start();
    }

    /**
     * 询问是否去设置界面
     */
    private void doAskAgain(List<String> permissions) {
        getPermissionUIProvider().showAskAgainDialog(mActivity, permissions.toArray(new String[0]),
                (dialog, which) -> openSettings(permissions),/*去设置界面*/
                (dialog, which) -> {
                    if (mOnDenied != null) {
                        mOnDenied.accept(permissions);/*通知权限被拒绝*/
                    }
                });
    }

    private void openSettings(List<String> permissions) {
        FragmentManager supportFragmentManager = mActivity.getSupportFragmentManager();
        AndPermissionFragment fragment = (AndPermissionFragment) supportFragmentManager.findFragmentByTag(AndPermissionFragment.class.getName());
        if (fragment == null) {
            fragment = AndPermissionFragment.newInstance();
            fragment.setRequester(this);
            supportFragmentManager
                    .beginTransaction()
                    .add(fragment, AndPermissionFragment.class.getName())
                    .commitNowAllowingStateLoss();
        } else {
            fragment.setRequester(this);
        }

        mDeniedPermission = permissions;
        fragment.startAsk();
    }

    void onAutoPermissionFragmentReady(AndPermissionFragment autoPermissionFragment) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", mActivity.getPackageName(), null);
        intent.setData(uri);
        try {
            autoPermissionFragment.startActivityForResult(intent, REQUEST_PERMISSION_FOR_SETTING, null);
        } catch (Exception ignore) {
        }
    }

    @SuppressWarnings("unused")
    void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_PERMISSION_FOR_SETTING) {//申请权限
            if (!AndUtils.hasPermission(mActivity, mPerms)) {//Setting界面回来之后，没有授予权限
                if (mOnDenied != null) {
                    mOnDenied.accept(mDeniedPermission);
                }
                if (mShowTips) {
                    getPermissionUIProvider().showPermissionDeniedTip(mActivity, mDeniedPermission.toArray(new String[0]));
                }
            } else {
                if (mOnGranted != null) {
                    mOnGranted.accept(mDeniedPermission);//所有权限被获取
                }
            }
        }
    }

    private IPermissionUIProvider getPermissionUIProvider() {
        if (mPermissionUIProvider == null) {
            mPermissionUIProvider = PermissionUIProviderFactory.getPermissionUIProvider();
        }
        return mPermissionUIProvider;
    }

}
