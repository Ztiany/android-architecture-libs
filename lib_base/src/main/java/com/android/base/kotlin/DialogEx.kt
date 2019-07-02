package com.android.base.kotlin

import android.app.Dialog

/**
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2019-03-04 10:46
 */
fun Dialog.noCancelable(): Dialog {
    this.setCancelable(false)
    return this
}