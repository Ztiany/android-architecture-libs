package com.android.base.app.fragment.injectable

import androidx.lifecycle.ViewModelProvider
import com.android.base.data.ErrorHandler


interface InjectableExtension {

    val viewModelFactory: ViewModelProvider.Factory

    val errorHandler: ErrorHandler

}
