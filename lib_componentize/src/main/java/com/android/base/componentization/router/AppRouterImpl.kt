package com.android.base.componentization.router

import android.app.Application
import android.net.Uri
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.facade.template.IProvider
import com.alibaba.android.arouter.launcher.ARouter

/**
 * @author Ztiany
 * Email: 1169654504@qq.com
 * Date : 2017-11-04 13:45
 */
internal class AppRouterImpl : AppRouter {

    override fun initRouter(app: Application, openDebug: Boolean) {
        ARouter.init(app)
        if (openDebug) {
            ARouter.openLog()
            ARouter.openDebug()
        }
    }

    override fun openDebug() {
        ARouter.openLog()
        ARouter.openDebug()
    }

    override fun build(path: String): IPostcard {
        val build = ARouter.getInstance().build(path)
        return PostcardImpl(build)
    }

    override fun build(path: Uri): IPostcard {
        val build = ARouter.getInstance().build(path)
        return PostcardImpl(build)
    }

    override fun inject(target: Any) {
        ARouter.getInstance().inject(target)
    }

    override fun <T : IProvider> requireService(providerClass: Class<T>): T {
        return ARouter.getInstance().navigation(providerClass)
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : IProvider> requireService(servicePath: String): T {
        return ARouter.getInstance().build(servicePath).navigation() as T
    }

    override fun <T : IProvider> findService(providerClass: Class<T>): T? {
        return ARouter.getInstance().navigation(providerClass)
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : IProvider> findService(servicePath: String): T? {
        return ARouter.getInstance().build(servicePath).navigation() as? T
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T> requireComponent(componentName: String): T {
        return ARouter.getInstance().build(componentName).navigation() as T
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T> findComponent(componentName: String): T? {
        return ARouter.getInstance().build(componentName).navigation() as? T
    }

}