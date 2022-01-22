package com.android.base.utils.android.network

enum class NetworkState {

    STATE_WIFI, STATE_GPRS, STATE_NONE;

    val isConnected: Boolean
        get() = this != STATE_NONE

}

