package com.android.base.architecture.ui;

public interface RefreshStateLayout extends StateLayout{

    void autoRefresh();

    void refreshCompleted();

    boolean isRefreshing();

}
