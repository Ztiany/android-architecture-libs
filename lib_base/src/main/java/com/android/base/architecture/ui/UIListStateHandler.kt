package com.android.base.architecture.ui

import com.android.base.AndroidSword
import com.android.base.architecture.ui.list.ListLayoutHost
import com.android.base.foundation.data.*

//----------------------------------------------Loading In List And With State----------------------------------------------

fun <T> ListLayoutHost<T>.handleListResource(
    resource: Resource<List<T>>,
    hasMore: (() -> Boolean)? = null,
    onEmpty: (() -> Unit)? = null
) {
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

fun <T> ListLayoutHost<T>.handleListResult(
    list: List<T>?,
    hasMore: (() -> Boolean)? = null,
    onEmpty: (() -> Unit)? = null
) {

    if (isLoadingMore()) {
        if (!list.isNullOrEmpty()) {
            addData(list)
        }
    } else {
        replaceData(list ?: emptyList())
        if (isRefreshEnable && isRefreshing()) {
            refreshCompleted()
        }
    }

    if (isLoadMoreEnable) {
        if (hasMore == null) {
            loadMoreCompleted(list != null && getPager().hasMore(list.size))
        } else {
            loadMoreCompleted(hasMore())
        }
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

fun ListLayoutHost<*>.handleListError(throwable: Throwable) {
    if (isRefreshEnable && isRefreshing()) {
        refreshCompleted()
    }

    if (isLoadMoreEnable && isLoadingMore()) {
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

fun ListLayoutHost<*>.showLoadingIfEmpty() {
    if (isEmpty()) {
        if (isRefreshing()) {
            showBlank()
        } else {
            showLoadingLayout()
        }
    }
}

//----------------------------------------------Fully Submit List And With State----------------------------------------------

fun <T> ListLayoutHost<T>.submitListResource(resource: Resource<List<T>>, hasMore: Boolean, onEmpty: (() -> Unit)? = null) {
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

fun <T> ListLayoutHost<T>.submitListResult(list: List<T>?, hasMore: Boolean, onEmpty: (() -> Unit)? = null) {
    if (isRefreshEnable && isRefreshing()) {
        refreshCompleted()
    }

    replaceData(list ?: emptyList())

    if (isLoadMoreEnable) {
        loadMoreCompleted(hasMore)
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