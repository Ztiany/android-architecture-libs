package com.android.base.foundation.fragment

import androidx.annotation.UiThread
import com.github.dmstocking.optional.java.util.function.Predicate

@UiThread
interface FragmentDelegateOwner {

    fun addDelegate(fragmentDelegate: FragmentDelegate<*>)

    fun removeDelegate(fragmentDelegate: FragmentDelegate<*>): Boolean

    fun findDelegate(predicate: (FragmentDelegate<*>)->Boolean): FragmentDelegate<*>?

}