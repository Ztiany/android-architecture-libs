package com.android.base.utils.android.text;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.style.ReplacementSpan;

import androidx.annotation.NonNull;

/**
 * 圆角背景Span
 */
public class RoundedBackgroundSpan extends ReplacementSpan {

    private int mPadding;
    private int mBackgroundColor;
    private int mTextColor;
    private int mCorner;

    public RoundedBackgroundSpan(int backgroundColor, int textColor, int padding, int corner) {
        super();
        mBackgroundColor = backgroundColor;
        mTextColor = textColor;
        mCorner = corner;
        mPadding = padding;
    }

    @Override
    public int getSize(@NonNull Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
        return (int) (mPadding + paint.measureText(text.subSequence(start, end).toString()) + mPadding);
    }

    @Override
    public void draw(@NonNull Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, @NonNull Paint paint) {
        float width = paint.measureText(text.subSequence(start, end).toString());
        RectF rect = new RectF(x, top, x + width + (2 * mPadding), bottom);
        paint.setColor(mBackgroundColor);
        canvas.drawRoundRect(rect, mCorner, mCorner, paint);
        paint.setColor(mTextColor);
        canvas.drawText(text, start, end, x + mPadding, y, paint);
    }

}