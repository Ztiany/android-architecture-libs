package com.android.base.utils.android.adaption;

import android.view.View;

import org.jetbrains.annotations.NotNull;

import androidx.drawerlayout.widget.DrawerLayout;


public interface DrawerListenerAdapter extends DrawerLayout.DrawerListener {

    @Override
    default void onDrawerSlide(@NotNull View drawerView, float slideOffset) {
    }

    @Override
    default void onDrawerOpened(@NotNull View drawerView) {
    }

    @Override
    default void onDrawerClosed(@NotNull View drawerView) {
    }

    @Override
    default void onDrawerStateChanged(int newState) {
    }

}
