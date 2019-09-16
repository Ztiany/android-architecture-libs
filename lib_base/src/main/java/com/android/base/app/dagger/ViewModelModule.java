package com.android.base.app.dagger;

import androidx.lifecycle.ViewModelProvider;
import dagger.Binds;
import dagger.Module;

/**
 * 使用 ViewModelModule 情况下，所有的 ViewModule 都由 Activity 界别容器提供，因此 Fragment 级容器无法为其 ViewModule 提供依赖。这是仅有的局限性。
 *
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2018-11-05 16:20
 */
@Module
public abstract class ViewModelModule {

    @Binds
    @ActivityScope
    abstract ViewModelProvider.Factory provideViewModelFactory(ViewModelFactory viewModelFactory);

}
