package com.android.base.architecture.mvp


/**
 * @author Ztiany
 * Email: 1169654504@qq.com
 * Date : 2017-04-18 16:23
 */
interface Lifecycle {

    /**
     * start the Lifecycle , initialize something, will be called only once
     */
    fun onStart()

    /**
     * will be called when view is ready.
     */
    fun onPostStart()

    fun onResume()

    fun onPause()

    /**
     * destroy the Lifecycle and release resource, will be called only once
     */
    fun onDestroy()

}

