package com.android.sdk.net.coroutines

import com.android.sdk.net.core.result.Result

/**
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2020-01-15 11:00
 */
private interface ExampleApi {
    suspend fun loadData(): Result<List<String>>
    suspend fun loadData2(): Result<List<String>?>
}

private suspend fun example(api: ExampleApi) {
    apiCall {
        api.loadData()
    } onSucceeded {

    } onFailed {

    }


}
