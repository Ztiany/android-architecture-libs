package com.android.base.app.fragment

import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * 懒加载代理
 *
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2019-03-08 12:50
 */
class LazyLoad(private val onPrepared: (() -> Unit)) : ReadOnlyProperty<BaseFragment, LazyDelegate> {

    private lateinit var lazyDelegate: LazyDelegate

    override fun getValue(thisRef: BaseFragment, property: KProperty<*>): LazyDelegate {
        if (!::lazyDelegate.isInitialized) {
            lazyDelegate = LazyDelegate.attach(thisRef) {
                onPrepared.invoke()
            }
        }
        return lazyDelegate
    }

}