package com.android.base.utils.common

import java.util.concurrent.atomic.AtomicBoolean
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2020-03-27 16:04
 */
fun once(initValue: Boolean = false): ReadWriteProperty<Any, Boolean> = OnceDelegate(initValue)

internal class OnceDelegate(initValue: Boolean) : ReadWriteProperty<Any, Boolean> {

    private var value = initValue

    private val called = AtomicBoolean(false)

    override fun getValue(thisRef: Any, property: KProperty<*>): Boolean {
        val returnValue = value
        if (called.compareAndSet(false, true)) {
            value = !returnValue
        }
        return returnValue
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: Boolean) {
        this.value = value
    }

}