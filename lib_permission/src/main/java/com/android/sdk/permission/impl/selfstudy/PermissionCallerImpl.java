package com.android.sdk.permission.impl.selfstudy;

import android.content.Intent;

import com.android.sdk.permission.utils.HostWrapper;

import java.util.List;

import timber.log.Timber;

import static com.android.sdk.permission.impl.selfstudy.PermissionCode.REQUEST_PERMISSION_FOR_SETTING;


class PermissionCallerImpl implements EasyPermissions.PermissionCaller {

    private final boolean mShouldAskAgain;
    private final boolean mShowTips;

    private HostWrapper mContextWrapper;
    private PermissionCallback mPermissionCallback;
    private IPermissionUIProvider mIPermissionUIProvider;

    PermissionCallerImpl(PermissionCallback permissionCallback, HostWrapper contextWrapper, boolean shouldAskAgain, boolean showTips, IPermissionUIProvider iPermissionUIProvider) {
        mPermissionCallback = permissionCallback;
        mContextWrapper = contextWrapper;
        mShouldAskAgain = shouldAskAgain;
        mShowTips = showTips;
        mIPermissionUIProvider = iPermissionUIProvider;
    }

    @Override
    public Object getRequester() {
        Timber.d("getRequester() called");
        if (mContextWrapper.getFragment() != null) {
            return mContextWrapper.getFragment();
        }
        return mContextWrapper.getContext();
    }

    @Override
    public IPermissionUIProvider getPermissionUIProvider() {
        Timber.d("getPermissionUIProvider() called");
        if (mIPermissionUIProvider == null) {
            return PermissionUIProviderFactory.getPermissionUIProvider();
        }
        return mIPermissionUIProvider;
    }

    /**
     * 只获取到部分权限则不管，会在onPermissionsDenied和onPermissionAllGranted中处理
     */
    @Override
    public void onPortionPermissionsGranted(boolean allGranted, int requestCode, List<String> perms) {
        Timber.d("onPortionPermissionsGranted() called with: allGranted = [" + allGranted + "], requestCode = [" + requestCode + "], perms = [" + perms + "]");
        // do nothing
        if (allGranted) {
            mPermissionCallback.onAllPermissionGranted();
        }
    }

    @Override
    public void onPermissionsDenied(final int requestCode, final List<String> perms) {
        Timber.d("onPermissionsDenied() called with: requestCode = [" + requestCode + "], perms = [" + perms + "]");
        if (!mShouldAskAgain) {
            notifyPermissionDenied(perms);
            return;
        }
        //ask again
        boolean again = EasyPermissions.checkDeniedPermissionsNeverAskAgain(this, perms);
        if (again) {
            getPermissionUIProvider()
                    .showAskAgainDialog(mContextWrapper.getContext(), perms.toArray(new String[0]),
                            (dialog, which) -> {
                                try {
                                    Intent intentForPermission = EasyPermissions.getIntentForPermission(mContextWrapper.getContext());
                                    mContextWrapper.startActivityForResult(intentForPermission, REQUEST_PERMISSION_FOR_SETTING, null);
                                } catch (Exception e) {
                                    notifyPermissionDenied(perms);
                                }
                            },
                            (dialog, which) -> notifyPermissionDenied(perms));
        } else {
            notifyPermissionDenied(perms);
        }
    }

    private void notifyPermissionDenied(List<String> perms) {
        Timber.d("notifyPermissionDenied() called with: perms = [" + perms + "]");
        mPermissionCallback.onPermissionDenied(perms);
        if (mShowTips) {
            getPermissionUIProvider().showPermissionDeniedTip(mContextWrapper.getContext(), perms.toArray(new String[0]));
        }
    }

}
