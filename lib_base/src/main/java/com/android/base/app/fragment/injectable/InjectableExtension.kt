package com.android.base.app.fragment.injectable

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.android.base.app.ui.LoadingView
import com.android.base.data.ErrorHandler
import com.android.base.data.Resource
import timber.log.Timber


interface InjectableExtension {

    val viewModelFactory: ViewModelProvider.Factory

    val errorHandler: ErrorHandler

}

fun <H, T> H.handleLiveResource(
        liveData: LiveData<Resource<T>>,
        force: Boolean = true,
        onSuccess: (T?) -> Unit
) where H : InjectableExtension, H : LoadingView, H : LifecycleOwner {

    liveData.observe(this, Observer {
        when {
            it.isError -> {
                Timber.d("handleLiveResource -> isError")
                dismissLoadingDialog()
                errorHandler.handleError(it.error())
            }
            it.isLoading -> {
                Timber.d("handleLiveResource -> isLoading")
                showLoadingDialog(!force)
            }
            it.isSuccess -> {
                Timber.d("handleLiveResource -> isSuccess")
                dismissLoadingDialog()
                onSuccess(it.get())
            }
        }
    })

}

fun <H, T> H.handleLiveResourceWithData(
        liveData: LiveData<Resource<T>>,
        force: Boolean = true,
        onSuccess: (T) -> Unit
) where H : InjectableExtension, H : LoadingView, H : LifecycleOwner {

    liveData.observe(this, Observer {
        when {
            it.isError -> {
                dismissLoadingDialog()
                errorHandler.handleError(it.error())
            }
            it.isLoading -> {
                showLoadingDialog(!force)
            }
            it.isSuccess && it.hasData() -> {
                dismissLoadingDialog()
                onSuccess(it.data())
            }
        }
    })

}