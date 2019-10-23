@file:JvmName("SchedulerProviders")

package com.android.base.rx

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.rx2.asCoroutineDispatcher


/**
 * Allow providing different types of [Scheduler]s.
 */
interface SchedulerProvider {

    fun computation(): Scheduler

    fun io(): Scheduler

    fun ui(): Scheduler

    fun database(): Scheduler

    fun ioDispatcher(): CoroutineDispatcher

    fun defaultDispatcher(): CoroutineDispatcher

    fun uiDispatcher(): CoroutineDispatcher

}

fun newDefaultSchedulerProvider(): SchedulerProvider {
    return DefaultSchedulerProvider()
}

private class DefaultSchedulerProvider : SchedulerProvider {

    private val ioDispatcher by lazy {
        io().asCoroutineDispatcher()
    }

    private val defaultDispatcher by lazy {
        io().asCoroutineDispatcher()
    }

    override fun ioDispatcher(): CoroutineDispatcher {
        return ioDispatcher
    }

    override fun defaultDispatcher(): CoroutineDispatcher {
        return defaultDispatcher
    }

    override fun uiDispatcher(): CoroutineDispatcher {
        return Dispatchers.Main
    }

    override fun computation(): Scheduler {
        return Schedulers.computation()
    }

    override fun io(): Scheduler {
        return Schedulers.io()
    }

    override fun ui(): Scheduler {
        return AndroidSchedulers.mainThread()
    }

    override fun database(): Scheduler {
        return Schedulers.single()
    }

}