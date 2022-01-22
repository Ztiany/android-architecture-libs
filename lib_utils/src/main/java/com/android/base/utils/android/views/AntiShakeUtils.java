package com.android.base.utils.android.views;

import android.view.View;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;

import com.android.base.utils.R;


public class AntiShakeUtils {

    private final static long INTERNAL_TIME = 500;

    /**
     * Whether this click event is invalid.
     *
     * @param target target view
     * @return true, invalid click event.
     * @see #isInvalidClick(View, long)
     */
    public static boolean isInvalidClick(@NonNull View target) {
        return isInvalidClick(target, INTERNAL_TIME);
    }

    /**
     * Whether this click event is invalid.
     *
     * @param target       target view
     * @param internalTime the internal time. The unit is millisecond.
     * @return true, invalid click event.
     */
    public static boolean isInvalidClick(@NonNull View target, @IntRange(from = 0) long internalTime) {
        long curTimeStamp = System.currentTimeMillis();
        long lastClickTimeStamp = 0;
        Object o = target.getTag(R.id.base_last_click_timestamp);
        if (o == null) {
            target.setTag(R.id.base_last_click_timestamp, curTimeStamp);
            return false;
        }
        lastClickTimeStamp = (long) o;
        boolean isInvalid = curTimeStamp - lastClickTimeStamp < internalTime;
        if (!isInvalid) {
            target.setTag(R.id.base_last_click_timestamp, curTimeStamp);
        }

        return isInvalid;
    }

}