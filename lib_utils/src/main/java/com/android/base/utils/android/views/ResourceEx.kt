@file:JvmName("Resources")

package com.android.base.utils.android.views

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.TypedValue
import android.view.View
import androidx.annotation.*
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.android.base.utils.BaseUtils
import com.android.base.utils.android.AppUtils

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

///////////////////////////////////////////////////////////////////////////
// color & drawable
///////////////////////////////////////////////////////////////////////////
fun Fragment.getColorCompat(@ColorRes colorRes: Int): Int {
    val safeContext = context ?: return 0
    return ContextCompat.getColor(safeContext, colorRes)
}

fun Fragment.getDrawableCompat(@DrawableRes colorRes: Int): Drawable? {
    val safeContext = context ?: return null
    return ContextCompat.getDrawable(safeContext, colorRes)
}

fun View.getColorCompat(@ColorRes colorRes: Int): Int {
    val safeContext = context ?: return 0
    return ContextCompat.getColor(safeContext, colorRes)
}

fun View.getDrawableCompat(@DrawableRes colorRes: Int): Drawable? {
    val safeContext = context ?: return null
    return ContextCompat.getDrawable(safeContext, colorRes)
}

fun Context.getColorCompat(@ColorRes id: Int): Int {
    return ContextCompat.getColor(this, id)
}

fun Context.getDrawableCompat(@DrawableRes id: Int): Drawable? {
    return ContextCompat.getDrawable(this, id)
}

fun RecyclerView.ViewHolder.getColorCompat(@ColorRes id: Int): Int {
    val safeContext = this.itemView.context ?: return 0
    return ContextCompat.getColor(safeContext, id)
}

fun RecyclerView.ViewHolder.getDrawableCompat(@DrawableRes id: Int): Drawable? {
    val safeContext = this.itemView.context ?: return null
    return ContextCompat.getDrawable(safeContext, id)
}

/**
 * - see [https://mp.weixin.qq.com/s?__biz=MzAwODY4OTk2Mg==&mid=2652064078&idx=2&sn=884a85bfa4f9e19ec531b8245dca6314&chksm=808ce90bb7fb601dd9e144ae796eb466a2ed805bf14dbc0906543509d95c24733b05bd7fc12c&mpshare=1&scene=1&srcid=0831g6YpKX89pYtCnFVNtFSn&sharer_sharetime=1598887915007&sharer_shareid=837da3c9c7d8315352e3f3c120932755&key=573aef4c1f9b4b5f6b748d6d2bfe57b91121aed37b7ab5befb427350215abee2d42095153128819e3458a2f7900e0b083f777100a3df99374344e4d71d46d2f4fd9f1899809b98d63f5f75f20d075c994d742b73d51bd81cbc1f9daebc6366021f69043f1ca77a85434e6261b9a1d74582dbff5ba02636166dadd2b1750bb0e7&ascene=1&uin=MTE4NTEwMDEzMA%3D%3D&devicetype=Windows+10+x64&version=62090538&lang=en&exportkey=A96nCDrEWbr1qzYfBqWXtt4%3D&pass_ticket=ss1Q7B2SYeRyfoU%2FAdoyx1xQRnQRw3XnUna%2FNKH3Q%2FnYnBXqqpEL3CF%2BMICAcEIk]
 * - see [https://stackoverflow.com/questions/43004886/resourcescompat-getdrawable-vs-appcompatresources-getdrawable]
 */
fun Context.getColorStateListCompat(@ColorRes id: Int): ColorStateList {
    return AppCompatResources.getColorStateList(this, id)
}

///////////////////////////////////////////////////////////////////////////
// style
///////////////////////////////////////////////////////////////////////////
/**
 * - [name] 资源的名称，如 ic_launcher 或者 com.example.android/drawable/ic_launcher(这时，下面两个参数可以省略)
 * - [defType] 资源的类型，如 drawable
 * - [defPackage] 包名
 *
 * 返回资源 id
 */
fun getResourceId(context: Context, name: String, defType: String = "", defPackage: String = ""): Int {
    return context.resources.getIdentifier(name, defType, defPackage)
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


///////////////////////////////////////////////////////////////////////////
// text
///////////////////////////////////////////////////////////////////////////
private fun getContext(): Context {
    return AppUtils.getTopActivity() ?: BaseUtils.getAppContext()
}

fun getText(@StringRes id: Int): CharSequence {
    return getContext().resources.getText(id)
}

fun getString(@StringRes id: Int): String {
    return getContext().resources.getString(id)
}

fun getString(@StringRes id: Int, vararg formatArgs: Any): String {
    return getContext().resources.getString(id, *formatArgs)
}

fun getStringArray(@ArrayRes id: Int): Array<String> {
    return getContext().resources.getStringArray(id)
}

fun getDimensionPixelSize(dimenId: Int): Int {
    return getContext().resources.getDimensionPixelSize(dimenId)
}

fun getIntArray(@ArrayRes id: Int): IntArray {
    return BaseUtils.getResources().getIntArray(id)
}

///////////////////////////////////////////////////////////////////////////
// uri
///////////////////////////////////////////////////////////////////////////
fun createResourceUri(id: Int): Uri {
    return Uri.parse("android.resource://" + BaseUtils.getAppContext().packageName + "/" + id)
}

fun createAssetsUri(path: String): Uri {
    return Uri.parse("file:///android_asset/$path")
}
