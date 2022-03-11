package com.android.base.architecture.ui.list;

import timber.log.Timber;

/**
 * @author Ztiany
 * @version 1.0
 */
public abstract class Paging {

    private static int PAGE_START = 0;
    private static int PAGE_SIZE = 20;

    private int mPageStart;
    private int mPageSize;

    private Object mPageToken;

    @SuppressWarnings("WeakerAccess,unused")
    public Paging() {
        this(PAGE_START, PAGE_SIZE);
    }

    @SuppressWarnings("WeakerAccess")
    public Paging(int pageStart, int pageSize) {
        mPageStart = pageStart;
        mPageSize = pageSize;
    }

    @SuppressWarnings("unused")
    public void setPageToken(Object pageToken) {
        mPageToken = pageToken;
    }

    @SuppressWarnings("unchecked,unused")
    public <T> T getPageToken() {
        return (T) mPageToken;
    }

    @SuppressWarnings("unused")
    public int getPageSize() {
        return mPageSize;
    }

    @SuppressWarnings("WeakerAccess")
    public int getPageStart() {
        return mPageStart;
    }

    public boolean hasMore(int size) {
        return size >= mPageSize;
    }

    /**
     * 根据page size计算当前的页码
     */
    int calcPageNumber(int dataSize) {
        int pageNumber;
        int pageSize = mPageSize;
        int pageStart = mPageStart;
        if (pageStart == 0) {
            pageNumber = (dataSize / pageSize) - 1;
            Timber.d("pageStart=0, dataSize=%d, pageSize=%d, nextPageNumber=%d", dataSize, pageSize, pageNumber);
        } else if (pageStart == 1) {
            pageNumber = (dataSize / pageSize);
            pageNumber = pageNumber + 1;
            Timber.d("pageStart=1, dataSize=%d, pageSize=%d, nextPageNumber=%d", dataSize, pageSize, pageNumber);
        } else {
            throw new RuntimeException("pageStart must be 0 or 1");
        }
        return pageNumber;
    }

    @SuppressWarnings("unused")
    public void changePageSetting(int pageStart, int pageSize) {
        mPageStart = pageStart;
        mPageSize = pageSize;
    }

    public static void setDefaultPageStart(int pageSize) {
        PAGE_START = pageSize;
    }

    public static void setDefaultPageSize(int pageSize) {
        PAGE_SIZE = pageSize;
    }

    public abstract int getCurrentPage();

    @SuppressWarnings("unused")
    public abstract int getLoadingPage();

    @SuppressWarnings("unused")
    public abstract int getItemCount();

}
