package com.android.sdk.net.core;

public interface DataExtractor<S, T> {

    S getDataFromHttpResult(Result<T> rResult);

}
