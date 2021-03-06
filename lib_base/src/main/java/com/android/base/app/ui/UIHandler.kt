@file:JvmName("UIKit")

package com.android.base.app.ui

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import com.android.base.app.Sword
import com.android.base.data.Resource
import androidx.lifecycle.observe as xObserve

//----------------------------------------------Common->Loading->Dialog ----------------------------------------------
fun LoadingView.dismissLoadingDialogDelayed(onDismiss: (() -> Unit)? = null) {
    dismissLoadingDialog(Sword.minimumShowingDialogMills, onDismiss)
}

class ResourceHandlerBuilder<T> {
    var onError: ((Throwable) -> Unit)? = null
    var onLoading: (() -> Unit)? = null
    var onSuccess: ((T?) -> Unit)? = null
    var onSuccessWithData: ((T) -> Unit)? = null
    var onEmpty: (() -> Unit)? = null

    //configuration
    var loadingMessage: CharSequence = ""
    var showLoading: Boolean = true
    var forceLoading: Boolean = true
}

/**
 * 这是一个网络请求状态的通用逻辑封装，一般情况下，网络请求流程为：
 *
 * 1. 发起网络请求，展示 loading 对话框
 * 2. 网络请求正常返回，则展示调用结果。
 * 3. 网络请求发送错误，则提示用户请求错误。
 *
 * [Resource] 表示请求状态，每次状态变更，LiveData 都应该进行通知，该方法订阅 LiveData 并对各种状态进行处理。展示 loading 和对错误进行提示都是自动进行的，通常情况下，只需要提供 [onSuccess] 对正常的网络结果进行处理即可。
 */
fun <H, T> H.handleLiveData(
        /**core: data*/
        liveData: LiveData<Resource<T>>,
        handlerBuilder: ResourceHandlerBuilder<T>.() -> Unit
) where H : LoadingView, H : LifecycleOwner {

    val builder = ResourceHandlerBuilder<T>()

    handlerBuilder(builder)

    liveData.xObserve(this, { state ->
        when {
            state.isError -> {
                dismissLoadingDialogDelayed {
                    val onError = builder.onError
                    if (onError != null) {
                        onError(state.error())
                    } else {
                        showMessage(Sword.errorConvert.convert(state.error()))
                    }
                }
            }
            state.isLoading && builder.showLoading -> {
                if (builder.onLoading == null) {
                    showLoadingDialog(builder.loadingMessage, !builder.forceLoading)
                } else {
                    builder.onLoading?.invoke()
                }
            }
            state.isSuccess -> {
                dismissLoadingDialogDelayed {
                    builder.onSuccess?.invoke(state.get())
                    if (state.hasData()) {
                        builder.onSuccessWithData?.invoke(state.data())
                    } else {
                        builder.onEmpty?.invoke()
                    }
                }
            }//success end
        }
    })
}

//----------------------------------------------Loading In StateView----------------------------------------------
private fun <T> newDefaultChecker(): ((T) -> Boolean)? {
    return { t ->
        (t is CharSequence && (t.isEmpty() || t.isBlank())) || (t is Collection<*> && t.isEmpty()) || (t is Map<*, *> && t.isEmpty())
    }
}

fun <T> RefreshStateLayout.handleResultState(resource: Resource<T>, isEmpty: ((T) -> Boolean)? = newDefaultChecker(), onEmpty: (() -> Unit)? = null, onResult: ((T) -> Unit)) {
    when {
        resource.isLoading -> showLoadingLayout()
        resource.isError -> handleResultError(resource.error())
        resource.isSuccess -> handleResult(resource.get(), isEmpty, onEmpty, onResult)
    }
}

fun <T> RefreshStateLayout.handleResult(t: T?, isEmpty: ((T) -> Boolean)? = newDefaultChecker(), onEmpty: (() -> Unit)? = null, onResult: ((T) -> Unit)) {
    if (isRefreshing) {
        refreshCompleted()
    }
    if (t == null || isEmpty?.invoke(t) == true) {
        if (onEmpty != null) {
            onEmpty()
        } else {
            showEmptyLayout()
        }
    } else {
        showContentLayout()
        onResult.invoke(t)
    }
}

fun RefreshStateLayout.handleResultError(throwable: Throwable) {
    if (isRefreshing) {
        refreshCompleted()
    }
    val errorTypeClassifier = Sword.errorClassifier
    if (errorTypeClassifier != null) {
        when {
            errorTypeClassifier.isNetworkError(throwable) -> showNetErrorLayout()
            errorTypeClassifier.isServerError(throwable) -> showServerErrorLayout()
            else -> showErrorLayout()
        }
    } else {
        showErrorLayout()
    }
}

//----------------------------------------------Loading In List----------------------------------------------
fun <T> RefreshListLayout<T>.handleListResult(list: List<T>?, hasMore: (() -> Boolean)? = null, onEmpty: (() -> Unit)? = null) {
    if (isLoadingMore()) {
        if (!list.isNullOrEmpty()) {
            addData(list)
        }
    } else {
        replaceData(list ?: emptyList())
        refreshCompleted()
    }

    if (hasMore == null) {
        loadMoreCompleted(list != null && getPager().hasMore(list.size))
    } else {
        loadMoreCompleted(hasMore())
    }

    if (isEmpty()) {
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
    if (isLoadingMore()) {
        loadMoreFailed()
    }
    if (isEmpty()) {
        val errorTypeClassifier = Sword.errorClassifier
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

    replaceData(list ?: emptyList())
    loadMoreCompleted(hasMore)

    if (isEmpty()) {
        if (onEmpty == null) {
            showEmptyLayout()
        } else {
            onEmpty()
        }
    } else {
        showContentLayout()
    }
}

fun <T> RefreshListLayout<T>.handleListState(resource: Resource<List<T>>, hasMore: (() -> Boolean)? = null, onEmpty: (() -> Unit)? = null) {
    when {
        resource.isLoading -> showLoadingIfEmpty()
        resource.isError -> handleListError(resource.error())
        resource.isSuccess -> handleListResult(resource.get(), hasMore, onEmpty)
    }
}

fun <T> RefreshListLayout<T>.submitListState(resource: Resource<List<T>>, hasMore: Boolean, onEmpty: (() -> Unit)? = null) {
    when {
        resource.isLoading -> showLoadingIfEmpty()
        resource.isError -> handleListError(resource.error())
        resource.isSuccess -> submitListResult(resource.get(), hasMore, onEmpty)
    }
}

fun RefreshListLayout<*>.showLoadingIfEmpty() {
    if (isEmpty()) {
        if (isRefreshing) {
            showBlank()
        } else {
            showLoadingLayout()
        }
    }
}

//----------------------------------------------Loading In List And Without State----------------------------------------------
fun <T> RefreshListLayout<T>.handleListResultWithoutState(list: List<T>?, hasMore: (() -> Boolean)? = null, onEmpty: (() -> Unit)? = null) {
    if (isLoadingMore()) {
        if (!list.isNullOrEmpty()) {
            addData(list)
        }
    } else {
        replaceData(list ?: emptyList())
        refreshCompleted()
    }

    if (hasMore == null) {
        loadMoreCompleted(list != null && getPager().hasMore(list.size))
    } else {
        loadMoreCompleted(hasMore())
    }

    if (onEmpty != null && isEmpty()) {
        onEmpty()
    }
}

fun RefreshListLayout<*>.handleListErrorWithoutState() {
    if (isRefreshing) {
        refreshCompleted()
    }
    if (isLoadingMore()) {
        loadMoreFailed()
    }
}

fun <T> RefreshListLayout<T>.submitListResultWithoutState(list: List<T>?, hasMore: Boolean, onEmpty: (() -> Unit)? = null) {
    if (isRefreshing) {
        refreshCompleted()
    }
    replaceData(list ?: emptyList())
    loadMoreCompleted(hasMore)
    if (onEmpty != null && isEmpty()) {
        onEmpty()
    }
}
