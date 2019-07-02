package com.android.base.permission;

import android.content.Intent;
import android.support.annotation.NonNull;

import com.android.base.utils.android.ActFragWrapper;

import java.util.Arrays;

import static com.android.base.permission.PermissionCode.REQUEST_PERMISSION_FOR_SETTING;

/**
 * <pre>
 *      1:使用{@link #requestPermission(int, String...)}来申请权限，当所有的权限都通过时回调权限获取成功，否则回调权限获取失败。
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
class PermissionRequester {

    private final ActFragWrapper mActFragWrapper;
    private String[] mPerms;
    private PermissionCallback mPermissionCallback;
    private EasyPermissions.PermissionCaller mPermissionCaller;
    private final boolean mAskAgain;
    private final IPermissionUIProvider mPermissionUIProvider;

    PermissionRequester(ActFragWrapper actFragWrapper, PermissionCallback permissionCallback, boolean askAgain, IPermissionUIProvider permissionUIProvider) {
        mPermissionCallback = permissionCallback;
        mActFragWrapper = actFragWrapper;
        mAskAgain = askAgain;
        mPermissionUIProvider = permissionUIProvider;
    }

    private EasyPermissions.PermissionCaller getPermissionCaller() {
        if (mPermissionCaller == null) {
            mPermissionCaller = new PermissionRequesterImpl(mPermissionCallback, mActFragWrapper, mAskAgain, mPermissionUIProvider);
        }
        return mPermissionCaller;
    }

    void requestPermission(int requestCode, String... perms) {
        mPerms = perms;
        EasyPermissions.requestPermissions(getPermissionCaller(), requestCode, mPerms);
    }

    void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, getPermissionCaller());
    }

    void onActivityResult(int requestCode, @SuppressWarnings("unused") int resultCode, @SuppressWarnings("unused") Intent data) {
        if (requestCode == REQUEST_PERMISSION_FOR_SETTING) {//申请权限
            if (!EasyPermissions.hasPermissions(mActFragWrapper.getContext(), mPerms)) {//Setting界面回来之后，没有授予权限
                String[] filter = EasyPermissions.filter(mActFragWrapper.getContext(), mPerms);
                mPermissionCallback.onPermissionDenied(Arrays.asList(filter));//权限被拒绝
                mPermissionCaller.getPermissionUIProvider().showPermissionDeniedTip(mActFragWrapper.getContext(), filter);
            } else {
                mPermissionCallback.onAllPermissionGranted();//所有权限被获取
            }
        }
    }

}
