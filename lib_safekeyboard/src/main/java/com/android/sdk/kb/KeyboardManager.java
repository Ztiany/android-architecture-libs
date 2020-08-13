package com.android.sdk.kb;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2020-02-18 12:45
 */
public class KeyboardManager {

    private static KeyboardMapper sKeyboardMapper = new DefaultKeyboardMapper();

    public static void configKeyboardMapper(KeyboardMapper keyboardMapper) {
        sKeyboardMapper = keyboardMapper;
    }

    static KeyboardMapper getKeyboardMapper() {
        return sKeyboardMapper;
    }

}