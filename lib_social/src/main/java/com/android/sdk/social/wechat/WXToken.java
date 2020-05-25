package com.android.sdk.social.wechat;


import org.jetbrains.annotations.NotNull;

import androidx.annotation.NonNull;

@SuppressWarnings("unused")
class WXToken extends AuthResult {

    private String access_token;
    private String expires_in;
    private String refresh_token;
    private String openid;
    private String scope;
    private String unionid;

    String getAccess_token() {
        return access_token;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    String getOpenid() {
        return openid;
    }

    @NotNull
    @NonNull
    @Override
    public String toString() {
        return "WXToken{" +
                "access_token='" + access_token + '\'' +
                ", expires_in='" + expires_in + '\'' +
                ", refresh_token='" + refresh_token + '\'' +
                ", openid='" + openid + '\'' +
                ", scope='" + scope + '\'' +
                ", unionid='" + unionid + '\'' +
                '}';
    }

}