package com.android.base.app.ui

import android.content.Context

interface LoadingViewFactory {

    fun createLoadingDelegate(context: Context): LoadingView

}