package com.android.base.app.ui

import com.android.base.app.Sword
import timber.log.Timber

fun <T> RefreshStateLayout.handleResultWithStatus(t: T?, onResult: ((T) -> Unit)) {
    if (isRefreshing) {
        refreshCompleted()
    }
    if (t == null || (t is Collection<*> && t.isEmpty()) || (t is Map<*, *> && t.isEmpty())) {
        showEmptyLayout()
    } else {
        onResult.invoke(t)
        showContentLayout()
    }
}

fun RefreshStateLayout.handleErrorWithStatus(throwable: Throwable?) {
    if (throwable == null) {
        Timber.d("processErrorWithStatus called, but throwable is null")
        return
    }
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