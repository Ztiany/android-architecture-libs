package com.android.base.adapter.recycler;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

/**
 * @see <a href='http://www.cezcb.com/2018/08/24/PagingWithHeader/'>PagingWithHeader</a>
 */
class AdapterDataObserverProxy extends RecyclerView.AdapterDataObserver {

    private RecyclerView.AdapterDataObserver adapterDataObserver;
    private int headerCount;

    AdapterDataObserverProxy(RecyclerView.AdapterDataObserver adapterDataObserver, int headerCount) {
        this.adapterDataObserver = adapterDataObserver;
        this.headerCount = headerCount;
    }

    @Override
    public void onChanged() {
        adapterDataObserver.onChanged();
    }

    @Override
    public void onItemRangeChanged(int positionStart, int itemCount) {
        adapterDataObserver.onItemRangeChanged(positionStart + headerCount, itemCount);
    }

    @Override
    public void onItemRangeChanged(int positionStart, int itemCount, @Nullable Object payload) {
        adapterDataObserver.onItemRangeChanged(positionStart + headerCount, itemCount, payload);
    }

    @Override
    public void onItemRangeInserted(int positionStart, int itemCount) {
        adapterDataObserver.onItemRangeInserted(positionStart + headerCount, itemCount);
    }

    @Override
    public void onItemRangeRemoved(int positionStart, int itemCount) {
        adapterDataObserver.onItemRangeRemoved(positionStart + headerCount, itemCount);
    }

    @Override
    public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
        super.onItemRangeMoved(fromPosition + headerCount, toPosition + headerCount, itemCount);
    }

}