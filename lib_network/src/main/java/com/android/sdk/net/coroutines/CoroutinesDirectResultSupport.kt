package com.android.sdk.net.coroutines

import com.android.sdk.net.NetContext
import com.android.sdk.net.core.exception.ServerErrorException
import com.android.sdk.net.core.result.ExceptionFactory
import com.android.sdk.net.core.result.Result

internal suspend fun <T> realCallDirectly(
    call: suspend () -> Result<T>,
    requireNonNullData: Boolean,
    exceptionFactory: ExceptionFactory? = null
): T {

    val result: Result<T>

    try {
        result = call.invoke()
    } catch (throwable: Throwable) {
        throw transformHttpException(throwable)
    }

    val netContext = NetContext.get()
    val hostFlag = NetContext.get().hostFlagHolder.getFlag(result.javaClass)
    val hostConfigProvider = netContext.hostConfigProvider(hostFlag)

    return if (!result.isSuccess) { //检测响应码是否正确

        val apiHandler = hostConfigProvider.aipHandler()
        apiHandler?.onApiError(result)
        throw createApiException(result, exceptionFactory, hostFlag, hostConfigProvider)

    } else if (requireNonNullData) { //如果约定必须返回的数据却没有返回数据，则认为是服务器错误。

        result.data ?: throw ServerErrorException(ServerErrorException.SERVER_NO_DATA)

    } else {

        result.data
    }

}