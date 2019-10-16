package com.android.base.app.fragment

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.android.base.app.ui.LoadingView
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2019-10-16 17:28
 */
internal fun <T> T.dismissDialog(recentShowingDialogTime: Long, minimumMills: Long, onDismiss: () -> Unit) where T : LoadingView, T : LifecycleOwner {

    if (!isLoadingDialogShowing()) {
        onDismiss()
        return
    }

    val dialogShowingTime = System.currentTimeMillis() - recentShowingDialogTime

    if (dialogShowingTime >= minimumMills) {
        onDismiss()
    } else {
        lifecycleScope.launch {
            try {
                delay(minimumMills - dialogShowingTime)
                dismissLoadingDialog()
                onDismiss()
            } catch (e: CancellationException) {
                onDismiss()
            }
        }
    }

}