package com.android.base.widget.ratio;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;

/**
 * 使 TopDrawable 居中，不要设置 gravity 属性为 center 或 center_vertical
 *
 * @author Ztiany
 * Email: 1169654504@qq.com
 */
public class TopDrawableCenterTextView extends RatioTextView {

    private Paint.FontMetrics metrics = new Paint.FontMetrics();

    public TopDrawableCenterTextView(Context context) {
        super(context);
    }

    public TopDrawableCenterTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TopDrawableCenterTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Drawable[] compoundDrawables = getCompoundDrawables();
        setToCenterPadding(compoundDrawables, canvas);
        super.onDraw(canvas);
    }

    private void setToCenterPadding(Drawable[] compoundDrawables, Canvas canvas) {
        if (compoundDrawables == null) {
            return;
        }

        Drawable topDrawable = compoundDrawables[1];

        if (topDrawable == null) {
            return;
        }

        int compoundDrawablePadding = getCompoundDrawablePadding();
        TextPaint paint = getPaint();
        paint.getFontMetrics(metrics);
        float contentHeight = (metrics.bottom - metrics.top + compoundDrawablePadding + topDrawable.getIntrinsicHeight());
        int measuredHeight = getMeasuredHeight();
        canvas.translate(0, measuredHeight / 2 - contentHeight / 2);
    }

}
