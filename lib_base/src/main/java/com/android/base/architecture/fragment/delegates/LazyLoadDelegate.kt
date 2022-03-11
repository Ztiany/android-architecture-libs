package com.android.base.architecture.fragment.delegates

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.android.base.foundation.fragment.FragmentDelegate
import com.android.base.foundation.fragment.FragmentDelegateOwner

/**
 * 用于在ViewPager中实现懒加载的Fragment：
 *
 * - changed-1: Android Support 24 把 setUserVisibleHint 方法放到了Attach 之前调用了，所以请在在构造代码块中设置 LazyDelegate。
 * - changed-2: 对于 AndroidX 中 ViewPager，仅支持 [androidx.fragment.app.FragmentPagerAdapter.BEHAVIOR_SET_USER_VISIBLE_HINT] 模式。
 *
 * @author Ztiany
 * Date : Date : 2016-05-06 15:02
 */
open class LazyLoadDelegate private constructor() : FragmentDelegate<Fragment> {

    /** View是否准备好，如果不需要绑定view数据，只是加载网络数据，那么该字段可以去掉 */
    private var isViewPrepared = false

    /** 滑动过来后，View是否可见 */
    private var isViewVisible = false

    private var preparedListener: (() -> Unit)? = null

    /**
     * 在这里实现Fragment数据的缓加载.
     *
     * @param isVisibleToUser true 表用户可见，false 表不可见
     */
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        if (isVisibleToUser) {
            isViewVisible = true
            onVisible()
        } else {
            isViewVisible = false
            onInvisible()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        isViewPrepared = true
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        lazyLoad()
    }

    /**
     * 滑动过来后，界面可见时执行
     */
    protected fun onVisible() {
        lazyLoad()
    }

    /**
     * 滑动过来后，界面不可见时执行
     */
    protected fun onInvisible() {}

    private fun lazyLoad() {
        if (isViewPrepared && isViewVisible) {
            notifyLazyLoad()
        }
    }

    /**
     * 懒加载数据，并在此绑定View数据
     */
    private fun notifyLazyLoad() {
        preparedListener?.invoke()
    }

    companion object {
        fun attach(delegateFragment: FragmentDelegateOwner, onPreparedListener: () -> Unit): LazyLoadDelegate {
            val delegate = LazyLoadDelegate()
            delegate.preparedListener = onPreparedListener
            delegateFragment.addDelegate(delegate)
            return delegate
        }
    }

}

class SimpleLazyLoadListener(private val onFirstLoad: () -> Unit) : (() -> Unit) {

    private var isCalled = false

    override fun invoke() {
        if (!isCalled) {
            onFirstLoad()
            isCalled = true
        }
    }

}