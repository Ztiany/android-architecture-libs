package com.android.base.adapter.pager;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

@SuppressWarnings("all")
public class ViewPageInfo {

    public final Class<? extends Fragment> clazz;
    public final Bundle args;
    public final String title;

    public ViewPageInfo(String title, Class<? extends Fragment> clazz, Bundle args) {
        this.title = title;
        this.clazz = clazz;
        this.args = args;
    }
}