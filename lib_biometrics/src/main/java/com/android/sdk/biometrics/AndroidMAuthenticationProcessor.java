package com.android.sdk.biometrics;

import android.app.Activity;
import android.os.Build;
import android.text.TextUtils;

import javax.crypto.Cipher;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.hardware.fingerprint.FingerprintManagerCompat;
import androidx.core.os.CancellationSignal;

import static com.android.sdk.biometrics.Utils.SYSTEM_CODE_CANCEL_AUTHENTICATION;


/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2020-03-10 15:00
 */
class AndroidMAuthenticationProcessor implements IAuthenticationProcessor {

    private Activity mActivity;

    private DialogInteractor mDialogInteractor;

    private AuthenticationCallback mAuthenticationCallback;

    private CancellationSignal mCancellationSignal;

    private FingerprintManagerCompat mFingerprintManagerCompat;

    private CipherHelper mCipherHelper;

    private Cipher mCipher;

    private final BiometricsAuthenticationManager mManager;

    AndroidMAuthenticationProcessor(BiometricsAuthenticationManager manager) {
        mActivity = manager.getActivity();
        mFingerprintManagerCompat = FingerprintManagerCompat.from(mActivity);
        mCipherHelper = new CipherHelper();
        mManager = manager;
    }

    private FingerprintManagerCompat.AuthenticationCallback authenticationCallback = new FingerprintManagerCompat.AuthenticationCallback() {
        @Override
        public void onAuthenticationError(int errMsgId, CharSequence errString) {
            super.onAuthenticationError(errMsgId, errString);
            LogUtils.log("onAuthenticationError() called with: errMsgId = [" + errMsgId + "], errString = [" + errString + "]");
            //取消对话框
            mDialogInteractor.dismiss();
            //通知错误
            if (SYSTEM_CODE_CANCEL_AUTHENTICATION == errMsgId) {
                mAuthenticationCallback.onAdditionalAction(ACTION_CANCEL);
            } else {
                mAuthenticationCallback.onAuthenticationError(errMsgId, Utils.makeErrorMessage(mActivity, errMsgId, errString));
            }
        }

        @Override
        public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
            super.onAuthenticationHelp(helpMsgId, helpString);
            LogUtils.log("onAuthenticationHelp() called with: helpMsgId = [" + helpMsgId + "], helpString = [" + helpString + "]");
            //只需要设置提示
            if (!TextUtils.isEmpty(helpString)) {
                mDialogInteractor.setTip(helpString);
            }
        }

        @Override
        public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result) {
            super.onAuthenticationSucceeded(result);
            LogUtils.log("onAuthenticationSucceeded() called with: result = [" + result + "]");
            mDialogInteractor.setTip("");
            mDialogInteractor.dismiss();
            if (mManager.getConfiguration().validatingResult) {
                mCipherHelper.validateResult(mActivity, mAuthenticationCallback, result.getCryptoObject().getCipher());
            } else {
                mAuthenticationCallback.onAuthenticationSucceeded();
            }
        }

        @Override
        public void onAuthenticationFailed() {
            super.onAuthenticationFailed();
            LogUtils.log("onAuthenticationFailed() called");
            //通知此次验证失败了
            mDialogInteractor.setTip(mActivity.getString(R.string.biometrics_authenticate_failed_retry));
            mAuthenticationCallback.onAuthenticationFailed();
        }
    };

    @Override
    @RequiresApi(Build.VERSION_CODES.M)
    public void authenticate(@Nullable DialogStyleConfiguration configuration, AuthenticationCallback callback) {
        //回调
        Utils.requireNonNull(callback, "AuthenticationCallback can not be null");
        this.mAuthenticationCallback = callback;

        //取消扫描，每次取消后需要重新创建新示例
        mCancellationSignal = new CancellationSignal();
        mCancellationSignal.setOnCancelListener(() -> mDialogInteractor.dismiss());

        if (createAndInitCipherChecked(callback)) {
            return;
        }

        if (mDialogInteractor == null) {
            mDialogInteractor = BiometricsAuthenticationManager.createDialog(mActivity);
        }
        mDialogInteractor.setOnAdditionalOperationListener(actionCode -> mAuthenticationCallback.onAdditionalAction(actionCode));

        //调起指纹验证
        mFingerprintManagerCompat.authenticate(new FingerprintManagerCompat.CryptoObject(mCipher), 0, mCancellationSignal, authenticationCallback, null);

        //指纹验证框
        showAuthenticationDialog(configuration);
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private boolean createAndInitCipherChecked(AuthenticationCallback callback) {
        if (mCipher == null) {
            try {
                mCipher = mCipherHelper.createCipher();
            } catch (Exception e) {
                e.printStackTrace();
                callback.onAuthenticationError(ERROR_CREATE_CIPHER, mActivity.getString(R.string.biometrics_authenticate_failed));
                return true;
            }
        }

        try {
            mCipherHelper.initCipher(mCipher, mManager.getConfiguration().setInvalidatedByBiometricEnrollment,mManager.getConfiguration().keyName);
        } catch (Exception e) {
            e.printStackTrace();
            if (mCipherHelper.isErrorByBiometricEnrolled(e)) {
                callback.onAuthenticationError(ERROR_NEW_BIOMETRIC_ENROLLED, mActivity.getString(R.string.biometrics_authenticate_failed));
                LogUtils.log("初始化加密对象失败，因为有新的生物特征注册到系统。");
            } else {
                callback.onAuthenticationError(ERROR_CREATE_CIPHER, mActivity.getString(R.string.biometrics_authenticate_failed));
            }
            return true;
        }
        return false;
    }

    private void showAuthenticationDialog(DialogStyleConfiguration configuration) {
        mDialogInteractor.setTip("");
        mDialogInteractor.configStyle(configuration);
        mDialogInteractor.show();
        mDialogInteractor.setOnDismissAction(this::cancelSignal);
    }

    private void cancelSignal() {
        if (mCancellationSignal != null && !mCancellationSignal.isCanceled()) {
            mCancellationSignal.cancel();
        }
    }

    @Override
    public void cancelAuthentication() {
        cancelSignal();
        if (mDialogInteractor != null) {
            mDialogInteractor.dismiss();
        }
    }

    @Override
    public boolean isHardwareDetected() {
        return mFingerprintManagerCompat.isHardwareDetected();
    }

    @Override
    public boolean hasEnrolledBiometrics() {
        return mFingerprintManagerCompat.hasEnrolledFingerprints();
    }

}