package com.android.sdk.push.jpush;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.android.sdk.push.PushContext;
import com.android.sdk.push.PushMessage;
import com.android.sdk.push.Utils;

import cn.jpush.android.api.JPushInterface;
import timber.log.Timber;

import static com.android.sdk.push.jpush.JPush.JPUSH_ID_KET;

/**
 * 极光推送-消息接收广播
 *
 * @author Wangwb
 * Email: 253123123@qq.com
 * Date : 2019-01-28 11:21
 */
public class JPushReceiver extends BroadcastReceiver {

    static JPush sJPush;

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            processMessage(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void processMessage(Intent intent) {
        Bundle bundle = intent.getExtras();

        if (PushContext.isDebug()) {
            Timber.d("[JPushReceiver] onReceive - " + intent.getAction() + ", extras: " + JPushUtils.printBundle(bundle));
        }

        if (bundle == null) {
            return;
        }

        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {

            processRegisterSuccess(bundle);

        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {

            processMessageReceived(bundle);

        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {

            processNotificationMessageReceived(bundle);

        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {

            //目前没有配置这个 action，不会接收此类消息
            processNotificationMessageClicked(bundle);

        } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {

            Timber.w("[JPushReceiver]" + intent.getAction() + " connected state change to " + intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false));

        } else {
            Timber.d("[JPushReceiver] Unhandled intent - " + intent.getAction());
        }

    }

    private void processNotificationMessageClicked(Bundle bundle) {
        Timber.d("[JPushReceiver] 接收到推送下来的通知被点击了");
        PushMessage pushMessage = extractMessage(bundle);
        sJPush.getMessageHandler().handleOnNotificationMessageClicked(pushMessage);
    }

    // 在这里可以做些统计，或者做些其他工作
    private void processNotificationMessageReceived(Bundle bundle) {
        Timber.d("[JPushReceiver] 接收到推送下来的通知");
        PushMessage pushMessage = extractMessage(bundle);
        sJPush.getMessageHandler().onNotificationMessageArrived(pushMessage);
    }

    @NonNull
    private PushMessage extractMessage(Bundle bundle) {
        int notificationId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
        String alertType = bundle.getString(JPushInterface.EXTRA_ALERT_TYPE);
        String alert = bundle.getString(JPushInterface.EXTRA_ALERT);
        String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);

        PushMessage pushMessage = new PushMessage();
        pushMessage.setTitle(title);
        pushMessage.setContent(alert);
        pushMessage.setExtra(extras);
        pushMessage.setNotificationId(notificationId);
        pushMessage.setAlertType(alertType);
        return pushMessage;
    }

    private void processRegisterSuccess(Bundle bundle) {
        String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
        Timber.d("[JPushReceiver] JPush 用户注册成功，接收Registration Id : " + regId);
        //you can send the Registration Id to your server...
        Utils.savePushId(JPUSH_ID_KET, regId);
        sJPush.getPushCallBack().onRegisterPushSuccess(regId);
    }

    // 自定义消息不会展示在通知栏，完全要开发者写代码去处理
    private void processMessageReceived(Bundle bundle) {
        Timber.d("[JPushReceiver] 接收到推送下来的透传消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));

        String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);

        PushMessage pushMessage = new PushMessage();
        pushMessage.setContent(message);
        pushMessage.setExtra(extras);

        sJPush.getMessageHandler().onDirectMessageArrived(pushMessage);
    }

}
