package com.android.base.architecture.ui

import com.android.base.architecture.ui.StateLayoutConfig.RetryableState

interface OnRetryActionListener {

    fun onRetry(@RetryableState state: Int)

}