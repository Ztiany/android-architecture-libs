package com.android.base.foundation.flow

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import java.util.concurrent.ConcurrentHashMap

/**
 * SharedFlowBus 只会将更新通知给活跃的观察者。
 */
abstract class FlowBus {

    private var events = ConcurrentHashMap<Any, MutableSharedFlow<Any>>()
    private var stickyEvents = ConcurrentHashMap<Any, MutableSharedFlow<Any>>()

    fun <T> with(objectKey: Class<T>): MutableSharedFlow<T> {
        if (!events.containsKey(objectKey)) {
            events[objectKey] = MutableSharedFlow(0, 1, BufferOverflow.DROP_OLDEST)
        }
        return events[objectKey] as MutableSharedFlow<T>
    }

    fun <T> withSticky(objectKey: Class<T>): MutableSharedFlow<T> {
        if (!stickyEvents.containsKey(objectKey)) {
            stickyEvents[objectKey] = MutableSharedFlow(1, 1, BufferOverflow.DROP_OLDEST)
        }
        return stickyEvents[objectKey] as MutableSharedFlow<T>
    }

    fun <T> on(objectKey: Class<T>): Flow<T> {
        return with(objectKey)
    }

    fun <T> onSticky(objectKey: Class<T>): Flow<T> {
        return withSticky(objectKey)
    }

}