package com.android.base.app.ui;

import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Ztiany
 * Email: 1169654504@qq.com
 * Date : 2017-04-20 23:32
 */
public interface StateLayoutConfig {

    int CONTENT = 0x01;
    int LOADING = 0x02;
    int ERROR = 0x03;
    int EMPTY = 0x04;
    int NET_ERROR = 0x05;
    int BLANK = 0x06;
    int REQUESTING = 0x07;
    int SERVER_ERROR = 0x08;

    @IntDef({
            EMPTY,
            ERROR,
            NET_ERROR,
    })
    @Retention(RetentionPolicy.SOURCE)
    @interface RetryableState {

    }

    @IntDef({
            EMPTY,
            ERROR,
            CONTENT,
            LOADING,
            NET_ERROR,
            BLANK,
            REQUESTING,
            SERVER_ERROR,
    })
    @Retention(RetentionPolicy.SOURCE)
    @interface ViewState {

    }

    StateLayoutConfig setStateMessage(@RetryableState int state, CharSequence message);

    StateLayoutConfig setStateIcon(@RetryableState int state, Drawable drawable);

    StateLayoutConfig setStateIcon(@RetryableState int state, @DrawableRes int drawableId);

    StateLayoutConfig setStateAction(@RetryableState int state, CharSequence actionText);

    StateLayoutConfig setStateRetryListener(OnRetryActionListener retryActionListener);

    StateLayoutConfig disableOperationWhenRequesting(boolean disable);

}
