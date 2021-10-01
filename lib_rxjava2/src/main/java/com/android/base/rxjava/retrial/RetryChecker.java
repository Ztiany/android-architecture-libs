package com.android.base.rxjava.retrial;

public interface RetryChecker {

    boolean verify(Throwable throwable);

}