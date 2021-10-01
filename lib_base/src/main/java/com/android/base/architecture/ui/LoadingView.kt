package com.android.base.architecture.ui

import androidx.annotation.StringRes

/**
 * 显示通用的 LoadingDialog 和 Message
 *
 * @author Ztiany
 * Email: 1169654504@qq.com
 * Date : 2016-12-02 15:12
 */
interface LoadingView {

    fun showLoadingDialog()
    fun showLoadingDialog(cancelable: Boolean)
    fun showLoadingDialog(message: CharSequence, cancelable: Boolean)
    fun showLoadingDialog(@StringRes messageId: Int, cancelable: Boolean)
    fun dismissLoadingDialog()
    fun dismissLoadingDialog(minimumMills: Long, onDismiss: (() -> Unit)? = null)
    fun isLoadingDialogShowing(): Boolean

    fun showMessage(message: CharSequence)
    fun showMessage(@StringRes messageId: Int)

}