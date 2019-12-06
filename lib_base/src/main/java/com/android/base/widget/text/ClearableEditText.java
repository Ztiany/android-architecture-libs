package com.android.base.widget.text;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.inputmethod.EditorInfo;

import com.android.base.R;
import com.android.base.utils.android.views.Sizes;

import org.jetbrains.annotations.NotNull;

import androidx.appcompat.widget.AppCompatEditText;
import timber.log.Timber;


public class ClearableEditText extends AppCompatEditText {

    private Bitmap mClearBitmap;
    private Bitmap mPasswordVisibleBitmap;
    private Bitmap mPasswordInvisibleBitmap;

    private Paint mBitmapPaint;

    /**
     * the edge offset of first bitmap
     */
    private int mBitmapRightEdgeOffset;

    /**
     * the margin between clearing bitmap and password bitmap
     */
    private int mBitmapMargin;

    private int mInitPaddingRight;

    private boolean mPasswordVisibleEnable;
    private boolean mContentClearableEnable;

    private static final int DOWN_POSITION_NONE = 1;
    private static final int DOWN_POSITION_CLEAR = 2;
    private static final int DOWN_POSITION_PASSWORD = 3;
    private int mDownPosition = DOWN_POSITION_NONE;

    private PasswordTransformationMethod mVisibleTransformation;

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
        TypedArray typedArray = null;
        try {
            typedArray = context.obtainStyledAttributes(attrs, R.styleable.ClearableEditText);

            BitmapDrawable clearDrawable = (BitmapDrawable) typedArray.getDrawable(R.styleable.ClearableEditText_cet_clear_drawable);
            if (clearDrawable != null) {
                mClearBitmap = clearDrawable.getBitmap();
            } else {
                mClearBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.base_icon_clear);
            }

            BitmapDrawable passwordVisibleDrawable = (BitmapDrawable) typedArray.getDrawable(R.styleable.ClearableEditText_cet_password_seeing_drawable);
            if (passwordVisibleDrawable != null) {
                mPasswordVisibleBitmap = passwordVisibleDrawable.getBitmap();
            }

            BitmapDrawable passwordInvisibleDrawable = (BitmapDrawable) typedArray.getDrawable(R.styleable.ClearableEditText_cet_password_closing_drawable);
            if (passwordInvisibleDrawable != null) {
                mPasswordInvisibleBitmap = passwordInvisibleDrawable.getBitmap();
            }

            mPasswordVisibleEnable = typedArray.getBoolean(R.styleable.ClearableEditText_cet_enable_password_visibility_toggle, false) && isInputTypePassword();
            mContentClearableEnable = typedArray.getBoolean(R.styleable.ClearableEditText_cet_enable_content_clearable, true);

        } finally {
            if (typedArray != null) {
                typedArray.recycle();
            }
        }

        mInitPaddingRight = getPaddingRight();

        mBitmapRightEdgeOffset = Sizes.dip(5);
        mBitmapMargin = Sizes.dip(15);

        mBitmapPaint = new Paint();
        mBitmapPaint.setAntiAlias(true);

        adjustPadding();
        addTextChangedListener(newWatcher());
    }

    private Bitmap getPasswordVisibleBitmap() {
        if (mPasswordVisibleBitmap == null) {
            mPasswordVisibleBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.base_icon_password_open);
        }
        return mPasswordVisibleBitmap;
    }

    private Bitmap getPasswordInvisibleBitmap() {
        if (mPasswordInvisibleBitmap == null) {
            mPasswordInvisibleBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.base_icon_password_close);
        }
        return mPasswordInvisibleBitmap;
    }

    private Bitmap getPasswordBitmap() {
        if (isPasswordSeeable()) {
            return getPasswordInvisibleBitmap();
        } else {
            return getPasswordVisibleBitmap();
        }
    }

    private PasswordTransformationMethod getVisibleTransformation() {
        if (mVisibleTransformation == null) {
            mVisibleTransformation = new PasswordTransformationMethod();
        }
        return mVisibleTransformation;
    }

    private void adjustPadding() {
        boolean hasClearBitmap = mContentClearableEnable && !TextUtils.isEmpty(getTextValue());

        int rightPadding;
        if (mPasswordVisibleEnable) {
            rightPadding = mInitPaddingRight + getPasswordBitmap().getWidth() + mBitmapRightEdgeOffset;
            if (hasClearBitmap) {
                rightPadding += (mBitmapMargin + mClearBitmap.getWidth());
            }
        } else if (hasClearBitmap) {
            rightPadding = mInitPaddingRight + mClearBitmap.getWidth() + mBitmapRightEdgeOffset;
        } else {
            rightPadding = mInitPaddingRight;
        }

        invalidate();

        setPadding(getPaddingLeft(), getPaddingTop(), rightPadding, getPaddingBottom());
    }

    @NotNull
    private TextWatcher newWatcher() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adjustPadding();
            }
        };
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();

        if (action == MotionEvent.ACTION_DOWN) {
            mDownPosition = detectTouchPosition(event);
        } else if (action == MotionEvent.ACTION_UP) {
            int upPosition = detectTouchPosition(event);
            Timber.d("upPosition = %d", upPosition);
            if (upPosition == mDownPosition) {
                if ((upPosition == DOWN_POSITION_CLEAR)) {
                    setText("");
                } else if (upPosition == DOWN_POSITION_PASSWORD) {
                    if (isPasswordSeeable()) {
                        setTransformationMethod(null);
                    } else {
                        setTransformationMethod(getVisibleTransformation());
                    }
                }
            }
        }
        return super.onTouchEvent(event);
    }

    private int detectTouchPosition(MotionEvent event) {
        float eventX = event.getX();

        if (mPasswordVisibleEnable) {

            int passwordRight = getMeasuredWidth() - mInitPaddingRight - mBitmapRightEdgeOffset;
            int passwordLeft = passwordRight - getPasswordBitmap().getWidth();
            if (eventX >= passwordLeft && eventX <= passwordRight) {
                return DOWN_POSITION_PASSWORD;
            }

            if (mContentClearableEnable && !TextUtils.isEmpty(getTextValue())) {
                int clearRight = passwordLeft - mBitmapMargin;
                int clearLeft = clearRight - mClearBitmap.getWidth();
                if (eventX >= clearLeft && eventX <= clearRight) {
                    return DOWN_POSITION_CLEAR;
                }
            }

        } else if (mContentClearableEnable && !TextUtils.isEmpty(getTextValue())) {

            int clearRight = getMeasuredWidth() - mInitPaddingRight - mBitmapRightEdgeOffset;
            int clearLeft = clearRight - mClearBitmap.getWidth();
            if (eventX >= clearLeft && eventX <= clearRight) {
                return DOWN_POSITION_CLEAR;
            }
        }

        return DOWN_POSITION_NONE;
    }

    private String getTextValue() {
        Editable text = getText();
        return (text == null) ? "" : text.toString();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.save();
        canvas.translate(getMeasuredWidth() - mInitPaddingRight, 0);

        if (mPasswordVisibleEnable) {
            Bitmap passwordBitmap = getPasswordBitmap();
            canvas.translate(-(passwordBitmap.getWidth() + mBitmapRightEdgeOffset), 0);
            canvas.drawBitmap(passwordBitmap, 0, (getMeasuredHeight() - passwordBitmap.getHeight()) / 2, mBitmapPaint);
        }

        boolean hasClearBitmap = mContentClearableEnable && !TextUtils.isEmpty(getTextValue());

        if (hasClearBitmap) {
            if (mPasswordVisibleEnable) {
                canvas.translate(-(mClearBitmap.getWidth() + mBitmapMargin), 0);
            } else {
                canvas.translate(-(mClearBitmap.getWidth() + mBitmapRightEdgeOffset), 0);
            }
            canvas.drawBitmap(mClearBitmap, 0, (getMeasuredHeight() - mClearBitmap.getHeight()) / 2, mBitmapPaint);
        }

        canvas.restore();
    }

    private boolean isInputTypePassword() {
        int inputType = getInputType();
        final int variation = inputType & (EditorInfo.TYPE_MASK_CLASS | EditorInfo.TYPE_MASK_VARIATION);
        final boolean passwordInputType = variation == (EditorInfo.TYPE_CLASS_TEXT | EditorInfo.TYPE_TEXT_VARIATION_PASSWORD);
        final boolean webPasswordInputType = variation == (EditorInfo.TYPE_CLASS_TEXT | EditorInfo.TYPE_TEXT_VARIATION_WEB_PASSWORD);
        final boolean numberPasswordInputType = variation == (EditorInfo.TYPE_CLASS_NUMBER | EditorInfo.TYPE_NUMBER_VARIATION_PASSWORD);
        return passwordInputType || webPasswordInputType || numberPasswordInputType;
    }

    private boolean isPasswordSeeable() {
        return getTransformationMethod() instanceof PasswordTransformationMethod;
    }

    public void setInitPaddingRight(int initPaddingRight) {
        mInitPaddingRight = initPaddingRight;
        adjustPadding();
    }

}