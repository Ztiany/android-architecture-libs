package com.android.sdk.net.provider

/**
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2020-01-14 18:02
 */
interface CoroutinesRetryer {

    suspend fun retry(throwable: Throwable): Boolean

}