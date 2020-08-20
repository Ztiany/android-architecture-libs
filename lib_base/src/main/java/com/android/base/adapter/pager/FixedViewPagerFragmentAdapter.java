package com.android.base.adapter.pager;

import android.content.Context;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

@SuppressWarnings("unused")
public class FixedViewPagerFragmentAdapter extends FixedFragmentPagerAdapter {

    private final List<ViewPagerInfo> mTabs;
    private Context mContext;

    public FixedViewPagerFragmentAdapter(FragmentManager fragmentManager, Context context) {
        this(fragmentManager, context, BEHAVIOR_SET_USER_VISIBLE_HINT);
    }

    /**
     * @param behavior {@link FragmentPagerAdapter#BEHAVIOR_SET_USER_VISIBLE_HINT} or {@link FragmentPagerAdapter#BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT}
     */
    public FixedViewPagerFragmentAdapter(FragmentManager fragmentManager, Context context, int behavior) {
        super(fragmentManager, behavior);
        mContext = context;
        mTabs = new ArrayList<>();
    }

    public void replaceAll(List<ViewPagerInfo> viewPagerInfoList) {
        mTabs.clear();
        mTabs.addAll(viewPagerInfoList);
        notifyDataSetChanged();
    }

    public void addTab(ViewPagerInfo viewPagerInfo) {
        mTabs.add(viewPagerInfo);
        notifyDataSetChanged();
    }

    public void addTabAt(int index, ViewPagerInfo viewPagerInfo) {
        mTabs.add(index, viewPagerInfo);
        notifyDataSetChanged();
    }

    public void removeTable(ViewPagerInfo viewPagerInfo) {
        mTabs.remove(viewPagerInfo);
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
        Fragment instantiate = Fragment.instantiate(mContext, viewPagerInfo.clazz.getName(), viewPagerInfo.args);
        viewPagerInfo.mFragment = instantiate;
        return instantiate;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTabs.get(position).title;
    }

    public List<ViewPagerInfo> getTabs() {
        return mTabs;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        for (int i = 0; i < mTabs.size(); i++) {
            if (mTabs.get(i) == object) {
                return i;
            }
        }
        return FixedFragmentPagerAdapter.POSITION_NONE;
    }

}