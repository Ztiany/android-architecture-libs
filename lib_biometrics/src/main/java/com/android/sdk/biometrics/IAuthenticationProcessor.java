package com.android.sdk.biometrics;

import androidx.annotation.Nullable;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2020-03-10 14:27
 */
public interface IAuthenticationProcessor {

    int ACTION_CANCEL = 12;

    /**
     * 尝试次数过多，请稍后重试
     */
    int ERROR_TIMES_OUT = 7;

    /**
     * 验证成功，但是验证加密对象时失败
     */
    int ERROR_CHECK_RESULT = 8;

    /**
     * 不支持生物识别
     */
    int ERROR_UNSUPPORTED = 9;

    /**
     * 初始化加密对象时失败，因为验证过程中有新的生物特征注册到系统
     */
    int ERROR_NEW_BIOMETRIC_ENROLLED = 12;

    /**
     * 生成加密对象时失败
     */
    int ERROR_CREATE_CIPHER = 11;

    /**
     * 调起指纹验证
     */
    void authenticate(@Nullable DialogStyleConfiguration configuration, AuthenticationCallback callback);

    /**
     * 取消指纹验证
     */
    void cancelAuthentication();

    /**
     * 硬件是否支持指纹识别
     */
    boolean isHardwareDetected();

    /**
     * 是否添加了指纹信息
     */
    boolean hasEnrolledBiometrics();

    default boolean canAuthenticate() {
        return isHardwareDetected() && hasEnrolledBiometrics();
    }

}
