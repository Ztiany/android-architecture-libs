package com.android.base.adapter

import android.view.View

@Suppress("UNCHECKED_CAST")
fun <T> newOnItemClickListenerLazy(listenerProvider: () -> ((item: T) -> Unit)): View.OnClickListener {
    return View.OnClickListener {
        (it.tag as? T)?.let { item ->
            listenerProvider()(item)
        }
    }
}

@Suppress("UNCHECKED_CAST")
fun <T> newOnItemClickListener(listener: (item: T) -> Unit): View.OnClickListener {
    return View.OnClickListener {
        (it.tag as? T)?.let { item ->
            listener(item)
        }
    }
}