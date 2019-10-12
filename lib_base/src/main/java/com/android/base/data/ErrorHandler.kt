package com.android.base.data


/**
 * 异常处理器
 *
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2018-11-08 16:02
 */
interface ErrorHandler {

    /** 根据异常，生成一个合理的错误提示 */
    fun createMessage(throwable: Throwable): CharSequence

    /** 直接处理异常，比如根据 [createMessage] 方法生成的消息弹出一个 toast。 */
    fun handleError(throwable: Throwable)

    /** 直接处理异常，自定义消息处理*/
    fun handleError(throwable: Throwable, processor: ((CharSequence) -> Unit))

    /**处理全局异常，此方法仅由数据层调用，用于统一处理全局异常*/
    fun handleGlobalError(throwable: Throwable)

}
