package me.ztiany.widget.text;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import com.android.sdk.ui.R;

import org.jetbrains.annotations.NotNull;

import me.ztiany.widget.common.Sizes;
import timber.log.Timber;


/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2020-03-02 11:52
 */
public class ClearableEditTextController {

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

    private EditText mEditText;

    @SuppressWarnings("WeakerAccess")
    public ClearableEditTextController(EditText editText, ClearableAttrs clearableAttrs) {
        mEditText = editText;
        getAttrs(clearableAttrs);
        init();
    }

    private void getAttrs(ClearableAttrs clearableAttrs) {
        mClearBitmap = clearableAttrs.getClearBitmap();
        if (mClearBitmap == null) {
            mClearBitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.base_ui_icon_clear);
        }
        mPasswordVisibleBitmap = clearableAttrs.getPasswordVisibleBitmap();
        mPasswordInvisibleBitmap = clearableAttrs.getPasswordInvisibleBitmap();
        mPasswordVisibleEnable = clearableAttrs.isPasswordVisibleEnable() && isInputTypePassword();
        mContentClearableEnable = clearableAttrs.isContentClearableEnable();
    }

    private void init() {
        mInitPaddingRight = mEditText.getPaddingRight();
        mBitmapRightEdgeOffset = Sizes.dpToPx(getContext(), 5);
        mBitmapMargin = Sizes.dpToPx(getContext(), 15);
        mBitmapPaint = new Paint();
        mBitmapPaint.setAntiAlias(true);
        adjustPadding();
        mEditText.addTextChangedListener(newWatcher());
    }

    private Context getContext() {
        return mEditText.getContext();
    }

    private Bitmap getPasswordVisibleBitmap() {
        if (mPasswordVisibleBitmap == null) {
            mPasswordVisibleBitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.base_ui_icon_password_open);
        }
        return mPasswordVisibleBitmap;
    }

    private Bitmap getPasswordInvisibleBitmap() {
        if (mPasswordInvisibleBitmap == null) {
            mPasswordInvisibleBitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.base_ui_icon_password_close);
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

        mEditText.invalidate();

        mEditText.setPadding(mEditText.getPaddingLeft(), mEditText.getPaddingTop(), rightPadding, mEditText.getPaddingBottom());
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


    public void onTouchEvent(MotionEvent event) {
        int action = event.getAction();

        if (action == MotionEvent.ACTION_DOWN) {
            mDownPosition = detectTouchPosition(event);
        } else if (action == MotionEvent.ACTION_UP) {
            int upPosition = detectTouchPosition(event);
            Timber.d("upPosition = %d", upPosition);
            if (upPosition == mDownPosition) {
                if ((upPosition == DOWN_POSITION_CLEAR)) {
                    mEditText.setText("");
                } else if (upPosition == DOWN_POSITION_PASSWORD) {
                    if (isPasswordSeeable()) {
                        mEditText.setTransformationMethod(null);
                    } else {
                        mEditText.setTransformationMethod(getVisibleTransformation());
                    }
                }
            }
        }

    }

    private int detectTouchPosition(MotionEvent event) {
        float eventX = event.getX();

        if (mPasswordVisibleEnable) {

            int passwordRight = mEditText.getMeasuredWidth() - mInitPaddingRight - mBitmapRightEdgeOffset;
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

            int clearRight = mEditText.getMeasuredWidth() - mInitPaddingRight - mBitmapRightEdgeOffset;
            int clearLeft = clearRight - mClearBitmap.getWidth();
            if (eventX >= clearLeft && eventX <= clearRight) {
                return DOWN_POSITION_CLEAR;
            }
        }

        return DOWN_POSITION_NONE;
    }

    private String getTextValue() {
        Editable text = mEditText.getText();
        return (text == null) ? "" : text.toString();
    }

    public void draw(Canvas canvas) {
        canvas.save();
        canvas.translate(mEditText.getMeasuredWidth() - mInitPaddingRight, 0);

        if (mPasswordVisibleEnable) {
            Bitmap passwordBitmap = getPasswordBitmap();
            canvas.translate(-(passwordBitmap.getWidth() + mBitmapRightEdgeOffset), 0);
            canvas.drawBitmap(passwordBitmap, 0, (mEditText.getMeasuredHeight() - passwordBitmap.getHeight()) / 2, mBitmapPaint);
        }

        boolean hasClearBitmap = mContentClearableEnable && !TextUtils.isEmpty(getTextValue());

        if (hasClearBitmap) {
            if (mPasswordVisibleEnable) {
                canvas.translate(-(mClearBitmap.getWidth() + mBitmapMargin), 0);
            } else {
                canvas.translate(-(mClearBitmap.getWidth() + mBitmapRightEdgeOffset), 0);
            }
            canvas.drawBitmap(mClearBitmap, 0, (mEditText.getMeasuredHeight() - mClearBitmap.getHeight()) / 2, mBitmapPaint);
        }

        canvas.restore();
    }

    private boolean isInputTypePassword() {
        int inputType = mEditText.getInputType();
        final int variation = inputType & (EditorInfo.TYPE_MASK_CLASS | EditorInfo.TYPE_MASK_VARIATION);
        final boolean passwordInputType = variation == (EditorInfo.TYPE_CLASS_TEXT | EditorInfo.TYPE_TEXT_VARIATION_PASSWORD);
        final boolean webPasswordInputType = variation == (EditorInfo.TYPE_CLASS_TEXT | EditorInfo.TYPE_TEXT_VARIATION_WEB_PASSWORD);
        final boolean numberPasswordInputType = variation == (EditorInfo.TYPE_CLASS_NUMBER | EditorInfo.TYPE_NUMBER_VARIATION_PASSWORD);
        return passwordInputType || webPasswordInputType || numberPasswordInputType;
    }

    private boolean isPasswordSeeable() {
        return mEditText.getTransformationMethod() instanceof PasswordTransformationMethod;
    }

    @SuppressWarnings("WeakerAccess")
    public void setInitPaddingRight(int initPaddingRight) {
        mInitPaddingRight = initPaddingRight;
        adjustPadding();
    }

}