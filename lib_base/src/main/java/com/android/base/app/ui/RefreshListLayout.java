package com.android.base.app.ui;

import java.util.List;

/**
 * 列表视图行为
 *
 * @author Ztiany
 * Email: 1169654504@qq.com
 * Date : 2017-03-29 22:16
 */
public interface RefreshListLayout<T> extends RefreshStateLayout {

    void replaceData(List<T> data);

    void addData(List<T> data);

    PageNumber getPager();

    boolean isEmpty();

    void loadMoreCompleted(boolean hasMore);

    void loadMoreFailed();

    boolean isLoadingMore();

    @Override
    boolean isRefreshing();

}
