package com.android.sdk.push;

/**
 * @author Ztiany
 *         Email: 1169654504@qq.com
 *         Date : 2017-03-09 11:31
 */
public interface MessageHandler {

    /**
     * 处理透传消息
     *
     * @param pushMessage 消息
     */
    void onDirectMessageArrived(PushMessage pushMessage);

    /**
     * 通知栏消息被点击
     */
    void handleOnNotificationMessageClicked(PushMessage pushMessage);

    /**
     * 通知栏消息到达
     */
    void onNotificationMessageArrived(PushMessage pushMessage);

}
