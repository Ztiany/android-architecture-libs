package com.android.base.componentization.router

import android.app.Application
import android.net.Uri
import com.alibaba.android.arouter.facade.template.IProvider

/**
 * @author Ztiany
 * Email: 1169654504@qq.com
 * Date : 2017-11-04 13:44
 */
interface AppRouter {

    fun initRouter(app: Application, openDebug: Boolean = false)

    fun openDebug()

    fun build(path: String): IPostcard

    fun build(path: Uri): IPostcard

    fun inject(target: Any)

    fun <T : IProvider> requireService(providerClass: Class<T>): T
    fun <T : IProvider> requireService(servicePath: String): T
    fun <T : IProvider> findService(providerClass: Class<T>): T?
    fun <T : IProvider> findService(servicePath: String): T?

    fun <T> requireComponent(componentName: String): T
    fun <T> findComponent(componentName: String): T?

}