package com.android.sdk.social.ali;

import org.reactivestreams.Subscriber;

import static com.android.sdk.social.ali.AliPayExecutor.PAY_RESULT_CANCEL;
import static com.android.sdk.social.ali.AliPayExecutor.PAY_RESULT_FAIL;
import static com.android.sdk.social.ali.AliPayExecutor.PAY_RESULT_SUCCESS;
import static com.android.sdk.social.ali.AliPayExecutor.PAY_RESULT_WAIT_CONFIRM;
import static com.android.sdk.social.ali.AliPayExecutor.parseResult;

@SuppressWarnings("unused")
public abstract class PaySubscriber implements PayResultCallback, Subscriber<AliPayResult> {

    @Override
    public void onComplete() {
    }

    @Override
    public final void onNext(AliPayResult aliPayResult) {
        int result = parseResult(aliPayResult);
        if (result == PAY_RESULT_CANCEL) {
            onPayCancel();
        } else if (result == PAY_RESULT_FAIL) {
            onPayFail(aliPayResult.getMemo());
        } else if (result == PAY_RESULT_SUCCESS) {
            onPaySuccess();
        } else if (result == PAY_RESULT_WAIT_CONFIRM) {
            onPayNeedConfirmResult();
        }
    }

}