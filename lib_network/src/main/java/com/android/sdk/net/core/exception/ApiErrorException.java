package com.android.sdk.net.core.exception;

import com.android.sdk.net.NetContext;

/**
 * ApiErrorException 表示当调用接口失败
 *
 * @author Ztiany
 * Date : 2016-10-13 11:39
 */
public class ApiErrorException extends Exception {

    private final int mCode;
    private final String mFlag;

    public ApiErrorException(int code, String message) {
        this(code, message, NetContext.DEFAULT_FLAG);
    }

    public ApiErrorException(int code, String message, String flag) {
        super(message);
        mCode = code;
        mFlag = flag;
    }

    public int getCode() {
        return mCode;
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }

    public String getFlag() {
        return mFlag;
    }

}
