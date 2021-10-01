package com.android.sdk.upgrade

data class CheckingState(
    val isLoading: Boolean = false,
    val isDownloading: Boolean = false,
    val error: Throwable? = null,
    val upgradeInfo: UpgradeInfo? = null
)
