package com.android.base.app.mvvm

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity

inline fun <reified VM : ViewModel> Fragment.getViewModel(factory: ViewModelProvider.Factory? = null): VM {
    return if (factory == null) {
        ViewModelProviders.of(this)[VM::class.java]
    } else {
        ViewModelProviders.of(this, factory)[VM::class.java]
    }
}

inline fun <reified VM : ViewModel> Fragment.getViewModelFromActivity(factory: ViewModelProvider.Factory? = null): VM {
    val activity = this.activity ?: throw IllegalStateException("fragment is not attach to activity")
    return if (factory == null) {
        ViewModelProviders.of(activity)[VM::class.java]
    } else {
        ViewModelProviders.of(activity, factory)[VM::class.java]
    }
}

inline fun <reified VM : ViewModel> FragmentActivity.getViewModel(factory: ViewModelProvider.Factory? = null): VM {
    return if (factory == null) {
        ViewModelProviders.of(this)[VM::class.java]
    } else {
        ViewModelProviders.of(this, factory)[VM::class.java]
    }
}