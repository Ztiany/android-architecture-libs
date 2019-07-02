package com.android.base.widget.viewpager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.AppCompatImageView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.base.imageloader.ImageLoaderFactory;

import java.util.ArrayList;
import java.util.List;


class OptimizeBannerPagerAdapter extends PagerAdapter {

    private final String mTransitionName;
    private final Context mContext;
    private final List<String> mEntities;
    private final List<ImageView> mLayouts = new ArrayList<>();
    private OnPageClickListener mOnPageClickListener;
    private boolean mIsLooper;

    OptimizeBannerPagerAdapter(Context context, List<String> entities, String transitionName) {
        mContext = context;
        mTransitionName = transitionName;
        mEntities = entities;
        mIsLooper = mEntities.size() > 1;
        setLayouts();
    }

    private void setLayouts() {
        ImageView view;
        mLayouts.clear();
        for (int i = 0; i < mEntities.size(); i++) {
            view = new AppCompatImageView(mContext);
            mLayouts.add(view);
            setTransitionName(view);
        }
    }

    private void setTransitionName(ImageView bannerLayout) {
        if (!TextUtils.isEmpty(mTransitionName)) {
            ViewCompat.setTransitionName(bannerLayout, mTransitionName);
        }
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public int getCount() {
        return mEntities.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        ImageView image = mLayouts.get(position);
        String url = mEntities.get(position);
        image.setOnClickListener(v -> {
            if (mOnPageClickListener != null) {
                mOnPageClickListener.onClick(image, mIsLooper ? position - 1 : position);
            }
        });
        ImageLoaderFactory.getImageLoader().display(image, url);
        container.addView(image, 0);
        return mLayouts.get(position);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView(mLayouts.get(position));
    }

    @Override
    public void setPrimaryItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        super.setPrimaryItem(container, position, object);
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    void setOnBannerClickListener(OnPageClickListener onBannerClickListener) {
        mOnPageClickListener = onBannerClickListener;
    }

}
