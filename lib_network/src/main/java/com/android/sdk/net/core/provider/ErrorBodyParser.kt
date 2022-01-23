package com.android.sdk.net.core.provider

import com.android.sdk.net.NetContext
import com.android.sdk.net.core.exception.ApiErrorException

/**
 * 有些服务器的实现，在 HTTP 响应码为 `[400-500)` 时也会返回响应体，其中可能包含着服务器返回的错误信息，而 Retrofit 在这种情况下
 * 并不会解析响应体。如果在 HTTP 请求出现失败时，还需要解析返回的响应体来提供给 UI 层错误的原因，则需要向 [NetContext] 提供该接口。
 */
interface ErrorBodyParser {

    /** 将服务器返回的请求体解析为  [ApiErrorException]，返回 null 则说明不能解析 */
    fun parseErrorBody(errorBody: String): ApiErrorException?

}