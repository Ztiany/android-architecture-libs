package com.android.sdk.common

import android.net.Uri

interface ResultListener {

    fun onTakeSuccess(result: List<Uri>)

    fun onTakeFail() {}

    fun onCancel() {}

}