package com.android.base.architecture.ui

import com.android.base.AndroidSword
import com.android.base.foundation.data.*

private fun <T> newDefaultChecker(): ((T) -> Boolean) {
    return { t ->
        (t is CharSequence && (t.isEmpty() || t.isBlank())) || (t is Collection<*> && t.isEmpty()) || (t is Map<*, *> && t.isEmpty())
    }
}

fun <T> RefreshStateLayout.handleResultState(
    resource: Resource<T>,
    ifEmpty: ((T) -> Boolean)? = newDefaultChecker(),
    onEmpty: (() -> Unit)? = null,
    onResult: ((T) -> Unit)
) {
    when (resource) {
        is Loading -> showLoadingLayout()
        is Error -> handleResultError(resource.error)
        is Success<T> -> {
            when (resource) {
                is NoData -> handleResult(null, ifEmpty, onEmpty, onResult)
                is Data<T> -> handleResult(resource.value, ifEmpty, onEmpty, onResult)
            }
        }
    }
}

fun <T> RefreshStateLayout.handleResult(
    t: T?,
    isEmpty: ((T) -> Boolean)? = newDefaultChecker(),
    onEmpty: (() -> Unit)? = null,
    onResult: ((T) -> Unit)
) {
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
    val errorTypeClassifier = AndroidSword.errorClassifier
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
