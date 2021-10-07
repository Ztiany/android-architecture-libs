package com.android.base.architecture.ui

import android.app.Activity
import android.app.Dialog
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.ViewGroup
import androidx.activity.ComponentActivity
import androidx.annotation.IdRes
import androidx.annotation.MainThread
import androidx.annotation.RestrictTo
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import timber.log.Timber
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/*
 * ViewBinding封装【代理方式】，具体参考：
 *
 *  1. https://github.com/pengxurui/DemoHall
 *  2. [Android | ViewBinding 与 Kotlin 委托双剑合璧](https://juejin.cn/post/6960914424865488932)
 */

// -------------------------------------------------------------------------------------
// ViewBindingProperty for Activity
// -------------------------------------------------------------------------------------

@JvmName("viewBindingActivity")
inline fun <V : ViewBinding> ComponentActivity.viewBinding(
    crossinline viewBinder: (View) -> V,
    crossinline viewProvider: (ComponentActivity) -> View = ::findRootView
): ViewBindingProperty<ComponentActivity, V> =
    ActivityViewBindingProperty { activity: ComponentActivity ->
        viewBinder(viewProvider(activity))
    }

@JvmName("viewBindingActivity")
inline fun <V : ViewBinding> ComponentActivity.viewBinding(
    crossinline viewBinder: (View) -> V,
    @IdRes viewBindingRootId: Int
): ViewBindingProperty<ComponentActivity, V> =
    ActivityViewBindingProperty { activity: ComponentActivity ->
        viewBinder(activity.requireViewByIdCompat(viewBindingRootId))
    }

// -------------------------------------------------------------------------------------
// ViewBindingProperty for ViewGroup
// -------------------------------------------------------------------------------------

@JvmName("viewBindingViewGroup")
inline fun <V : ViewBinding> ViewGroup.viewBinding(
    crossinline viewBinder: (View) -> V,
    crossinline viewProvider: (ViewGroup) -> View = { this }
): ViewBindingProperty<ViewGroup, V> = LazyViewBindingProperty { viewGroup: ViewGroup ->
    viewBinder(viewProvider(viewGroup))
}

@JvmName("viewBindingViewGroup")
inline fun <V : ViewBinding> ViewGroup.viewBinding(
    crossinline viewBinder: (View) -> V,
    @IdRes viewBindingRootId: Int
): ViewBindingProperty<ViewGroup, V> = LazyViewBindingProperty { viewGroup: ViewGroup ->
    viewBinder(viewGroup.requireViewByIdCompat(viewBindingRootId))
}

// -------------------------------------------------------------------------------------
// ViewBindingProperty for Dialog
// -------------------------------------------------------------------------------------

@JvmName("viewBindingViewGroup")
inline fun <V : ViewBinding> Dialog.viewBinding(
    crossinline viewBinder: (View) -> V,
    crossinline viewProvider: (Dialog) -> View = ::findRootView
): ViewBindingProperty<Dialog, V> = LazyViewBindingProperty { dialog: Dialog ->
    viewBinder(viewProvider(dialog))
}

@JvmName("viewBindingViewGroup")
inline fun <V : ViewBinding> Dialog.viewBinding(
    crossinline viewBinder: (View) -> V,
    @IdRes viewBindingRootId: Int
): ViewBindingProperty<ViewGroup, V> = LazyViewBindingProperty { viewGroup: ViewGroup ->
    viewBinder(viewGroup.requireViewByIdCompat(viewBindingRootId))
}

// -------------------------------------------------------------------------------------
// ViewBindingProperty for RecyclerView#ViewHolder
// -------------------------------------------------------------------------------------

@JvmName("viewBindingViewHolder")
inline fun <V : ViewBinding> RecyclerView.ViewHolder.viewBinding(
    crossinline viewBinder: (View) -> V,
    crossinline viewProvider: (RecyclerView.ViewHolder) -> View = RecyclerView.ViewHolder::itemView
): ViewBindingProperty<RecyclerView.ViewHolder, V> =
    LazyViewBindingProperty { holder: RecyclerView.ViewHolder ->
        viewBinder(viewProvider(holder))
    }

@JvmName("viewBindingViewHolder")
inline fun <V : ViewBinding> RecyclerView.ViewHolder.viewBinding(
    crossinline viewBinder: (View) -> V,
    @IdRes viewBindingRootId: Int
): ViewBindingProperty<RecyclerView.ViewHolder, V> =
    LazyViewBindingProperty { holder: RecyclerView.ViewHolder ->
        viewBinder(holder.itemView.requireViewByIdCompat(viewBindingRootId))
    }

// -------------------------------------------------------------------------------------
// ViewBindingProperty
// -------------------------------------------------------------------------------------

interface ViewBindingProperty<in R : Any, out V : ViewBinding> : ReadOnlyProperty<R, V> {
    @MainThread
    fun clear()
}

class LazyViewBindingProperty<in R : Any, out V : ViewBinding>(
    private val viewBinder: (R) -> V
) : ViewBindingProperty<R, V> {

    private var viewBinding: V? = null

    @Suppress("UNCHECKED_CAST")
    @MainThread
    override fun getValue(thisRef: R, property: KProperty<*>): V {
        // Already bound
        viewBinding?.let { return it }

        return viewBinder(thisRef).also {
            this.viewBinding = it
        }
    }

    @MainThread
    override fun clear() {
        viewBinding = null
    }
}

abstract class LifecycleViewBindingProperty<in R : Any, out V : ViewBinding>(
    private val viewBinder: (R) -> V
) : ViewBindingProperty<R, V> {

    private var viewBinding: V? = null

    protected abstract fun getLifecycleOwner(thisRef: R): LifecycleOwner

    @MainThread
    override fun getValue(thisRef: R, property: KProperty<*>): V {
        // Already bound
        viewBinding?.let { return it }

        val lifecycle = getLifecycleOwner(thisRef).lifecycle
        val viewBinding = viewBinder(thisRef)
        if (lifecycle.currentState == Lifecycle.State.DESTROYED) {
            Timber.w("Access to viewBinding after Lifecycle is destroyed or hasn't created yet. The instance of viewBinding will be not cached.")
            // We can access to ViewBinding after Fragment.onDestroyView(), but don't save it to prevent memory leak
        } else {
            lifecycle.addObserver(ClearOnDestroyLifecycleObserver(this))
            this.viewBinding = viewBinding
        }
        return viewBinding
    }

    @MainThread
    override fun clear() {
        viewBinding = null
    }

    private class ClearOnDestroyLifecycleObserver(
        private val property: LifecycleViewBindingProperty<*, *>
    ) : DefaultLifecycleObserver {

        private companion object {
            private val mainHandler = Handler(Looper.getMainLooper())
        }

        override fun onDestroy(owner: LifecycleOwner) {
            super.onDestroy(owner)
            mainHandler.post { property.clear() }
        }
    }

}

@RestrictTo(RestrictTo.Scope.LIBRARY)
class ActivityViewBindingProperty<in A : ComponentActivity, out V : ViewBinding>(
    viewBinder: (A) -> V
) : LifecycleViewBindingProperty<A, V>(viewBinder) {

    override fun getLifecycleOwner(thisRef: A): LifecycleOwner {
        return thisRef
    }

}

// -------------------------------------------------------------------------------------
// Utils
// -------------------------------------------------------------------------------------

fun <V : View> View.requireViewByIdCompat(@IdRes id: Int): V {
    return ViewCompat.requireViewById(this, id)
}

fun <V : View> Activity.requireViewByIdCompat(@IdRes id: Int): V {
    return ActivityCompat.requireViewById(this, id)
}

/**
 * Utility to find root view for ViewBinding in Activity
 */
fun findRootView(activity: Activity): View {
    val contentView = activity.findViewById<ViewGroup>(android.R.id.content)
    checkNotNull(contentView) { "Activity has no content view" }
    return when (contentView.childCount) {
        1 -> contentView.getChildAt(0)
        0 -> error("Content view has no children. Provide root view explicitly")
        else -> error("More than one child view found in Activity content view")
    }
}

/**
 * Utility to find root view for ViewBinding in Dialog
 */
fun findRootView(dialog: Dialog): View {
    val contentView = dialog.findViewById<ViewGroup>(android.R.id.content)
    checkNotNull(contentView) { "Activity has no content view" }
    return when (contentView.childCount) {
        1 -> contentView.getChildAt(0)
        0 -> error("Content view has no children. Provide root view explicitly")
        else -> error("More than one child view found in Activity content view")
    }
}

fun DialogFragment.getRootView(viewBindingRootId: Int): View {
    val dialog = checkNotNull(dialog) {
        "DialogFragment doesn't have dialog. Use viewBinding delegate after onCreateDialog"
    }

    val window = checkNotNull(dialog.window) { "Fragment's Dialog has no window" }

    return with(window.decorView) {
        if (viewBindingRootId != 0) {
            requireViewByIdCompat(viewBindingRootId)
        } else this
    }

}
