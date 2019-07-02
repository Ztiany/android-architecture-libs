package com.android.base.widget.viewpager;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.android.base.R;
import com.android.base.utils.android.UnitConverter;


public class PageNumberView extends View implements IPagerNumberView {

    private static final String DIVISION = "/";

    private int mPageSize;
    private int mPosition;

    private int mCenterX;

    private Paint mPaint;
    private int mTextHeight;
    private int mTextBaseLine;

    public PageNumberView(Context context) {
        this(context, null);
    }

    public PageNumberView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PageNumberView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.PageNumberView);

        /*是否可以缩放*/
        int color = typedArray.getColor(R.styleable.PageNumberView_pnv_text_color, Color.BLACK);
        /*用于支持5.0的transition动画*/
        int size = typedArray.getDimensionPixelSize(R.styleable.PageNumberView_pnv_text_size, UnitConverter.spToPx(14));

        typedArray.recycle();

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setTextSize(size);
        mPaint.setColor(color);

        Paint.FontMetricsInt fontMetricsInt = mPaint.getFontMetricsInt();
        mTextHeight = fontMetricsInt.bottom - fontMetricsInt.top;
        mTextBaseLine = mTextHeight - fontMetricsInt.bottom;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        String text = String.valueOf(mPageSize);
        float textWidth = mPaint.measureText(text);
        float divisionWidth = mPaint.measureText(DIVISION);
        setMeasuredDimension((int) (textWidth * 2 + divisionWidth), mTextHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float divisionHalfSize = mPaint.measureText(DIVISION) / 2;
        float left = mCenterX - divisionHalfSize;
        float right = mCenterX + divisionHalfSize;
        String positionText = String.valueOf(mPosition);
        String sizeText = String.valueOf(mPageSize);
        canvas.drawText(DIVISION, left, mTextBaseLine, mPaint);
        canvas.drawText(positionText, left - mPaint.measureText(positionText), mTextBaseLine, mPaint);
        canvas.drawText(sizeText, right, mTextBaseLine, mPaint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mCenterX = w / 2;
    }

    @Override
    public void setPageScrolled(int position, @SuppressWarnings("UnusedParameters") float positionOffset) {
        mPosition = position;
        if (mPageSize > 1) {
            if (mPosition == 0) {
                mPosition = mPageSize;
            } else if (mPosition == mPageSize + 1) {
                mPosition = 1;
            }
        } else {
            mPosition = mPageSize;
        }
        invalidate();
    }

    @Override
    public void setViewPager(ZViewPager viewPager) {
        //no op
    }

    @Override
    public void setPageSize(int pageSize) {
        mPageSize = pageSize;
        mPosition = pageSize;//只有一個的時候
        requestLayout();
    }

}