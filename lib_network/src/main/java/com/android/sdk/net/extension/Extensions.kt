package com.android.sdk.net.extension

import android.content.Context
import com.android.sdk.net.CommonBuilder
import com.android.sdk.net.HostConfigBuilder
import com.android.sdk.net.NetContext
import com.android.sdk.net.core.service.ServiceFactory

inline fun <reified T> ServiceFactory.create(): T = create(T::class.java)

fun NetContext.init(context: Context, init: CommonBuilder.() -> Unit): NetContext {
    val commonConfig = NetContext.get().newCommonConfig(context)
    init(commonConfig)
    commonConfig.setUp()
    return this
}

fun NetContext.addHostConfig(config: HostConfigBuilder.() -> Unit): NetContext {
    val builder = NetContext.get().newHostBuilder()
    config(builder)
    builder.setUp()
    return this
}

fun NetContext.addHostConfig(flag: String, config: HostConfigBuilder.() -> Unit): NetContext {
    val builder = NetContext.get().newHostBuilder(flag)
    config(builder)
    builder.setUp()
    return this
}