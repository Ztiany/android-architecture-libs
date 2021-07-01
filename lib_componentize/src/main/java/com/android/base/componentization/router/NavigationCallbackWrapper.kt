package com.android.base.componentization.router

import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.facade.callback.NavigationCallback

internal class NavigationCallbackWrapper(
    private val callback: AppNavigationCallback
) : NavigationCallback {

    override fun onFound(postcard: Postcard) {
        callback.onFound(Postcard(postcard))
    }

    override fun onLost(postcard: Postcard) {
        callback.onLost(Postcard(postcard))
    }

    override fun onArrival(postcard: Postcard) {
        callback.onArrival(Postcard(postcard))
    }

    override fun onInterrupt(postcard: Postcard) {
        callback.onInterrupt(Postcard(postcard))
    }

}