package com.android.sdk.net.coroutines

import com.android.sdk.net.NetContext
import com.android.sdk.net.core.exception.NetworkErrorException
import com.android.sdk.net.core.exception.ServerErrorException
import com.android.sdk.net.core.result.ExceptionFactory
import com.android.sdk.net.core.result.Result
import kotlinx.coroutines.delay
import timber.log.Timber

suspend fun <T> apiCall(
    requireNonNullData: Boolean = false,
    exceptionFactory: ExceptionFactory? = null,
    call: suspend () -> Result<T>
): CallResult<T> {

    val retryPostAction = retryPostAction()

    var result = realCall(call, requireNonNullData, exceptionFactory)

    if (result is CallResult.Error && retryPostAction.retry(result.error)) {
        result = realCall(call, requireNonNullData, exceptionFactory)
    }

    return result
}

suspend fun <T> apiCallRetry(
    times: Int = RETRY_TIMES,
    delay: Long = RETRY_DELAY,
    requireNonNullData: Boolean = false,
    exceptionFactory: ExceptionFactory? = null,
    checker: ((Throwable) -> Boolean)? = null,
    call: suspend () -> Result<T>,
): CallResult<T> {

    var result = apiCall(requireNonNullData, exceptionFactory, call)
    var count = 0

    repeat(times) {

        if (result is CallResult.Error && (checker == null || checker((result as CallResult.Error).error))) {
            delay(delay)
            Timber.d("executeApiCallRetry at ${++count}")
            result = apiCall(requireNonNullData, exceptionFactory, call)
        } else {
            return result
        }

    }

    return result
}

private suspend fun <T> realCall(
    call: suspend () -> Result<T>,
    requireNonNullData: Boolean,
    exceptionFactory: ExceptionFactory? = null
): CallResult<T> {

    try {
        val result = call.invoke()
        val netContext = NetContext.get()
        val hostFlag = NetContext.get().flagHolder.getFlag(result.javaClass)
        val netProvider = netContext.netProvider(hostFlag)

        return if (netProvider.errorDataAdapter().isErrorDataStub(result)) {//服务器数据格式错误

            CallResult.Error(ServerErrorException(ServerErrorException.SERVER_DATA_ERROR))

        } else if (!result.isSuccess) { //检测响应码是否正确

            val apiHandler = netProvider.aipHandler()
            apiHandler?.onApiError(result)
            CallResult.Error(createException(result, exceptionFactory, hostFlag, netProvider))

        } else if (requireNonNullData) { //如果约定必须返回的数据却没有返回数据，则认为是服务器错误。

            val data = result.data
            if (data == null) {
                CallResult.Error(ServerErrorException(ServerErrorException.UNKNOW_ERROR))
            } else {
                CallResult.Success(data)
            }

        } else {
            CallResult.Success(result.data)
        }

    } catch (e: Throwable) {
        return if (NetContext.get().isConnected) {
            //有连接无数据，服务器错误
            CallResult.Error(ServerErrorException(ServerErrorException.UNKNOW_ERROR))
        } else {
            //无连接网络错误
            CallResult.Error(NetworkErrorException())
        }
    }

}

sealed class CallResult<out T> {

    class Success<out T>(val data: T) : CallResult<T>()

    class Error(val error: Throwable) : CallResult<Nothing>()

    fun isSuccessful() = this is Success

    fun isFailed() = this is Error

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$error]"
        }
    }

}

inline infix fun <T> CallResult<T>.onSucceeded(onSuccess: (T) -> Unit): CallResult<T> {
    if (this is CallResult.Success) {
        onSuccess(this.data)
    }
    return this
}

inline infix fun <T> CallResult<T>.onFailed(onFailed: (Throwable) -> Unit): CallResult<T> {
    if (this is CallResult.Error) {
        onFailed(this.error)
    }
    return this
}