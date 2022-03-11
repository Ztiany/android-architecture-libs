package com.android.base.architecture.ui.state

import com.android.base.architecture.ui.state.StateLayoutConfig.RetryableState

interface OnRetryActionListener {

    fun onRetry(@RetryableState state: Int)

}