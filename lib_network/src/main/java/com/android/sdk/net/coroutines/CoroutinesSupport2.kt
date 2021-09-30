package com.android.sdk.net.coroutines

import com.android.sdk.net.NetContext
import com.android.sdk.net.core.exception.ApiErrorException
import com.android.sdk.net.core.exception.NetworkErrorException
import com.android.sdk.net.core.exception.ServerErrorException
import com.android.sdk.net.core.result.ExceptionFactory
import com.android.sdk.net.core.result.Result
import kotlinx.coroutines.delay
import timber.log.Timber

suspend fun <T> executeApiCall(
    requireNonNullData: Boolean = false,
    exceptionFactory: ExceptionFactory? = null,
    call: suspend () -> Result<T>
): T {

    val retryPostAction = retryPostAction()

    var result = realCallDirectly(call, requireNonNullData, exceptionFactory)

    if (result is CallResult.Error && retryPostAction.retry(result.error)) {
        result = realCallDirectly(call, requireNonNullData, exceptionFactory)
    }

    return result
}

suspend fun <T> executeApiCallRetry(
    requireNonNullData: Boolean = false,
    times: Int = RETRY_TIMES,
    delay: Long = RETRY_DELAY,
    exceptionFactory: ExceptionFactory? = null,
    checker: ((Throwable) -> Boolean)? = null,
    call: suspend () -> Result<T>
): T {

    var result = executeApiCall(requireNonNullData, exceptionFactory, call)
    var count = 0

    repeat(times) {

        if (result is CallResult.Error && (checker == null || checker((result as CallResult.Error).error))) {
            delay(delay)
            Timber.d("executeApiCallRetry at ${++count}")
            result = executeApiCall(requireNonNullData, exceptionFactory, call)
        } else {
            return result
        }

    }

    return result
}

private suspend fun <T> realCallDirectly(
    call: suspend () -> Result<T>,
    requireNonNullData: Boolean,
    exceptionFactory: ExceptionFactory? = null
): T {

    try {
        val result = call.invoke()
        val netProvider = NetContext.get().netProviderByResultType(result.javaClass)

        return if (netProvider.errorDataAdapter().isErrorDataStub(result)) {//服务器数据格式错误

            throw ServerErrorException(ServerErrorException.SERVER_DATA_ERROR)

        } else if (!result.isSuccess) { //检测响应码是否正确

            val apiHandler = netProvider.aipHandler()
            apiHandler?.onApiError(result)
            throw createException(result, exceptionFactory)

        } else if (requireNonNullData) { //如果约定必须返回的数据却没有返回数据，则认为是服务器错误。

            result.data ?: throw ServerErrorException(ServerErrorException.UNKNOW_ERROR)

        } else {

            result.data

        }

    } catch (e: Throwable) {

        if (NetContext.get().isConnected) {
            //有连接无数据，服务器错误
            throw ServerErrorException(ServerErrorException.UNKNOW_ERROR)
        } else {
            //无连接网络错误
            throw NetworkErrorException()
        }
    }

}