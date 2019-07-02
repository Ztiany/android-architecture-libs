package com.android.sdk.social.ali;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import com.alipay.sdk.app.PayTask;

import org.reactivestreams.Subscriber;

import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.functions.Consumer;

/**
 * 支付宝支付执行器
 * <pre>
 *     支付宝SDK版本：2016.01.20
 * </pre>
 *
 * @author Ztiany
 */
public class AliPayExecutor {

    private static final int PAY_RESULT_SUCCESS = 1;
    private static final int PAY_RESULT_CANCEL = 2;
    private static final int PAY_RESULT_FAIL = 3;
    private static final int PAY_RESULT_WAIT_CONFIRM = 4;

    public static Observable<AliPayResult> doAliPay(final Activity activity, final String sign) {
        return Observable.create(
                (ObservableEmitter<AliPayResult> subscriber) -> {
                    if (subscriber.isDisposed()) {
                        return;
                    }
                    try {
                        PayTask payTask = new PayTask(activity);
                        Map<String, String> pay = payTask.payV2(sign, false);//不要出现丑陋AliPay对话框-_-!
                        subscriber.onNext(new AliPayResult(pay));
                        subscriber.onComplete();
                    } catch (Exception e) {
                        subscriber.onError(e);
                    }
                });
    }

    /**
     * 检测是否安装支付宝
     */
    public static boolean isAliPayInstalled(Context context) {
        Uri uri = Uri.parse("alipays://platformapi/startApp");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        ComponentName componentName = intent.resolveActivity(context.getPackageManager());
        return componentName != null;
    }

    /**
     * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
     * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
     * docType=1) 建议商户依赖异步通知
     */
    private static Integer parseResult(AliPayResult payResult) {
        //String resultInfo = payResult.getResult();// 同步返回需要验证的信息
        String resultStatus = payResult.getResultStatus();
        // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
        if (TextUtils.equals(resultStatus, "9000")) {
            return PAY_RESULT_SUCCESS;
        } else {
            // 判断resultStatus 为非"9000"则代表可能支付失败
            // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
            if (TextUtils.equals(resultStatus, "8000")) {
                //Toast.makeText(PayDemoActivity.this, "支付结果确认中", Toast.LENGTH_SHORT).show();
                return PAY_RESULT_WAIT_CONFIRM;
            } else if (TextUtils.equals(resultStatus, "6001")) {
                return PAY_RESULT_CANCEL;
            } else {
                // 其他值就可以判断为支付失败，或者系统返回的错误
                return PAY_RESULT_FAIL;
            }
        }
    }

    public static class PayConsumer implements Consumer<AliPayResult> {
        PayResultCallback payResultCallback;

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

    @SuppressWarnings("unused")
    public abstract static class PaySubscriber implements PayResultCallback, Subscriber<AliPayResult> {

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

}
