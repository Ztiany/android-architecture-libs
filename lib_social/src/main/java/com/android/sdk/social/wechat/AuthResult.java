package com.android.sdk.social.wechat;


import androidx.annotation.NonNull;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2018-11-07 15:17
 */
@SuppressWarnings("unused")
public class AuthResult {

    private int errcode;
    private String errmsg;

    int getErrcode() {
        return errcode;
    }

    String getErrmsg() {
        return errmsg;
    }

    @NonNull
    @Override
    public String toString() {
        return "AuthResult{" +
                "errcode=" + errcode +
                ", errmsg='" + errmsg + '\'' +
                '}';
    }

}