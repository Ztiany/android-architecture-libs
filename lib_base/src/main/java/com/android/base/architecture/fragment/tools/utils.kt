package com.android.base.architecture.fragment.tools

import android.os.Bundle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.android.base.architecture.ui.loading.LoadingViewHost
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2019-10-16 17:28
 */
internal fun <T> T.dismissDialog(recentShowingDialogTime: Long, minimumMills: Long, onDismiss: (() -> Unit)?) where T : LoadingViewHost, T : LifecycleOwner {

    if (!isLoadingDialogShowing()) {
        onDismiss?.invoke()
        return
    }

    val dialogShowingTime = System.currentTimeMillis() - recentShowingDialogTime

    if (dialogShowingTime >= minimumMills) {
        dismissLoadingDialog()
        onDismiss?.invoke()

    } else {
        lifecycleScope.launch {
            try {
                delay(minimumMills - dialogShowingTime)
                dismissLoadingDialog()
                onDismiss?.invoke()
            } catch (e: CancellationException) {
                onDismiss?.invoke()
            }
        }
    }

}

private const val FRAGMENT_STATE_KEY = "fragment_state_key"

internal fun saveInstanceState(outState: Bundle, _state: Bundle?) {
    _state?.let {
        outState.putBundle(FRAGMENT_STATE_KEY, it)
    }
}

internal fun getInstanceState(state: Bundle?): Bundle? {
    return state?.getBundle(FRAGMENT_STATE_KEY)
}