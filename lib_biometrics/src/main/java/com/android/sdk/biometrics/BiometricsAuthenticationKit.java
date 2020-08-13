package com.android.sdk.biometrics;

import android.content.Context;
import android.hardware.fingerprint.FingerprintManager;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.core.hardware.fingerprint.FingerprintManagerCompat;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2020-03-10 19:21
 */
public class BiometricsAuthenticationKit {

    public static boolean isHardwareDetected(Context context) {
        return FingerprintManagerCompat.from(context).isHardwareDetected();
    }

    public static boolean hasEnrolledBiometrics(Context context) {
        return FingerprintManagerCompat.from(context).hasEnrolledFingerprints();
    }

    public static boolean canAuthenticate(Context context) {
        return isHardwareDetected(context) && hasEnrolledBiometrics(context);
    }

    /**
     * invoking hidden api, you need <a href='https://github.com/tiann/FreeReflection'>FreeReflection</a>.
     */
    public static List<Fingerprint> getFingerprintInfo(Context context) {
        if (!AndroidVersion.isAboveAndroidM()) {
            return Collections.emptyList();
        }

        List<Fingerprint> fingerIdList = new ArrayList<>();
        try {
            FingerprintManager fingerprintManager = (FingerprintManager) context.getSystemService(Context.FINGERPRINT_SERVICE);
            Method method = FingerprintManager.class.getDeclaredMethod("getEnrolledFingerprints");
            Object obj = method.invoke(fingerprintManager);

            if (obj != null) {
                Class<?> clazz = Class.forName("android.hardware.fingerprint.Fingerprint");
                Method getFingerId = clazz.getDeclaredMethod("getFingerId");

                for (int i = 0; i < ((List) obj).size(); i++) {
                    Object item = ((List) obj).get(i);
                    if (item != null) {
                        fingerIdList.add(new Fingerprint((int) getFingerId.invoke(item)));
                    }
                }
            }
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return fingerIdList;
    }

}