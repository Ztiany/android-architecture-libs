package com.android.sdk.net.coroutines

/**
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2020-01-15 11:00
 */
private interface ExampleApi {
    suspend fun loadData(): com.android.sdk.net.core.Result<List<String>>
    suspend fun loadData2(): com.android.sdk.net.core.Result<List<String>?>
}

private suspend fun example(api: ExampleApi) {

    apiCall({ api.loadData() }).ifSuccessful {

    } ifFailed {

    }

    val callResult = apiCall({ api.loadData2() })
    callResult.ifSuccessful {

    } ifFailed {

    }

    try {
        val loadData = api.loadData()
    } catch (e: Exception) {

    }

    try {
        val loadData = api.loadData2()
    } catch (e: Exception) {

    }

}
