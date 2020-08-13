package com.android.sdk.biometrics;

import android.app.Activity;

import androidx.annotation.Nullable;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2020-03-10 18:25
 */
class EmptyAuthenticationProcessor implements IAuthenticationProcessor {

    private Activity mActivity;

    EmptyAuthenticationProcessor(Activity activity) {
        mActivity = activity;
    }

    @Override
    public void authenticate(@Nullable DialogStyleConfiguration configuration, AuthenticationCallback callback) {
        callback.onAuthenticationError(ERROR_UNSUPPORTED, mActivity.getString(R.string.biometrics_unsupported_tips));
    }

    @Override
    public void cancelAuthentication() {

    }

    @Override
    public boolean isHardwareDetected() {
        return false;
    }

    @Override
    public boolean hasEnrolledBiometrics() {
        return false;
    }

}
