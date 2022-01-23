package com.android.sdk.net.core.result

/**
 * 业务数据模型抽象：
 *
 * 1. 如果没有提供默认构造器，则 GSON 使用 Unsafe 的方式创建对象，事实上实现 Result 的 Kotlin Data 类不可能提供默认构造器。
 * 2. T 表示隐式的 Any?，对于实现 Result 的 Kotlin Data 类，生成的 Java 代码在 getData 时不会执行  null 检测，而是在创建具体的
 *      Result 对象时根据实际类型参数来约束参数的是否可 null，然而通过反射却可以“逃避”构造 Result 时的 null 约束检测。
 *
 * @author Ztiany
 * Date : 2018-08-13
 */
interface Result<T> {

    /**
     * 实际返回类型
     */
    val data: T

    /**
     * 响应码
     */
    val code: Int

    /**
     * 相应消息
     */
    val message: String

    /**
     * 请求是否成功
     */
    val isSuccess: Boolean

}