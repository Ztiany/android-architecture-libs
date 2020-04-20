package com.android.base.app.ui;

import com.android.base.foundation.adapter.DataManager;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2018-04-27 14:04
 */
public class AutoPageNumber extends PageNumber {

    private final DataManager mDataManager;
    private final RefreshListLayout mRefreshListLayout;

    public AutoPageNumber(RefreshListLayout refreshListLayout, DataManager dataManager) {
        mRefreshListLayout = refreshListLayout;
        mDataManager = dataManager;
    }

    @Override
    public int getCurrentPage() {
        return calcPageNumber(mDataManager.getDataSize());
    }

    @Override
    public int getLoadingPage() {
        if (mRefreshListLayout.isRefreshing()) {
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
