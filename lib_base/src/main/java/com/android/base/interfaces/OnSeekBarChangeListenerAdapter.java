package com.android.base.interfaces;

import android.widget.SeekBar;

/**
 * @author Ztiany
 *         Email: 1169654504@qq.com
 *         Date : 2017-04-18 16:29
 */
public interface OnSeekBarChangeListenerAdapter extends SeekBar.OnSeekBarChangeListener {

    @Override
    default void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
    }

    @Override
    default void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    default void onStopTrackingTouch(SeekBar seekBar) {
    }

}
