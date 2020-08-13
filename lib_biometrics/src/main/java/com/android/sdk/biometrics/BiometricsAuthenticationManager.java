package com.android.sdk.biometrics;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2020-03-10 14:26
 */
public class BiometricsAuthenticationManager implements IAuthenticationProcessor {

    private static DialogFactory sDialogFactory;

    private IAuthenticationProcessor mIAuthenticationProcessor;

    private Activity mActivity;

    private AuthenticationConfiguration mConfiguration;

    public BiometricsAuthenticationManager(@NonNull Activity activity, boolean forceUsingOldApi, @NonNull LifecycleOwner lifecycleOwner) {
        mActivity = activity;
        mConfiguration = AuthenticationConfiguration.createDefault();

        if (AndroidVersion.isAboveAndroidP() && !forceUsingOldApi) {
            mIAuthenticationProcessor = new AndroidPAuthenticationProcessor(this);
        } else if (AndroidVersion.isAboveAndroidM()) {
            mIAuthenticationProcessor = new AndroidMAuthenticationProcessor(this);
        } else {
            mIAuthenticationProcessor = new EmptyAuthenticationProcessor(activity);
        }

        lifecycleOwner.getLifecycle().addObserver(new DefaultLifecycleObserver() {
            @Override
            public void onDestroy(@NonNull LifecycleOwner owner) {
                cancelAuthentication();
            }
        });
    }

    public BiometricsAuthenticationManager(@NonNull Activity activity, @NonNull LifecycleOwner lifecycleOwner) {
        this(activity, false, lifecycleOwner);
    }

    public static void registerDialogFactory(DialogFactory dialogFactory) {
        sDialogFactory = dialogFactory;
    }

    static DialogInteractor createDialog(Activity activity) {
        if (sDialogFactory != null) {
            return sDialogFactory.createDialog(activity);
        }
        return new DefaultFingerprintDialog(activity);
    }

    @Override
    public void authenticate(@Nullable DialogStyleConfiguration configuration, AuthenticationCallback callback) {
        mIAuthenticationProcessor.authenticate(configuration, callback);
    }

    @Override
    public void cancelAuthentication() {
        mIAuthenticationProcessor.cancelAuthentication();
    }

    @Override
    public boolean isHardwareDetected() {
        return mIAuthenticationProcessor.isHardwareDetected();
    }

    @Override
    public boolean hasEnrolledBiometrics() {
        return mIAuthenticationProcessor.hasEnrolledBiometrics();
    }

    Activity getActivity() {
        return mActivity;
    }

    public AuthenticationConfiguration getConfiguration() {
        return mConfiguration;
    }

}
