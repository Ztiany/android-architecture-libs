package com.android.sdk.net.provider;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Retrofit;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2018-11-08 18:00
 */
public interface ErrorDataAdapter {

    Object createErrorDataStub(Type type, Annotation[] annotations, Retrofit retrofit, ResponseBody value);

    boolean isErrorDataStub(Object object);

}
