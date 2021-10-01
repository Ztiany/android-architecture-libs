package com.android.sdk.net.coroutines

import com.android.sdk.net.HostConfigProvider
import com.android.sdk.net.NetContext
import com.android.sdk.net.core.exception.ApiErrorException
import com.android.sdk.net.core.result.ExceptionFactory
import com.android.sdk.net.core.result.Result

internal const val RETRY_TIMES = 3
internal const val RETRY_DELAY = 3000L

internal fun createException(
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