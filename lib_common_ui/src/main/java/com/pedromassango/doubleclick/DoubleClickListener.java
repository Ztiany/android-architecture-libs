package com.pedromassango.doubleclick;

import android.os.Handler;
import android.view.View;

/**
 * @author pedromassango on 12/20/17.
 * @see <a href='https://github.com/pedromassango/doubleClick'>doubleClick</a>
 */
public class DoubleClickListener implements View.OnClickListener {

    /*
     * Duration of click interval.
     * 200 milliseconds is a best fit to double click interval.
     */
    private long DOUBLE_CLICK_INTERVAL;  // Time to wait the second click.

    /*
     * Handler to process click event.
     */
    private final Handler mHandler = new Handler();

    /*
     * Number of clicks in @DOUBLE_CLICK_INTERVAL interval.
     */
    private int clicks;

    /*
     * Flag to check if click handler is busy.
     */
    private boolean isBusy = false;

    /**
     * Builds a DoubleClick.
     */
    public DoubleClickListener() {
        this(200L);
    }

    public DoubleClickListener(final long doubleClickInterval) {
        // developer specified time to wait the second click.
        this.DOUBLE_CLICK_INTERVAL = doubleClickInterval;
    }

    @Override
    public void onClick(final View view) {

        if (!isBusy) {
            //  Prevent multiple click in this short time
            isBusy = true;

            // Increase clicks count
            clicks++;

            mHandler.postDelayed(() -> {
                if (clicks >= 2) {  // Double tap.
                    onDoubleClick(view);
                }
                if (clicks == 1) {  // Single tap
                    onSingleClick(view);
                }
                // we need to  restore clicks count
                clicks = 0;
            }, DOUBLE_CLICK_INTERVAL);

            isBusy = false;
        }
    }

    protected void onSingleClick(View view) {

    }

    protected void onDoubleClick(View view) {

    }

}