package com.android.sdk.upgrade

/**
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2020-08-13 10:50
 */
class UpgradeException(val type: Int) : Exception() {

    companion object {
        const val NETWORK_ERROR = 1
        const val DIGITS_COMPARING_ERROR = 2
    }

}
