package com.android.base.app.fragment;

import android.os.Bundle;

import com.android.base.adapter.DataManager;
import com.android.base.app.ui.AutoPageNumber;
import com.android.base.app.ui.PageNumber;
import com.android.base.app.ui.RefreshListLayout;
import com.ztiany.loadmore.adapter.ILoadMore;
import com.ztiany.loadmore.adapter.OnLoadMoreListener;
import com.ztiany.loadmore.adapter.WrapperAdapter;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 通用的RecyclerView列表界面：支持下拉刷新和加载更多。
 *
 * @param <T> 当前列表使用的数据类型
 * @author Ztiany
 * date :   2016-03-19 23:09
 * email:    1169654504@qq.com
 */
public abstract class BaseListFragment<T> extends BaseStateFragment implements RefreshListLayout<T> {

    /**
     * 加载更多
     */
    private ILoadMore mLoadMore;

    /**
     * 列表数据管理
     */
    private DataManager<T> mDataManager;

    /**
     * 分页页码
     */
    private PageNumber mPageNumber;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mDataManager == null) {
            throw new NullPointerException("you need set DataManager");
        }
    }

    protected final void setDataManager(@NonNull DataManager<T> dataManager) {
        mDataManager = dataManager;
    }

    /**
     * Default PageNumber is {@link AutoPageNumber}
     *
     * @param recyclerAdapter adapter
     * @return recycler adapter wrapper
     */
    protected final RecyclerView.Adapter setupLoadMore(@NonNull RecyclerView.Adapter<?> recyclerAdapter) {
        if (mDataManager == null) {
            throw new IllegalStateException("you should setup a DataManager before call this method");
        }
        return setupLoadMore(recyclerAdapter, new AutoPageNumber(this, mDataManager));
    }

    protected final RecyclerView.Adapter setupLoadMore(@NonNull RecyclerView.Adapter<?> recyclerAdapter, @NonNull PageNumber pageNumber) {
        mPageNumber = pageNumber;

        WrapperAdapter wrap = WrapperAdapter.wrap(recyclerAdapter);
        mLoadMore = wrap;
        mLoadMore.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                BaseListFragment.this.onLoadMore();
            }

            @Override
            public boolean canLoadMore() {
                return !isRefreshing();
            }
        });
        return wrap;
    }

    @Override
    protected void onRefresh() {
        onStartLoad();
    }

    protected void onLoadMore() {
        onStartLoad();
    }

    @Override
    final boolean canRefresh() {
        return !isLoadingMore();
    }

    /**
     * call by {@link #onRefresh()} or {@link #onLoadMore()}, you can get current loading type from {@link #isRefreshing()} or {@link #isLoadingMore()}.
     */
    protected void onStartLoad() {
    }

    @Override
    public void replaceData(List<T> data) {
        mDataManager.replaceAll(data);
    }

    @Override
    public void addData(List<T> data) {
        mDataManager.addItems(data);
    }

    protected final ILoadMore getLoadMoreController() {
        return mLoadMore;
    }

    @Override
    public PageNumber getPager() {
        return mPageNumber;
    }

    @Override
    public boolean isEmpty() {
        return mDataManager.isEmpty();
    }

    @Override
    public boolean isLoadingMore() {
        return mLoadMore != null && mLoadMore.isLoadingMore();
    }

    @Override
    public void loadMoreCompleted(boolean hasMore) {
        if (mLoadMore != null) {
            mLoadMore.loadCompleted(hasMore);
        }
    }

    @Override
    public void loadMoreFailed() {
        if (mLoadMore != null) {
            mLoadMore.loadFail();
        }
    }

}