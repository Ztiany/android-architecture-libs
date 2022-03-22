package com.android.sdk.net.coroutines.flow

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

/**
 *TODO: verify if wrapping calls to flow is necessary.
 *
 * @param requestBlock 请求的整体逻辑
 * @return Flow<T>
 */
fun <T> request(requestBlock: suspend FlowCollector<T>.() -> Unit): Flow<T> {
    return flow(block = requestBlock).flowOn(Dispatchers.IO)
}