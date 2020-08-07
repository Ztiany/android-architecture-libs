package com.android.sdk.mediaselector.common

import android.net.Uri

interface ResultListener {

    fun onTakeSuccess(result: List<Uri>)

    fun onTakeFail() {}

    fun onCancel() {}

}