package com.android.base.adapter.pager;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerFragmentAdapter extends FragmentPagerAdapter {

    private final List<ViewPagerInfo> mTabs;
    private final Context mContext;

    public ViewPagerFragmentAdapter(FragmentManager fragmentManager, Context context) {
        this(fragmentManager, context, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    /**
     * @param behavior {@link FragmentPagerAdapter#BEHAVIOR_SET_USER_VISIBLE_HINT} or {@link FragmentPagerAdapter#BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT}
     */
    public ViewPagerFragmentAdapter(FragmentManager fragmentManager, Context context, int behavior) {
        super(fragmentManager, behavior);
        mContext = context;
        mTabs = new ArrayList<>();
    }

    public void setDataSource(List<ViewPagerInfo> viewPagerInfoList) {
        mTabs.clear();
        mTabs.addAll(viewPagerInfoList);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mTabs.size();
    }

    @NotNull
    @Override
    public Fragment getItem(int position) {
        ViewPagerInfo viewPagerInfo = mTabs.get(position);
        return Fragment.instantiate(mContext, viewPagerInfo.clazz.getName(), viewPagerInfo.args);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTabs.get(position).title;
    }

}