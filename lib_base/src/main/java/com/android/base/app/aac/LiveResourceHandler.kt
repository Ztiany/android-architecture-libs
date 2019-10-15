package com.android.base.app.aac

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.android.base.app.ui.LoadingView
import com.android.base.data.Resource
import timber.log.Timber


interface LiveResourceHandler {

    /**处理异常*/
    fun handleError(throwable: Throwable)

}

fun <H, T> H.handleLiveResource(
        liveData: LiveData<Resource<T>>,
        forceLoading: Boolean = true,
        onSuccess: (T?) -> Unit
) where H : LiveResourceHandler, H : LoadingView, H : LifecycleOwner {

    liveData.observe(this, Observer {
        when {
            it.isError -> {
                Timber.d("handleLiveResource -> isError")
                dismissLoadingDialog()
                handleError(it.error())
            }
            it.isLoading -> {
                Timber.d("handleLiveResource -> isLoading")
                showLoadingDialog(!forceLoading)
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
        forceLoading: Boolean = true,
        onEmpty: (() -> Unit)? = null,
        onSuccess: (T) -> Unit
) where H : LiveResourceHandler, H : LoadingView, H : LifecycleOwner {

    liveData.observe(this, Observer {
        when {
            it.isError -> {
                Timber.d("handleLiveResourceWithData -> isError")
                dismissLoadingDialog()
                handleError(it.error())
            }
            it.isLoading -> {
                Timber.d("handleLiveResourceWithData -> isLoading")
                showLoadingDialog(!forceLoading)
            }
            it.isSuccess -> {
                Timber.d("handleLiveResourceWithData -> isSuccess")
                dismissLoadingDialog()
                if (it.hasData()) {
                    onSuccess(it.data())
                } else {
                    onEmpty?.invoke()
                }
            }
        }
    })

}