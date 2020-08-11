package com.android.sdk.net.coroutines

/**
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2020-01-14 18:02
 */
interface CoroutinesResultPostProcessor {

    suspend fun retry(throwable: Throwable): Boolean

}