package com.android.sdk.biometrics;

import androidx.annotation.NonNull;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2020-03-10 14:34
 */
public interface AuthenticationCallback {

    /**
     * 验证失败，该次流程取消。
     *
     * @param errorCode 由系统返回的code，或者是在 {@link IAuthenticationProcessor} 中定义的 ERROR 开头的 Code。
     */
    void onAuthenticationError(int errorCode, @NonNull CharSequence errString);

    /**
     * 验证成功，该次流程取消。
     */
    void onAuthenticationSucceeded();

    /**
     * 生物特征已经识别，但是与注册的不匹配，可再次尝试。
     */
    void onAuthenticationFailed();

    /**
     * 验证过程中的相关操作，比如取消验证。
     */
    void onAdditionalAction(int actionCode);

}
