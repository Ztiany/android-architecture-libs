package com.android.sdk.social.ali;

import io.reactivex.functions.Consumer;

import static com.android.sdk.social.ali.AliPayExecutor.PAY_RESULT_CANCEL;
import static com.android.sdk.social.ali.AliPayExecutor.PAY_RESULT_FAIL;
import static com.android.sdk.social.ali.AliPayExecutor.PAY_RESULT_SUCCESS;
import static com.android.sdk.social.ali.AliPayExecutor.PAY_RESULT_WAIT_CONFIRM;
import static com.android.sdk.social.ali.AliPayExecutor.parseResult;

public class PayConsumer implements Consumer<AliPayResult> {

    private PayResultCallback payResultCallback;

    public PayConsumer(PayResultCallback payResultCallback) {
        this.payResultCallback = payResultCallback;
    }

    @Override
    public void accept(AliPayResult aliPayResult) {
        int result = parseResult(aliPayResult);
        if (result == PAY_RESULT_CANCEL) {
            payResultCallback.onPayCancel();
        } else if (result == PAY_RESULT_FAIL) {
            payResultCallback.onPayFail(aliPayResult.getMemo());
        } else if (result == PAY_RESULT_SUCCESS) {
            payResultCallback.onPaySuccess();
        } else if (result == PAY_RESULT_WAIT_CONFIRM) {
            payResultCallback.onPayNeedConfirmResult();
        }
    }

}