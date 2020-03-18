@file:JvmName("DispatcherProviders")

package com.android.base.concurrent

import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.rx2.asCoroutineDispatcher


/**
 * Allow providing different types of [CoroutineDispatcher]s.
 *
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2020-03-18 14:06
 */
interface DispatcherProvider {

    fun io(): CoroutineDispatcher

    fun computation(): CoroutineDispatcher

    fun ui(): CoroutineDispatcher

}

private class RxDispatcherProvider : DispatcherProvider {

    private val ioDispatcher by lazy {
        Schedulers.io().asCoroutineDispatcher()
    }

    private val computationDispatcher by lazy {
        Schedulers.computation().asCoroutineDispatcher()
    }

    override fun io(): CoroutineDispatcher {
        return ioDispatcher
    }

    override fun computation(): CoroutineDispatcher {
        return computationDispatcher
    }

    override fun ui(): CoroutineDispatcher {
        return Dispatchers.Main
    }

}

private class DefaultDispatcherProvider : DispatcherProvider {

    override fun io(): CoroutineDispatcher {
        return Dispatchers.IO
    }

    override fun computation(): CoroutineDispatcher {
        return Dispatchers.Default
    }

    override fun ui(): CoroutineDispatcher {
        return Dispatchers.Main
    }

}

fun newRxDispatchProvider(): DispatcherProvider {
    return RxDispatcherProvider()
}

fun newDefaultDispatchProvider(): DispatcherProvider {
    return DefaultDispatcherProvider()
}
