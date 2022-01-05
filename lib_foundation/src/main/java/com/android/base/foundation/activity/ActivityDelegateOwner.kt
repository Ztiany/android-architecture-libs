package com.android.base.foundation.activity

import androidx.annotation.UiThread

@UiThread
interface ActivityDelegateOwner {

    fun addDelegate(activityDelegate: ActivityDelegate<*>)

    fun removeDelegate(activityDelegate: ActivityDelegate<*>): Boolean

    fun findDelegate(predicate: (ActivityDelegate<*>) -> Boolean): ActivityDelegate<*>?

    fun getStatus(): ActivityState

}