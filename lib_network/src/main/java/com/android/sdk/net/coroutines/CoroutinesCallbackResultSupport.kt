package com.android.sdk.net.coroutines

import com.android.sdk.net.NetContext
import com.android.sdk.net.core.exception.ServerErrorException
import com.android.sdk.net.core.result.ExceptionFactory
import com.android.sdk.net.core.result.Result

internal suspend fun <T> realCall(
    call: suspend () -> Result<T>,
    requireNonNullData: Boolean,
    exceptionFactory: ExceptionFactory? = null
): CallResult<T> {

    val result: Result<T>

    try {
        result = call.invoke()
    } catch (throwable: Throwable) {
        return CallResult.Error(transformHttpException(throwable))
    }

    val netContext = NetContext.get()
    val hostFlag = NetContext.get().hostFlagHolder.getFlag(result.javaClass)
    val netProvider = netContext.hostConfigProvider(hostFlag)

    return if (!result.isSuccess) { //检测响应码是否正确

        val apiHandler = netProvider.aipHandler()
        apiHandler?.onApiError(result)
        CallResult.Error(createApiException(result, exceptionFactory, hostFlag, netProvider))

    } else if (requireNonNullData) { //如果约定必须返回的数据却没有返回数据，则认为是服务器错误。

        val data: T? = result.data
        if (data == null) {
            CallResult.Error(ServerErrorException(ServerErrorException.SERVER_NO_DATA))
        } else {
            CallResult.Success(data)
        }

    } else {
        CallResult.Success(result.data)
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