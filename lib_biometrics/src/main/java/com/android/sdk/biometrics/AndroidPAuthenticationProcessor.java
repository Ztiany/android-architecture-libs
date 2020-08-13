package com.android.sdk.biometrics;

import android.app.Activity;
import android.hardware.biometrics.BiometricPrompt;
import android.os.Build;
import android.os.CancellationSignal;
import android.text.TextUtils;

import javax.crypto.Cipher;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.hardware.fingerprint.FingerprintManagerCompat;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2020-03-10 17:51
 */
class AndroidPAuthenticationProcessor implements IAuthenticationProcessor {

    private final BiometricsAuthenticationManager mManager;

    private Activity mActivity;

    private FingerprintManagerCompat mFingerprintManagerCompat;

    private AuthenticationCallback mAuthenticationCallback;

    private CancellationSignal mCancellationSignal;

    private CipherHelper mCipherHelper;

    private Cipher mCipher;

    AndroidPAuthenticationProcessor(BiometricsAuthenticationManager manager) {
        mFingerprintManagerCompat = FingerprintManagerCompat.from(manager.getActivity());
        mActivity = manager.getActivity();
        mCipherHelper = new CipherHelper();
        mManager = manager;
    }

    /**
     * 认证结果回调
     */
    @RequiresApi(Build.VERSION_CODES.P)
    private BiometricPrompt.AuthenticationCallback authenticationCallback = new BiometricPrompt.AuthenticationCallback() {
        @Override
        public void onAuthenticationError(int errorCode, CharSequence errString) {
            super.onAuthenticationError(errorCode, errString);
            LogUtils.log("onAuthenticationError() called with: errMsgId = [" + errorCode + "], errString = [" + errString + "]");
            //通知错误
            mAuthenticationCallback.onAuthenticationError(errorCode, Utils.makeErrorMessage(mActivity, errorCode, errString));
        }

        @Override
        public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
            super.onAuthenticationHelp(helpCode, helpString);
            LogUtils.log("onAuthenticationHelp() called with: helpMsgId = [" + helpCode + "], helpString = [" + helpString + "]");
            // Don't forward the result to the client, since the dialog takes care of it.
        }

        @Override
        public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
            super.onAuthenticationSucceeded(result);
            LogUtils.log("onAuthenticationSucceeded() called with: result = [" + result + "]");
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
            mAuthenticationCallback.onAuthenticationFailed();
        }

    };

    @Override
    @RequiresApi(Build.VERSION_CODES.P)
    public void authenticate(@Nullable DialogStyleConfiguration configuration, AuthenticationCallback callback) {
        Utils.requireNonNull(callback, "AuthenticationCallback can not be null");
        this.mAuthenticationCallback = callback;

        //取消扫描，每次取消后需要重新创建新示例
        mCancellationSignal = new CancellationSignal();

        if (createAndInitCipherChecked(callback)) {
            return;
        }

        //构建 BiometricPrompt
        BiometricPrompt.Builder builder = new BiometricPrompt.Builder(mActivity);
        applyUIConfiguration(builder, configuration);
        BiometricPrompt biometricPrompt = builder.build();

        //拉起指纹验证模块，等待验证
        biometricPrompt.authenticate(new BiometricPrompt.CryptoObject(mCipher), mCancellationSignal, mActivity.getMainExecutor(), authenticationCallback);
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
            mCipherHelper.initCipher(mCipher, mManager.getConfiguration().setInvalidatedByBiometricEnrollment, mManager.getConfiguration().keyName);
        } catch (Exception e) {
            e.printStackTrace();
            if (mCipherHelper.isErrorByBiometricEnrolled(e)) {
                LogUtils.log("初始化加密对象失败，因为有新的生物特征注册到系统。");
                callback.onAuthenticationError(ERROR_NEW_BIOMETRIC_ENROLLED, mActivity.getString(R.string.biometrics_authenticate_failed));
            } else {
                callback.onAuthenticationError(ERROR_CREATE_CIPHER, mActivity.getString(R.string.biometrics_authenticate_failed));
            }
            return true;
        }
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    private void applyUIConfiguration(BiometricPrompt.Builder builder, @Nullable DialogStyleConfiguration configuration) {
        CharSequence configurationTitle = configuration == null ? "" : configuration.getTitle();
        CharSequence configurationCancelText = configuration == null ? "" : configuration.getCancelAction();

        CharSequence title = TextUtils.isEmpty(configurationTitle) ? mActivity.getString(R.string.biometrics_dialog_title) : configurationTitle;
        CharSequence cancelText = TextUtils.isEmpty(configurationCancelText) ? mActivity.getString(R.string.biometrics_dialog_cancel) : configurationCancelText;

        builder.setTitle(title);

        if (configuration != null && !TextUtils.isEmpty(configuration.getSubtitle())) {
            builder.setSubtitle(configuration.getSubtitle());
        }

        if (configuration != null && !TextUtils.isEmpty(configuration.getDescription())) {
            builder.setDescription(configuration.getDescription());
        }

        builder.setNegativeButton(cancelText, Runnable::run, (dialog, which) -> {
            mAuthenticationCallback.onAdditionalAction(ACTION_CANCEL);
            LogUtils.log("onBiometricPrompt.Builder onNegativeButton ");
        });
    }

    @Override
    public void cancelAuthentication() {
        if (mCancellationSignal != null && !mCancellationSignal.isCanceled()) {
            mCancellationSignal.cancel();
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