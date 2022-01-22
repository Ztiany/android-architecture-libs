package com.android.base.utils.android.network

import android.content.Context
import android.os.Build
import android.text.TextUtils
import timber.log.Timber

/**
 * 获取当前系统是否设置代理，参考 [App 防止 Fiddler 抓包小技巧](https://cloud.tencent.com/developer/article/1445715)。
 */
fun checkWifiProxy(context: Context): Boolean {
    val isIcsOrLater = Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH
    val proxyAddress: String?
    val proxyPort: Int?
    if (isIcsOrLater) {
        proxyAddress = System.getProperty("http.proxyHost")
        val portStr = System.getProperty("http.proxyPort")
        proxyPort = Integer.parseInt(portStr ?: "-1")
    } else {
        proxyAddress = android.net.Proxy.getHost(context)
        proxyPort = android.net.Proxy.getPort(context)
    }
    Timber.d("proxyAddress : ${proxyAddress}, port : $proxyPort")
    return !TextUtils.isEmpty(proxyAddress) && proxyPort != -1
}