package com.android.base.app.fragment;

import android.content.Context;

import com.android.base.app.ui.LoadingView;

public interface LoadingViewFactory {

    LoadingView createLoadingDelegate(Context context);
    
}