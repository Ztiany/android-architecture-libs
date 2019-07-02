package com.android.base.app.ui;

import android.support.annotation.StringRes;

/**
 * 显示通用的 LoadingDialog 和 Message
 *
 * @author Ztiany
 * Email: 1169654504@qq.com
 * Date : 2016-12-02 15:12
 */
public interface LoadingView {

    void showLoadingDialog();

    void showLoadingDialog(boolean cancelable);

    void showLoadingDialog(CharSequence message, boolean cancelable);

    void showLoadingDialog(@StringRes int messageId, boolean cancelable);

    void dismissLoadingDialog();

    void showMessage(CharSequence message);

    void showMessage(@StringRes int messageId);

}
