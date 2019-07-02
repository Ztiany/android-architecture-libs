package com.android.sdk.net.errorhandler;

import android.text.TextUtils;

import com.android.sdk.net.NetContext;
import com.android.sdk.net.exception.ApiErrorException;
import com.android.sdk.net.exception.NetworkErrorException;
import com.android.sdk.net.exception.ServerErrorException;
import com.android.sdk.net.provider.ErrorMessage;

import java.io.IOException;
import java.net.ConnectException;

import retrofit2.HttpException;
import timber.log.Timber;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2018-11-08 16:11
 */
public class ErrorMessageFactory {

    public static CharSequence createMessage(Throwable exception) {
        ErrorMessage mErrorMessage = NetContext.get().netProvider().errorMessage();
        Timber.d("createMessage with：" + exception);

        CharSequence message = null;
        //SocketTimeoutException android NetworkErrorException extends IOException
        //1：网络连接错误处理
        if (exception instanceof ConnectException ||
                exception instanceof IOException
                || exception instanceof NetworkErrorException) {
            message = mErrorMessage.netErrorMessage(exception);
        }
        //2：服务器错误处理
        else if (exception instanceof ServerErrorException) {
            int errorType = ((ServerErrorException) exception).getErrorType();
            if (errorType == ServerErrorException.SERVER_DATA_ERROR) {
                message = mErrorMessage.serverDataErrorMessage(exception);
            } else if (errorType == ServerErrorException.UNKNOW_ERROR) {
                message = mErrorMessage.serverErrorMessage(exception);
            }
        }
        //3：响应码非200处理
        else if (exception instanceof HttpException) {
            int code = ((HttpException) exception).code();
            if (code >= 500/*http 500 表示服务器错误*/) {
                message = mErrorMessage.serverErrorMessage(exception);
            } else if (code >= 400/*http 400 表示客户端请求出错*/) {
                message = mErrorMessage.clientRequestErrorMessage(exception);
            }
        } else {
            //4：api 错误处理
            if (exception instanceof ApiErrorException) {
                message = exception.getMessage();
                if (TextUtils.isEmpty(message)) {
                    message = mErrorMessage.apiErrorMessage((ApiErrorException) exception);
                }
            } else {
                throw new RuntimeException(exception);
            }
        }

        if (isEmpty(message)) {
            message = mErrorMessage.unknowErrorMessage(exception);
        }

        return message;
    }

    private static boolean isEmpty(CharSequence str) {
        return str == null || str.toString().trim().length() == 0;
    }

}
