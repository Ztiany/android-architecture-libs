package com.android.sdk.net.exception;

/**
 * ApiErrorException 表示当调用接口失败
 *
 * @author Ztiany
 * Date : 2016-10-13 11:39
 */
public class ApiErrorException extends Exception {

    private final int mCode;

    public ApiErrorException(int code, String message) {
        super(message);
        mCode = code;
    }

    public int getCode() {
        return mCode;
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }

}
