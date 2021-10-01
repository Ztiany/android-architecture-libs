package com.android.base.architecture.ui;

/**
 * <br/>   对下拉刷新的抽象
 * <br/>   Email: 1169654504@qq.com
 *
 * @author Ztiany
 * @version 1.0
 */
public interface RefreshView {

    void autoRefresh();

    void refreshCompleted();

    void setRefreshHandler(RefreshHandler refreshHandler);

    boolean isRefreshing();

    void setRefreshEnable(boolean enable);

    abstract class RefreshHandler {

        public boolean canRefresh() {
            return true;
        }

        public abstract void onRefresh();

    }

}