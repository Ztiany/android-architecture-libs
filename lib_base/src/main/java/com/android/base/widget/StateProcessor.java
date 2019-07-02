package com.android.base.widget;

import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.view.View;

import com.android.base.app.ui.StateLayoutConfig;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2018-05-17 17:25
 */
interface StateProcessor {

    void onInitialize(SimpleMultiStateView simpleMultiStateView);

    void onParseAttrs(TypedArray typedArray);

    void processStateInflated(@StateLayoutConfig.ViewState int viewState, @NonNull View view);

    StateLayoutConfig getStateLayoutConfigImpl();

}
