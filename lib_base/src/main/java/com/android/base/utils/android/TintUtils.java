package com.android.base.utils.android;

import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;

import androidx.core.graphics.drawable.DrawableCompat;

/**
 * usage:
 * <pre>
 *     {@code
 *
 *              int[][] states = new int[][]{
 *                  new int[]{android.R.attr.state_selected}, // pressed
 *                  new int[]{-android.R.attr.state_selected}  // unpressed
 *             };
 *
 *              int[] colors = new int[]{
 *                  Color.BLACK,
 *                  Color.GRAY
 *              };
 *
 *              ColorStateList colorStateList = new ColorStateList(states, colors);
 *              Drawable aaa = ContextCompat.getDrawable(getContext(), R.drawable.aaa);
 *              Drawable bbb = ContextCompat.getDrawable(getContext(), R.drawable.bbb);
 *              Drawable aaaTint = TintUtils.tint(aaa, colorStateList);
 *              Drawable bbbTint = TintUtils.tint(bbb, colorStateList);
 *              aaaTv.setCompoundDrawablesWithIntrinsicBounds(null, aaaTint, null, null);
 *              bbbTv.setCompoundDrawablesWithIntrinsicBounds(null, bbbTint, null, null);
 * </pre>
 */
@SuppressWarnings("unused,WeakerAccess")
public class TintUtils {

    private TintUtils() {
    }

    public static Drawable tint(Drawable originDrawable, int color) {
        return tint(originDrawable, ColorStateList.valueOf(color));
    }

    public static Drawable tint(Drawable originDrawable, int color, PorterDuff.Mode tintMode) {
        return tint(originDrawable, ColorStateList.valueOf(color), tintMode);
    }

    public static Drawable tint(Drawable originDrawable, ColorStateList colorStateList) {
        return tint(originDrawable, colorStateList, null);
    }

    public static Drawable tint(Drawable originDrawable, ColorStateList colorStateList, PorterDuff.Mode tintMode) {
        Drawable tintDrawable = DrawableCompat.wrap(originDrawable);
        if (tintMode != null) {
            DrawableCompat.setTintMode(tintDrawable, tintMode);
        }
        DrawableCompat.setTintList(tintDrawable, colorStateList);
        return tintDrawable;
    }

    public static Bitmap tintBitmap(Bitmap inBitmap, int tintColor) {
        if (inBitmap == null) {
            return null;
        }
        Bitmap outBitmap = Bitmap.createBitmap(inBitmap.getWidth(), inBitmap.getHeight(), inBitmap.getConfig());
        Canvas canvas = new Canvas(outBitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColorFilter(new PorterDuffColorFilter(tintColor, PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(inBitmap, 0, 0, paint);
        return outBitmap;
    }

}