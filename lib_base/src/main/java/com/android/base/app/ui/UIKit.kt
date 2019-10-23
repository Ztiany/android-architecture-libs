@file:JvmName("UIKit")

package com.android.base.app.ui

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.android.base.app.Sword
import com.android.base.data.State
import com.android.base.data.StateHandler
import com.android.base.utils.common.isEmpty
import timber.log.Timber

//----------------------------------------------Common->Loading->Dialog ----------------------------------------------
interface UIErrorHandler {
    /**处理异常*/
    fun handleError(throwable: Throwable)

    /**异常描述*/
    fun generateErrorMessage(throwable: Throwable)
}

fun <H, T> H.handleLiveState(
        liveData: LiveData<State<T>>,
        forceLoading: Boolean = true,
        onError: ((Throwable) -> Unit)? = null,
        onSuccess: (T?) -> Unit
) where H : UIErrorHandler, H : LoadingView, H : LifecycleOwner {

    liveData.observe(this, Observer { state ->
        when {
            state.isError -> {
                Timber.d("handleLiveState -> isError")
                dismissLoadingDialog(Sword.get().minimumShowingDialogMills()) {
                    if (onError != null) {
                        onError(state.error())
                    } else {
                        handleError(state.error())
                    }
                }
            }
            state.isLoading -> {
                Timber.d("handleLiveState -> isLoading")
                showLoadingDialog(!forceLoading)
            }
            state.isSuccess -> {
                Timber.d("handleLiveState -> isSuccess")
                dismissLoadingDialog(Sword.get().minimumShowingDialogMills()) {
                    onSuccess(state.get())
                }
            }//success end
        }
    })

}

fun <H, T> H.handleLiveState2(
        liveData: LiveData<State<T>>,
        forceLoading: Boolean = true,
        handler: StateHandler<T>.() -> Unit
) where H : UIErrorHandler, H : LoadingView, H : LifecycleOwner {

    liveData.observe(this, Observer { state ->
        handleState(state, forceLoading, handler)
    })

}

fun <T> LoadingView.handleState(
        state: State<T>,
        forceLoading: Boolean = true,
        handler: StateHandler<T>.() -> Unit
) {

    val stateHandler = StateHandler<T>()
    handler(stateHandler)

    when {
        state.isError -> {
            Timber.d("handleState -> isError")
            dismissLoadingDialog(Sword.get().minimumShowingDialogMills()) {
                stateHandler.onError?.invoke(state.error())
            }
        }
        state.isLoading -> {
            Timber.d("handleState -> isLoading")
            showLoadingDialog(!forceLoading)
        }
        state.isSuccess -> {
            Timber.d("handleState -> isSuccess")
            dismissLoadingDialog(Sword.get().minimumShowingDialogMills()) {
                stateHandler.onSuccess?.invoke(state.get())
                if (state.hasData()) {
                    stateHandler.onSuccessWithData?.invoke(state.data())
                } else {
                    stateHandler.onEmpty?.invoke()
                }
            }
        }//success end
    }
}

//----------------------------------------------Loading In List----------------------------------------------
fun <T> RefreshListLayout<T>.handleListResult(list: List<T>?, onEmpty: (() -> Unit)? = null) {
    if (isLoadingMore) {
        if (!isEmpty(list)) {
            addData(list)
        }
    } else {
        replaceData(list)
        refreshCompleted()
    }

    if (pager != null) {
        loadMoreCompleted(list != null && pager.hasMore(list.size))
    }

    if (isEmpty) {
        if (onEmpty == null) {
            showEmptyLayout()
        } else {
            onEmpty()
        }
    } else {
        showContentLayout()
    }
}

fun RefreshListLayout<*>.handleListError(throwable: Throwable) {
    if (isRefreshing) {
        refreshCompleted()
    }
    if (isLoadingMore) {
        loadMoreFailed()
    }
    if (isEmpty) {
        val errorTypeClassifier = Sword.get().errorClassifier()
        if (errorTypeClassifier != null) {
            when {
                errorTypeClassifier.isNetworkError(throwable) -> showNetErrorLayout()
                errorTypeClassifier.isServerError(throwable) -> showServerErrorLayout()
                else -> showErrorLayout()
            }
        } else {
            showErrorLayout()
        }
    } else {
        showContentLayout()
    }
}

fun <T> RefreshListLayout<T>.submitListResult(list: List<T>?, hasMore: Boolean, onEmpty: (() -> Unit)? = null) {
    if (isRefreshing) {
        refreshCompleted()
    }

    replaceData(list)
    loadMoreCompleted(hasMore)

    if (isEmpty) {
        if (onEmpty == null) {
            showEmptyLayout()
        } else {
            onEmpty()
        }
    } else {
        showContentLayout()
    }
}

fun <T> RefreshListLayout<T>.handleStateList(state: State<List<T>>, onEmpty: (() -> Unit)? = null) {
    when {
        state.isLoading -> {
            showLoadingIfEmpty()
        }
        state.isError -> {
            handleListError(state.error())
        }
        state.isSuccess -> {
            handleListResult(state.get(), onEmpty)
        }
    }
}

fun <T> RefreshListLayout<T>.handleStateFullList(state: State<List<T>>, hasMore: Boolean, onEmpty: (() -> Unit)? = null) {
    when {
        state.isLoading -> {
            showLoadingIfEmpty()
        }
        state.isError -> {
            handleListError(state.error())
        }
        state.isSuccess -> {
            submitListResult(state.get(), hasMore, onEmpty)
        }
    }
}

fun RefreshListLayout<*>.showLoadingIfEmpty() {
    if (isEmpty) {
        if (isRefreshing) {
            showBlank()
        } else {
            showLoadingLayout()
        }
    }
}

//----------------------------------------------Loading In List And Without State----------------------------------------------
fun <T> RefreshListLayout<T>.handleListResultWithoutState(list: List<T>?, onEmpty: (() -> Unit)? = null) {
    if (isLoadingMore) {
        if (!isEmpty(list)) {
            addData(list)
        }
    } else {
        replaceData(list)
        refreshCompleted()
    }

    if (pager != null) {
        loadMoreCompleted(list != null && pager.hasMore(list.size))
    }

    if (onEmpty != null && isEmpty) {
        onEmpty()
    }

}

fun RefreshListLayout<*>.handleListErrorWithoutState() {
    if (isRefreshing) {
        refreshCompleted()
    }
    if (isLoadingMore) {
        loadMoreFailed()
    }
}

fun <T> RefreshListLayout<T>.submitListResultWithoutState(list: List<T>?, hasMore: Boolean, onEmpty: (() -> Unit)? = null) {
    if (isRefreshing) {
        refreshCompleted()
    }

    replaceData(list)
    loadMoreCompleted(hasMore)

    if (onEmpty != null && isEmpty) {
        onEmpty()
    }
}

//----------------------------------------------Loading In StateView----------------------------------------------
fun <T> RefreshStateLayout.handleStateResult(state: State<T>, onEmpty: (() -> Unit)? = null, onResult: ((T) -> Unit)) {
    when {
        state.isLoading -> {
            showLoadingLayout()
        }
        state.isError -> {
            handleResultError(state.error())
        }
        state.isSuccess -> {
            handleResult(state.get(), onEmpty, onResult)
        }
    }
}

fun <T> RefreshStateLayout.handleResult(t: T?, onEmpty: (() -> Unit)? = null, onResult: ((T) -> Unit)) {
    if (isRefreshing) {
        refreshCompleted()
    }
    if (t == null || (t is Collection<*> && t.isEmpty()) || (t is Map<*, *> && t.isEmpty())) {
        if (onEmpty != null) {
            onEmpty()
        } else {
            showEmptyLayout()
        }
    } else {
        onResult.invoke(t)
        showContentLayout()
    }
}

fun RefreshStateLayout.handleResultError(throwable: Throwable) {
    if (isRefreshing) {
        refreshCompleted()
    }
    val errorTypeClassifier = Sword.get().errorClassifier()
    if (errorTypeClassifier != null) {
        when {
            errorTypeClassifier.isNetworkError(throwable) -> {
                Timber.d("isNetworkError showNetErrorLayout")
                showNetErrorLayout()
            }
            errorTypeClassifier.isServerError(throwable) -> {
                Timber.d("isServerError showServerErrorLayout")
                showServerErrorLayout()
            }
            else -> showErrorLayout()
        }
    } else {
        showErrorLayout()
    }
}