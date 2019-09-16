package com.android.base.app.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.android.base.app.BaseKit;
import com.android.base.app.activity.BackHandlerHelper;
import com.android.base.app.activity.OnBackPressListener;
import com.android.base.app.ui.LoadingView;
import com.github.dmstocking.optional.java.util.function.Predicate;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.annotation.UiThread;
import androidx.fragment.app.Fragment;
import timber.log.Timber;

/**
 * 提供：
 * <pre>
 *     1. RxJava 生命周期绑定。
 *     2. 返回键监听。
 *     3. 显示 LoadingDialog 和 Message。
 *     4. 可以添加生命周期代理。
 * </pre>
 *
 * @author Ztiany
 * date :   2016-03-19 23:09
 * email:    1169654504@qq.com
 */
public class BaseFragment extends Fragment implements LoadingView, OnBackPressListener, FragmentDelegateOwner {

    private LoadingView mLoadingViewImpl;

    private View mLayoutView;

    /* just for cache*/
    private View mCachedView;

    private final FragmentDelegates mFragmentDelegates = new FragmentDelegates(this);

    private String tag() {
        return this.getClass().getSimpleName();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Timber.tag(tag()).d("onAttach() called with: context = [" + context + "]");
        mFragmentDelegates.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Timber.tag(tag()).d("-->onCreate  savedInstanceState   =   " + savedInstanceState);
        mFragmentDelegates.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mCachedView == null) {
            Object layout = provideLayout();
            if (layout == null) {
                return null;
            }
            if (layout instanceof Integer) {
                return mCachedView = inflater.inflate((Integer) layout, container, false);
            }
            if (layout instanceof View) {
                return mCachedView = (View) layout;
            }
            throw new IllegalArgumentException("Here you should provide  a  layout id  or a View");
        }

        Timber.tag(tag()).d("mCachedView.parent: " + mCachedView.getParent());

        if (mCachedView.getParent() != null) {
            ViewParent parent = mCachedView.getParent();
            if (parent instanceof ViewGroup) {
                ((ViewGroup) parent).removeView(mCachedView);
            }
        }

        return mCachedView;
    }

    /**
     * 使用此方法提供的布局，将只会被缓存起来，即此方法将只会被调用一次。
     *
     * @return provide  a  layout id  or a View
     */
    @Nullable
    @SuppressWarnings("unused")
    protected Object provideLayout() {
        return null;
    }

    @Override
    public final void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Timber.tag(tag()).d("-->onViewCreated  savedInstanceState   =   " + savedInstanceState);
        if (mLayoutView != view) {
            mLayoutView = view;
            internalOnViewPrepared(view, savedInstanceState);
            onViewPrepared(view, savedInstanceState);
        }
        mFragmentDelegates.onViewCreated(view, savedInstanceState);
    }

    void internalOnViewPrepared(@NonNull View view, @Nullable Bundle savedInstanceState) {
    }

    /**
     * View is prepared, If {@link androidx.fragment.app.Fragment#onCreateView(LayoutInflater, ViewGroup, Bundle)} return same layout, it will be called once
     *
     * @param view view of fragment
     */
    protected void onViewPrepared(@NonNull View view, @Nullable Bundle savedInstanceState) {
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Timber.tag(tag()).d("-->onActivityCreated savedInstanceState   =   " + savedInstanceState);
        mFragmentDelegates.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        Timber.tag(tag()).d("-->onStart");
        mFragmentDelegates.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        Timber.tag(tag()).d("-->onResume");
        mFragmentDelegates.onResume();
    }

    @Override
    public void onPause() {
        Timber.tag(tag()).d("-->onPause");
        mFragmentDelegates.onPause();
        super.onPause();
    }

    @Override
    public void onStop() {
        Timber.tag(tag()).d("-->onStop");
        mFragmentDelegates.onStop();
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        Timber.tag(tag()).d("-->onDestroyView");
        mFragmentDelegates.onDestroyView();
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        Timber.tag(tag()).d("-->onDestroy");
        mFragmentDelegates.onDestroy();
        super.onDestroy();
        dismissLoadingDialog();
    }

    @Override
    public void onDetach() {
        Timber.tag(tag()).d("-->onDetach");
        mFragmentDelegates.onDetach();
        super.onDetach();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        mFragmentDelegates.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Timber.tag(tag()).d("-->setUserVisibleHint ==" + isVisibleToUser);
        mFragmentDelegates.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Timber.tag(tag()).d("-->onHiddenChanged = " + hidden);
        mFragmentDelegates.onHiddenChanged(hidden);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mFragmentDelegates.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mFragmentDelegates.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    @UiThread
    public final void addDelegate(FragmentDelegate fragmentDelegate) {
        mFragmentDelegates.addDelegate(fragmentDelegate);
    }

    @Override
    @UiThread
    public final boolean removeDelegate(FragmentDelegate fragmentDelegate) {
        return mFragmentDelegates.removeDelegate(fragmentDelegate);
    }

    @Override
    public FragmentDelegate findDelegate(Predicate<FragmentDelegate> predicate) {
        return mFragmentDelegates.findDelegate(predicate);
    }

    @Override
    public boolean onBackPressed() {
        return handleBackPress() || BackHandlerHelper.handleBackPress(this);
    }

    /**
     * Fragment需要自己处理BackPress事件，如果不处理，就交给子Fragment处理。都不处理则由Activity处理
     */
    protected boolean handleBackPress() {
        return false;
    }

    private LoadingView getLoadingViewImpl() {
        if (mLoadingViewImpl == null) {
            mLoadingViewImpl = onCreateLoadingView();
        }
        if (mLoadingViewImpl == null) {
            mLoadingViewImpl = BaseKit.get().getLoadingViewFactory().createLoadingDelegate(getContext());
        }
        return mLoadingViewImpl;
    }

    protected LoadingView onCreateLoadingView() {
        return null;
    }

    @Override
    public void showLoadingDialog() {
        getLoadingViewImpl().showLoadingDialog(true);
    }

    @Override
    public void showLoadingDialog(boolean cancelable) {
        getLoadingViewImpl().showLoadingDialog(cancelable);
    }

    @Override
    public void showLoadingDialog(CharSequence message, boolean cancelable) {
        getLoadingViewImpl().showLoadingDialog(message, cancelable);
    }

    @Override
    public void showLoadingDialog(@StringRes int messageId, boolean cancelable) {
        getLoadingViewImpl().showLoadingDialog(messageId, cancelable);
    }

    @Override
    public void dismissLoadingDialog() {
        getLoadingViewImpl().dismissLoadingDialog();
    }

    @Override
    public void showMessage(CharSequence message) {
        getLoadingViewImpl().showMessage(message);
    }

    @Override
    public void showMessage(@StringRes int messageId) {
        getLoadingViewImpl().showMessage(messageId);
    }

}