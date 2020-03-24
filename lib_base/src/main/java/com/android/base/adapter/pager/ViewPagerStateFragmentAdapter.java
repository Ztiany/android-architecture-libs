package com.android.base.adapter.pager;

import android.content.Context;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class ViewPagerStateFragmentAdapter extends FragmentStatePagerAdapter {

    private final List<ViewPagerInfo> mTabs;
    private Context mContext;

    public ViewPagerStateFragmentAdapter(FragmentManager fragmentManager, Context context) {
        super(fragmentManager);
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

    protected List<ViewPagerInfo> getTabs() {
        return mTabs;
    }

}