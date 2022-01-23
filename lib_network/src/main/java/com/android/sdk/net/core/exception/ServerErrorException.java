package com.android.sdk.net.core.exception;

import androidx.annotation.NonNull;

/**
 * 表示服务器异常
 *
 * @author Ztiany
 * Date :   2016-05-06 17:23
 */
public class ServerErrorException extends RuntimeException {

    private final int mErrorType;

    public static final int UNKNOW_ERROR = 1;
    public static final int SERVER_DATA_ERROR = 2;
    public static final int SERVER_NO_DATA = 3;

    /**
     * @param errorType {@link #UNKNOW_ERROR},{@link #SERVER_DATA_ERROR}
     */
    public ServerErrorException(int errorType) {
        mErrorType = errorType;
    }

    public int getErrorType() {
        return mErrorType;
    }

    @NonNull
    @Override
    public String toString() {
        String string = super.toString();
        return string + (mErrorType == UNKNOW_ERROR ? " 未知错误 " : " Json 格式错误 ");
    }

}
