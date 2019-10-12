package com.android.base.app.fragment.injectable

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android.base.data.ErrorHandler


interface InjectableEx {

    val viewModelFactory: ViewModelProvider.Factory

    val errorHandler: ErrorHandler

}

inline fun <reified VM : ViewModel> InjectableEx.injectViewModel(fragment: Fragment): Lazy<VM> {
    return fragment.viewModels { viewModelFactory }
}

inline fun <reified VM : ViewModel> InjectableEx.injectActivityViewModel(fragment: Fragment): Lazy<VM> {
    return fragment.viewModels(
            ownerProducer = {
                fragment.requireActivity()
            }
    ) { viewModelFactory }
}

