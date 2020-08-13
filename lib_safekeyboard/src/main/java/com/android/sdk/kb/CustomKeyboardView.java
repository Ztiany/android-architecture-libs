package com.android.sdk.kb;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.inputmethodservice.Keyboard.Key;
import android.inputmethodservice.KeyboardView;
import android.util.AttributeSet;

import java.util.List;

import androidx.annotation.ColorInt;

/**
 * 承担键盘绘制工作
 */
public class CustomKeyboardView extends KeyboardView {

    private Drawable mKeyDrawable;
    private Rect mRect;
    private Paint mPaint;

    public CustomKeyboardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initResources();
    }

    public CustomKeyboardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initResources();
    }

    private void initResources() {
        mRect = new Rect();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setTextSize(Util.spToPx(getContext(), 16));
    }

    @Override
    public void onDraw(Canvas canvas) {
        List<Key> keys = getKeyboard().getKeys();

        for (Key key : keys) {
            canvas.save();
            canvas.translate(getPaddingLeft(), getPaddingTop());

            int offsetY = 0;
            if (key.y == 0) {
                offsetY = 1;
            }
            int drawY = key.y + offsetY;
            if (mKeyDrawable != null) {
                mRect.left = key.x;
                mRect.top = drawY;
                mRect.right = key.x + key.width;
                mRect.bottom = key.y + key.height;
                canvas.clipRect(mRect);
                int[] state = key.getCurrentDrawableState();
                //设置按压效果
                mKeyDrawable.setState(state);
                //设置边距
                mKeyDrawable.setBounds(mRect);
                mKeyDrawable.draw(canvas);
            }
            if (key.label != null) {

                canvas.drawText(
                        key.label.toString(),
                        key.x + (key.width / 2),
                        drawY + (key.height + mPaint.getTextSize() - mPaint.descent()) / 2,
                        mPaint);

            } else if (key.icon != null) {

                int intrinsicWidth = key.icon.getIntrinsicWidth();
                int intrinsicHeight = key.icon.getIntrinsicHeight();
                final int drawableX = key.x + (key.width - intrinsicWidth) / 2;
                final int drawableY = drawY + (key.height - intrinsicHeight) / 2;

                key.icon.setBounds(drawableX, drawableY, drawableX + intrinsicWidth, drawableY + intrinsicHeight);

                key.icon.draw(canvas);
            }

            canvas.restore();
        }

    }

    public void setKeyDrawable(Drawable keyBgDrawable) {
        if (keyBgDrawable != mKeyDrawable) {
            this.mKeyDrawable = keyBgDrawable;
            invalidate();
        }
    }

    public void setKeyTextColor(@ColorInt int color) {
        mPaint.setColor(color);
        invalidate();
    }

    public void setKeyTextSize(float textSizePx) {
        mPaint.setTextSize(textSizePx);
        invalidate();
    }

}