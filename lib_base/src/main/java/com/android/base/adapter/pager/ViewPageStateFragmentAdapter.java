package com.android.base.adapter.pager;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

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
