package com.android.sdk.kb;

import android.inputmethodservice.Keyboard;
import android.text.Editable;
import android.widget.EditText;
import android.widget.PopupWindow;

/**
 * 说明：键盘原生接口实现类，可继承重写。
 */
final class CoreOnKeyboardActionListener extends OnKeyboardActionListenerAdapter {

    private EditText editText;
    private PopupWindow keyboardPopupWindow;
    private KeyBoardActionListener mActionListener;
    private KeyboardLayout mSystemKeyboard;

    CoreOnKeyboardActionListener(KeyboardLayout systemKeyboard) {
        mSystemKeyboard = systemKeyboard;
    }

    void setEditText(EditText editText) {
        this.editText = editText;
    }

    void setKeyboardPopupWindow(PopupWindow keyboardPopupWindow) {
        this.keyboardPopupWindow = keyboardPopupWindow;
    }

    void setKeyActionListener(KeyBoardActionListener listener) {
        this.mActionListener = listener;
    }

    @Override
    public void onKey(int primaryCode, int[] keyCodes) {
        if (null != editText) {
            Editable editable = editText.getText();
            int start = editText.getSelectionStart();

            if (primaryCode == Keyboard.KEYCODE_DONE) {// 隐藏键盘
                if (null != keyboardPopupWindow && keyboardPopupWindow.isShowing()) {
                    keyboardPopupWindow.dismiss();
                }
                if (mActionListener != null) {
                    mActionListener.onComplete();
                }
            } else if (primaryCode == Keyboard.KEYCODE_DELETE) {// 回退
                if (editable != null && editable.length() > 0) {
                    if (start > 0) {
                        editable.delete(start - 1, start);
                    }
                }
                if (mActionListener != null) {
                    mActionListener.onClear();
                }
            } else if (primaryCode == KeyCodes.KEY_CODE_CLEAR_ALL) {
                editable.clear();
                if (mActionListener != null) {
                    mActionListener.onClearAll();
                }
            } else if (!switchKeyboard(primaryCode)) {
                if (mActionListener == null || !mActionListener.handOnKey(primaryCode, keyCodes)) {
                    editable.insert(start, Character.toString((char) primaryCode));
                    if (mActionListener != null) {
                        mActionListener.onTextChange(editable);
                    }
                }
            }
        }
    }

    private boolean switchKeyboard(int primaryCode) {
        KeyboardMapper keyboardMapper = KeyboardManager.getKeyboardMapper();
        if (keyboardMapper == null) {
            return false;
        }
        int keyboardId = keyboardMapper.getKeyboardIdByKeyCode(primaryCode);
        if (keyboardId <= 0) {
            return false;
        }
        mSystemKeyboard.setKeyBoard(keyboardId);
        return true;
    }

}