package com.android.base.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;

import androidx.core.view.ViewCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.base.R;


/**
 * Extends {@link SwipeRefreshLayout} to support non-direct descendant scrolling views.
 * <p>
 * {@link SwipeRefreshLayout} works as expected when a scroll view is a direct child: it triggers
 * the refresh only when the view is on top. This class adds a way (@link #setScrollUpChild} to
 * define which view controls this behavior.
 */
public class ScrollChildSwipeRefreshLayout extends SwipeRefreshLayout {

    private View mScrollUpChild;

    private final int mTargetId;

    private final boolean mRestoreRefreshStatus;

    public ScrollChildSwipeRefreshLayout(Context context) {
        this(context, null);
    }

    public ScrollChildSwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ScrollChildSwipeRefreshLayout);
        mTargetId = typedArray.getResourceId(R.styleable.ScrollChildSwipeRefreshLayout_srl_target_id, 0);
        int colorSchemeArrayId = typedArray.getResourceId(R.styleable.ScrollChildSwipeRefreshLayout_srl_color_scheme, 0);
        mRestoreRefreshStatus = typedArray.getBoolean(R.styleable.ScrollChildSwipeRefreshLayout_srl_restore_refresh_status, true);
        typedArray.recycle();
        if (colorSchemeArrayId != 0) {
            setColors(colorSchemeArrayId);
        }
    }

    private void setColors(int colorSchemeArrayId) {
        TypedArray colorsTypeArray = getResources().obtainTypedArray(colorSchemeArrayId);
        int indexCount = colorsTypeArray.length();
        if (indexCount != 0) {
            int[] colors = new int[indexCount];
            for (int i = 0; i < indexCount; i++) {
                colors[i] = colorsTypeArray.getColor(i, Color.BLACK);
            }
            setColorSchemeColors(colors);
        }
        colorsTypeArray.recycle();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (mTargetId != 0) {
            mScrollUpChild = findViewById(mTargetId);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mRestoreRefreshStatus && isRefreshing()) {
            //show animation
            setRefreshing(false);
            setRefreshing(true);
        }
    }

    @Override
    public boolean canChildScrollUp() {
        if (mScrollUpChild != null) {
            return ViewCompat.canScrollVertically(mScrollUpChild, -1);
        }
        return super.canChildScrollUp();
    }

    @SuppressWarnings("unused")
    public void setScrollUpChild(View view) {
        mScrollUpChild = view;
    }

}
