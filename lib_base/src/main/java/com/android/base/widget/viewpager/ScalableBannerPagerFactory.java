package com.android.base.widget.viewpager;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2019-07-01 17:43
 */
public interface ScalableBannerPagerFactory {

    /**
     * return a scalable ImageView
     */
    ImageView createBannerPagerView(Context context);

    /**
     * Set a click listener for image view which is created by {@link #createBannerPagerView(Context)}, like PhotoView:
     *
     * <pre>{@code
     *         imageView.setOnPhotoTapListener((view, x, y) -> {
     *             if (mClickListener != null) {
     *                 mClickListener.onClick(imageView, mIsLooper ? position - 1 : position);
     *             }
     *         });
     * }</pre>
     */
    void setOnClickListener(ImageView view, View.OnClickListener onClickListener);

}
