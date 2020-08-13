package com.android.sdk.kb;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2020-02-18 12:44
 */
public interface KeyboardMapper {

    /**
     * 根据定义在 xml 中用于切换键盘的 keyCode 返回对应的键盘 xml，没有对应的则返回小于等于 0 的数。
     */
    int getKeyboardIdByKeyCode(int keyCode);

}
