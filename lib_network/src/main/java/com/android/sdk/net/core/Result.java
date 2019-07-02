package com.android.sdk.net.core;

/**
 * 业务数据模型抽象
 *
 * @author Ztiany
 * Date : 2018-08-13
 */
public interface Result<T> {

    /**
     * 实际返回类型
     */
    T getData();

    /**
     * 响应码
     */
    int getCode();

    /**
     * 相应消息
     */
    String getMessage();

    /**
     * 请求是否成功
     */
    boolean isSuccess();

}
