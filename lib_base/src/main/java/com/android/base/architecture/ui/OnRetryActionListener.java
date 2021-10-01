package com.android.base.architecture.ui;

import static com.android.base.architecture.ui.StateLayoutConfig.*;

public interface OnRetryActionListener {

    void onRetry(@RetryableState int state);

}