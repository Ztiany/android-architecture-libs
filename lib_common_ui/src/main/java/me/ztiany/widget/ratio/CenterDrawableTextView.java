package me.ztiany.widget.ratio;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.Gravity;

/**
 * 应用场景，比如：当 TextView 的 width 为 match 时，此时使用的 leftDrawable 是靠左的，且无法通过 Gravity 属性来修改其位置。该控件用于类似此种情况下使 Drawable 居中，仅支持单个方向的 Drawable。
 * 优先级为：左、上、右、下。
 *
 * @author Ztiany
 */
public class CenterDrawableTextView extends RatioTextView {

    private Paint.FontMetrics metrics = new Paint.FontMetrics();

    public CenterDrawableTextView(Context context) {
        super(context);
    }

    public CenterDrawableTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CenterDrawableTextView(Context context, AttributeSet attrs, int defStyleAttr) {
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

        Drawable leftDrawable = compoundDrawables[0];
        Drawable topDrawable = compoundDrawables[1];
        Drawable rightDrawable = compoundDrawables[2];
        Drawable bottomDrawable = compoundDrawables[3];

        if (leftDrawable != null) {
            setLeftDrawableCenter(canvas, leftDrawable);
        } else if (topDrawable != null) {
            setTopDrawableCenter(canvas, topDrawable);
        } else if (rightDrawable != null) {
            setRightDrawableCenter(canvas, rightDrawable);
        } else if (bottomDrawable != null) {
            setBottomDrawableCenter(canvas, bottomDrawable);
        }
    }

    private void setBottomDrawableCenter(Canvas canvas, Drawable downDrawable) {
        setGravity(Gravity.BOTTOM | getGravity());

        int compoundDrawablePadding = getCompoundDrawablePadding();
        TextPaint paint = getPaint();
        paint.getFontMetrics(metrics);
        float contentHeight = (metrics.bottom - metrics.top + compoundDrawablePadding + downDrawable.getIntrinsicHeight());
        int measuredHeight = getMeasuredHeight();
        canvas.translate(0F, -(measuredHeight / 2.0F - contentHeight / 2));
    }

    private void setRightDrawableCenter(Canvas canvas, Drawable rightDrawable) {
        setGravity(Gravity.END | getGravity());

        int compoundDrawablePadding = getCompoundDrawablePadding();
        TextPaint paint = getPaint();
        float measureText = paint.measureText(getText().toString());
        float contentWidth = (measureText + compoundDrawablePadding + rightDrawable.getIntrinsicHeight());
        int measuredWidth = getMeasuredWidth();
        canvas.translate(-(measuredWidth / 2.0F - contentWidth / 2), 0F);
    }

    private void setLeftDrawableCenter(Canvas canvas, Drawable leftDrawable) {
        setGravity(Gravity.START | getGravity());

        int compoundDrawablePadding = getCompoundDrawablePadding();
        TextPaint paint = getPaint();
        float measureText = paint.measureText(getText().toString());
        float contentWidth = (measureText + compoundDrawablePadding + leftDrawable.getIntrinsicHeight());
        int measuredWidth = getMeasuredWidth();
        canvas.translate((measuredWidth / 2.0F - contentWidth / 2), 0F);
    }

    private void setTopDrawableCenter(Canvas canvas, Drawable topDrawable) {
        int compoundDrawablePadding = getCompoundDrawablePadding();
        TextPaint paint = getPaint();
        paint.getFontMetrics(metrics);
        float contentHeight = (metrics.bottom - metrics.top + compoundDrawablePadding + topDrawable.getIntrinsicHeight());
        int measuredHeight = getMeasuredHeight();
        canvas.translate(0F, (measuredHeight / 2.0F - contentHeight / 2));
    }

}