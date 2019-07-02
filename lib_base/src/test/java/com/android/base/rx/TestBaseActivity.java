package com.android.base.rx;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.android.base.app.activity.BaseActivity;

/**
 * @author Ztiany
 *         Email: 1169654504@qq.com
 *         Date : 2017-06-22 09:37
 */
public class TestBaseActivity extends BaseActivity {

    @Override
    protected void initialize(@Nullable Bundle savedInstanceState) {
        super.initialize(savedInstanceState);
    }
    
    @Override
    protected Object layout() {
        return 0;
    }

    @Override
    protected void setupView(@Nullable Bundle savedInstanceState) {
    }

}
