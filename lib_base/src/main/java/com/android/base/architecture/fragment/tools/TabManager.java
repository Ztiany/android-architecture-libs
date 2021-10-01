package com.android.base.architecture.fragment.tools;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Lifecycle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class TabManager {

    @SuppressWarnings("WeakerAccess") public static final int ATTACH_DETACH = 1;
    @SuppressWarnings("WeakerAccess") public static final int SHOW_HIDE = 2;

    private final FragmentManager mFragmentManager;
    private final int mContainerId;
    private final Tabs mMainTabs;
    private final Context mContext;

    private FragmentInfo mCurrentFragmentInfo;

    private static final String CURRENT_ID_KET = "main_tab_id";
    private static final int NONE = -1;
    private int mCurrentId = NONE;

    private final int mOperationType;
    private final boolean mEnableMaxLifecycle;

    public TabManager(Context context, FragmentManager fragmentManager, Tabs tabs, int containerId) {
        this(context, fragmentManager, tabs, containerId, SHOW_HIDE, true);
    }

    /**
     * @param operationType {@link #ATTACH_DETACH} or {@link #SHOW_HIDE}
     */
    public TabManager(Context context, FragmentManager fragmentManager, Tabs tabs, int containerId, int operationType, boolean enableMaxLifecycle) {
        if (operationType != ATTACH_DETACH && operationType != SHOW_HIDE) {
            throw new IllegalArgumentException("the operationType must be ATTACH_DETACH or SHOW_HIDE");
        }
        mMainTabs = tabs;
        mContainerId = containerId;
        mContext = context;
        mFragmentManager = fragmentManager;
        mOperationType = operationType;
        mEnableMaxLifecycle = enableMaxLifecycle;
    }

    public final void setup(Bundle bundle) {
        int pageId = mMainTabs.homePage().getPageId();
        if (bundle == null) {
            switchPage(pageId);
        } else {
            mCurrentId = bundle.getInt(CURRENT_ID_KET, pageId);
            restoreState();
        }
    }

    private void restoreState() {
        List<FragmentInfo> pages = mMainTabs.getPages();
        for (FragmentInfo page : pages) {
            page.setInstance(mFragmentManager.findFragmentByTag(page.getTag()));
            if (mCurrentId == page.getPageId()) {
                mCurrentFragmentInfo = page;
            }
        }
        if (mCurrentId == NONE) {
            doChangeTab(mMainTabs.homePage().getPageId());
        }
    }

    private void switchPage(int pageId) {
        if (mCurrentId == pageId) {
            return;
        }
        FragmentTransaction ft = null;
        if (mCurrentFragmentInfo != null) {
            Fragment fragment = mCurrentFragmentInfo.getInstance();
            if (fragment != null) {
                ft = mFragmentManager.beginTransaction();
                hideOrDetach(ft, fragment);
            }
        }
        if (ft != null) {
            ft.commit();
        }
        doChangeTab(pageId);
    }

    private void hideOrDetach(FragmentTransaction fragmentTransaction, Fragment fragment) {
        if (mOperationType == SHOW_HIDE) {
            fragmentTransaction.hide(fragment);
        } else {
            fragmentTransaction.detach(fragment);
        }
        if (mEnableMaxLifecycle) {
            fragmentTransaction.setMaxLifecycle(fragment, Lifecycle.State.STARTED);
        }
    }

    private void showOrAttach(FragmentTransaction fragmentTransaction, Fragment fragment) {
        if (mOperationType == SHOW_HIDE) {
            fragmentTransaction.show(fragment);
        } else {
            fragmentTransaction.attach(fragment);
        }
        if (mEnableMaxLifecycle) {
            fragmentTransaction.setMaxLifecycle(fragment, Lifecycle.State.RESUMED);
        }
    }

    private void doChangeTab(int fragmentId) {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        FragmentInfo fragmentInfo = mMainTabs.getFragmentInfo(fragmentId);
        Fragment fragment = fragmentInfo.getInstance();
        if (fragment != null) {
            showOrAttach(fragmentTransaction, fragment);
        } else {
            Fragment newFragment = fragmentInfo.newFragment(mContext);
            fragmentInfo.setInstance(newFragment);
            onFragmentCreated(fragmentId, newFragment);
            fragmentTransaction.add(mContainerId, newFragment, fragmentInfo.getTag());
        }
        mCurrentFragmentInfo = fragmentInfo;
        mCurrentId = fragmentId;
        fragmentTransaction.commit();
    }

    @SuppressWarnings("WeakerAccess")
    public void selectTabByPosition(int position) {
        switchPage(mMainTabs.getIdByPosition(position));
    }

    public void selectTabById(int tabId) {
        selectTabByPosition(mMainTabs.getPositionById(tabId));
    }

    @SuppressWarnings("unused")
    public int getCurrentPosition() {
        return mMainTabs.getPositionById(mCurrentId);
    }

    @SuppressWarnings("WeakerAccess,unused")
    protected void onFragmentCreated(int id, Fragment newFragment) {
    }

    public void onSaveInstanceState(Bundle bundle) {
        bundle.putInt(CURRENT_ID_KET, mCurrentId);
    }

    public static abstract class Tabs {

        private final List<FragmentInfo> mPages;

        protected Tabs() {
            mPages = new ArrayList<>();
        }

        protected void add(FragmentInfo page) {
            mPages.add(page);
        }

        FragmentInfo homePage() {
            return mPages.get(defaultIndex());
        }

        protected int defaultIndex() {
            return 0;
        }

        public int size() {
            return mPages.size();
        }

        FragmentInfo getFragmentInfo(int id) {
            for (FragmentInfo page : mPages) {
                if (page.getPageId() == id) {
                    return page;
                }
            }
            throw new IllegalArgumentException("MainPages not has this pageId :" + id);
        }

        /**
         * @param clazz Fragment对应的clazz
         * @return pagerId ，没有则返回-1
         */
        @SuppressWarnings("unused")
        int getIdByClazz(Class<? extends Fragment> clazz) {
            for (FragmentInfo page : mPages) {
                if (page.getClazz() == clazz) {
                    return page.getPageId();
                }
            }
            return -1;
        }

        List<FragmentInfo> getPages() {
            return Collections.unmodifiableList(mPages);
        }

        private int getPositionById(int tabId) {
            int size = mPages.size();
            for (int i = 0; i < size; i++) {
                if (mPages.get(i).getPageId() == tabId) {
                    return i;
                }
            }
            return -1;
        }

        private int getIdByPosition(int position) {
            return mPages.get(position).getPageId();
        }
    }

}