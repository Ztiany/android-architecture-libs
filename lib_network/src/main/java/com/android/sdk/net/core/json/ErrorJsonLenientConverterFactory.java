package com.android.sdk.net.core.json;


import androidx.annotation.NonNull;

import com.android.sdk.net.NetContext;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;
import timber.log.Timber;

/**
 * json 解析容错处理，<a href="http://blog.piasy.com/2016/09/04/RESTful-Android-Network-Solution-2/">参考</a>
 *
 * @author Ztiany
 * Date : 2018-08-13
 */
public class ErrorJsonLenientConverterFactory extends Converter.Factory {

    private final Converter.Factory mGsonConverterFactory;

    public ErrorJsonLenientConverterFactory(Converter.Factory gsonConverterFactory) {
        mGsonConverterFactory = gsonConverterFactory;
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(
            @NonNull Type type,
            @NonNull Annotation[] parameterAnnotations,
            @NonNull Annotation[] methodAnnotations,
            @NonNull Retrofit retrofit
    ) {
        return mGsonConverterFactory.requestBodyConverter(type, parameterAnnotations, methodAnnotations, retrofit);
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(
            @NonNull Type type,
            @NonNull Annotation[] annotations,
            @NonNull Retrofit retrofit
    ) {

        final Converter<ResponseBody, ?> delegateConverter = mGsonConverterFactory.responseBodyConverter(type, annotations, retrofit);
        assert delegateConverter != null;

        return (Converter<ResponseBody, Object>) value -> {
            try {
                return delegateConverter.convert(value);
            } catch (Exception e/*防止闪退：JsonSyntaxException、IOException or MalformedJsonException*/) {
                Timber.e(e, "Json covert error -->error, type is %s", type);
                //服务器数据格式错误
                return NetContext.get().netProvider().errorDataAdapter().createErrorDataStub(type, annotations, retrofit, value);
            }
        };

    }

}