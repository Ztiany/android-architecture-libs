package com.android.sdk.push.jpush;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.android.sdk.push.MessageHandler;
import com.android.sdk.push.Push;
import com.android.sdk.push.PushCallBack;
import com.android.sdk.push.PushContext;
import com.android.sdk.push.Utils;

import cn.jpush.android.api.JPushInterface;
import timber.log.Timber;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2019-01-26 17:44
 */
public class JPush implements Push {

    private Context mContext;
    private MessageHandler mMessageHandler;
    private PushCallBack mPushCallBack;

    static final String JPUSH_ID_KET = "jpush_id_key";

    public JPush(Application mApplication) {
        mContext = mApplication.getApplicationContext();
    }

    @Override
    public void registerPush(@NonNull PushCallBack pushCallBack) {
        this.mPushCallBack = pushCallBack;
        //给 JPushReceiver 设置 JPush
        JPushReceiver.sJPush = this;
        // 设置开启日志,发布时请关闭日志
        JPushInterface.setDebugMode(PushContext.isDebug());
        // 初始化极光推送服务
        JPushInterface.init(mContext);
        String registrationID = getRegistrationID();
        Timber.d("jpush registrationID = " + registrationID);
        boolean isRegistrationSuccess = !TextUtils.isEmpty(registrationID);
        if (isRegistrationSuccess) {
            this.mPushCallBack.onRegisterPushSuccess(registrationID);
        }
    }

    private String getRegistrationID() {
        String registrationID = JPushInterface.getRegistrationID(mContext);
        if (TextUtils.isEmpty(registrationID)) {
            registrationID = Utils.getPushId(JPUSH_ID_KET);
        }
        return registrationID;
    }

    @Override
    public void setAlias(String alias) {
        JPushUtils.setAlias(mContext, alias);
    }

    @Override
    public void clearAlias() {
        JPushUtils.clearAlias(mContext);
    }

    @Override
    public void setTag(String tag) {
        JPushUtils.setTag(mContext, tag);
    }

    @Override
    public void addTag(String tag) {
        JPushUtils.deleteTag(mContext, tag);
    }

    @Override
    public void deleteTag(String tag) {
        JPushUtils.setTag(mContext, tag);
    }

    @Override
    public void clearTag() {
        JPushUtils.clearTags(mContext);
    }

    @Override
    public void enablePush() {
        JPushInterface.resumePush(mContext);
    }

    @Override
    public void disablePush() {
        JPushInterface.stopPush(mContext);
    }

    @Override
    public void setMessageHandler(MessageHandler messageHandler) {
        this.mMessageHandler = messageHandler;
    }

    @Override
    public MessageHandler getMessageHandler() {
        return mMessageHandler;
    }

    PushCallBack getPushCallBack() {
        return mPushCallBack;
    }

    @Override
    public void setChannel(String channel) {
        JPushInterface.setChannel(mContext, channel);
    }

    @Override
    public void onActivityCreate(Activity activity) {
        // no op
    }

}
