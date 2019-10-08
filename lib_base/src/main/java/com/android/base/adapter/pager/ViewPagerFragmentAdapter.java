package com.android.base.adapter.pager;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

@SuppressWarnings("unused")
public class ViewPagerFragmentAdapter extends FragmentPagerAdapter {

    private final List<ViewPagerInfo> mTabs;
    private Context mContext;

    public ViewPagerFragmentAdapter(FragmentManager fragmentManager, Context context) {
        super(fragmentManager);
        mContext = context;
        mTabs = new ArrayList<>();
    }

    public void setDataSource(List<ViewPagerInfo> viewPagerInfoList) {
        mTabs.clear();
        mTabs.addAll(viewPagerInfoList);
    }

    @Override
    public int getCount() {
        return mTabs.size();
    }

    @Override
    public Fragment getItem(int position) {
        ViewPagerInfo viewPagerInfo = mTabs.get(position);
        return Fragment.instantiate(mContext, viewPagerInfo.clazz.getName(), viewPagerInfo.args);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTabs.get(position).title;
    }

    public List<ViewPagerInfo> getTabs() {
        return mTabs;
    }
}