package com.android.base.utils.android.views

import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2

/** get the ViewPager2's internal [RecyclerView]. */
fun ViewPager2.recyclerView(): RecyclerView? = this.getChildAt(0) as? RecyclerView