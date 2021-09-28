package com.android.sdk.net.extension

import com.android.sdk.net.core.service.ServiceFactory

/**
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2019-04-02 11:09
 */
inline fun <reified T> ServiceFactory.create(): T = create(T::class.java)