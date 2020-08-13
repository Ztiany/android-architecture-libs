package com.android.sdk.kb;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2020-02-18 16:17
 */
public class DefaultKeyboardMapper implements KeyboardMapper {

    private static final int KCODE_TO_LOWER_CASE = 20200220;
    private static final int KCODE_TO_UPPER_CASE = 20200221;
    private static final int KCODE_TO_SYMBOL = 20200223;

    @Override
    public int getKeyboardIdByKeyCode(int keyCode) {
        if (keyCode == KCODE_TO_LOWER_CASE) {
            return R.xml.keyboard_letter_lower_case;
        } else if (keyCode == KCODE_TO_UPPER_CASE) {
            return R.xml.keyboard_letter_upper_case;
        } else if (keyCode == KCODE_TO_SYMBOL) {
            return R.xml.keyboard_symbol;
        }
        return 0;
    }

}
