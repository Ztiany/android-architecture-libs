package com.android.sdk.net.provider;

import com.android.sdk.net.exception.ApiErrorException;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2018-11-08 16:01
 */
public interface ErrorMessage {

    /**
     * 网络错误提示消息。
     */
    CharSequence netErrorMessage(Throwable exception);

    /**
     * 服务器返回的数据格式异常消息。
     */
    CharSequence serverDataErrorMessage(Throwable exception);

    /**
     * 服务器错误，比如 500-600 响应码。
     */
    CharSequence serverErrorMessage(Throwable exception);

    /**
     * 客户端请求错误，比如 400-499 响应码
     */
    CharSequence clientRequestErrorMessage(Throwable exception);

    /**
     * API 调用错误
     */
    CharSequence apiErrorMessage(ApiErrorException exception);

    /**
     * 未知错误
     */
    CharSequence unknowErrorMessage(Throwable exception);

}
