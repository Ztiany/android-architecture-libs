package com.android.base.componentization

import com.android.base.componentization.app.ErrorHandler
import com.android.sdk.net.core.provider.ApiHandler
import com.android.sdk.net.core.provider.ErrorDataAdapter
import com.android.sdk.net.core.provider.ErrorMessage
import com.android.sdk.net.core.provider.HttpConfig
import com.android.sdk.net.rxjava.RxResultPostTransformer

class GlobalConfigHolder(

) {

    val errorHandler: ErrorHandler
        get() = object : ErrorHandler {
            override fun generateMessage(throwable: Throwable): CharSequence {
                TODO("Not yet implemented")
            }

            override fun handleError(throwable: Throwable) {
                TODO("Not yet implemented")
            }

            override fun handleGlobalError(throwable: Throwable) {
                TODO("Not yet implemented")
            }

        }

    class Builder {

        lateinit var errorHandler: ErrorHandler

        lateinit var errorMessage: ErrorMessage

        lateinit var errorDataAdapter: ErrorDataAdapter

        lateinit var httpConfig: HttpConfig

        lateinit var apiHandler: ApiHandler

        lateinit var rxResultPostTransformer: RxResultPostTransformer<*>

        fun build(): GlobalConfigHolder {
            return GlobalConfigHolder()
        }

    }

}

