package com.android.base.widget.recyclerview;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class MarginDecoration extends RecyclerView.ItemDecoration {

    private int mTop;
    private int mLeft;
    private int mRight;
    private int mBottom;

    public MarginDecoration(int left, int top, int right, int bottom) {
        mTop = top;
        mBottom = bottom;
        mRight = right;
        mLeft = left;
    }

    public MarginDecoration(int margin) {
        mTop = margin;
        mBottom = margin;
        mRight = margin;
        mLeft = margin;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.top = mTop;
        outRect.bottom = mBottom;
        outRect.left = mLeft;
        outRect.right = mRight;
    }

}
