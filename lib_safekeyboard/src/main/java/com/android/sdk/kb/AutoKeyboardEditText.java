package com.android.sdk.kb;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

/**
 * 说明：附带键盘弹出的EditText
 */
public class AutoKeyboardEditText extends KeyboardEditText {

    /**
     * 否启用自定义键盘
     */
    private boolean enable = true;

    /**
     * 默认获取焦点
     */
    private boolean focusEnable = true;

    private KeyboardLayout mKeyboardLayout;
    private CoreOnKeyboardActionListener mCoreOnKeyboardActionListener;
    private View.OnFocusChangeListener mSpareFocusChangeListener;
    private View.OnFocusChangeListener mInternalFocusChangeListener;

    public AutoKeyboardEditText(Context context) {
        super(context);
        initInternal(context, null);
    }

    public AutoKeyboardEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        initInternal(context, attrs);
    }

    public AutoKeyboardEditText(final Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initInternal(context, attrs);
    }

    private void initInternal(Context context, AttributeSet attributeSet) {
        initKeyboardView(context);
        parseAttributes(context, attributeSet);
        initListener();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initListener() {
        setOnTouchListener((v, event) -> {
            if (!isShowing()) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (focusEnable) {
                        requestFocus();
                        requestFocusFromTouch();
                        if (enable) {
                            Util.hideKeyboard(getContext());
                            Util.disableShowSoftInput(AutoKeyboardEditText.this);
                            showKeyboardWindow();
                        }
                    } else {
                        dismissKeyboardWindow();
                        Util.disableShowSoftInput(AutoKeyboardEditText.this);
                    }
                }
            } else {
                requestFocus();
                requestFocusFromTouch();
            }
            return false;
        });

        mInternalFocusChangeListener = (v, hasFocus) -> {
            //根据焦点变化判断外部点击区域
            if (!hasFocus) {
                dismissKeyboardWindow();
            }

            if (mSpareFocusChangeListener != null) {
                mSpareFocusChangeListener.onFocusChange(v, hasFocus);
            }
        };
        setOnFocusChangeListener(mInternalFocusChangeListener);
    }

    private void initKeyboardView(Context context) {
        initPopWindow(mKeyboardLayout = new KeyboardLayout(context));
        mCoreOnKeyboardActionListener = new CoreOnKeyboardActionListener(mKeyboardLayout);
        mCoreOnKeyboardActionListener.setEditText(this);
        mCoreOnKeyboardActionListener.setKeyboardPopupWindow(getKeyboardWindow());
        mKeyboardLayout.getKeyboardView().setOnKeyboardActionListener(mCoreOnKeyboardActionListener);
    }

    private void parseAttributes(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.AutoKeyboardEditText, 0, R.style.DefaultKeyboardStyle);

        boolean randomKeys = typedArray.getBoolean(R.styleable.AutoKeyboardEditText_isRandom, false);
        int keyboardLayoutResId = typedArray.getResourceId(R.styleable.AutoKeyboardEditText_xmlLayoutResId, 0);

        Drawable keyDrawable = typedArray.getDrawable(R.styleable.AutoKeyboardEditText_keyDrawable);
        int textColor = typedArray.getColor(R.styleable.AutoKeyboardEditText_keyboardTextColor, Color.WHITE);
        float textSize = typedArray.getDimension(R.styleable.AutoKeyboardEditText_keyboardTextSize, Util.spToPx(context, 16));

        CharSequence title = typedArray.getText(R.styleable.AutoKeyboardEditText_keyboardTitle);
        float titleSize = typedArray.getDimension(R.styleable.AutoKeyboardEditText_keyboardTitleSize, Util.spToPx(context, 16));
        int titleColor = typedArray.getColor(R.styleable.AutoKeyboardEditText_keyboardTitleColor, Color.WHITE);
        int titleBgColor = typedArray.getColor(R.styleable.AutoKeyboardEditText_keyboardTitleBgColor, Color.BLACK);
        int keyboardBgColor = typedArray.getColor(R.styleable.AutoKeyboardEditText_keyboardBgColor, Color.BLACK);

        mKeyboardLayout.setRandomKeys(randomKeys);
        mKeyboardLayout.setKeyBoard(keyboardLayoutResId);

        mKeyboardLayout.setKeyboardBgColor(keyboardBgColor);
        mKeyboardLayout.setKeyDrawable(keyDrawable);
        mKeyboardLayout.setKeyTextColor(textColor);
        mKeyboardLayout.setKeyTextSize(textSize);
        mKeyboardLayout.setKeyboardTitle(title);
        mKeyboardLayout.setKeyboardTitleSize(titleSize);
        mKeyboardLayout.setKeyboardTitleColor(titleColor);
        mKeyboardLayout.setKeyboardTitleBgColor(titleBgColor);

        boolean disableCopyAndPaste = typedArray.getBoolean(R.styleable.AutoKeyboardEditText_disableCopyAndPaste, false);
        if (disableCopyAndPaste) {
            removeCopyAndPaste();
        }

        typedArray.recycle();
    }

    public KeyboardLayout getKeyboardLayout() {
        return mKeyboardLayout;
    }

    /**
     * 重写 onKeyDown 当键盘弹出按回退键关闭
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && getKeyboardWindow().isShowing()) {
            dismissKeyboardWindow();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 设置键盘输入监听
     *
     * @param listener listener
     */
    public void setOnKeyboardActionListener(KeyBoardActionListener listener) {
        this.mCoreOnKeyboardActionListener.setKeyActionListener(listener);
    }

    public void setSpareFocusChangeListener(View.OnFocusChangeListener focusChangeListener) {
        this.mSpareFocusChangeListener = focusChangeListener;
    }

    @Override
    public void setOnFocusChangeListener(View.OnFocusChangeListener l) {
        if (l == mInternalFocusChangeListener) {
            super.setOnFocusChangeListener(l);
        }
    }

}