package com.android.base.rx;

public interface RetryChecker {

    boolean verify(Throwable throwable);

}