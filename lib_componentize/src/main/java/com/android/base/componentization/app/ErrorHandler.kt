package com.android.base.componentization.app

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2018-11-08 16:02
 */
interface ErrorHandler {

    /** 根据异常，生成一个合理的错误提示 */
    fun generateMessage(throwable: Throwable): CharSequence

    /** 直接处理异常，比如根据 [generateMessage] 方法生成的消息弹出一个 toast。 */
    fun handleError(throwable: Throwable)

    /**处理全局异常，此方法仅由数据层调用，用于统一处理全局异常*/
    fun handleGlobalError(throwable: Throwable)

}
