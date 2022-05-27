package com.android.sdk.net.rxjava2;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.sdk.net.HostConfigProvider;
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
        ObservableTransformer<T, Downstream>,
        FlowableTransformer<T, Downstream>,
        SingleTransformer<T, Downstream> {

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
        mExceptionFactory = exceptionFactory;
    }

    @NonNull
    @Override
    public Publisher<Downstream> apply(Flowable<T> upstream) {

        Flowable<Downstream> downstreamFlowable = upstream
                .switchIfEmpty(Flowable.error(newEmptyError()))
                .map(this::processData);

        @SuppressWarnings("unchecked")
        RxResultPostTransformer<Downstream> rxResultPostTransformer =
                (RxResultPostTransformer<Downstream>) NetContext.get().commonProvider().rxResultPostTransformer();

        if (rxResultPostTransformer != null) {
            return downstreamFlowable.compose(rxResultPostTransformer);
        } else {
            return downstreamFlowable;
        }
    }

    @NonNull
    @Override
    public ObservableSource<Downstream> apply(Observable<T> upstream) {

        Observable<Downstream> downstreamObservable = upstream
                .switchIfEmpty(Observable.error(newEmptyError()))
                .map(this::processData);

        @SuppressWarnings("unchecked")
        RxResultPostTransformer<Downstream> rxResultPostTransformer =
                (RxResultPostTransformer<Downstream>) NetContext.get().commonProvider().rxResultPostTransformer();

        if (rxResultPostTransformer != null) {
            return downstreamObservable.compose(rxResultPostTransformer);
        } else {
            return downstreamObservable;
        }
    }

    @NonNull
    @Override
    public SingleSource<Downstream> apply(Single<T> upstream) {

        Single<Downstream> downstreamSingle = upstream.map(this::processData);

        @SuppressWarnings("unchecked")
        RxResultPostTransformer<Downstream> rxResultPostTransformer =
                (RxResultPostTransformer<Downstream>) NetContext.get().commonProvider().rxResultPostTransformer();

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

        NetContext netContext = NetContext.get();
        String flag = netContext.getHostFlagHolder().getFlag(rResult.getClass());
        HostConfigProvider hostConfigProvider = netContext.hostConfigProvider(flag);

        // 参考 ErrorJsonLenientConverterFactory，这个步骤已经不再需要了。
        /*if (hostConfigProvider.errorDataAdapter().isErrorDataStub(rResult)) {

            throwAs(new ServerErrorException(ServerErrorException.SERVER_DATA_ERROR));//服务器数据格式错误

        } else*/
        if (!rResult.isSuccess()) {//检测响应码是否正确

            ApiHandler apiHandler = hostConfigProvider.aipHandler();
            if (apiHandler != null) {
                apiHandler.onApiError(rResult);
            }

            throwAs(createException(rResult, flag, hostConfigProvider));
        }

        if (mRequireNonNullData) {
            //如果约定必须返回的数据却没有返回数据，则认为是服务器错误
            if (rResult.getData() == null) {
                throwAs(new ServerErrorException(ServerErrorException.UNKNOW_ERROR));
            }
        }

        return mDataExtractor.getDataFromHttpResult(rResult);
    }

    private Throwable createException(@NonNull Result<Upstream> rResult, String flag, HostConfigProvider hostConfigProvider) {
        ExceptionFactory exceptionFactory = mExceptionFactory;

        if (exceptionFactory == null) {
            exceptionFactory = hostConfigProvider.exceptionFactory();
        }

        if (exceptionFactory != null) {
            Exception exception = exceptionFactory.create(rResult, flag);
            if (exception != null) {
                return exception;
            }
        }

        return new ApiErrorException(rResult.getCode(), rResult.getMessage(), flag);
    }

    @SuppressWarnings("unchecked")
    private <E extends Throwable> void throwAs(Throwable throwable) throws E {
        throw (E) throwable;
    }

}