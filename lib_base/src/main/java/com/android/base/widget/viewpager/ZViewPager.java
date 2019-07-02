package com.android.base.widget.viewpager;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.android.base.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 支持无限轮播的 ViewPager
 *
 * @author Ztiany
 */
public class ZViewPager extends FrameLayout {

    private final ViewPager mViewPager;
    private IPagerNumberView mPageNumberView;

    private List<String> mImageUrlList = new ArrayList<>();

    private OnBannerPositionChangedListener mOnBannerPositionChangedListener;

    private boolean mScalable;
    private String mTransitionName;
    private OnPageClickListener mOnPageClickListener;

    public ZViewPager(Context context) {
        this(context, null);
    }

    public ZViewPager(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ZViewPager(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ZViewPager);

        /*是否可以缩放*/
        mScalable = typedArray.getBoolean(R.styleable.ZViewPager_zvp_scale, false);
        /*用于支持5.0的transition动画*/
        mTransitionName = typedArray.getString(R.styleable.ZViewPager_zvp_item_transition_name);
        int pageId = typedArray.getResourceId(R.styleable.ZViewPager_zvp_pager_number_id, -1);

        typedArray.recycle();

        inflate(context, R.layout.base_widget_banner_view, this);

        mViewPager = getRootView().findViewById(R.id.base_widget_banner_vp);

        if (pageId != -1) {
            View pageNumber = findViewById(pageId);
            if (pageNumber instanceof IPagerNumberView) {
                mPageNumberView = (IPagerNumberView) pageNumber;
                mPageNumberView.setViewPager(this);
            }
        }
    }

    public void setOnBannerPositionChangedListener(OnBannerPositionChangedListener onBannerPositionChangedListener) {
        mOnBannerPositionChangedListener = onBannerPositionChangedListener;
    }

    public void setPageNumberView(IPagerNumberView pageNumberView) {
        mPageNumberView = pageNumberView;
    }

    public void setImages(List<String> entities) {
        if (entities == null || entities.isEmpty()) {
            mImageUrlList.clear();
            mViewPager.setAdapter(null);
            setPageSize(0);
            return;
        }
        mImageUrlList.clear();
        setPageSize(entities.size());
        if (entities.size() > 1) {
            addExtraPage(entities);
            showBanner();
            setLooper();
        } else {
            mImageUrlList.addAll(entities);
            showBanner();
        }
    }

    private void setPageSize(int pageSize) {
        if (mPageNumberView != null) {
            mPageNumberView.setPageSize(pageSize);
        }
    }

    private void setPageScrolled(int position, float positionOffset) {
        if (mPageNumberView != null) {
            mPageNumberView.setPageScrolled(position, positionOffset);
        }
    }

    public void setCurrentPosition(int position) {
        if (mImageUrlList.size() > 1) {
            if (position <= mImageUrlList.size() - 2) {
                position++;
            } else {
                position = mImageUrlList.size() - 2;
            }
        }
        mViewPager.setCurrentItem(position);
    }

    private void setLooper() {
        mViewPager.setCurrentItem(1, false);
        mViewPager.clearOnPageChangeListeners();

        mViewPager.addOnPageChangeListener(
                new ViewPager.SimpleOnPageChangeListener() {

                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                        if (positionOffsetPixels == 0.0) {
                            setPageScrolled(position, positionOffset);
                        }
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {
                        super.onPageScrollStateChanged(state);
                        //(positionOffset为0的时候，并不一定是切换完成，所以动画还在执行，强制再次切换，就会闪屏)
                        switch (state) {
                            case ViewPager.SCROLL_STATE_IDLE:// 空闲状态，没有任何滚动正在进行（表明完成滚动）
                                setViewPagerItemPosition(mViewPager.getCurrentItem());
                                break;
                            case ViewPager.SCROLL_STATE_DRAGGING:// 正在拖动page状态
                                break;
                            case ViewPager.SCROLL_STATE_SETTLING:// 手指已离开屏幕，自动完成剩余的动画效果
                                break;
                        }
                    }

                    @Override
                    public void onPageSelected(int position) {
                        if (mOnBannerPositionChangedListener != null) {
                            if (mImageUrlList.size() > 1) {
                                if (position == 0) {
                                    position = mImageUrlList.size() - 3;
                                } else if (position == mImageUrlList.size() - 1) {
                                    position = 0;
                                } else {
                                    position--;
                                }
                            }
                            mOnBannerPositionChangedListener.onPagePositionChanged(position);
                        }
                    }
                });
    }

    private void addExtraPage(List<String> entities) {
        mImageUrlList.add(entities.get(entities.size() - 1));
        mImageUrlList.addAll(entities);
        mImageUrlList.add(entities.get(0));
    }

    private void showBanner() {
        PagerAdapter adapter;
        if (mScalable) {
            BannerPagerAdapter bannerPagerAdapter = new BannerPagerAdapter(getContext(), mImageUrlList, mTransitionName);
            bannerPagerAdapter.setOnBannerClickListener(mOnPageClickListener);
            adapter = bannerPagerAdapter;
        } else {
            OptimizeBannerPagerAdapter optimizeBannerPagerAdapter = new OptimizeBannerPagerAdapter(getContext(), mImageUrlList, mTransitionName);
            optimizeBannerPagerAdapter.setOnBannerClickListener(mOnPageClickListener);
            adapter = optimizeBannerPagerAdapter;
        }
        mViewPager.setAdapter(adapter);
    }

    public void setOnPageClickListener(OnPageClickListener onPageClickListener) {
        mOnPageClickListener = onPageClickListener;
    }

    private void setViewPagerItemPosition(int position) {
        if (position == mImageUrlList.size() - 1) {
            mViewPager.setCurrentItem(1, false);
        } else if (position == 0) {
            mViewPager.setCurrentItem(mImageUrlList.size() - 2, false);
        }
    }

    public interface OnBannerPositionChangedListener {
        void onPagePositionChanged(int position);
    }

    public static void setScalableBannerPagerFactory(ScalableBannerPagerFactory scalableBannerPagerFactory) {
        BannerPagerAdapter.sBannerPagerFactory = scalableBannerPagerFactory;
    }

}