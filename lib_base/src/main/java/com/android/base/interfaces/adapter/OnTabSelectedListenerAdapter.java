package com.android.base.interfaces.adapter;

import android.support.design.widget.TabLayout;

public interface OnTabSelectedListenerAdapter extends TabLayout.OnTabSelectedListener {

    @Override
    default void onTabSelected(TabLayout.Tab tab) {
    }

    @Override
    default void onTabUnselected(TabLayout.Tab tab) {
    }

    @Override
    default void onTabReselected(TabLayout.Tab tab) {
    }

}
