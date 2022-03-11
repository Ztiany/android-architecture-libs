package com.android.base.architecture.ui.list;

import com.android.base.foundation.adapter.DataManager;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2018-04-27 14:04
 */
@SuppressWarnings("rawtypes")
public class AutoPaging extends Paging {

    private final DataManager mDataManager;
    private final ListLayoutHost mRefreshListLayoutHost;

    public AutoPaging(ListLayoutHost refreshListLayoutHost, DataManager dataManager) {
        mRefreshListLayoutHost = refreshListLayoutHost;
        mDataManager = dataManager;
    }

    @Override
    public int getCurrentPage() {
        return calcPageNumber(mDataManager.getDataSize());
    }

    @Override
    public int getLoadingPage() {
        if (mRefreshListLayoutHost.isRefreshing()) {
            return getPageStart();
        } else {
            return getCurrentPage() + 1;
        }
    }

    @Override
    public int getItemCount() {
        return mDataManager.getDataSize();
    }

}
