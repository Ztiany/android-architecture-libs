package com.android.base.app.ui;

import static com.android.base.app.ui.StateLayoutConfig.*;

public interface OnRetryActionListener {

    void onRetry(@RetryableState int state);

}