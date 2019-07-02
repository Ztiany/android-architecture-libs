package com.android.base.app.ui;

public interface RefreshStateLayout extends StateLayout{

    void autoRefresh();

    void refreshCompleted();

    boolean isRefreshing();

}
