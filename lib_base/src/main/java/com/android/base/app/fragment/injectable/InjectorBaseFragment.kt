package com.android.base.app.fragment.injectable

import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android.base.app.dagger.Injectable
import com.android.base.app.fragment.BaseFragment
import com.android.base.data.ErrorHandler
import javax.inject.Inject

/**
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2019-01-15 12:57
 */
open class InjectorBaseFragment : BaseFragment(), Injectable, InjectableExtension {

    @Inject override lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject override lateinit var errorHandler: ErrorHandler

    inline fun <reified VM : ViewModel> injectViewModel(): Lazy<VM> {
        return viewModels { viewModelFactory }
    }

    inline fun <reified VM : ViewModel> injectActivityViewModel(): Lazy<VM> {
        return viewModels(
                ownerProducer = {
                    requireActivity()
                }
        ) { viewModelFactory }
    }

}