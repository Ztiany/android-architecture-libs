package com.android.base.app;

import android.os.Bundle;
import android.view.View;

import com.android.base.app.fragment.BaseUIFragment;
import com.android.base.databinding.BaseLayoutEmptyBinding;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2021-03-06 02:44
 */
public class ViewBindingJavaFragment extends BaseUIFragment<BaseLayoutEmptyBinding> {

    @Override
    protected void onViewPrepared(@NotNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewPrepared(view, savedInstanceState);
    }

}
