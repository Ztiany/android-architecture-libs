package com.android.sdk.net.coroutines

/**
 *TODO: verify if this method is necessary.
 *
 * @param requestBlock 请求的整体逻辑
 * @return Flow<T>
 */
protected fun <T> request(requestBlock: suspend FlowCollector<T>.() -> Unit): Flow<T> {
    return flow(block = requestBlock).flowOn(Dispatchers.IO)
}