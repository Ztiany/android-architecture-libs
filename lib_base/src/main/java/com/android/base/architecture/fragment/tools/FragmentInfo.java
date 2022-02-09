package com.android.base.architecture.fragment.tools;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import java.lang.ref.WeakReference;

@SuppressWarnings("WeakerAccess,unused")
public class FragmentInfo {

    private final int mPageId;
    private final String mTag;
    private final Class<? extends Fragment> mClazz;
    private final int mTitleId;
    private final Bundle mArguments;
    private final String mStackName;
    private WeakReference<Fragment> mFragment;

    private FragmentInfo(int pageId, String tag, Class<? extends Fragment> clazz, int titleId, Bundle arguments,  String stackName) {
        mPageId = pageId;
        mTag = tag;
        mClazz = clazz;
        mTitleId = titleId;
        mArguments = arguments;
        mStackName = stackName;
    }

    public Fragment getInstance() {
        return mFragment == null ? null : mFragment.get();
    }

    public void setInstance(Fragment fragment) {
        mFragment = new WeakReference<>(fragment);
    }

    public Fragment newFragment(Context context) {
        return Fragment.instantiate(context, mClazz.getName(), mArguments);
    }

    public String getStackName() {
        return mStackName;
    }

    public Bundle getArguments() {
        return mArguments;
    }

    public int getTitleId() {
        return mTitleId;
    }

    public int getPageId() {
        return mPageId;
    }

    public String getTag() {
        return mTag;
    }

    public Class<? extends Fragment> getClazz() {
        return mClazz;
    }

    public static PageBuilder builder() {
        return new PageBuilder();
    }


    public static class PageBuilder {

        private int mPagerId;
        private String mTag;
        private Class<? extends Fragment> mClazz;
        private int mTitleId;
        private Bundle mArguments;
        private String mStackName;

        public FragmentInfo build() {
            return new FragmentInfo(mPagerId, mTag, mClazz, mTitleId, mArguments, mStackName);
        }

        public PageBuilder pagerId(int pagerId) {
            mPagerId = pagerId;
            return this;
        }

        public PageBuilder tag(String tag) {
            this.mTag = tag;
            return this;
        }

        public PageBuilder clazz(Class<? extends Fragment> clazz) {
            mClazz = clazz;
            return this;
        }

        public PageBuilder titleId(int titleId) {
            mTitleId = titleId;
            return this;
        }

        public PageBuilder arguments(Bundle arguments) {
            mArguments = arguments;
            return this;
        }

        /**
         * 如果需要加入到Stack，建议加上StackName。
         *
         * @param stackName StackName
         * @return PageBuilder
         */
        public PageBuilder stackName(String stackName) {
            mStackName = stackName;
            return this;
        }
    }

}