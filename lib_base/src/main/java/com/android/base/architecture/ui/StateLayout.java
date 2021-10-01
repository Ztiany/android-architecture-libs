package com.android.base.architecture.ui;

/**
 * @author Ztiany
 * Email: 1169654504@qq.com
 * Date : 2017-07-08 14:52
 */
public interface StateLayout {

    void showContentLayout();

    void showLoadingLayout();

    void showEmptyLayout();

    void showErrorLayout();

    void showRequesting();

    void showBlank();

    void showNetErrorLayout();

    void showServerErrorLayout();

    StateLayoutConfig getStateLayoutConfig();

    @StateLayoutConfig.ViewState
    int currentStatus();

}
