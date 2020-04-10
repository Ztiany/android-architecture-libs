@file:JvmName("Resources")

package com.android.base.utils.android.views

import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.TypedValue
import android.view.View
import androidx.annotation.*

import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.android.base.utils.BaseUtils

inline fun <T : TypedArray?, R> T.use(block: (T) -> R): R {
    var recycled = false
    try {
        return block(this)
    } catch (e: Exception) {
        recycled = true
        try {
            this?.recycle()
        } catch (exception: Exception) {
        }
        throw e
    } finally {
        if (!recycled) {
            this?.recycle()
        }
    }
}

fun Fragment.colorFromId(@ColorRes colorRes: Int): Int {
    val safeContext = context ?: return 0
    return ContextCompat.getColor(safeContext, colorRes)
}

fun Fragment.drawableFromId(@DrawableRes colorRes: Int): Drawable? {
    val safeContext = context ?: return null
    return ContextCompat.getDrawable(safeContext, colorRes)
}

fun View.colorFromId(@ColorRes colorRes: Int): Int {
    val safeContext = context ?: return 0
    return ContextCompat.getColor(safeContext, colorRes)
}

fun View.drawableFromId(@DrawableRes colorRes: Int): Drawable? {
    val safeContext = context ?: return null
    return ContextCompat.getDrawable(safeContext, colorRes)
}

fun Context.colorFromId(@ColorRes id: Int): Int {
    return ContextCompat.getColor(this, id)
}

fun Context.drawableFromId(@DrawableRes id: Int): Drawable? {
    return ContextCompat.getDrawable(this, id)
}

/**
 * - [name] 资源的名称，如 ic_launcher 或者 com.example.android/drawable/ic_launcher(这是，下面两个参数可以省略)
 * - [defType] 资源的类型，如 drawable
 * - [defPackage] 包名
 *
 * 返回资源 id
 */
fun getResourceId(name: String, defType: String, defPackage: String): Int {
    return BaseUtils.getResources().getIdentifier(name, defType, defPackage)
}

/**
 * - attr, like [android.R.attr.selectableItemBackground] or other attr id.
 */
fun getResourceId(context: Context, attr: Int): Int {
    return try {
        val outValue = TypedValue()
        context.theme.resolveAttribute(attr, outValue, true)
        outValue.resourceId
    } catch (e: Exception) {
        e.printStackTrace()
        0
    }
}

fun getText(@StringRes id: Int): CharSequence {
    return BaseUtils.getResources().getText(id)
}

fun getString(@StringRes id: Int): String {
    return BaseUtils.getResources().getString(id)
}

fun getString(@StringRes id: Int, vararg formatArgs: Any): String {
    return BaseUtils.getResources().getString(id, *formatArgs)
}

fun getStringArray(@ArrayRes id: Int): Array<String> {
    return BaseUtils.getResources().getStringArray(id)
}

fun getIntArray(@ArrayRes id: Int): IntArray {
    return BaseUtils.getResources().getIntArray(id)
}

fun createResourceUri(id: Int): Uri {
    return Uri.parse("android.resource://" + BaseUtils.getAppContext().packageName + "/" + id)
}

fun createAssetsUri(path: String): Uri {
    return Uri.parse("file:///android_asset/$path")
}

fun getStyledColor(context: Context, @AttrRes attr: Int): Int {
    val a = context.obtainStyledAttributes(null, intArrayOf(attr))
    try {
        return a.getColor(0, 0x000000)
    } finally {
        a.recycle()
    }
}

fun getStyledDrawable(context: Context, @AttrRes attr: Int): Drawable? {
    val a = context.obtainStyledAttributes(null, intArrayOf(attr))
    try {
        return a.getDrawable(0)
    } finally {
        a.recycle()
    }
}

fun getDimensionPixelSize(dimenId: Int): Int {
    return BaseUtils.getResources().getDimensionPixelSize(dimenId)
}