package com.android.base.network

import com.blankj.utilcode.util.NetworkUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

enum class NetworkState {

    STATE_WIFI, STATE_GPRS, STATE_NONE;

    val isConnected: Boolean
        get() = this != STATE_NONE

}

private val networkStateFlow = MutableStateFlow(getCurrentState())

fun getCurrentState(): NetworkState {
    return when {
        NetworkUtils.isWifiConnected() -> NetworkState.STATE_WIFI
        NetworkUtils.isConnected() -> NetworkState.STATE_GPRS
        else -> NetworkState.STATE_NONE
    }
}

internal fun observableState(): Flow<NetworkState> {
    return networkStateFlow
}

internal fun notifyState(state: NetworkState) {
    networkStateFlow.value = state
}
