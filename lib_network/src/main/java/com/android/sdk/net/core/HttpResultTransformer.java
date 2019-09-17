package com.android.sdk.net.core;


import com.android.sdk.net.NetContext;
import com.android.sdk.net.exception.ApiErrorException;
import com.android.sdk.net.exception.NetworkErrorException;
import com.android.sdk.net.exception.ServerErrorException;
import com.android.sdk.net.provider.ApiHandler;
import com.android.sdk.net.provider.PostTransformer;

import org.reactivestreams.Publisher;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.SingleTransformer;

public class HttpResultTransformer<Upstream, Downstream, T extends Result<Upstream>> implements ObservableTransformer<T, Downstream>, FlowableTransformer<T, Downstream>, SingleTransformer<T, Downstream> {

    private final boolean mRequireNonNullData;
    private final DataExtractor<Downstream, Upstream> mDataExtractor;
    @Nullable private final ExceptionFactory mExceptionFactory;

    public HttpResultTransformer(boolean requireNonNullData, @NonNull DataExtractor<Downstream, Upstream> dataExtractor, @Nullable ExceptionFactory exceptionFactory) {
        mRequireNonNullData = requireNonNullData;
        mDataExtractor = dataExtractor;
        if (exceptionFactory == null) {
            exceptionFactory = NetContext.get().netProvider().exceptionFactory();
        }
        mExceptionFactory = exceptionFactory;
    }

    @Override
    public Publisher<Downstream> apply(Flowable<T> upstream) {
        Flowable<Downstream> downstreamFlowable = upstream.map(this::processData);
        @SuppressWarnings("unchecked")
        PostTransformer<Downstream> postTransformer = (PostTransformer<Downstream>) NetContext.get().netProvider().postTransformer();
        if (postTransformer != null) {
            return downstreamFlowable.compose(postTransformer);
        } else {
            return downstreamFlowable;
        }
    }

    @Override
    public ObservableSource<Downstream> apply(Observable<T> upstream) {
        Observable<Downstream> downstreamObservable = upstream.map(this::processData);
        @SuppressWarnings("unchecked")
        PostTransformer<Downstream> postTransformer = (PostTransformer<Downstream>) NetContext.get().netProvider().postTransformer();
        if (postTransformer != null) {
            return downstreamObservable.compose(postTransformer);
        } else {
            return downstreamObservable;
        }
    }

    @Override
    public SingleSource<Downstream> apply(Single<T> upstream) {
        Single<Downstream> downstreamSingle = upstream.map(this::processData);
        @SuppressWarnings("unchecked")
        PostTransformer<Downstream> postTransformer = (PostTransformer<Downstream>) NetContext.get().netProvider().postTransformer();
        if (postTransformer != null) {
            return downstreamSingle.compose(postTransformer);
        } else {
            return downstreamSingle;
        }
    }

    private Downstream processData(Result<Upstream> rResult) {
        if (rResult == null) {

            if (NetContext.get().connected()) {
                throwAs(new ServerErrorException(ServerErrorException.UNKNOW_ERROR));//有连接无数据，服务器错误
            } else {
                throw new NetworkErrorException();//无连接网络错误
            }

        } else if (NetContext.get().netProvider().errorDataAdapter().isErrorDataStub(rResult)) {

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