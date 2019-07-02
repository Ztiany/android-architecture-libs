package com.android.sdk.social.ali;


public interface PayResultCallback {

    void onPayCancel();

    void onPayFail(String errStr);

    void onPaySuccess();

    void onPayNeedConfirmResult();

}
