package com.android.base.kotlin

import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

fun RecyclerView.verticalLinearLayoutManager(): LinearLayoutManager {
    val linearLayoutManager = LinearLayoutManager(context)
    layoutManager = linearLayoutManager
    return linearLayoutManager
}

fun RecyclerView.horizontalLinearlLayoutManager(): LinearLayoutManager {
    val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    layoutManager = linearLayoutManager
    return linearLayoutManager
}

fun RecyclerView.verticalLinearLayoutManager(span: Int): GridLayoutManager {
    val gridLayoutManager = GridLayoutManager(context, span)
    layoutManager = gridLayoutManager
    return gridLayoutManager
}

fun RecyclerView.horizontalLinearlLayoutManager(span: Int): GridLayoutManager {
    val gridLayoutManager = GridLayoutManager(context, span, GridLayoutManager.HORIZONTAL, false)
    layoutManager = gridLayoutManager
    return gridLayoutManager
}