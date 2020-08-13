package com.android.sdk.biometrics;

import android.content.Context;
import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.text.TextUtils;
import android.util.Base64;

import java.security.InvalidAlgorithmParameterException;
import java.security.Key;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;

import androidx.annotation.RequiresApi;

/**
 * 加密类，用于判定指纹合法性。
 */
class CipherHelper {

    // This can be key name you want. Should be unique for the app.
    private static final String DEFAULT_KEY_NAME = "android_biometrics_cipher";

    // We always use this keystore on Android.
    private static final String KEYSTORE_NAME = "AndroidKeyStore";

    // Don't modify following definitions
    private static final String KEY_ALGORITHM = KeyProperties.KEY_ALGORITHM_AES;
    private static final String BLOCK_MODE = KeyProperties.BLOCK_MODE_CBC;
    private static final String ENCRYPTION_PADDING = KeyProperties.ENCRYPTION_PADDING_PKCS7;
    private static final String TRANSFORMATION = KEY_ALGORITHM + "/" + BLOCK_MODE + "/" + ENCRYPTION_PADDING;

    private KeyStore mKeystore;

    /**
     * 创建一个Cipher，用于 FingerprintManager.CryptoObject 的初始化。
     *
     * @see <a href='https://developer.android.google.cn/reference/javax/crypto/Cipher.html'>Cipher</>
     */
    Cipher createCipher() throws Exception {
        return Cipher.getInstance(TRANSFORMATION);
    }

    @RequiresApi(Build.VERSION_CODES.M)
    void initCipher(Cipher cipher, boolean setInvalidatedByBiometricEnrollment, String keyName) throws Exception {
        LogUtils.log("initCipher() called with: cipher = [" + cipher + "], setInvalidatedByBiometricEnrollment = [" + setInvalidatedByBiometricEnrollment + "], keyName = [" + keyName + "]");

        String finalKeyName = TextUtils.isEmpty(keyName) ? DEFAULT_KEY_NAME : keyName;
        try {
            Key key = getKey(setInvalidatedByBiometricEnrollment, finalKeyName);
            cipher.init(Cipher.ENCRYPT_MODE, key);
        } catch (Exception e) {
            if (e instanceof KeyPermanentlyInvalidatedException) {
                resetBiometricsKey(finalKeyName);
            }
            e.printStackTrace();
            throw e;
        }
    }

    private void resetBiometricsKey(String finalKeyName) {
        try {
            if (mKeystore != null) {
                mKeystore.deleteEntry(finalKeyName);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    boolean isErrorByBiometricEnrolled(Throwable throwable) {
        if (AndroidVersion.isAboveAndroidM()) {
            return throwable instanceof KeyPermanentlyInvalidatedException;
        }
        return false;
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private Key getKey(boolean setInvalidatedByBiometricEnrollment, String keyName) throws Exception {

        if (mKeystore == null) {
            mKeystore = KeyStore.getInstance(KEYSTORE_NAME);
            mKeystore.load(null);
        }

        Key secretKey;

        if (!mKeystore.isKeyEntry(keyName)) {
            LogUtils.log("createKey");
            createKey(setInvalidatedByBiometricEnrollment, keyName);
        }

        secretKey = mKeystore.getKey(keyName, null);

        return secretKey;
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private void createKey(boolean setInvalidatedByBiometricEnrollment, String keyName) throws NoSuchProviderException, NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        KeyGenerator keyGen = KeyGenerator.getInstance(KEY_ALGORITHM, KEYSTORE_NAME);

        KeyGenParameterSpec.Builder builder = new KeyGenParameterSpec.Builder(keyName, KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT);

        builder.setBlockModes(BLOCK_MODE)
                .setEncryptionPaddings(ENCRYPTION_PADDING)
                .setUserAuthenticationRequired(true);

        if (AndroidVersion.isAboveAndroidN()) {
            builder.setInvalidatedByBiometricEnrollment(setInvalidatedByBiometricEnrollment);
        }

        KeyGenParameterSpec keyGenSpec = builder.build();
        keyGen.init(keyGenSpec);
        keyGen.generateKey();
    }

    void validateResult(Context context, AuthenticationCallback authenticationCallback, Cipher cipher) {
        try {
            byte[] bytes = cipher.doFinal("The test data".getBytes());
            LogUtils.log(Base64.encodeToString(bytes, 0 /* flags */));
            authenticationCallback.onAuthenticationSucceeded();
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.log("生物特征验证成功，但是加密对象验证失败。");
            authenticationCallback.onAuthenticationError(IAuthenticationProcessor.ERROR_CHECK_RESULT, context.getString(R.string.biometrics_authenticate_failed));
        }
    }

}