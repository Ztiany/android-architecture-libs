package com.android.sdk.push;

import android.app.Activity;
import android.support.annotation.NonNull;

/**
 * @author Ztiany
 * Email: 1169654504@qq.com
 * Date : 2017-03-09 11:31
 */
public interface Push {

    void registerPush(@NonNull final PushCallBack pushCallBack);

    void setAlias(String alias);

    void clearAlias();

    void setTag(String tag);

    void addTag(String tag);

    void deleteTag(String tag);

    void clearTag();

    void enablePush();

    void disablePush();

    void setMessageHandler(MessageHandler messageHandler);

    MessageHandler getMessageHandler();

    void onActivityCreate(Activity activity);

    void setChannel(String channel);

}
