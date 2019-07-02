package com.android.sdk.push;


import android.support.annotation.NonNull;

public class PushMessage {

    private int messageId;
    private int notificationId;
    private String alertType;
    private String content;
    private String extra;
    private String title;

    public int getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(int notificationId) {
        this.notificationId = notificationId;
    }

    public String getAlertType() {
        return alertType;
    }

    public void setAlertType(String alertType) {
        this.alertType = alertType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    @NonNull
    @Override
    public String toString() {
        return "PushMessage{" +
                "messageiId=" + messageId +
                ", notificationId=" + notificationId +
                ", alertType=" + alertType +
                ", content='" + content + '\'' +
                ", extra='" + extra + '\'' +
                ", title='" + title + '\'' +
                '}';
    }

}
