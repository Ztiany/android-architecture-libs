package com.android.sdk.net.rxjava;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.sdk.net.NetContext;
import com.android.sdk.net.core.exception.ApiErrorException;
import com.android.sdk.net.core.exception.NetworkErrorException;
import com.android.sdk.net.core.exception.ServerErrorException;
import com.android.sdk.net.core.provider.ApiHandler;
import com.android.sdk.net.core.result.ExceptionFactory;
import com.android.sdk.net.core.result.Result;

import org.reactivestreams.Publisher;

import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.SingleTransformer;

public class HttpResultTransformer<Upstream, Downstream, T extends Result<Upstream>> implements
        ObservableTransformer<T, Downstream>, FlowableTransformer<T, Downstream>, SingleTransformer<T, Downstream> {

    private final boolean mRequireNonNullData;
    private final DataExtractor<Downstream, Upstream> mDataExtractor;
    @Nullable
    private final ExceptionFactory mExceptionFactory;

    public HttpResultTransformer(
            boolean requireNonNullData,
            @NonNull DataExtractor<Downstream, Upstream> dataExtractor,
            @Nullable ExceptionFactory exceptionFactory
    ) {

        mRequireNonNullData = requireNonNullData;
        mDataExtractor = dataExtractor;
        if (exceptionFactory == null) {
            exceptionFactory = NetContext.get().netProvider().exceptionFactory();
        }

        mExceptionFactory = exceptionFactory;
    }

    @Override
    public Publisher<Downstream> apply(Flowable<T> upstream) {

        Flowable<Downstream> downstreamFlowable = upstream
                .switchIfEmpty(Flowable.error(newEmptyError()))
                .map(this::processData);

        @SuppressWarnings("unchecked")
        RxResultPostTransformer<Downstream> rxResultPostTransformer = (RxResultPostTransformer<Downstream>) NetContext.get().netProvider().rxResultPostTransformer();
        if (rxResultPostTransformer != null) {
            return downstreamFlowable.compose(rxResultPostTransformer);
        } else {
            return downstreamFlowable;
        }
    }

    @Override
    public ObservableSource<Downstream> apply(Observable<T> upstream) {

        Observable<Downstream> downstreamObservable = upstream
                .switchIfEmpty(Observable.error(newEmptyError()))
                .map(this::processData);

        @SuppressWarnings("unchecked")
        RxResultPostTransformer<Downstream> rxResultPostTransformer = (RxResultPostTransformer<Downstream>) NetContext.get().netProvider().rxResultPostTransformer();
        if (rxResultPostTransformer != null) {
            return downstreamObservable.compose(rxResultPostTransformer);
        } else {
            return downstreamObservable;
        }
    }

    @Override
    public SingleSource<Downstream> apply(Single<T> upstream) {
        Single<Downstream> downstreamSingle = upstream.map(this::processData);
        @SuppressWarnings("unchecked")
        RxResultPostTransformer<Downstream> rxResultPostTransformer = (RxResultPostTransformer<Downstream>) NetContext.get().netProvider().rxResultPostTransformer();
        if (rxResultPostTransformer != null) {
            return downstreamSingle.compose(rxResultPostTransformer);
        } else {
            return downstreamSingle;
        }
    }

    private Throwable newEmptyError() {
        if (NetContext.get().isConnected()) {
            return new ServerErrorException(ServerErrorException.UNKNOW_ERROR);//有连接无数据，服务器错误
        } else {
            return new NetworkErrorException();//无连接网络错误
        }
    }

    private Downstream processData(Result<Upstream> rResult) {
        if (NetContext.get().netProvider().errorDataAdapter().isErrorDataStub(rResult)) {

            throwAs(new ServerErrorException(ServerErrorException.SERVER_DATA_ERROR));//服务器数据格式错误

        } else if (!rResult.isSuccess()) {//检测响应码是否正确
            ApiHandler apiHandler = NetContext.get().netProvider().aipHandler();
            if (apiHandler != null) {
                apiHandler.onApiError(rResult);
            }
            throwAs(createException(rResult));
        }

        if (mRequireNonNullData) {
            //如果约定必须返回的数据却没有返回数据，则认为是服务器错误
            if (rResult.getData() == null) {
                throwAs(new ServerErrorException(ServerErrorException.UNKNOW_ERROR));
            }
        }

        return mDataExtractor.getDataFromHttpResult(rResult);
    }

    private Throwable createException(@NonNull Result<Upstream> rResult) {
        ExceptionFactory exceptionFactory = mExceptionFactory;
        if (exceptionFactory != null) {
            Exception exception = exceptionFactory.create(rResult);
            if (exception != null) {
                return exception;
            }
        }
        return new ApiErrorException(rResult.getCode(), rResult.getMessage());
    }

    @SuppressWarnings("unchecked")
    private <E extends Throwable> void throwAs(Throwable throwable) throws E {
        throw (E) throwable;
    }

}