package com.android.sdk.net.rxjava;

import com.android.sdk.net.core.result.Result;

public interface DataExtractor<S, T> {

    S getDataFromHttpResult(Result<T> rResult);

}
