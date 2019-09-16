/*
 * Copyright (C) 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.base.app.aac

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class AutoClearedValue<T : Any>(
        lifecycle: Lifecycle,
        private val _event: Lifecycle.Event,
        private val onCleared: (() -> Unit)?
) : ReadWriteProperty<Fragment, T> {

    private var _value: T? = null

    init {
        lifecycle.addObserver(object : DefaultLifecycleObserver {

            override fun onPause(owner: LifecycleOwner) {
                clearValue(Lifecycle.Event.ON_PAUSE)
            }

            override fun onStop(owner: LifecycleOwner) {
                clearValue(Lifecycle.Event.ON_STOP)
            }

            override fun onDestroy(owner: LifecycleOwner) {
                clearValue(Lifecycle.Event.ON_DESTROY)
            }

        })
    }

    override fun getValue(thisRef: Fragment, property: KProperty<*>): T {
        return _value
                ?: throw IllegalStateException("should never call auto-cleared-value get when it might not be available")
    }

    override fun setValue(thisRef: Fragment, property: KProperty<*>, value: T) {
        _value = value
    }

    private fun clearValue(event: Lifecycle.Event) {
        if (_event == event) {
            _value = null
            onCleared?.invoke()
        }
    }

}

fun <T : Any> Fragment.autoCleared(
        event: Lifecycle.Event = Lifecycle.Event.ON_DESTROY,
        onCleared: (() -> Unit)? = null
) = AutoClearedValue<T>(this.lifecycle, event, onCleared)

fun <T : Any> FragmentActivity.autoCleared(
        event: Lifecycle.Event = Lifecycle.Event.ON_DESTROY,
        onCleared: (() -> Unit)? = null
) = AutoClearedValue<T>(this.lifecycle, event, onCleared)