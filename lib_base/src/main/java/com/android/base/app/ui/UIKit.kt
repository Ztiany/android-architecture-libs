@file:JvmName("UIKit")

package com.android.base.app.ui

import com.android.base.app.Sword
import com.android.base.utils.common.isEmpty
import timber.log.Timber

fun <T> RefreshListLayout<T>.handleListResultWithStatus(list: List<T>?, onEmpty: (() -> Unit)? = null) {
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

fun <T> RefreshListLayout<T>.handleListResultWithoutStatus(list: List<T>?, onEmpty: (() -> Unit)? = null) {
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

fun <T> RefreshListLayout<T>.submitListResultWithStatus(list: List<T>?, hasMore: Boolean, onEmpty: (() -> Unit)? = null) {
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

fun <T> RefreshListLayout<T>.submitListResultWithoutStatus(list: List<T>?, hasMore: Boolean, onEmpty: (() -> Unit)? = null) {
    if (isRefreshing) {
        refreshCompleted()
    }

    replaceData(list)
    loadMoreCompleted(hasMore)

    if (onEmpty != null && isEmpty) {
        onEmpty()
    }
}

fun RefreshListLayout<*>.handleListErrorWithStatus(throwable: Throwable) {
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

fun RefreshListLayout<*>.handleListErrorWithoutStatus() {
    if (isRefreshing) {
        refreshCompleted()
    }
    if (isLoadingMore) {
        loadMoreFailed()
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

fun RefreshStateLayout.handleErrorWithStatus(throwable: Throwable) {
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