package me.ztiany.widget.viewpager;

import android.content.Context;
import android.content.res.TypedArray;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.android.sdk.ui.R;

import java.util.ArrayList;
import java.util.List;

import androidx.viewpager.widget.ViewPager;

/**
 * 支持无限轮播的 ViewPager
 *
 * @author Ztiany
 */
public class BannerViewPager extends FrameLayout {

    private final ViewPager mViewPager;

    private IPagerNumberView mPageNumberView;

    private final List<Uri> mImageUrlList = new ArrayList<>();

    private OnBannerPositionChangedListener mOnBannerPositionChangedListener;

    private final String mTransitionName;

    private OnPageClickListener mOnPageClickListener;

    private boolean mEnableLopper = false;

    public BannerViewPager(Context context) {
        this(context, null);
    }

    public BannerViewPager(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BannerViewPager(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BannerViewPager);

        /*用于支持5.0的transition动画*/
        mTransitionName = typedArray.getString(R.styleable.BannerViewPager_zvp_item_transition_name);
        int pageId = typedArray.getResourceId(R.styleable.BannerViewPager_zvp_pager_number_id, -1);

        typedArray.recycle();

        inflate(context, R.layout.base_ui_widget_banner_view, this);

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

    public void setImages(List<Uri> entities, BannerViewPagerAdapter adapter) {
        if (entities == null || entities.isEmpty()) {
            mImageUrlList.clear();
            mViewPager.setAdapter(null);
            setPageSize(0);
            return;
        }

        mImageUrlList.clear();
        setPageSize(entities.size());

        if (entities.size() > 1 && mEnableLopper) {
            addExtraPage(entities);
            showBanner(adapter);
            setLooperListener();
        } else {
            mImageUrlList.addAll(entities);
            showBanner(adapter);
            setNormalListener();
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

    public void setEnableLooper(boolean enableLooper) {
        mEnableLopper = enableLooper;
    }

    public void setCurrentPosition(int position) {
        if (position < 0) {
            return;
        }

        if (!mEnableLopper) {
            mViewPager.setCurrentItem(position);
            if (mOnBannerPositionChangedListener != null) {
                mOnBannerPositionChangedListener.onPagePositionChanged(position);
            }
            return;
        }

        if (mImageUrlList.size() > 1) {
            int realSize = mImageUrlList.size() - 2;
            if (position >= realSize) {
                position = realSize - 1;
            }
            if (mOnBannerPositionChangedListener != null) {
                mOnBannerPositionChangedListener.onPagePositionChanged(position);
            }
            position++;
        } else {
            if (mOnBannerPositionChangedListener != null) {
                mOnBannerPositionChangedListener.onPagePositionChanged(0);
            }
        }

        mViewPager.setCurrentItem(position);
    }

    private void setNormalListener() {
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
                    public void onPageSelected(int position) {
                        mOnBannerPositionChangedListener.onPagePositionChanged(position);
                    }
                });
    }

    private void setLooperListener() {
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

    private void addExtraPage(List<Uri> entities) {
        mImageUrlList.add(entities.get(entities.size() - 1));
        mImageUrlList.addAll(entities);
        mImageUrlList.add(entities.get(0));
    }

    private void showBanner(BannerViewPagerAdapter adapter) {
        adapter.setContext(getContext());
        adapter.setTransitionName(mTransitionName);
        adapter.setEntities(mImageUrlList, mEnableLopper);
        adapter.setOnBannerClickListener(mOnPageClickListener);
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

}