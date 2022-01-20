package me.ztiany.widget.text;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.appcompat.widget.AppCompatEditText;

import com.android.sdk.ui.R;


public class ClearableEditText extends AppCompatEditText {

    private final ClearableEditTextController mClearableEditTextController = new ClearableEditTextController(this);

    public ClearableEditText(Context context) {
        super(context);
        init(context, null);
    }

    public ClearableEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ClearableEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {

        ClearableAttrs clearableAttrs = new ClearableAttrs();
        TypedArray typedArray = null;
        try {
            typedArray = context.obtainStyledAttributes(attrs, R.styleable.ClearableEditText);

            BitmapDrawable clearDrawable = (BitmapDrawable) typedArray.getDrawable(R.styleable.ClearableEditText_cet_clear_drawable);
            if (clearDrawable != null) {
                clearableAttrs.setClearBitmap(clearDrawable.getBitmap());
            }

            BitmapDrawable passwordVisibleDrawable = (BitmapDrawable) typedArray.getDrawable(R.styleable.ClearableEditText_cet_password_seeing_drawable);
            if (passwordVisibleDrawable != null) {
                clearableAttrs.setPasswordVisibleBitmap(passwordVisibleDrawable.getBitmap());
            }

            BitmapDrawable passwordInvisibleDrawable = (BitmapDrawable) typedArray.getDrawable(R.styleable.ClearableEditText_cet_password_closing_drawable);
            if (passwordInvisibleDrawable != null) {
                clearableAttrs.setPasswordInvisibleBitmap(passwordInvisibleDrawable.getBitmap());
            }

            clearableAttrs.setPasswordVisibleEnable(typedArray.getBoolean(R.styleable.ClearableEditText_cet_enable_password_visibility_toggle, false));
            clearableAttrs.setContentClearableEnable(typedArray.getBoolean(R.styleable.ClearableEditText_cet_enable_content_clearable, true));

        } finally {
            if (typedArray != null) {
                typedArray.recycle();
            }
        }
        mClearableEditTextController.init(clearableAttrs);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mClearableEditTextController.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mClearableEditTextController.draw(canvas);
    }

    public void setInitPaddingRight(int initPaddingRight) {
        mClearableEditTextController.setInitPaddingRight(initPaddingRight);
    }

}