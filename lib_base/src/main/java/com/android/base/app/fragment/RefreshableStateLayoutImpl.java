package com.android.base.app.fragment;

import android.graphics.drawable.Drawable;
import android.view.View;

import com.android.base.app.ui.OnRetryActionListener;
import com.android.base.app.ui.RefreshStateLayout;
import com.android.base.app.ui.RefreshView;
import com.android.base.app.ui.RefreshViewFactory;
import com.android.base.app.ui.StateLayout;
import com.android.base.app.ui.StateLayoutConfig;
import com.android.base.widget.StateProcessor;

import androidx.annotation.DrawableRes;

import static com.android.base.app.ui.CommonId.REFRESH_ID;
import static com.android.base.app.ui.CommonId.STATE_ID;

/**
 * @author Ztiany
 */
final class RefreshableStateLayoutImpl implements RefreshStateLayout, StateLayoutConfig {

    private StateLayout mMultiStateView;
    private RefreshView mRefreshView;
    private RefreshView.RefreshHandler mRefreshHandler;

    static RefreshableStateLayoutImpl init(View layoutView) {
        return new RefreshableStateLayoutImpl(layoutView);
    }

    private RefreshableStateLayoutImpl(View layoutView) {
        setupBaseUiLogic(layoutView);
        setupRefreshLogic(layoutView);
    }

    RefreshView getRefreshView() {
        return mRefreshView;
    }

    void setRefreshHandler(RefreshView.RefreshHandler refreshHandler) {
        mRefreshHandler = refreshHandler;
    }

    void setStateRetryListenerUnchecked(OnRetryActionListener retryActionListener) {
        if (mMultiStateView != null) {
            setStateRetryListener(retryActionListener);
        }
    }

    @SuppressWarnings("all")
    private void setupBaseUiLogic(View layoutView) {
        mMultiStateView = (StateLayout) layoutView.findViewById(STATE_ID);
    }

    private void setupRefreshLogic(View layoutView) {
        View refreshLayout = layoutView.findViewById(REFRESH_ID);
        if (refreshLayout == null) {
            return;
        }
        mRefreshView = RefreshViewFactory.createRefreshView(refreshLayout);
        mRefreshView.setRefreshHandler(new RefreshView.RefreshHandler() {
            @Override
            public boolean canRefresh() {
                return mRefreshHandler.canRefresh();
            }

            @Override
            public void onRefresh() {
                mRefreshHandler.onRefresh();
            }
        });
    }

    @Override
    public final void autoRefresh() {
        if (mRefreshView != null) {
            mRefreshView.autoRefresh();
        }
    }

    @Override
    public void showLoadingLayout() {
        checkMultiStateView().showLoadingLayout();
    }

    @Override
    public void showContentLayout() {
        refreshCompleted();
        checkMultiStateView().showContentLayout();
    }

    @Override
    public void showEmptyLayout() {
        refreshCompleted();
        checkMultiStateView().showEmptyLayout();
    }

    @Override
    public void showErrorLayout() {
        refreshCompleted();
        checkMultiStateView().showErrorLayout();
    }

    @Override
    public void showRequesting() {
        checkMultiStateView().showRequesting();
    }

    @Override
    public void showBlank() {
        checkMultiStateView().showBlank();
    }

    @Override
    public void showNetErrorLayout() {
        refreshCompleted();
        checkMultiStateView().showNetErrorLayout();
    }

    @Override
    public void showServerErrorLayout() {
        refreshCompleted();
        checkMultiStateView().showServerErrorLayout();
    }

    @Override
    public StateLayoutConfig getStateLayoutConfig() {
        checkMultiStateView();
        return this;
    }

    @Override
    public int currentStatus() {
        return mMultiStateView.currentStatus();
    }

    @Override
    public void refreshCompleted() {
        if (mRefreshView != null) {
            mRefreshView.refreshCompleted();
        }
    }

    @Override
    public boolean isRefreshing() {
        if (mRefreshView != null) {
            return mRefreshView.isRefreshing();
        }
        return false;
    }

    @Override
    public StateLayoutConfig setStateMessage(@RetryableState int state, CharSequence message) {
        checkMultiStateView().getStateLayoutConfig().setStateMessage(state, message);
        return this;
    }

    @Override
    public StateLayoutConfig setStateIcon(@RetryableState int state, Drawable drawable) {
        checkMultiStateView().getStateLayoutConfig().setStateIcon(state, drawable);
        return this;
    }

    @Override
    public StateLayoutConfig setStateIcon(@RetryableState int state, @DrawableRes int drawableId) {
        checkMultiStateView().getStateLayoutConfig().setStateIcon(state, drawableId);
        return this;
    }

    @Override
    public StateLayoutConfig setStateAction(@RetryableState int state, CharSequence actionText) {
        checkMultiStateView().getStateLayoutConfig().setStateAction(state, actionText);
        return this;
    }

    @Override
    public StateLayoutConfig setStateRetryListener(OnRetryActionListener retryActionListener) {
        checkMultiStateView().getStateLayoutConfig().setStateRetryListener(retryActionListener);
        return this;
    }

    @Override
    public StateLayoutConfig disableOperationWhenRequesting(boolean disable) {
        checkMultiStateView().getStateLayoutConfig().disableOperationWhenRequesting(disable);
        return this;
    }

    @Override
    public StateProcessor getProcessor() {
        return checkMultiStateView().getStateLayoutConfig().getProcessor();
    }

    private StateLayout checkMultiStateView() {
        if (mMultiStateView == null) {
            throw new IllegalStateException("Calling this function requires defining a view that implements StateLayout in the Layout");
        }
        return mMultiStateView;
    }

}
