package com.android.base.widget;

import static com.android.base.architecture.ui.state.StateLayoutConfig.CONTENT;
import static com.android.base.architecture.ui.state.StateLayoutConfig.EMPTY;
import static com.android.base.architecture.ui.state.StateLayoutConfig.ERROR;
import static com.android.base.architecture.ui.state.StateLayoutConfig.LOADING;
import static com.android.base.architecture.ui.state.StateLayoutConfig.ViewState;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;

import com.android.base.R;
import com.android.base.architecture.ui.state.StateLayout;
import com.android.base.architecture.ui.state.StateLayoutConfig;

import timber.log.Timber;

/**
 * @author Ztiany
 * Date : 2017-04-21 10:21
 */
public class SimpleMultiStateView extends MultiStateView implements StateLayout {

    private StateProcessor mStateProcessor;
    private StateListener mStateListener;

    public SimpleMultiStateView(Context context) {
        this(context, null);
    }

    public SimpleMultiStateView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SimpleMultiStateView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setListener();

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SimpleMultiStateView, defStyle, defStyle);

        initProcessor(typedArray);

        mStateProcessor.onInitialize(this);
        mStateProcessor.onParseAttrs(typedArray);

        typedArray.recycle();
    }

    private void initProcessor(TypedArray typedArray) {
        String processorPath = typedArray.getString(R.styleable.SimpleMultiStateView_msv_state_processor);

        if (!TextUtils.isEmpty(processorPath)) {
            try {
                Class<?> processorClass = Class.forName(processorPath);
                mStateProcessor = (StateProcessor) processorClass.newInstance();
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                e.printStackTrace();
                Timber.e("initProcessor() called can not instance processor: %s", processorPath);
            }
        }

        if (mStateProcessor == null) {
            mStateProcessor = new StateActionProcessor();
        }
    }

    private void setListener() {
        super.setStateListener(new StateListener() {
            @Override
            public void onStateChanged(@StateLayoutConfig.ViewState int viewState) {
                if (mStateListener != null) {
                    mStateListener.onStateChanged(viewState);
                }
            }

            @Override
            public void onStateInflated(@StateLayoutConfig.ViewState int viewState, @NonNull android.view.View view) {
                if (mStateListener != null) {
                    mStateListener.onStateInflated(viewState, view);
                }
                processStateInflated(viewState, view);
            }
        });
    }

    private void processStateInflated(@ViewState int viewState, @NonNull View view) {
        mStateProcessor.processStateInflated(viewState, view);
    }

    @Override
    public void setStateListener(StateListener listener) {
        mStateListener = listener;
    }

    @Override
    public void showContentLayout() {
        setViewState(CONTENT);
    }

    @Override
    public void showLoadingLayout() {
        setViewState(LOADING);
    }

    @Override
    public void showEmptyLayout() {
        setViewState(EMPTY);
    }

    @Override
    public void showErrorLayout() {
        setViewState(ERROR);
    }

    @Override
    public void showRequesting() {
        setViewState(StateLayoutConfig.REQUESTING);
    }

    @Override
    public void showBlank() {
        setViewState(StateLayoutConfig.BLANK);
    }

    @Override
    public void showNetErrorLayout() {
        setViewState(StateLayoutConfig.NET_ERROR);
    }

    @Override
    public void showServerErrorLayout() {
        setViewState(StateLayoutConfig.SERVER_ERROR);
    }

    @Override
    public StateLayoutConfig getStateLayoutConfig() {
        return mStateProcessor.getStateLayoutConfigImpl();
    }

    @Override
    @ViewState
    public int currentStatus() {
        return getViewState();
    }

}
