package com.android.base.app.dagger;

import androidx.lifecycle.ViewModelProvider;
import dagger.Binds;
import dagger.Module;

/**
 * {@link androidx.lifecycle.AbstractSavedStateViewModelFactory} is not supported.
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
