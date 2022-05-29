package com.android.base.foundation.flow

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

/**
 * An event bus implementation by  SharedFlow. Notes: Don't spread a Bus everywhere, restrict a Bus within a module.
 */
abstract class FlowBus {

    private val events = HashMap<Any, MutableSharedFlow<Any>>()

    private val stickyEvents = HashMap<Any, MutableSharedFlow<Any>>()

    fun <T> with(objectKey: Class<T>): MutableSharedFlow<T> {
        synchronized(events) {
            if (!events.containsKey(objectKey)) {
                events[objectKey] = MutableSharedFlow(0, 1, BufferOverflow.DROP_OLDEST)
            }
        }

        return events[objectKey] as MutableSharedFlow<T>
    }

    fun <T> withSticky(objectKey: Class<T>): MutableSharedFlow<T> {
        synchronized(stickyEvents) {
            if (!stickyEvents.containsKey(objectKey)) {
                stickyEvents[objectKey] = MutableSharedFlow(1, 1, BufferOverflow.DROP_OLDEST)
            }
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