package com.android.sdk.biometrics;

import android.content.Context;
import android.text.TextUtils;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2020-03-10 18:02
 */
class Utils {

    /**
     * copy from system code. it means the authentication is canceled.
     */
    final static int SYSTEM_CODE_CANCEL_AUTHENTICATION = 5;

    static CharSequence makeErrorMessage(Context context, int errMsgId, CharSequence errString) {
        CharSequence error = errString;
        if (TextUtils.isEmpty(error)) {
            error = context.getString(R.string.biometrics_authenticate_failed) + " " + errMsgId;
        }
        return error;
    }

    static <T> T requireNonNull(T obj, String message) {
        if (obj == null) {
            throw new NullPointerException(message);
        }
        return obj;
    }

}