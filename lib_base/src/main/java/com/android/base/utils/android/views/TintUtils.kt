package com.android.base.utils.android.views

import android.content.res.ColorStateList
import android.graphics.*
import android.graphics.drawable.Drawable
import androidx.core.graphics.drawable.DrawableCompat


fun tintBitmap(inBitmap: Bitmap, tintColor: Int): Bitmap {
    val outBitmap = Bitmap.createBitmap(inBitmap.width, inBitmap.height, inBitmap.config)
    val canvas = Canvas(outBitmap)
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    paint.colorFilter = PorterDuffColorFilter(tintColor, PorterDuff.Mode.SRC_IN)
    canvas.drawBitmap(inBitmap, 0f, 0f, paint)
    return outBitmap
}

fun tint(originDrawable: Drawable, color: Int): Drawable {
    return tint(originDrawable, ColorStateList.valueOf(color))
}

fun tint(originDrawable: Drawable, color: Int, tintMode: PorterDuff.Mode? = null): Drawable {
    return tint(originDrawable, ColorStateList.valueOf(color), tintMode)
}

/**
 * usage:
 * ```
 *  int[][] states = new int[][]{
 *      new int[]{android.R.attr.state_selected}, // pressed
 *      new int[]{-android.R.attr.state_selected}  // unpressed
 *  };
 *  int[] colors = new int[]{
 *      Color.BLACK,
 *      Color.GRAY
 *  };
 *  ColorStateList colorStateList = new ColorStateList(states, colors);
 *  Drawable aaa = ContextCompat.getDrawable(getContext(), R.drawable.aaa);
 *  Drawable bbb = ContextCompat.getDrawable(getContext(), R.drawable.bbb);
 *  Drawable aaaTint = TintUtils.tint(aaa, colorStateList);
 *  Drawable bbbTint = TintUtils.tint(bbb, colorStateList);
 *  aaaTv.setCompoundDrawablesWithIntrinsicBounds(null, aaaTint, null, null);
 *  bbbTv.setCompoundDrawablesWithIntrinsicBounds(null, bbbTint, null, null);
 * ```
 */
@JvmOverloads
fun tint(originDrawable: Drawable, colorStateList: ColorStateList, tintMode: PorterDuff.Mode? = null): Drawable {
    val tintDrawable = DrawableCompat.wrap(originDrawable)
    if (tintMode != null) {
        DrawableCompat.setTintMode(tintDrawable, tintMode)
    }
    DrawableCompat.setTintList(tintDrawable, colorStateList)
    return tintDrawable
}
