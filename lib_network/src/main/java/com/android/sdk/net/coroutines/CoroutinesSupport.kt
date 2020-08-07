package com.android.sdk.net.coroutines

import com.android.sdk.net.core.result.Result
import com.android.sdk.net.NetContext
import com.android.sdk.net.core.result.ExceptionFactory
import com.android.sdk.net.core.exception.ApiErrorException
import com.android.sdk.net.core.exception.NetworkErrorException
import com.android.sdk.net.core.exception.ServerErrorException
import com.android.sdk.net.core.provider.CoroutinesRetryer
import kotlinx.coroutines.delay

suspend fun <T> apiCall(
        call: suspend () -> Result<T>,
        requireNonNullData: Boolean = false,
        exceptionFactory: ExceptionFactory? = null
): CallResult<T> {

    val retryPostAction = retryPostAction()

    var result = realCall(call, requireNonNullData, exceptionFactory)

    if (result is CallResult.Error && retryPostAction.retry(result.error)) {
        result = realCall(call, requireNonNullData, exceptionFactory)
    }

    return result
}

suspend fun <T> apiCall(
        call: suspend () -> Result<T>,
        requireNonNullData: Boolean = false,
        times: Int = 3,
        delay: Long = 1000,
        exceptionFactory: ExceptionFactory? = null,
        checker: ((Throwable) -> Boolean)? = null): CallResult<T> {

    var result = apiCall(call, requireNonNullData, exceptionFactory)

    repeat(times) {

        if (result is CallResult.Error && (checker == null || checker((result as CallResult.Error).error))) {
            delay(delay)
            result = apiCall(call, requireNonNullData, exceptionFactory)
        } else {
            return result
        }

    }

    return result
}

private suspend fun <T> realCall(call: suspend () -> Result<T>, requireNonNullData: Boolean, exceptionFactory: ExceptionFactory? = null): CallResult<T> {
    try {
        val result = call.invoke()

        return if (NetContext.get().netProvider().errorDataAdapter().isErrorDataStub(result)) {//服务器数据格式错误
            CallResult.Error(ServerErrorException(ServerErrorException.SERVER_DATA_ERROR))
        } else if (!result.isSuccess) { //检测响应码是否正确
            val apiHandler = NetContext.get().netProvider().aipHandler()
            apiHandler?.onApiError(result)
            CallResult.Error(createException(result, exceptionFactory))
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
        return if (NetContext.get().connected()) {
            //有连接无数据，服务器错误
            CallResult.Error(ServerErrorException(ServerErrorException.UNKNOW_ERROR))
        } else {
            //无连接网络错误
            CallResult.Error(NetworkErrorException())
        }
    }

}

suspend fun <T> apiCallDirectly(
        call: suspend () -> Result<T>,
        requireNonNullData: Boolean = false,
        exceptionFactory: ExceptionFactory? = null
): T {

    val retryPostAction = retryPostAction()

    var result = realCallDirectly(call, requireNonNullData, exceptionFactory)

    if (result is CallResult.Error && retryPostAction.retry(result.error)) {
        result = realCallDirectly(call, requireNonNullData, exceptionFactory)
    }

    return result
}

suspend fun <T> apiCallDirectly(
        call: suspend () -> Result<T>,
        requireNonNullData: Boolean = false,
        times: Int = 3,
        delay: Long = 1000,
        exceptionFactory: ExceptionFactory? = null,
        checker: ((Throwable) -> Boolean)? = null): T {

    var result = apiCallDirectly(call, requireNonNullData, exceptionFactory)

    repeat(times) {

        if (result is CallResult.Error && (checker == null || checker((result as CallResult.Error).error))) {
            delay(delay)
            result = apiCallDirectly(call, requireNonNullData, exceptionFactory)
        } else {
            return result
        }

    }

    return result
}

private suspend fun <T> realCallDirectly(call: suspend () -> Result<T>, requireNonNullData: Boolean, exceptionFactory: ExceptionFactory? = null): T {
    try {
        val result = call.invoke()

        return if (NetContext.get().netProvider().errorDataAdapter().isErrorDataStub(result)) {//服务器数据格式错误
            throw ServerErrorException(ServerErrorException.SERVER_DATA_ERROR)
        } else if (!result.isSuccess) { //检测响应码是否正确
            val apiHandler = NetContext.get().netProvider().aipHandler()
            apiHandler?.onApiError(result)
            throw createException(result, exceptionFactory)
        } else if (requireNonNullData) { //如果约定必须返回的数据却没有返回数据，则认为是服务器错误。
            result.data ?: throw ServerErrorException(ServerErrorException.UNKNOW_ERROR)
        } else {
            result.data
        }

    } catch (e: Throwable) {
        if (NetContext.get().connected()) {
            //有连接无数据，服务器错误
            throw ServerErrorException(ServerErrorException.UNKNOW_ERROR)
        } else {
            //无连接网络错误
            throw NetworkErrorException()
        }
    }

}

private fun createException(result: Result<*>, exceptionFactory: ExceptionFactory? = null): Throwable {
    if (exceptionFactory != null) {
        val exception = exceptionFactory.create(result)
        if (exception != null) {
            return exception
        }
    }
    return ApiErrorException(result.code, result.message)
}

private fun retryPostAction(): CoroutinesRetryer {
    val coroutinesRetryer = NetContext.get().netProvider().coroutinesRetryer()
    if (coroutinesRetryer != null) {
        return coroutinesRetryer
    }
    return object : CoroutinesRetryer {
        override suspend fun retry(throwable: Throwable): Boolean {
            return false
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

inline infix fun <T> CallResult<T>.ifSuccessful(onSuccess: (T) -> Unit): CallResult<T> {
    if (this is CallResult.Success) {
        onSuccess(this.data)
    }
    return this
}

inline infix fun <T> CallResult<T>.ifFailed(onFailed: (Throwable) -> Unit) {
    if (this is CallResult.Error) {
        onFailed(this.error)
    }
}