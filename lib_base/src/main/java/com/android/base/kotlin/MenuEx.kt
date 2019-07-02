package com.android.base.kotlin

import android.view.MenuItem

fun MenuItem.alwaysShow(): MenuItem {
    setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
    return this
}

fun MenuItem.setSimpleClickListener(onClick: (MenuItem) -> Unit): MenuItem {
    setOnMenuItemClickListener {
        onClick(it)
        true
    }
    return this
}