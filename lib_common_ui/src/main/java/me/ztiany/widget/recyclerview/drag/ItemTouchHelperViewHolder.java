package me.ztiany.widget.recyclerview.drag;

/**
 *RecyclerView 的 ViewHolder 需要实现此接口，当 item 的状态变化时的回调。
 */
public interface ItemTouchHelperViewHolder {

    /**
     * 当 Item被选中时。
     */
    void onItemSelected();

    /**
     * 当 Item 被释放时。
     */
    void onItemClear();

}
