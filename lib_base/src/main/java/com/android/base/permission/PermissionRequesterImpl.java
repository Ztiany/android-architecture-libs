package com.android.base.permission;

import android.content.Intent;

import com.android.base.utils.android.ActFragWrapper;

import java.util.List;

import static com.android.base.permission.PermissionCode.REQUEST_PERMISSION_FOR_SETTING;


class PermissionRequesterImpl implements EasyPermissions.PermissionCaller {

    private final boolean mShouldAskAgain;
    private ActFragWrapper mContextWrapper;
    private PermissionCallback mPermissionCallback;
    private IPermissionUIProvider mIPermissionUIProvider;

    PermissionRequesterImpl(PermissionCallback permissionCallback, ActFragWrapper contextWrapper, boolean shouldAskAgain, IPermissionUIProvider iPermissionUIProvider) {
        mPermissionCallback = permissionCallback;
        mContextWrapper = contextWrapper;
        mShouldAskAgain = shouldAskAgain;
        mIPermissionUIProvider = iPermissionUIProvider;
    }

    @Override
    public Object getRequester() {
        if (mContextWrapper.getFragment() != null) {
            return mContextWrapper.getFragment();
        }
        return mContextWrapper.getContext();
    }

    @Override
    public IPermissionUIProvider getPermissionUIProvider() {
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
        // do nothing
        if (allGranted) {
            mPermissionCallback.onAllPermissionGranted();
        }
    }

    @Override
    public void onPermissionsDenied(final int requestCode, final List<String> perms) {
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
                                Intent intentForPermission = EasyPermissions.getIntentForPermission(mContextWrapper.getContext());
                                mContextWrapper.startActivityForResult(intentForPermission, REQUEST_PERMISSION_FOR_SETTING, null);
                            },
                            (dialog, which) -> notifyPermissionDenied(perms));
        } else {
            notifyPermissionDenied(perms);
        }
    }

    private void notifyPermissionDenied(List<String> perms) {
        mPermissionCallback.onPermissionDenied(perms);
        getPermissionUIProvider().showPermissionDeniedTip(mContextWrapper.getContext(), perms.toArray(new String[0]));
    }


}
