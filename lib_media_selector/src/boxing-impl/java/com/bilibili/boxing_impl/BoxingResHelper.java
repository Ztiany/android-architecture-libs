package com.bilibili.boxing_impl;

import com.bilibili.boxing.model.BoxingManager;
import com.bilibili.boxing.model.callback.MediaFilter;
import com.ztiany.mediaselector.R;

import androidx.annotation.DrawableRes;

/**
 * Help getting the resource in config.
 *
 * @author ChenSL
 */

public class BoxingResHelper {

    @DrawableRes
    public static int getMediaCheckedIndexRes() {
        return R.drawable.shape_boxing_checked;
    }

    @DrawableRes
    public static int getMediaCheckedRes() {
        int result = BoxingManager.getInstance().getBoxingConfig().getMediaCheckedRes();
        return result > 0 ? result : R.drawable.ic_boxing_checked;
    }

    @DrawableRes
    public static int getMediaUncheckedRes() {
        int result = BoxingManager.getInstance().getBoxingConfig().getMediaUnCheckedRes();
        return result > 0 ? result : R.drawable.shape_boxing_unchecked;
    }

    @DrawableRes
    public static int getCameraRes() {
        return BoxingManager.getInstance().getBoxingConfig().getCameraRes();
    }

    public static MediaFilter getMediaFilter() {
        return BoxingManager.getInstance().getBoxingConfig().getMediaFilter();
    }

}
