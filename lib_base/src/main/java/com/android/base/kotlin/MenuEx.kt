package com.android.base.kotlin

import android.support.v7.widget.PopupMenu
import android.view.Menu
import android.view.MenuItem

fun PopupMenu.setMenus(items: Array<String>): PopupMenu {
    for (i in 0 until items.size) {
        menu.add(Menu.NONE, i, i, items[i])
    }
    return this
}

fun PopupMenu.setMenus(items: Array<String>, ids: IntArray): PopupMenu {
    for (i in 0 until items.size) {
        menu.add(Menu.NONE, ids[i], i, items[i])
    }
    return this
}

fun PopupMenu.onMenuItemClick(listener: (id: Int) -> Unit): PopupMenu {
    setOnMenuItemClickListener {
        listener(it.itemId)
        true
    }
    return this
}

fun MenuItem.alwaysShow(): MenuItem {
    setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
    return this
}

fun MenuItem.onMenuItemClick(onClick: (MenuItem) -> Unit): MenuItem {
    setOnMenuItemClickListener {
        onClick(it)
        true
    }
    return this
}