package com.android.sdk.push.exception;

/**
 * @author Ztiany
 *         Email: 1169654504@qq.com
 *         Date : 2017-05-18 12:00
 */
public class PushUnConfigException extends RuntimeException {

    private static final String MESSAGE = "you have not deployed the %s id";

    public PushUnConfigException(String message) {
        super(String.format(MESSAGE, message));
    }

}
