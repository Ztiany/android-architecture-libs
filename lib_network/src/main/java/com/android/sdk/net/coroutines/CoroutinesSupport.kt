package com.android.sdk.net.coroutines

import com.android.sdk.net.NetContext
import com.android.sdk.net.core.ExceptionFactory
import com.android.sdk.net.exception.ApiErrorException
import com.android.sdk.net.exception.NetworkErrorException
import com.android.sdk.net.exception.ServerErrorException
import kotlinx.coroutines.delay


suspend fun <T> apiCall(call: suspend () -> com.android.sdk.net.core.Result<T>): Result<T> {
    val retryPostAction = retryPostAction()

    var result = realCall(call)

    if (result is Result.Error && retryPostAction.invoke(result.error)) {
        result = realCall(call)
    }

    return result
}

private suspend fun <T> realCall(call: suspend () -> com.android.sdk.net.core.Result<T>): Result<T> {
    return try {
        val networkResult = call.invoke()
        handleResult(networkResult)
    } catch (e: Throwable) {
        handleError(e)
    }
}

fun <T> handleError(@Suppress("UNUSED_PARAMETER") ignore: Throwable): Result<T> {
    return if (NetContext.get().connected()) {
        //有连接无数据，服务器错误
        Result.Error(ServerErrorException(ServerErrorException.UNKNOW_ERROR))
    } else {
        //无连接网络错误
        Result.Error(NetworkErrorException())
    }
}

fun <T> handleResult(result: com.android.sdk.net.core.Result<T>, requireNonNullData: Boolean = true, exceptionFactory: ExceptionFactory? = null): Result<T> {
    if (NetContext.get().netProvider().errorDataAdapter().isErrorDataStub(result)) {
        Result.Error(ServerErrorException(ServerErrorException.SERVER_DATA_ERROR)) //服务器数据格式错误
    } else if (!result.isSuccess) { //检测响应码是否正确
        val apiHandler = NetContext.get().netProvider().aipHandler()
        apiHandler?.onApiError(result)
        return Result.Error(createException(result, exceptionFactory))
    }

    if (requireNonNullData) { //如果约定必须返回的数据却没有返回数据，则认为是服务器错误
        if (result.data == null) {
            return Result.Error(ServerErrorException(ServerErrorException.UNKNOW_ERROR))
        }
    }


    return Result.Error(RuntimeException())
}

/*
suspend fun <T> apiCallChecker(call: suspend () -> Result<T>): kotlin.Result<> {
    try {
        val result = call.invoke()
        checkResult()
    } catch (e: Throwable) {

    }
}
*/

private fun createException(rResult: com.android.sdk.net.core.Result<*>, exceptionFactory: ExceptionFactory? = null): Throwable {
    if (exceptionFactory != null) {
        val exception = exceptionFactory.create(rResult)
        if (exception != null) {
            return exception
        }
    }
    return ApiErrorException(rResult.code, rResult.message)
}

private suspend fun <T> retryRequest(
        times: Int = 2,
        delay: Long = 100,
        block: suspend () -> T,
        retry: suspend (Throwable) -> Unit): T {
    repeat(times - 1) {
        try {
            return block()
        } catch (throwable: Throwable) {
            retry(throwable)
        }
        delay(delay)
    }
    return block() // last attempt
}

private fun retryPostAction(): (suspend (Throwable) -> Boolean) {
    return { false }
}

sealed class Result<out T> {

    data class Success<out T : Any>(val data: T) : Result<T>()

    data class Error(val error: Throwable) : Result<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$error]"
        }
    }

}
