package com.android.base.utils.android.network

import android.app.Application
import android.content.IntentFilter
import android.net.ConnectivityManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

enum class NetworkState {

    STATE_WIFI, STATE_GPRS, STATE_NONE;

    val isConnected: Boolean
        get() = this != STATE_NONE

}

private val networkStateFlow by lazy {
    MutableStateFlow(
        when {
            NetworkUtils.isWifiConnected() -> NetworkState.STATE_WIFI
            NetworkUtils.isConnected() -> NetworkState.STATE_GPRS
            else -> NetworkState.STATE_NONE
        }
    )
}

/**监听网络状态*/
fun observableNetworkState(): Flow<NetworkState> {
    return networkStateFlow
}

//网络状态监听
internal fun initNetworkState(application: Application) {
    application.registerReceiver(
        object : NetStateReceiver() {
            override fun onNetworkStateChanged(tempStatus: NetworkState) {
                networkStateFlow.value = tempStatus
            }
        },
        IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
    )
}