package com.android.base.app.fragment;

import android.os.Bundle;
import android.view.View;

import com.android.base.app.ui.RefreshStateLayout;
import com.android.base.app.ui.RefreshView;
import com.android.base.app.ui.StateLayoutConfig;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @author Ztiany
 * date :   2016-03-19 23:09
 * email:    1169654504@qq.com
 * @see BaseStateFragment
 */
@SuppressWarnings("unused")
public abstract class BaseStateDialogFragment extends BaseDialogFragment implements RefreshStateLayout {

    private RefreshableStateLayoutImpl mStateLayout;

    protected static final int CONTENT = StateLayoutConfig.CONTENT;
    protected static final int LOADING = StateLayoutConfig.LOADING;
    protected static final int ERROR = StateLayoutConfig.ERROR;
    protected static final int EMPTY = StateLayoutConfig.EMPTY;
    protected static final int NET_ERROR = StateLayoutConfig.NET_ERROR;
    protected static final int SERVER_ERROR = StateLayoutConfig.SERVER_ERROR;

    @Override
    void internalOnViewPrepared(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mStateLayout = RefreshableStateLayoutImpl.init(view);
        mStateLayout.setRefreshHandler(new RefreshView.RefreshHandler() {
            @Override
            public void onRefresh() {
                BaseStateDialogFragment.this.onRefresh();
            }

            @Override
            public boolean canRefresh() {
                return BaseStateDialogFragment.this.canRefresh();
            }
        });

        mStateLayout.setStateRetryListenerUnchecked(this::onRetry);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        refreshCompleted();
    }

    boolean canRefresh() {
        return true;
    }

    final RefreshView getRefreshView() {
        return mStateLayout.getRefreshView();
    }

    protected void onRetry(@StateLayoutConfig.RetryableState int state) {
        if (getRefreshView() != null) {
            if (!getRefreshView().isRefreshing()) {
                autoRefresh();
            }
        } else {
            onRefresh();
        }
    }

    public final void setRefreshEnable(boolean enable) {
        if (getRefreshView() != null) {
            getRefreshView().setRefreshEnable(enable);
        }
    }

    protected void onRefresh() {
    }

    @Override
    public final StateLayoutConfig getStateLayoutConfig() {
        return mStateLayout.getStateLayoutConfig();
    }

    private RefreshStateLayout getStateLayout() {
        return mStateLayout;
    }

    @Override
    public final boolean isRefreshing() {
        return mStateLayout.isRefreshing();
    }

    @Override
    public void refreshCompleted() {
        getStateLayout().refreshCompleted();
    }

    @Override
    public void autoRefresh() {
        getStateLayout().autoRefresh();
    }

    @Override
    public void showContentLayout() {
        getStateLayout().showContentLayout();
    }

    @Override
    public void showLoadingLayout() {
        getStateLayout().showLoadingLayout();
    }

    @Override
    public void showEmptyLayout() {
        getStateLayout().showEmptyLayout();
    }

    @Override
    public void showErrorLayout() {
        getStateLayout().showErrorLayout();
    }

    @Override
    public void showRequesting() {
        getStateLayout().showRequesting();
    }

    @Override
    public void showBlank() {
        getStateLayout().showBlank();
    }

    @Override
    public void showNetErrorLayout() {
        getStateLayout().showNetErrorLayout();
    }

    @Override
    public void showServerErrorLayout() {
        getStateLayout().showServerErrorLayout();
    }

    @Override
    public int currentStatus() {
        return mStateLayout.currentStatus();
    }

}