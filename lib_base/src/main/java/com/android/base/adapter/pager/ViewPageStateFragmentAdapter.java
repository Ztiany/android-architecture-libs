package com.android.base.adapter.pager;

import android.content.Context;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class ViewPageStateFragmentAdapter extends FragmentStatePagerAdapter {

    private final List<ViewPageInfo> mTabs;
    private Context mContext;

    public ViewPageStateFragmentAdapter(FragmentManager fragmentManager, Context context) {
        super(fragmentManager);
        mContext = context;
        mTabs = new ArrayList<>();
    }

    public void setDataSource(List<ViewPageInfo> viewPageInfoList) {
        mTabs.clear();
        mTabs.addAll(viewPageInfoList);
    }

    @Override
    public int getCount() {
        return mTabs.size();
    }

    @NotNull
    @Override
    public Fragment getItem(int position) {
        ViewPageInfo viewPageInfo = mTabs.get(position);
        return Fragment.instantiate(mContext, viewPageInfo.clazz.getName(), viewPageInfo.args);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTabs.get(position).title;
    }

    protected List<ViewPageInfo> getTabs() {
        return mTabs;
    }

}