package com.android.sdk.net.coroutines

import com.android.sdk.net.HostConfigProvider
import com.android.sdk.net.NetContext
import com.android.sdk.net.core.exception.ApiErrorException
import com.android.sdk.net.core.exception.NetworkErrorException
import com.android.sdk.net.core.exception.ServerErrorException
import com.android.sdk.net.core.result.ExceptionFactory
import com.android.sdk.net.core.result.Result
import retrofit2.HttpException

internal const val RETRY_TIMES = 3
internal const val RETRY_DELAY = 3000L

internal fun createApiException(
    result: Result<*>,
    exceptionFactory: ExceptionFactory? = null,
    hostFlag: String,
    netProvider: HostConfigProvider
): Throwable {

    var checkedExceptionFactory = exceptionFactory

    if (checkedExceptionFactory == null) {
        checkedExceptionFactory = netProvider.exceptionFactory()
    }

    if (checkedExceptionFactory != null) {
        val exception = checkedExceptionFactory.create(result, hostFlag)
        if (exception != null) {
            return exception
        }
    }

    return ApiErrorException(result.code, result.message, hostFlag)
}

internal fun retryPostAction(): CoroutinesResultPostProcessor {
    val commonProvider = NetContext.get().commonProvider()
    val coroutinesResultPostProcessor = commonProvider.coroutinesResultPostProcessor()

    if (coroutinesResultPostProcessor != null) {
        return coroutinesResultPostProcessor
    }

    return EMPTY_ENTRY
}

private val EMPTY_ENTRY = object : CoroutinesResultPostProcessor {
    override suspend fun retry(throwable: Throwable): Boolean {
        return false
    }
}

internal fun transformHttpException(throwable: Throwable): Throwable {
    if (throwable is ServerErrorException || throwable is ApiErrorException) {
        return throwable
    }

    val errorBodyHandler = NetContext.get().commonProvider().errorBodyHandler()

    return if (errorBodyHandler != null && throwable is HttpException && !isServerInternalError(throwable)) {
        val errorBody = throwable.response()?.errorBody()
        if (errorBody == null) {
            newNetworkErrorException()
        } else {
            errorBodyHandler.parseErrorBody(errorBody.string()) ?: newNetworkErrorException()
        }
    } else {
        newNetworkErrorException()
    }
}

private fun newNetworkErrorException() = if (NetContext.get().isConnected) {
    //有连接无数据，服务器错误
    ServerErrorException(ServerErrorException.UNKNOW_ERROR)
} else {
    //无连接网络错误
    NetworkErrorException()
}

private fun isServerInternalError(httpException: HttpException): Boolean {
    return httpException.code() >= 500/*http status code*/
}