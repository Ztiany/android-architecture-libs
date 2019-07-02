package com.android.base.app.fragment;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2019-03-05 15:25
 */
public class FragmentConfig {

    private static final int INVALIDATE_ID = -1;
    private static int sDefaultContainerId = INVALIDATE_ID;

    public static void setDefaultContainerId(int defaultContainerId) {
        sDefaultContainerId = defaultContainerId;
    }

    public static int defaultContainerId() {
        if (sDefaultContainerId == INVALIDATE_ID) {
            throw new IllegalStateException("sDefaultContainerId has not set");
        }
        return sDefaultContainerId;
    }

}
