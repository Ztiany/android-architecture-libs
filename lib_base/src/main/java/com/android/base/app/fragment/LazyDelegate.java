package com.android.base.app.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

/**
 * <pre>
 *    在ViewPager中实现懒加载的Fragment
 *          changed--1: Android Support 24 把setUserVisibleHint方法放到了Attach之前调用了,所以请在在构造代码块中设置LazyDelegate
 * </pre>
 *
 * @author Ztiany
 * Date : Date : 2016-05-06 15:02
 * Email: 1169654504@qq.com
 */
public class LazyDelegate implements FragmentDelegate<Fragment> {

    /**
     * View是否准备好，如果不需要绑定view数据，只是加载网络数据，那么该字段可以去掉
     */
    private boolean mIsViewPrepared;
    /**
     * 滑动过来后，View是否可见
     */
    private boolean mIsViewVisible;

    private onPreparedListener mOnPreparedListener;

    private LazyDelegate() {
    }

    public static LazyDelegate attach(FragmentDelegateOwner delegateFragment, final onPreparedListener onPreparedListener) {
        LazyDelegate delegate = new LazyDelegate();
        delegate.mOnPreparedListener = onPreparedListener;
        delegateFragment.addDelegate(delegate);
        return delegate;
    }

    /**
     * 在这里实现Fragment数据的缓加载.
     *
     * @param isVisibleToUser true表用户可见，false表不可见
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser) {
            mIsViewVisible = true;
            onVisible();
        } else {
            mIsViewVisible = false;
            onInvisible();
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        mIsViewPrepared = true;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        lazyLoad();
    }

    /**
     * 滑动过来后，界面可见时执行
     */
    @SuppressWarnings("all")
    protected void onVisible() {
        lazyLoad();
    }

    /**
     * 滑动过来后，界面不可见时执行
     */
    @SuppressWarnings("all")
    protected void onInvisible() {
    }

    private void lazyLoad() {
        if (mIsViewPrepared && mIsViewVisible) {
            notifyLazyLoad();
        }
    }

    /**
     * 懒加载数据，并在此绑定View数据
     */
    private void notifyLazyLoad() {
        if (mOnPreparedListener != null) {
            mOnPreparedListener.onPrepared();
        }
    }

    public interface onPreparedListener {
        void onPrepared();
    }

    public static abstract class SimpleLazyLoadListener implements onPreparedListener {

        private boolean mIsCalled;

        @Override
        public final void onPrepared() {
            if (!mIsCalled) {
                onFirstLoad();
                mIsCalled = true;
            }
        }

        protected abstract void onFirstLoad();
    }

}