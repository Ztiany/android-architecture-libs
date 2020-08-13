package com.android.sdk.kb;

import android.text.Editable;

/**
 * 键盘输入监听
 */
public interface KeyBoardActionListener {

    /**
     * 完成点击
     */
    void onComplete();

    /**
     * 文本改变
     */
    void onTextChange(Editable editable);

    /**
     * 正在删除
     */
    void onClear();

    /**
     * 全部清除
     */
    void onClearAll();

    /**
     * 处理其他键盘key的按下，如果返回 true 表示由 KeyBoardActionListener，否则正常输入。
     */
    boolean handOnKey(int primaryCode, int[] keyCodes);

}