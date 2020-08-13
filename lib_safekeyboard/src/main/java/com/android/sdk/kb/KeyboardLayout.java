package com.android.sdk.kb;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.inputmethodservice.Keyboard;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;

/**
 * 说明：使用系统API实现的键盘
 */
public class KeyboardLayout extends FrameLayout {

    private CustomKeyboardView mKeyboardView;

    private boolean mIsRandom;

    private CoreOnKeyboardActionListener mCoreOnKeyboardActionListener;

    private Map<Integer, Keyboard> mKeyboards = new HashMap<>();

    private int mCurrentKeyboardRes;

    private FrameLayout mKeyboardTitleContainer;

    private TextView mKeyboardTitle;

    public KeyboardLayout(Context context) {
        this(context, null);
    }

    public KeyboardLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public KeyboardLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Activity realContext = Util.getRealContext(context);
        if (realContext != null) {
            realContext.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        }
        initKeyBoardView(context);
        parseAttributes(context, attrs);
        setupKeyboard(getCurrentKeyboard());
    }

    private void parseAttributes(Context context, @Nullable AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.KeyboardLayout, 0, R.style.DefaultKeyboardStyle);

        mIsRandom = typedArray.getBoolean(R.styleable.KeyboardLayout_isRandom, false);
        mCurrentKeyboardRes = typedArray.getResourceId(R.styleable.KeyboardLayout_xmlLayoutResId, 0);

        int keyboardBgColor = typedArray.getColor(R.styleable.KeyboardLayout_keyboardBgColor, Color.BLACK);
        Drawable keyDrawable = typedArray.getDrawable(R.styleable.KeyboardLayout_keyDrawable);
        CharSequence title = typedArray.getText(R.styleable.KeyboardLayout_keyboardTitle);
        float titleSize = typedArray.getDimension(R.styleable.KeyboardLayout_keyboardTitleSize, Util.spToPx(context, 16));
        int titleColor = typedArray.getColor(R.styleable.KeyboardLayout_keyboardTitleColor, Color.BLACK);
        int titleBgColor = typedArray.getColor(R.styleable.KeyboardLayout_keyboardTitleBgColor, Color.BLACK);
        int textColor = typedArray.getColor(R.styleable.KeyboardLayout_keyboardTextColor, Color.BLACK);
        float textSize = typedArray.getDimension(R.styleable.KeyboardLayout_keyboardTextSize, Util.spToPx(context, 16));

        setKeyboardBgColor(keyboardBgColor);
        setKeyDrawable(keyDrawable);
        setKeyTextColor(textColor);
        setKeyTextSize(textSize);
        setKeyboardTitle(title);
        setKeyboardTitleSize(titleSize);
        setKeyboardTitleColor(titleColor);
        setKeyboardTitleBgColor(titleBgColor);

        typedArray.recycle();
    }

    private void initKeyBoardView(Context context) {
        View keyboardContainer = LayoutInflater.from(context).inflate(R.layout.custom_keyboardview, this, false);
        //title
        mKeyboardTitleContainer = keyboardContainer.findViewById(R.id.ekbFlKeyboardTitle);
        mKeyboardTitle = keyboardContainer.findViewById(R.id.ekbTvKeyboardTitle);
        //keyboard view
        mKeyboardView = keyboardContainer.findViewById(R.id.ekbKvKeyboardTitle);
        mKeyboardView.setEnabled(true);
        mKeyboardView.setPreviewEnabled(false);
        mKeyboardView.setOnKeyboardActionListener(mCoreOnKeyboardActionListener = new CoreOnKeyboardActionListener(this));
        this.addView(keyboardContainer, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    private void setupKeyboard(Keyboard currentKeyboard) {
        if (currentKeyboard != null) {
            mKeyboardView.setKeyboard(currentKeyboard);
        }
    }

    public CustomKeyboardView getKeyboardView() {
        return mKeyboardView;
    }

    /**
     * 设置键盘布局
     *
     * @param keyBoardResId xml
     */
    public void setKeyBoard(int keyBoardResId) {
        if (keyBoardResId > 0) {
            mCurrentKeyboardRes = keyBoardResId;
            setupKeyboard(getCurrentKeyboard());
        }
    }

    public void setKeyboardBgColor(int keyboardBgColor) {
        mKeyboardView.setBackgroundColor(keyboardBgColor);
        setBackgroundColor(keyboardBgColor);
    }

    public void setKeyboardTitleColor(int titleColor) {
        mKeyboardTitle.setTextColor(titleColor);
    }

    public void setKeyboardTitleBgColor(int titleBgColor) {
        mKeyboardTitleContainer.setBackgroundColor(titleBgColor);
    }

    public void setKeyboardTitleSize(float titleSize) {
        mKeyboardTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleSize);
    }

    /**
     * 设置键盘键上文字的颜色。
     */
    public void setKeyTextColor(@ColorInt int color) {
        mKeyboardView.setKeyTextColor(color);
    }

    /**
     * 设置键盘键上文字的大小。
     */
    public void setKeyTextSize(float textSizePx) {
        mKeyboardView.setKeyTextSize(textSizePx);
    }

    /**
     * 设置键盘标题，如果传空标题，则隐藏标题。
     *
     * @param title 键盘标题
     */
    public void setKeyboardTitle(CharSequence title) {
        if (TextUtils.isEmpty(title)) {
            mKeyboardTitleContainer.setVisibility(View.GONE);
        } else {
            mKeyboardTitleContainer.setVisibility(View.VISIBLE);
            mKeyboardTitle.setText(title);
        }
    }

    /**
     * 设置随机数字键盘
     *
     * @param isRandomKeys 是否随机,再次设置为false则恢复正常
     */
    public void setRandomKeys(boolean isRandomKeys) {
        if (mIsRandom == isRandomKeys) {
            return;
        }
        if (isRandomKeys) {
            randomAllKeyboard();
        } else {
            mKeyboards.clear();
        }
        setupKeyboard(getCurrentKeyboard());
        mIsRandom = isRandomKeys;
    }

    private void randomAllKeyboard() {
        Set<Map.Entry<Integer, Keyboard>> entries = mKeyboards.entrySet();
        for (Map.Entry<Integer, Keyboard> entry : entries) {
            Util.randomKey(entry.getValue());
        }
    }

    private Keyboard getCurrentKeyboard() {
        if (mCurrentKeyboardRes <= 0) {
            return null;
        }
        Keyboard keyboard = mKeyboards.get(mCurrentKeyboardRes);
        if (keyboard == null) {
            keyboard = new Keyboard(getContext(), mCurrentKeyboardRes);
            mKeyboards.put(mCurrentKeyboardRes, keyboard);
            if (mIsRandom) {
                Util.randomKey(keyboard);
            }
        }
        return keyboard;
    }

    /**
     * 设置按压背景，线条粗细等。
     *
     * @param keyDrawable mKeyDrawable
     */
    public void setKeyDrawable(Drawable keyDrawable) {
        mKeyboardView.setKeyDrawable(keyDrawable);
    }

    public void bindEditTextArray(EditText... texts) {
        for (EditText editText : texts) {
            setListener(editText);
            if (editText.hasFocus()) {
                setVisibility(VISIBLE);
                mCoreOnKeyboardActionListener.setEditText(editText);
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setListener(EditText editText) {
        Util.disableShowSoftInput(editText);

        editText.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                if (editText.hasFocus()) {
                    mCoreOnKeyboardActionListener.setEditText((EditText) v);
                    Util.hideKeyboard(editText.getContext());
                    setVisibility(VISIBLE);
                }
            }
            return false;
        });

        editText.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                setVisibility(GONE);
            } else {
                mCoreOnKeyboardActionListener.setEditText((EditText) v);
                Util.hideKeyboard(editText.getContext());
                setVisibility(VISIBLE);
            }
        });

        editText.setOnKeyListener((v, keyCode, event) -> {
            if (getVisibility() != VISIBLE) {
                return false;
            }
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                int action = event.getAction();
                if (action == KeyEvent.ACTION_DOWN) {
                    return true;
                }
                if (action == KeyEvent.ACTION_UP) {
                    setVisibility(GONE);
                    return true;
                }
            }
            return false;
        });
    }

    /**
     * 如果外部也监听了 KeyEvent 且优先级高于绑定的 EditText，则应该使用此方法处理按键返回。
     */
    public boolean handBackPress(int action) {
        if (getVisibility() == View.VISIBLE) {
            if (action == KeyEvent.ACTION_UP) {
                setVisibility(View.GONE);
            }
            return true;
        }
        return false;
    }

    /**
     * 设置键盘输入监听
     *
     * @param listener listener
     */
    public void setOnKeyboardActionListener(KeyBoardActionListener listener) {
        mCoreOnKeyboardActionListener.setKeyActionListener(listener);
    }

}