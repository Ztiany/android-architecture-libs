package com.android.sdk.push;


public interface PushCallBack {
    void onRegisterPushSuccess(String registrationID);
    void onRegisterPushFail();
}
