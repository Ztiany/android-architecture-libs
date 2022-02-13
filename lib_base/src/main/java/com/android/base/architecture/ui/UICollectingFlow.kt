package com.android.base.architecture.ui

import android.app.Activity
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

/**
 * Collect flow in [Activity].
 *
 * Notes: call the method in [Activity.onCreate].
 */
fun <T> LifecycleOwner.collectFlowOnLifecycleRepeat(
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

/**
 *  Fragments should *always* use the `viewLifecycleOwner` to trigger UI updates. However, that’s not the case for `DialogFragment`s which might not have a `View` sometimes. For `DialogFragment`s, you can use the `lifecycleOwner`.
 *
 *  Notes: call the method in [Fragment.onViewCreated].
 *
 *  Refer to [A safer way to collect flows from Android UIs](https://medium.com/androiddevelopers/a-safer-way-to-collect-flows-from-android-uis-23080b1f8bda) for detail.
 */
fun <T> Fragment.collectFlowOnViewLifecycleRepeat(
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

/** Collect flow in [Activity] or [Fragment]. */
fun <T> LifecycleOwner.collectFlowOnLifecycle(
    activeState: Lifecycle.State = Lifecycle.State.STARTED,
    data: Flow<T>,
    onResult: (result: T) -> Unit
) {
    val job = data.onEach {
        onResult(it)
    }.launchIn(lifecycleScope)

    val observer = object : LifecycleEventObserver {
        override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
            if (!source.lifecycle.currentState.isAtLeast(activeState)) {
                job.cancel()
                lifecycle.removeObserver(this)
            }
        }
    }

    lifecycle.addObserver(observer)

    job.invokeOnCompletion {
        lifecycle.removeObserver(observer)
    }
}

/**
 *  Collect flow in [Fragment].
 *
 *  Notes:
 *  1. Call this method after [Fragment.onViewCreated].
 *  2. when  [Fragment.onDestroyView] is called, the collecting ends.
 */
fun <T> Fragment.collectFlowOnViewLifecycle(
    data: Flow<T>,
    onResult: (result: T) -> Unit
) {
    data.onEach {
        onResult(it)
    }.launchIn(viewLifecycleOwner.lifecycleScope)
}