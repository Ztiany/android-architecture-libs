@file:JvmName("SchedulerProviders")

package com.android.base.concurrent

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Allow providing different types of [Scheduler]s.
 */
interface SchedulerProvider {

    fun computation(): Scheduler

    fun io(): Scheduler

    fun ui(): Scheduler

    fun database(): Scheduler

}

private class DefaultSchedulerProvider : SchedulerProvider {

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

fun newDefaultSchedulerProvider(): SchedulerProvider {
    return DefaultSchedulerProvider()
}
