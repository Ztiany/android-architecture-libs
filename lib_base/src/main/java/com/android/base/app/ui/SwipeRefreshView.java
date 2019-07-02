package com.android.base.app.ui;

import android.support.v4.widget.SwipeRefreshLayout;

class SwipeRefreshView implements RefreshView {

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RefreshHandler mRefreshHandler;

    SwipeRefreshView(SwipeRefreshLayout swipeRefreshLayout) {
        mSwipeRefreshLayout = swipeRefreshLayout;
    }

    /**
     * SwipeRefreshLayout
     */
    @Override
    public void autoRefresh() {
        mSwipeRefreshLayout.setRefreshing(true);
        doRefresh();
    }

    @Override
    public void refreshCompleted() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void setRefreshHandler(final RefreshHandler refreshHandler) {
        mRefreshHandler = refreshHandler;
        mSwipeRefreshLayout.setOnRefreshListener(this::doRefresh);
    }

    private void doRefresh() {
        if (mRefreshHandler.canRefresh()) {
            mRefreshHandler.onRefresh();
        } else {
            mSwipeRefreshLayout.post(() -> mSwipeRefreshLayout.setRefreshing(false));
        }
    }

    @Override
    public boolean isRefreshing() {
        return mSwipeRefreshLayout.isRefreshing();
    }

    @Override
    public void setRefreshEnable(boolean enable) {
        mSwipeRefreshLayout.setEnabled(enable);
    }
}
