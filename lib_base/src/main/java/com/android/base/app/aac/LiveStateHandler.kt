package com.android.base.app.aac

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.android.base.app.Sword
import com.android.base.app.ui.LoadingView
import com.android.base.data.State
import timber.log.Timber


interface LiveStateHandler {

    /**处理异常*/
    fun handleError(throwable: Throwable)

}

fun <H, T> H.handleLiveState(
        liveData: LiveData<State<T>>,
        forceLoading: Boolean = true,
        onSuccess: (T?) -> Unit
) where H : LiveStateHandler, H : LoadingView, H : LifecycleOwner {

    liveData.observe(this, Observer {
        when {
            it.isError -> {
                Timber.d("handleLiveState -> isError")
                dismissLoadingDialog()
                handleError(it.error())
            }
            it.isLoading -> {
                Timber.d("handleLiveState -> isLoading")
                showLoadingDialog(!forceLoading)
            }
            it.isSuccess -> {
                Timber.d("handleLiveState -> isSuccess")
                val minimumShowingDialogMills = Sword.get().minimumShowingDialogMills()

                dismissLoadingDialog(minimumShowingDialogMills) {
                    onSuccess(it.get())
                }
            }//success end
        }
    })

}

fun <H, T> H.handleLiveStateWithData(
        liveData: LiveData<State<T>>,
        forceLoading: Boolean = true,
        onEmpty: (() -> Unit)? = null,
        onSuccess: (T) -> Unit
) where H : LiveStateHandler, H : LoadingView, H : LifecycleOwner {

    liveData.observe(this, Observer {
        when {
            it.isError -> {
                Timber.d("handleLiveStateWithData -> isError")
                dismissLoadingDialog()
                handleError(it.error())
            }
            it.isLoading -> {
                Timber.d("handleLiveStateWithData -> isLoading")
                showLoadingDialog(!forceLoading)
            }
            it.isSuccess -> {
                Timber.d("handleLiveStateWithData -> isSuccess")
                val minimumShowingDialogMills = Sword.get().minimumShowingDialogMills()
                dismissLoadingDialog(minimumShowingDialogMills) {
                    if (it.hasData()) {
                        onSuccess(it.data())
                    } else {
                        onEmpty?.invoke()
                    }
                }
            }//success end
        }
    })

}