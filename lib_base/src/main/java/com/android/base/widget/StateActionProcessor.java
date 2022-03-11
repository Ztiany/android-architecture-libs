package com.android.base.widget;

import static com.android.base.architecture.ui.state.StateLayoutConfig.EMPTY;
import static com.android.base.architecture.ui.state.StateLayoutConfig.ERROR;
import static com.android.base.architecture.ui.state.StateLayoutConfig.NET_ERROR;
import static com.android.base.architecture.ui.state.StateLayoutConfig.SERVER_ERROR;

import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.android.base.R;
import com.android.base.architecture.ui.CommonId;
import com.android.base.architecture.ui.state.OnRetryActionListener;
import com.android.base.architecture.ui.state.StateLayoutConfig;


/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2018-05-17 17:08
 */
public class StateActionProcessor extends StateProcessor {

    private OnRetryActionListener mOnRetryActionListener;

    private ViewInfo mEmptyViewInfo;
    private ViewInfo mErrorViewInfo;
    private ViewInfo mNetErrorViewInfo;
    private ViewInfo mServerErrorViewInfo;

    private SimpleMultiStateView mSimpleMultiStateView;

    @Override
    public void onInitialize(SimpleMultiStateView simpleMultiStateView) {
        mSimpleMultiStateView = simpleMultiStateView;
    }

    @Override
    public void onParseAttrs(TypedArray typedArray) {
        mErrorViewInfo = new ViewInfo(ERROR);
        mErrorViewInfo.mDrawable = typedArray.getDrawable(R.styleable.SimpleMultiStateView_msv_errorImg);
        mErrorViewInfo.mMessage = typedArray.getText(R.styleable.SimpleMultiStateView_msv_errorText);
        mErrorViewInfo.mActionText = typedArray.getText(R.styleable.SimpleMultiStateView_msv_errorAction);

        mEmptyViewInfo = new ViewInfo(EMPTY);
        mEmptyViewInfo.mDrawable = typedArray.getDrawable(R.styleable.SimpleMultiStateView_msv_emptyImg);
        mEmptyViewInfo.mMessage = typedArray.getText(R.styleable.SimpleMultiStateView_msv_emptyText);
        mEmptyViewInfo.mActionText = typedArray.getText(R.styleable.SimpleMultiStateView_msv_emptyAction);

        mNetErrorViewInfo = new ViewInfo(NET_ERROR);
        mNetErrorViewInfo.mDrawable = typedArray.getDrawable(R.styleable.SimpleMultiStateView_msv_net_errorImg);
        mNetErrorViewInfo.mMessage = typedArray.getText(R.styleable.SimpleMultiStateView_msv_net_errorText);
        mNetErrorViewInfo.mActionText = typedArray.getText(R.styleable.SimpleMultiStateView_msv_net_errorAction);

        mServerErrorViewInfo = new ViewInfo(SERVER_ERROR);
        mServerErrorViewInfo.mDrawable = typedArray.getDrawable(R.styleable.SimpleMultiStateView_msv_server_errorImg);
        mServerErrorViewInfo.mMessage = typedArray.getText(R.styleable.SimpleMultiStateView_msv_server_errorText);
        mServerErrorViewInfo.mActionText = typedArray.getText(R.styleable.SimpleMultiStateView_msv_server_errorAction);
    }

    @Override
    public void processStateInflated(@StateLayoutConfig.ViewState int viewState, @NonNull View view) {
        if (viewState == ERROR) {
            mErrorViewInfo.setStateView(view);
        } else if (viewState == EMPTY) {
            mEmptyViewInfo.setStateView(view);
        } else if (viewState == NET_ERROR) {
            mNetErrorViewInfo.setStateView(view);
        } else if (viewState == SERVER_ERROR) {
            mServerErrorViewInfo.setStateView(view);
        }
    }

    private StateActionProcessor.ViewInfo getViewInfoForState(@StateLayoutConfig.ViewState int viewState) {
        if (viewState == EMPTY) {
            return mEmptyViewInfo;
        } else if (viewState == ERROR) {
            return mErrorViewInfo;
        } else if (viewState == NET_ERROR) {
            return mNetErrorViewInfo;
        } else if (viewState == SERVER_ERROR) {
            return mServerErrorViewInfo;
        }
        throw new IllegalArgumentException("no viewInfo in this state");
    }

    @Override
    public StateLayoutConfig getStateLayoutConfigImpl() {
        return mStateLayoutConfig;
    }

    class ViewInfo {

        private final int mState;
        private Drawable mDrawable;
        private CharSequence mMessage;
        private CharSequence mActionText;
        private TextView mMessageTv;
        private ImageView mIconTv;
        private Button mActionBtn;

        private ViewInfo(int state) {
            mState = state;
        }

        void setStateView(View stateView) {
            mIconTv = stateView.findViewById(CommonId.RETRY_IV_ID);
            mMessageTv = stateView.findViewById(CommonId.RETRY_TV_ID);
            mActionBtn = stateView.findViewById(CommonId.RETRY_BTN_ID);
            mActionBtn.setOnClickListener(v -> {
                if (mOnRetryActionListener != null) {
                    mOnRetryActionListener.onRetry(mState);
                }
            });
            setActionText(mActionText);
            setMessage(mMessage);
            setDrawable(mDrawable);
        }

        void setDrawable(Drawable drawable) {
            mDrawable = drawable;
            if (mIconTv != null) {
                mIconTv.setImageDrawable(drawable);
            }
        }

        void setMessage(CharSequence message) {
            mMessage = message;
            if (mMessageTv != null) {
                mMessageTv.setText(mMessage);
            }
        }

        void setActionText(CharSequence actionText) {
            mActionText = actionText;
            if (mActionBtn == null) {
                return;
            }
            mActionBtn.setText(mActionText);
            if (TextUtils.isEmpty(mActionText)) {
                mActionBtn.setVisibility(View.GONE);
            } else {
                mActionBtn.setVisibility(View.VISIBLE);
            }
        }
    }

    private final StateLayoutConfig mStateLayoutConfig = new StateLayoutConfig() {

        @Override
        public StateLayoutConfig setStateMessage(@ViewState int state, CharSequence message) {
            getViewInfoForState(state).setMessage(message);
            return this;
        }

        @Override
        public StateLayoutConfig setStateIcon(@ViewState int state, Drawable drawable) {
            getViewInfoForState(state).setDrawable(drawable);
            return this;
        }

        @Override
        public StateLayoutConfig setStateIcon(@ViewState int state, @DrawableRes int drawableId) {
            getViewInfoForState(state).setDrawable(ContextCompat.getDrawable(mSimpleMultiStateView.getContext(), drawableId));
            return this;
        }

        @Override
        public StateLayoutConfig setStateAction(@ViewState int state, CharSequence actionText) {
            getViewInfoForState(state).setActionText(actionText);
            return this;
        }

        @Override
        public StateLayoutConfig setStateRetryListener(OnRetryActionListener retryActionListener) {
            mOnRetryActionListener = retryActionListener;
            return this;
        }

        @Override
        public StateLayoutConfig disableOperationWhenRequesting(boolean disable) {
            mSimpleMultiStateView.setDisableOperationWhenRequesting(disable);
            return this;
        }

        @Override
        public StateProcessor getProcessor() {
            return StateActionProcessor.this;
        }

    };

}