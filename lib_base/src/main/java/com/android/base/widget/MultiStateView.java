package com.android.base.widget;

import static com.android.base.architecture.ui.state.StateLayoutConfig.BLANK;
import static com.android.base.architecture.ui.state.StateLayoutConfig.CONTENT;
import static com.android.base.architecture.ui.state.StateLayoutConfig.EMPTY;
import static com.android.base.architecture.ui.state.StateLayoutConfig.ERROR;
import static com.android.base.architecture.ui.state.StateLayoutConfig.LOADING;
import static com.android.base.architecture.ui.state.StateLayoutConfig.NET_ERROR;
import static com.android.base.architecture.ui.state.StateLayoutConfig.REQUESTING;
import static com.android.base.architecture.ui.state.StateLayoutConfig.SERVER_ERROR;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.base.R;
import com.android.base.architecture.ui.state.StateLayoutConfig.ViewState;

/**
 * View that contains 7 different states: Content, Error/NetError/ServerError, Empty, and Loading/Request.
 */
public class MultiStateView extends FrameLayout {

    private LayoutInflater mInflater;
    private SparseArray<ViewHolder> mChildren;
    private View mContentView;
    private boolean mDisableOperationWhenRequesting = false;

    @Nullable
    private StateListener mListener;
    @ViewState
    private int mViewState = CONTENT;

    public MultiStateView(Context context) {
        this(context, null);
    }

    public MultiStateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public MultiStateView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        mChildren = new SparseArray<>();
        mInflater = LayoutInflater.from(getContext());

        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.MultiStateView, defStyle, 0);

        int loadingViewResId = a.getResourceId(R.styleable.MultiStateView_msv_loadingView, -1);
        int requestingViewResId = a.getResourceId(R.styleable.MultiStateView_msv_requestingView, -1);
        int emptyViewResId = a.getResourceId(R.styleable.MultiStateView_msv_emptyView, -1);
        int errorViewResId = a.getResourceId(R.styleable.MultiStateView_msv_errorView, -1);
        int netErrorViewResId = a.getResourceId(R.styleable.MultiStateView_msv_net_errorView, -1);
        int serverErrorViewResId = a.getResourceId(R.styleable.MultiStateView_msv_server_errorView, -1);

        mChildren.put(LOADING, new ViewHolder(loadingViewResId));
        mChildren.put(REQUESTING, new ViewHolder(requestingViewResId));
        mChildren.put(EMPTY, new ViewHolder(emptyViewResId));
        mChildren.put(ERROR, new ViewHolder(errorViewResId));
        mChildren.put(NET_ERROR, new ViewHolder(netErrorViewResId));
        mChildren.put(SERVER_ERROR, new ViewHolder(serverErrorViewResId));

        mDisableOperationWhenRequesting = a.getBoolean(R.styleable.MultiStateView_msv_disable_when_requesting, false);

        ensureInitState(a.getInt(R.styleable.MultiStateView_msv_viewState, CONTENT));

        a.recycle();
    }

    private void ensureInitState(int viewState) {
        /*
            <enum name="content" value="1"/>
            <enum name="loading" value="2"/>
            <enum name="empty" value="3"/>
            <enum name="error" value="4"/>
            <enum name="net_error" value="5"/>
            <enum name="server_error" value="6"/>
            <enum name="requesting" value="7"/>
            <enum name="blank" value="8"/>
        */
        switch (viewState) {
            case 2:
                mViewState = LOADING;
                break;
            case 3:
                mViewState = EMPTY;
                break;
            case 4:
                mViewState = ERROR;
                break;
            case 5:
                mViewState = NET_ERROR;
                break;
            case 6:
                mViewState = SERVER_ERROR;
                break;
            case 7:
                mViewState = REQUESTING;
                break;
            case 8:
                mViewState = BLANK;
                break;
            case 1:
            default:
                mViewState = CONTENT;
                break;
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mContentView == null) {
            throw new IllegalArgumentException("Content view is not defined");
        }
        setView();
    }

    /* All of the addView methods have been overridden so that it can obtain the content view via XML
     It is NOT recommended to add views into MultiStateView via the addView methods, but rather use
     any of the setViewForState methods to set views for their given ContentViewState accordingly */
    @Override
    public void addView(View child) {
        if (isValidContentView(child)) {
            mContentView = child;
        }
        super.addView(child);
    }

    @Override
    public void addView(View child, int index) {
        if (isValidContentView(child)) {
            mContentView = child;
        }
        super.addView(child, index);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (isValidContentView(child)) {
            mContentView = child;
        }
        super.addView(child, index, params);
    }

    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {
        if (isValidContentView(child)) {
            mContentView = child;
        }
        super.addView(child, params);
    }

    @Override
    public void addView(View child, int width, int height) {
        if (isValidContentView(child)) {
            mContentView = child;
        }
        super.addView(child, width, height);
    }

    @Override
    protected boolean addViewInLayout(View child, int index, ViewGroup.LayoutParams params) {
        if (isValidContentView(child)) {
            mContentView = child;
        }
        return super.addViewInLayout(child, index, params);
    }

    @Override
    protected boolean addViewInLayout(View child, int index, ViewGroup.LayoutParams params, boolean preventRequestLayout) {
        if (isValidContentView(child)) {
            mContentView = child;
        }
        return super.addViewInLayout(child, index, params, preventRequestLayout);
    }

    /**
     * Returns the {@link View} associated with the {@link ViewState}
     *
     * @param state The {@link ViewState} with to return the view for
     * @return The {@link View} associated with the {@link ViewState}, null if no view is present
     */
    @Nullable
    @SuppressWarnings("unused")
    public View getView(@ViewState int state) {
        if (state == BLANK) {
            return null;
        }
        return ensureStateView(state);
    }

    private View ensureStateView(@ViewState int state) {
        ViewHolder viewHolder = mChildren.get(state);
        if (viewHolder == null) {
            throw new NullPointerException("the ViewHolder is null, state = " + state);
        }

        if (viewHolder.mView != null) {
            return viewHolder.mView;
        }

        int viewLayoutId = viewHolder.mViewLayoutId;

        if (viewLayoutId > 0) {
            View newView = mInflater.inflate(viewLayoutId, this, false);
            newView.setTag(R.id.base_tag_multi_state_view, state);
            addView(newView, newView.getLayoutParams());
            if (mListener != null) {
                mListener.onStateInflated(state, newView);
            }
            if (mViewState != state) {
                newView.setVisibility(GONE);
            }
            viewHolder.mView = newView;
            return newView;
        } else {
            throw new IllegalStateException("the view layout id is invalidate, layout id = " + viewLayoutId + " state = " + state);
        }
    }

    /**
     * Returns the current {@link ViewState}
     *
     * @return ContentViewState
     */
    @ViewState
    @SuppressWarnings("unused")
    public int getViewState() {
        return mViewState;
    }

    /**
     * Sets the current {@link ViewState}
     *
     * @param state The {@link ViewState} to set {@link MultiStateView} to
     */
    public void setViewState(@ViewState int state) {
        if (state != mViewState) {
            mViewState = state;
            setView();
            if (mListener != null) {
                mListener.onStateChanged(mViewState);
            }
        }
    }

    /**
     * Shows the {@link View} based on the {@link ViewState}
     */
    private void setView() {
        if (mViewState == BLANK) {
            int size = mChildren.size();
            View view;
            for (int i = 0; i < size; i++) {
                view = mChildren.valueAt(i).mView;
                if (view != null) {
                    view.setVisibility(View.GONE);
                }
            }
            return;
        }

        View curStateView = ensureStateView(mViewState);
        int size = mChildren.size();

        for (int i = 0; i < size; i++) {
            ViewHolder viewHolder = mChildren.valueAt(i);
            if (viewHolder.mView == null) {
                continue;
            }

            if (viewHolder.mView != curStateView) {
                if (mViewState == REQUESTING && viewHolder.mView == mContentView) {
                    viewHolder.mView.setVisibility(VISIBLE);
                } else {
                    viewHolder.mView.setVisibility(GONE);
                }
            }
        }

        curStateView.setVisibility(VISIBLE);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            if (mViewState == REQUESTING) {
                return mDisableOperationWhenRequesting;
            } else {
                return false;
            }
        }
        return super.onInterceptTouchEvent(ev);
    }

    /**
     * Checks if the given {@link View} is valid for the Content View
     *
     * @param view The {@link View} to check
     */
    private boolean isValidContentView(View view) {
        if (mContentView != null && mContentView != view) {
            return false;
        }
        Object tag = view.getTag(R.id.base_tag_multi_state_view);
        if (tag == null) {
            mChildren.put(CONTENT, new ViewHolder(view, 0));
            return true;
        }
        if (tag instanceof Integer) {
            int viewTag = (Integer) tag;
            if (viewTag != LOADING && viewTag != EMPTY && viewTag != ERROR && viewTag != NET_ERROR && viewTag != SERVER_ERROR && viewTag != REQUESTING) {
                mChildren.put(CONTENT, new ViewHolder(view, 0));
                return true;
            } else {
                return false;
            }
        }
        mChildren.put(CONTENT, new ViewHolder(view, 0));
        return true;
    }

    public void setDisableOperationWhenRequesting(boolean disableOperationWhenRequesting) {
        mDisableOperationWhenRequesting = disableOperationWhenRequesting;
    }

    /**
     * Sets the {@link StateListener} for the view
     *
     * @param listener The {@link StateListener} that will receive callbacks
     */
    public void setStateListener(StateListener listener) {
        mListener = listener;
    }

    public interface StateListener {
        /**
         * Callback for when the {@link ViewState} has changed
         *
         * @param viewState The {@link ViewState} that was switched to
         */
        void onStateChanged(@ViewState int viewState);

        /**
         * Callback for when a {@link ViewState} has been inflated
         *
         * @param viewState The {@link ViewState} that was inflated
         * @param view      The {@link View} that was inflated
         */
        void onStateInflated(@ViewState int viewState, @NonNull View view);
    }

    private static class ViewHolder {
        private View mView;
        private final int mViewLayoutId;

        ViewHolder(int viewLayoutId) {
            this(null, viewLayoutId);
        }

        ViewHolder(View view, int viewLayoutId) {
            mView = view;
            mViewLayoutId = viewLayoutId;
        }
    }

}