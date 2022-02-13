package com.android.base.architecture.activity;

/**
 * Activity 的返回键监听。
 */
public interface OnBackPressListener {

    /**
     * @return true 表示Fragment处理back press，false表示由Activity处理。
     */
    boolean onBackPressed();

}