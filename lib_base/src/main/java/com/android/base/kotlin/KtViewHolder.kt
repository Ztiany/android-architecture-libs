package com.android.base.kotlin

import android.view.View
import com.android.base.adapter.recycler.ViewHolder
import kotlinx.android.extensions.CacheImplementation
import kotlinx.android.extensions.ContainerOptions
import kotlinx.android.extensions.LayoutContainer

/**
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2018-11-12 17:07
 */
@ContainerOptions(cache = CacheImplementation.SPARSE_ARRAY)
open class KtViewHolder(override val containerView: View) : ViewHolder(containerView), LayoutContainer
