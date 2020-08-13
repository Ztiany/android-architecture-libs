package me.ztiany.widget.ratio;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.android.sdk.ui.R;


public class RatioLayout extends FrameLayout {

    // 宽和高的比例
    private float ratio;

    public RatioLayout(Context context) {
        this(context, null);
    }

    public RatioLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RatioLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RatioLayout);
        ratio = a.getFloat(R.styleable.RatioLayout_layout_ratio, 0.0f);
        a.recycle();
    }

    public void setRatio(float f) {
        ratio = f;
        requestLayout();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int width = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight();
        int height = MeasureSpec.getSize(heightMeasureSpec) - getPaddingTop() - getPaddingBottom();

        if (widthMode == MeasureSpec.EXACTLY && heightMode != MeasureSpec.EXACTLY && ratio != 0.0f) {

            height = (int) (width / ratio + 0.5f);
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(height + getPaddingTop() + getPaddingBottom(), MeasureSpec.EXACTLY);

        } else if (widthMode != MeasureSpec.EXACTLY && heightMode == MeasureSpec.EXACTLY && ratio != 0.0f) {

            width = (int) (height * ratio + 0.5f);
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(width + getPaddingLeft() + getPaddingRight(), MeasureSpec.EXACTLY);

        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
