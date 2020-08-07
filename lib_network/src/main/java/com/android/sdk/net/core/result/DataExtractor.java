package com.android.sdk.net.core.result;

public interface DataExtractor<S, T> {

    S getDataFromHttpResult(Result<T> rResult);

}
