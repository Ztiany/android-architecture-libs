package com.android.base.architecture.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

/** Collect flow in Activities or Fragments. For fragments, Assume you use them by [FragmentTransaction.hide]  and [FragmentTransaction.show]  operations. */
fun <T> LifecycleOwner.collectFlowOnLifecycle(
    activeState: Lifecycle.State = Lifecycle.State.STARTED,
    data: Flow<T>,
    onResult: (result: T) -> Unit
) {
    lifecycleScope.launch {
        repeatOnLifecycle(activeState) {
            data.onEach {
                onResult(it)
            }.launchIn(this)
        }
    }
}

/** If you operate Fragments by [FragmentTransaction.replace] them, you should use this way to collect flow. refer to [A safer way to collect flows from Android UIs](https://medium.com/androiddevelopers/a-safer-way-to-collect-flows-from-android-uis-23080b1f8bda) for detail.*/
fun <T> Fragment.collectFlowOnViewLifecycle(
    activeState: Lifecycle.State = Lifecycle.State.STARTED,
    data: Flow<T>,
    onResult: (result: T) -> Unit
) {
    viewLifecycleOwner.lifecycleScope.launch {
        repeatOnLifecycle(activeState) {
            data.onEach {
                onResult(it)
            }.launchIn(this)
        }
    }
}