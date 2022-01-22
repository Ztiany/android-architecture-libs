package com.android.base.utils.coroutines

import android.view.View
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

fun View.onClickAutoDisposable(
        context: CoroutineContext = Dispatchers.Main,
        handler: suspend CoroutineScope.(v: View?) -> Unit
) {
    setOnClickListener { v ->
        GlobalScope.launch(context, CoroutineStart.DEFAULT) {
            handler(v)
        }.asAutoDisposable(v)
    }
}