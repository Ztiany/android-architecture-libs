package com.android.sdk.mediaselector.common

import android.content.Intent
import android.os.Bundle

/**
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2020-08-11 11:17
 */
interface ActivityStateHandler {

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)

    fun onSaveInstanceState(outState: Bundle)

    fun onRestoreInstanceState(outState: Bundle?)

}