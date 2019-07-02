package com.android.base.utils.android;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Picture;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

public class ViewUtils {

    private ViewUtils() {
    }

    private static final int ACTION_VISIBLE = 0x01;
    private static final int ACTION_GONE = 0x02;
    private static final int ACTION_INVISIBLE = 0x03;
    private static final int ACTION_DISABLE = 0x04;
    private static final int ACTION_ENABLE = 0x05;

    public static boolean measureWithMaxSize(View view) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams == null || (layoutParams.width == ViewGroup.LayoutParams.MATCH_PARENT && layoutParams.height == ViewGroup.LayoutParams.MATCH_PARENT)) {
            return false;
        }
        int size = 1 << 30 - 1;//即后30位
        int measureSpec = View.MeasureSpec.makeMeasureSpec(size, View.MeasureSpec.AT_MOST);
        view.measure(measureSpec, measureSpec);
        return true;
    }

    public static boolean measureWithScreenSize(View view) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams == null || (layoutParams.width == ViewGroup.LayoutParams.MATCH_PARENT && layoutParams.height == ViewGroup.LayoutParams.MATCH_PARENT)) {
            return false;
        }
        view.measure(
                View.MeasureSpec.makeMeasureSpec(WindowUtils.getScreenWidth(), View.MeasureSpec.AT_MOST),
                View.MeasureSpec.makeMeasureSpec(WindowUtils.getScreenHeight(), View.MeasureSpec.AT_MOST));
        return true;
    }

    public static boolean measureWithSize(View view, int width, int height) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams == null || (layoutParams.width == ViewGroup.LayoutParams.MATCH_PARENT && layoutParams.height == ViewGroup.LayoutParams.MATCH_PARENT)) {
            return false;
        }
        view.measure(
                View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.AT_MOST),
                View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.AT_MOST));
        return true;
    }


    public static void disable(View view1, View view2) {
        view1.setEnabled(false);
        view2.setEnabled(false);
    }

    public static void disable(View view1, View view2, View view3) {
        view1.setEnabled(false);
        view2.setEnabled(false);
        view3.setEnabled(false);
    }

    public static void disable(View view1, View view2, View view3, View... views) {
        view1.setEnabled(false);
        view2.setEnabled(false);
        view3.setEnabled(false);
        doAction(ACTION_DISABLE, views);
    }

    public static void enable(View view1, View view2) {
        view1.setEnabled(true);
        view2.setEnabled(true);
    }

    public static void enable(View view1, View view2, View view3) {
        view1.setEnabled(true);
        view2.setEnabled(true);
        view3.setEnabled(true);
    }

    public static void enable(View view1, View view2, View view3, View... views) {
        view1.setEnabled(true);
        view2.setEnabled(true);
        view3.setEnabled(true);
        doAction(ACTION_ENABLE, views);
    }


    public static void gone(View view1, View view2) {
        view1.setVisibility(View.GONE);
        view2.setVisibility(View.GONE);
    }

    public static void gone(View view1, View view2, View view3) {
        view1.setVisibility(View.GONE);
        view2.setVisibility(View.GONE);
        view3.setVisibility(View.GONE);
    }

    public static void gone(View view1, View view2, View view3, View... views) {
        view1.setVisibility(View.GONE);
        view2.setVisibility(View.GONE);
        view3.setVisibility(View.GONE);
        doAction(ACTION_GONE, views);
    }

    public static void visible(View view1, View view2) {
        view1.setVisibility(View.VISIBLE);
        view2.setVisibility(View.VISIBLE);
    }

    public static void visible(View view1, View view2, View view3) {
        view1.setVisibility(View.VISIBLE);
        view2.setVisibility(View.VISIBLE);
        view3.setVisibility(View.VISIBLE);
    }

    public static void visible(View view1, View view2, View view3, View... views) {
        view1.setVisibility(View.VISIBLE);
        view2.setVisibility(View.VISIBLE);
        view3.setVisibility(View.VISIBLE);
        doAction(ACTION_VISIBLE, views);
    }

    public static void invisible(View view1, View view2) {
        view1.setVisibility(View.INVISIBLE);
        view2.setVisibility(View.INVISIBLE);
    }

    public static void invisible(View view1, View view2, View view3) {
        view1.setVisibility(View.INVISIBLE);
        view2.setVisibility(View.INVISIBLE);
        view3.setVisibility(View.INVISIBLE);
    }

    public static void invisible(View view1, View view2, View view3, View... views) {
        view1.setVisibility(View.INVISIBLE);
        view2.setVisibility(View.INVISIBLE);
        view3.setVisibility(View.INVISIBLE);
        doAction(ACTION_INVISIBLE, views);
    }

    private static void doAction(int action, View... views) {
        for (View view : views) {
            if (action == ACTION_GONE) {
                view.setVisibility(View.GONE);
            } else if (action == ACTION_INVISIBLE) {
                view.setVisibility(View.INVISIBLE);
            } else if (action == ACTION_VISIBLE) {
                view.setVisibility(View.VISIBLE);
            } else if (action == ACTION_ENABLE) {
                view.setEnabled(true);
            } else if (action == ACTION_DISABLE) {
                view.setEnabled(false);
            }
        }
    }

    public static void setBackgroundDrawable(View view, Drawable drawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackground(drawable);
        } else {
            view.setBackgroundDrawable(drawable);
        }
    }

    public static <V extends View> V find(View view, @IdRes int viewId) {
        @SuppressWarnings("unchecked")
        V v = (V) view.findViewById(viewId);
        return v;
    }

    public static Bitmap captureBitmapFromWebView(WebView webView) {
        Picture snapShot = webView.capturePicture();
        Bitmap bmp = Bitmap.createBitmap(snapShot.getWidth(), snapShot.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmp);
        snapShot.draw(canvas);
        return bmp;
    }

    public static void clearTextDrawable(TextView textView) {
        textView.setCompoundDrawables(null, null, null, null);
    }

    @Nullable
    public static FragmentActivity getRealContext(View view) {
        Context context = view.getContext();
        while (context instanceof android.content.ContextWrapper) {
            if (context instanceof FragmentActivity) {
                return (FragmentActivity) context;
            }
            context = ((android.content.ContextWrapper) context).getBaseContext();
        }
        return null;
    }

}
