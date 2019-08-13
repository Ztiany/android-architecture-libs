package com.android.base.kotlin

import android.app.Dialog

fun Dialog.notCancelable(): Dialog {
    this.setCancelable(false)
    return this
}

fun Dialog.onDismiss(action: () -> Unit): Dialog {
    setOnDismissListener {
        action()
    }
    return this
}