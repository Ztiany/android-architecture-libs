package com.android.sdk.net.coroutines

import kotlinx.coroutines.delay


suspend fun <T> apiCall(call: suspend () -> com.android.sdk.net.core.Result<T>): Result<T> {
    val retryPostAction = retryPostAction()

    val result = realCall(call)

    if (result is Result.Error && retryPostAction.invoke(result.exception)) {
        return realCall(call)
    }
    return result
}

private suspend fun <T> realCall(call: suspend () -> com.android.sdk.net.core.Result<T>): Result<T> {
    return try {
        val networkResult = call.invoke()
        handleResult(networkResult)
    } catch (e: Throwable) {
        Result.Error(RuntimeException())
    }
}

fun <T> handleResult(result: com.android.sdk.net.core.Result<T>): Result<T> {
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
    data class Error(val exception: Exception) : Result<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$exception]"
        }
    }
}
