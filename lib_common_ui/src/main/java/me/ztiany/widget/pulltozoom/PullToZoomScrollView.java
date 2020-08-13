package me.ztiany.widget.pulltozoom;

import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.android.sdk.ui.R;


/**
 * usage:
 * <pre>
 *     {@code
 *     <com.android.base.widget.pulltozoom.PullToZoomScrollView
 *         android:id="@+id/me_sv_content"
 *         android:layout_width="match_parent"
 *         android:layout_height="match_parent"
 *         android:overScrollMode="always"
 *         app:psv_header_view="@+id/fl_header"
 *         app:psv_zoom_view="@+id/iv_zoom">
 *
 *         <LinearLayout
 *             android:layout_width="match_parent"
 *             android:layout_height="wrap_content"
 *             android:orientation="vertical">
 *
 *             //header
 *             <RelativeLayout
 *                 android:id="@+id/fl_header"
 *                 android:layout_width="match_parent"
 *                 android:layout_height="@dimen/user_center_header_height">
 *
 *                  //image view to pull zoom
 *                 <ImageView
 *                     android:id="@+id/iv_zoom"
 *                     android:layout_width="match_parent"
 *                     android:layout_height="wrap_content"
 *                     android:background="@drawable/img_top_bg"
 *                     tools:ignore="ContentDescription"/>
 *
 *              </RelativeLayout>
 *
 *              //content
 *              <android.support.v7.widget.RecyclerView
 *                 android:id="@id/base_list_view"
 *                 android:layout_width="match_parent"
 *                 android:layout_height="wrap_content"/>
 *
 *          </LinearLayout>
 *
 *         </com.android.base.widget.pulltozoom.PullToZoomScrollView>
 *     }
 * </pre>
 */
public class PullToZoomScrollView extends NestedScrollView {

    private OnScrollListener mScrollListener;

    private View mZoomView;
    private int mZoomViewId;

    private View mContainerView;
    private int mContainerViewId;

    private int mMaxZoomHeight;
    private int mOriginContainerViewHeight;
    private int mOriginZoomViewHeight;
    private int mDamp;
    private float mZoomFactory;

    private TimeInterpolator mInterpolator;
    private ValueAnimator mValueAnimator;
    private static final int ANIMATION_TIME = 500;

    public PullToZoomScrollView(Context context) {
        this(context, null);
    }

    public PullToZoomScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PullToZoomScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.PullToZoomScrollView);
        mZoomViewId = typedArray.getResourceId(R.styleable.PullToZoomScrollView_psv_zoom_view, -1);
        mContainerViewId = typedArray.getResourceId(R.styleable.PullToZoomScrollView_psv_header_view, -1);
        //默认阻尼2
        mDamp = typedArray.getInteger(R.styleable.PullToZoomScrollView_psv_damp, 2);
        //默认最大scroll 1500DP
        mMaxZoomHeight = typedArray.getInteger(R.styleable.PullToZoomScrollView_psv_max_over, dpToPx(1500));
        mZoomFactory = typedArray.getFloat(R.styleable.PullToZoomScrollView_psv_zoom_factory, 2.0F);
        typedArray.recycle();

        init();
    }

    private void init() {
        mInterpolator = new DecelerateInterpolator();
    }

    public void setScrollListener(OnScrollListener scrollListener) {
        mScrollListener = scrollListener;
    }

    public interface OnScrollListener {
        void onScroll(int headerLayoutHeight, int zoomViewHeight, int scrollY);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (mZoomViewId != -1) {
            mZoomView = findViewById(mZoomViewId);
        }
        if (mContainerViewId != -1) {
            mContainerView = findViewById(mContainerViewId);
        }
        getInnerViewHeight();
    }

    private boolean innerViewInitialized() {
        return mContainerView != null && mZoomView != null && mOriginContainerViewHeight != 0 && mOriginZoomViewHeight != 0;
    }

    @Override
    protected boolean overScrollByCompat(int deltaX, int deltaY,
                                         int scrollX, int scrollY,
                                         int scrollRangeX, int scrollRangeY,
                                         int maxOverScrollX,
                                         int maxOverScrollY, boolean isTouchEvent) {
        //overScrollBy如果返回True，那么ScrollView将不会滑动
        return processOverScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent)
                || super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent);
    }

    @SuppressWarnings("unused")
    private boolean processOverScrollBy(int deltaX, int deltaY, int scrollX,
                                        int scrollY, int scrollRangeX, int scrollRangeY,
                                        int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
        if (!innerViewInitialized()) {
            return false;
        }
        if (mContainerView.getHeight() <= mMaxZoomHeight && isTouchEvent) {
            if (deltaY < 0) {
                int offset = (int) (deltaY * 1.0F / mDamp);
                if (mContainerView.getHeight() - offset >= mOriginContainerViewHeight) {
                    int height = mContainerView.getHeight() - offset < mMaxZoomHeight ? mContainerView.getHeight() - offset : mMaxZoomHeight;
                    setContainerHeight(height);
                }
            } else {
                if (mContainerView.getHeight() > mOriginContainerViewHeight) {
                    int height = mContainerView.getHeight() - deltaY > mOriginContainerViewHeight ? mContainerView.getHeight() - deltaY : mOriginContainerViewHeight;
                    setContainerHeight(height);
                    return true;
                }
            }
        }
        return false;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (innerViewInitialized()) {
            int action = ev.getAction();
            if (action == MotionEvent.ACTION_DOWN) {
                cancelAnim();
            }
            if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
                zoomBackIfNeed();
            }
        }
        return super.onTouchEvent(ev);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mScrollListener != null) {
            if (mOriginContainerViewHeight != 0 && mOriginZoomViewHeight != 0) {
                mScrollListener.onScroll(mOriginContainerViewHeight, mOriginZoomViewHeight, getScrollY());
            }
        }
    }

    private void zoomBackIfNeed() {
        cancelAnim();
        if (mOriginContainerViewHeight - 1 < mContainerView.getHeight()) {
            mValueAnimator = ValueAnimator.ofInt(mContainerView.getHeight(), mOriginContainerViewHeight);
            mValueAnimator.setDuration(ANIMATION_TIME);
            mValueAnimator.setInterpolator(mInterpolator);
            mValueAnimator.addUpdateListener(mAnimatorUpdateListener);
            mValueAnimator.start();
        }
    }

    private void cancelAnim() {
        if (mValueAnimator != null) {
            mValueAnimator.cancel();
            mValueAnimator = null;
        }
    }

    private ValueAnimator.AnimatorUpdateListener mAnimatorUpdateListener = animation -> {
        int animatedValue = (int) animation.getAnimatedValue();
        setContainerHeight(animatedValue);
    };

    private void setContainerHeight(int height) {
        mContainerView.getLayoutParams().height = height;
        if (height >= mOriginContainerViewHeight) {
            zoomView(height);
        }
        mContainerView.setLayoutParams(mContainerView.getLayoutParams());
    }

    private void zoomView(int height) {
        int measuredWidth = mZoomView.getMeasuredWidth();
        mZoomView.setPivotX(measuredWidth / 2);
        mZoomView.setPivotY(mOriginContainerViewHeight / 3F);
        float addOffset = (height - mOriginContainerViewHeight) * mZoomFactory / mOriginContainerViewHeight;
        float scale = height * 1.0F / mOriginContainerViewHeight + addOffset;

        if (!Float.isInfinite(scale) && !Float.isNaN(scale) && scale >= 1F) {
            mZoomView.setScaleX(scale);
            mZoomView.setScaleY(scale);
        } else {
            mZoomView.setScaleX(1);
            mZoomView.setScaleY(1);
        }
    }

    private void getInnerViewHeight() {
        post(() -> {
            if (mContainerView != null) {
                mOriginContainerViewHeight = mContainerView.getHeight();
            }
            if (mZoomView != null) {
                mOriginZoomViewHeight = mZoomView.getHeight();
            }
        });
    }

    public int dpToPx(int dp) {
        return (int) (dp * getContext().getResources().getDisplayMetrics().density + 0.5f);
    }

}