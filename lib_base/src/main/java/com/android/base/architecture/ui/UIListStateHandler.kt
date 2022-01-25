package com.android.base.architecture.ui

import com.android.base.AndroidSword
import com.android.base.foundation.data.*

//----------------------------------------------Loading In List And With State----------------------------------------------

fun <T> RefreshListLayout<T>.submitListState(resource: Resource<List<T>>, hasMore: Boolean, onEmpty: (() -> Unit)? = null) {
    when (resource) {
        is Loading -> showLoadingIfEmpty()
        is Error -> handleListError(resource.error)
        is Success<List<T>> -> {
            when (resource) {
                is NoData -> submitListResult(null, hasMore, onEmpty)
                is Data<List<T>> -> submitListResult(resource.value, hasMore, onEmpty)
            }
        }
    }
}

fun <T> RefreshListLayout<T>.handleListState(resource: Resource<List<T>>, hasMore: (() -> Boolean)? = null, onEmpty: (() -> Unit)? = null) {
    when (resource) {
        is Loading -> showLoadingIfEmpty()
        is Error -> handleListError(resource.error)
        is Success<List<T>> -> {
            when (resource) {
                is NoData -> handleListResult(null, hasMore, onEmpty)
                is Data<List<T>> -> handleListResult(resource.value, hasMore, onEmpty)
            }
        }
    }
}

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
