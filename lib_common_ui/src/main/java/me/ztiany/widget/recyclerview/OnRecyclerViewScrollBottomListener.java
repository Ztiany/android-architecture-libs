package me.ztiany.widget.recyclerview;


import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

/**
 * RecyclerView多布局通用滑动底部监听器
 */
@SuppressWarnings("all")
public abstract class OnRecyclerViewScrollBottomListener extends RecyclerView.OnScrollListener {

    /**
     * layoutManager的类型（枚举）
     */
    private int mLayoutManagerType;

    private static final int LINEAR = 1;
    private static final int GRID = 2;
    private static final int STAGGERED_GRID = 3;

    /**
     * 最后一个的位置
     */
    private int[] mLastPositions;

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();

        if (mLayoutManagerType == 0) {
            if (layoutManager instanceof GridLayoutManager) {
                mLayoutManagerType = GRID;
            } else if (layoutManager instanceof LinearLayoutManager) {
                mLayoutManagerType = LINEAR;
            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                mLayoutManagerType = STAGGERED_GRID;
            } else {
                //do nothing
            }
        }

        //最后一个可见的item的位置
        int lastVisibleItemPosition;
        switch (mLayoutManagerType) {
            case LINEAR:
                lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                break;
            case GRID:
                lastVisibleItemPosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
                break;
            case STAGGERED_GRID:
                StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
                if (mLastPositions == null) {
                    mLastPositions = new int[staggeredGridLayoutManager.getSpanCount()];
                }
                staggeredGridLayoutManager.findLastVisibleItemPositions(mLastPositions);
                lastVisibleItemPosition = findMax(mLastPositions);
                break;
            default: {
                throw new IllegalStateException("un support  layoutManager");
            }
        }

        int visibleItemCount = layoutManager.getChildCount();
        int totalItemCount = layoutManager.getItemCount();

        if ((visibleItemCount > 0 && (lastVisibleItemPosition) >= totalItemCount - 1)) {
            onBottom();
        } else {
            onLeaveBottom();
        }
    }

    private int findMax(int[] lastPositions) {
        int max = lastPositions[0];
        for (int value : lastPositions) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
    }

    protected abstract void onBottom();

    protected abstract void onLeaveBottom();

}