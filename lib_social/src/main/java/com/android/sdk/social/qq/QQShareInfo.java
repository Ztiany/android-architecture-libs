package com.android.sdk.social.qq;

import android.os.Bundle;

import com.tencent.connect.share.QQShare;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2019-08-27 19:00
 */
public class QQShareInfo {

    private final Bundle mBundle = new Bundle();

    public QQShareInfo() {
        shareToFriend();
    }

    public QQShareInfo setTitle(String title) {
        mBundle.putString(QQShare.SHARE_TO_QQ_TITLE, title);
        return this;
    }

    public QQShareInfo setTargetUrl(String targetUrl) {
        mBundle.putString(QQShare.SHARE_TO_QQ_TARGET_URL, targetUrl);
        return this;
    }

    public QQShareInfo setSummary(String summary) {
        mBundle.putString(QQShare.SHARE_TO_QQ_SUMMARY, summary);
        return this;
    }

    public QQShareInfo setImage(String imageUrl) {
        mBundle.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, imageUrl);
        return this;
    }

    public QQShareInfo setLocalImage(String imageUrl) {
        mBundle.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, imageUrl);
        return this;
    }

    public QQShareInfo shareToFriend() {
        mBundle.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        return this;
    }

    Bundle getBundle() {
        return mBundle;
    }

}
