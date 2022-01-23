package com.android.sdk.net.core.message;

import android.text.TextUtils;

import androidx.annotation.RestrictTo;

import com.android.sdk.net.NetContext;
import com.android.sdk.net.core.exception.ApiErrorException;
import com.android.sdk.net.core.exception.NetworkErrorException;
import com.android.sdk.net.core.exception.ServerErrorException;
import com.android.sdk.net.core.provider.ErrorMessage;

import java.io.IOException;
import java.util.NoSuchElementException;

import retrofit2.HttpException;
import timber.log.Timber;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2018-11-08 16:11
 */
@RestrictTo(RestrictTo.Scope.LIBRARY)
public class ErrorMessageFactory {

    public static CharSequence createMessage(Throwable exception) {

        ErrorMessage errorMessage = NetContext.get().commonProvider().errorMessage();

        Timber.d("createMessage with：%s", exception.toString());

        CharSequence message = null;
        //SocketTimeoutException, NetworkErrorException extends IOException
        //1：网络连接错误处理
        if (exception instanceof IOException || exception instanceof NetworkErrorException) {
            message = errorMessage.netErrorMessage(exception);
        }

        //2：服务器错误处理
        else if (exception instanceof ServerErrorException) {
            int errorType = ((ServerErrorException) exception).getErrorType();
            if (errorType == ServerErrorException.SERVER_DATA_ERROR) {
                message = errorMessage.serverDataErrorMessage(exception);
            } else if (errorType == ServerErrorException.UNKNOW_ERROR) {
                message = errorMessage.serverErrorMessage(exception);
            } else if (errorType == ServerErrorException.SERVER_NO_DATA) {
                message = errorMessage.serverReturningNullDataErrorMessage(exception);
            }
        }

        //3：响应码非200处理
        else if (exception instanceof HttpException) {
            int code = ((HttpException) exception).code();
            if (code >= 500/*http 500 表示服务器错误*/) {
                message = errorMessage.serverErrorMessage(exception);
            } else if (code >= 400/*http 400 表示客户端请求出错*/) {
                message = errorMessage.clientRequestErrorMessage(exception);
            }
        }

        //4：Api Error
        else if (exception instanceof ApiErrorException) {
            message = exception.getMessage();
            if (TextUtils.isEmpty(message)) {
                message = errorMessage.apiErrorMessage((ApiErrorException) exception);
            }
        }

        //6：RxJava Single
        else if (exception instanceof NoSuchElementException) {
            message = errorMessage.serverErrorMessage(exception);
        }

        //7：Others
        if (isEmpty(message)) {
            message = errorMessage.unknownErrorMessage(exception);
        }

        return message;
    }

    private static boolean isEmpty(CharSequence str) {
        return str == null || str.toString().trim().length() == 0;
    }

}