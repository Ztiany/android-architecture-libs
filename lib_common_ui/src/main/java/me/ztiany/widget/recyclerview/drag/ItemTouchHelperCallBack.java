package me.ztiany.widget.recyclerview.drag;

import org.jetbrains.annotations.NotNull;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public abstract class ItemTouchHelperCallBack extends ItemTouchHelper.Callback {

    private ItemTouchHelperAdapter mItemTouchHelperAdapter;

    public ItemTouchHelperCallBack(@NotNull ItemTouchHelperAdapter itemTouchHelperAdapter) {
        mItemTouchHelperAdapter = itemTouchHelperAdapter;
    }

    /*
        ItemTouchHelper可以让你轻易得到一个事件的方向。你需要重写 getMovementFlags() 方法来指定可以支持的拖放和滑动的方向。
        使用 helperItemTouchHelper.makeMovementFlags(int, int) 来构造返回的flag。注：上下为拖动（drag），左右为滑动（swipe）。
     */
    @Override
    public abstract int getMovementFlags(@NotNull RecyclerView recyclerView, @NotNull RecyclerView.ViewHolder viewHolder);

    @Override
    public boolean onMove(@NotNull RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        mItemTouchHelperAdapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        mItemTouchHelperAdapter.onItemDismiss(viewHolder.getAdapterPosition());
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        super.onSelectedChanged(viewHolder, actionState);
        //当 Item 的选择状态改变时我们可以在这里通知其改变状态。
        if (viewHolder instanceof ItemTouchHelperViewHolder) {
            if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
                ((ItemTouchHelperViewHolder) viewHolder).onItemSelected();
            }
        }
    }

    @Override
    public void clearView(@NotNull RecyclerView recyclerView, @NotNull RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        if (viewHolder instanceof ItemTouchHelperViewHolder) {
            ((ItemTouchHelperViewHolder) viewHolder).onItemClear();
        }
    }

}