package com.android.sdk.net.coroutines

import com.android.sdk.net.core.result.Result
import com.android.sdk.net.coroutines.nonnull.apiCall
import com.android.sdk.net.coroutines.nonnull.executeApiCall
import com.android.sdk.net.coroutines.nullable.apiCallNullable
import com.android.sdk.net.coroutines.nullable.executeApiCallNullable

/**
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2020-01-15 11:00
 */
private interface ExampleApi {
    suspend fun loadData(): Result<List<String>>
    suspend fun loadDataNullable(): Result<List<String>?>
}

private suspend fun example(api: ExampleApi) {
    apiCall {
        api.loadData()
    } onSucceeded {

    } onFailed {

    }

    try {
        val result: List<String> = executeApiCall { api.loadData() }
    } catch (e: Exception) {

    }
}

private suspend fun exampleNullable(api: ExampleApi) {
    apiCallNullable {
        api.loadDataNullable()
    } onSucceeded {

    } onFailed {

    }

    try {
        val result: List<String>? = executeApiCallNullable { api.loadDataNullable() }
    } catch (e: Exception) {

    }
}
