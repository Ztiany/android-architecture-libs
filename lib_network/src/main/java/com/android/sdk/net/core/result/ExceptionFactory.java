package com.android.sdk.net.core.result;

/**
 * 某些业务调用所产生的异常不是全局通用的，可以传递此接口用于创建特定的异常。
 */
public interface ExceptionFactory {

    /**
     * 根据{@link Result}创建特定的业务异常
     */
    Exception create(Result<?> result, String hostFlag);

}