package com.android.base.adapter

import android.view.View

fun <T> newOnItemClickListenerLazy(listenerProvider: () -> ((item: T) -> Unit)): View.OnClickListener {
    return View.OnClickListener {
        (it.tag as? T)?.let { item ->
            listenerProvider()(item)
        }
    }
}

fun <T> newOnItemClickListener(listener: (item: T) -> Unit): View.OnClickListener {
    return View.OnClickListener {
        (it.tag as? T)?.let { item ->
            listener(item)
        }
    }
}