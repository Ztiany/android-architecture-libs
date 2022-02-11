package com.android.base.utils.common

import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty


/**
 * Returning ture after the specified [interval] time passed, and repeatedly.
 */
fun timing(interval: Long = 1000L, immediateFirst: Boolean = false): ReadOnlyProperty<Any, Boolean> = TimingDelegate(interval, immediateFirst)

internal class TimingDelegate(
    private val interval: Long,
    immediateFirst: Boolean
) : ReadOnlyProperty<Any, Boolean> {

    private var timing = if (immediateFirst) 0 else System.currentTimeMillis()

    override fun getValue(thisRef: Any, property: KProperty<*>): Boolean {
        var returnValue = false
        val currentTimeMillis = System.currentTimeMillis()
        if (currentTimeMillis - timing >= interval) {
            timing = currentTimeMillis
            returnValue = true
        }
        return returnValue
    }



}