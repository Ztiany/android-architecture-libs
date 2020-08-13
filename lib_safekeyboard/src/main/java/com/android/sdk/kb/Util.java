package com.android.sdk.kb;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.inputmethodservice.Keyboard;
import android.os.Build;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.lang.reflect.Method;
import java.util.regex.Pattern;

import androidx.fragment.app.FragmentActivity;

public class Util {

    private static final String LETTERS_REG = "^[a-zA-Z]+$";

    private static final String DIGITAL_REG = "^[0-9]+$";

    static int spToPx(Context context, float sp) {
        final float scale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (sp * scale + 0.5f);
    }

    /**
     * 禁止EditText弹出软件盘，光标依然正常显示，并且能正常选取光标。
     * <pre>
     *     1. 3.0 以下版本可以用 editText.setInputType(InputType.TYPE_NULL) 来实现。（但是在4.0的测试系统来看，使用 editText.setInputType(InputType.TYPE_NULL) 方法能隐藏键盘，但是光标也会隐藏，所以无法使用。）
     *     2. 3.0 及以上版本除了调用隐藏方法 setSoftInputShownOnFocus(false)。
     *     3. 到 4.2 系统对应设置的是 setSoftInputShownOnFocus 方法改为 setShowSoftInputOnFocus(false)。
     * </pre>
     *
     * @see <a href='https://www.zhangbj.com/p/168.html'>Android禁止EditText弹出软件盘</>
     */
    static void disableShowSoftInput(EditText editText) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            editText.setShowSoftInputOnFocus(false);
            return;
        }

        int currentVersion = Build.VERSION.SDK_INT;
        String methodName = null;

        if (currentVersion >= Build.VERSION_CODES.JELLY_BEAN) {// 4.2
            methodName = "setShowSoftInputOnFocus";
        } else if (currentVersion >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {// 4.0
            methodName = "setSoftInputShownOnFocus";
        }

        if (methodName == null) {
            editText.setInputType(InputType.TYPE_NULL);
        } else {
            Class<EditText> cls = EditText.class;
            Method setShowSoftInputOnFocus;
            try {
                setShowSoftInputOnFocus = cls.getMethod(methodName, boolean.class);
                setShowSoftInputOnFocus.setAccessible(true);
                setShowSoftInputOnFocus.invoke(editText, false);
            } catch (NoSuchMethodException e) {
                editText.setInputType(InputType.TYPE_NULL);
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    static Activity getRealContext(Context context) {
        if (context instanceof Activity) {
            return (Activity) context;
        }
        while (context instanceof android.content.ContextWrapper) {
            if (context instanceof FragmentActivity) {
                return (Activity) context;
            }
            context = ((ContextWrapper) context).getBaseContext();
        }
        return null;
    }

    /**
     * 显示系统键盘
     */
    static void showKeyboard(View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                view.requestFocus();
                imm.showSoftInput(view, 0);
            }
        }
    }

    /**
     * 隐藏软键盘
     */
    static void hideKeyboard(Context context) {
        Activity realContext = getRealContext(context);
        if (realContext == null) {
            return;
        }
        View view = realContext.getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    static void checkNull(Object object, String info) {
        if (object == null) {
            throw new NullPointerException(info);
        }
    }

    static void randomKey(Keyboard keyboard) {
        //todo
    }

    private static boolean isNumeric(String text) {
        return !TextUtils.isEmpty(text) && Pattern.matches(DIGITAL_REG, text);
    }

    private static boolean isLetter(String text) {
        return !TextUtils.isEmpty(text) && Pattern.matches(LETTERS_REG, text);
    }

    /**
     * 屏蔽EditText长按复制功能，启用后粘贴功能也会失效
     */
    public static void removeCopyAndPaste(EditText editText) {
        editText.setCustomSelectionActionModeCallback(new ActionMode.Callback() {

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {

            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return false;
            }
        });
        editText.setLongClickable(false);
    }

    public static void log(String log) {
        Log.d("CustomKeyboard", log);
    }

}