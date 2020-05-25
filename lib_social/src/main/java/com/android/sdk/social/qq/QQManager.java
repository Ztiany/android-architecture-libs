package com.android.sdk.social.qq;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.android.sdk.social.common.Utils;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import androidx.annotation.Nullable;
import timber.log.Timber;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2019-08-27 18:16
 */
public class QQManager {

    private final Tencent mTencent;

    private static String sAppId;

    public static void initQQSDK(String appId) {
        sAppId = appId;
    }

    private static String getAppId() {
        Utils.requestNotNull(sAppId, "weChat app id");
        return sAppId;
    }

    public QQManager(Context context) {
        mTencent = Tencent.createInstance(getAppId(), context);
    }

    public void shareToQQ(Activity activity, QQShareInfo shareInfo, @Nullable ShareResultCallback shareResultCallback) {
        mTencent.shareToQQ(activity, shareInfo.getBundle(), newDefaultListener(shareResultCallback));
    }

    public boolean isQQInstalled(Context context) {
        return mTencent.isQQInstalled(context);
    }

    public static boolean onActivityResult(int requestCode, int resultCode, Intent intent) {
        return Tencent.onActivityResultData(requestCode, resultCode, intent, null);
    }

    private static IUiListener newDefaultListener(ShareResultCallback shareResultCallback) {
        if (shareResultCallback == null) {
            return null;
        }
        return new IUiListener() {
            @Override
            public void onComplete(Object o) {
                Timber.d("shareToQQ onComplete: %s", o.toString());
                shareResultCallback.onSuccess();
            }

            @Override
            public void onError(UiError uiError) {
                Timber.d("shareToQQ onError: %s", uiError.errorDetail);
                shareResultCallback.onError();
            }

            @Override
            public void onCancel() {
                Timber.d("shareToQQ onCancel");
                shareResultCallback.onCancel();
            }
        };
    }

}