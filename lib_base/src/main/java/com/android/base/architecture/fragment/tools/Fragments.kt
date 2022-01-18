@file:JvmName("Fragments")

package com.android.base.architecture.fragment.tools


import android.view.View
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Lifecycle
import com.android.base.foundation.activity.ActivityDelegate
import com.android.base.foundation.activity.ActivityDelegateOwner
import com.android.base.foundation.activity.ActivityState
import com.android.base.foundation.fragment.FragmentDelegate
import com.android.base.foundation.fragment.FragmentDelegateOwner
import com.android.base.utils.common.javaClassName
import kotlin.reflect.KClass

/*There are some useful methods in this file for you to operate Fragment. But maybe Jetpack Compose is a better choice.*/

/**被此 annotation 标注的方法，表示需要使用 [Fragment] 的全类名作为 [FragmentTransaction] 中相关方法的 flag 参数的实参，比如 add/replace 等*/
annotation class UsingFragmentClassNameAsFlag

@JvmOverloads
fun Fragment.exitFragment(immediate: Boolean = false) {
    activity.exitFragment(immediate)
}

@JvmOverloads
fun FragmentActivity?.exitFragment(immediate: Boolean = false) {
    if (this == null) {
        return
    }
    val supportFragmentManager = this.supportFragmentManager
    val backStackEntryCount = supportFragmentManager.backStackEntryCount
    if (backStackEntryCount > 0) {
        if (immediate) {
            supportFragmentManager.popBackStackImmediate()
        } else {
            supportFragmentManager.popBackStack()
        }
    } else {
        this.supportFinishAfterTransition()
    }
}

/**
 * @param clazz the interface container must implemented
 * @param <T> Type
 * @return the interface context must implemented
 */
fun <T> Fragment.requireContainerImplement(clazz: Class<T>): T? {
    if (clazz.isInstance(parentFragment)) {
        return clazz.cast(parentFragment)
    }
    return if (clazz.isInstance(activity)) {
        clazz.cast(activity)
    } else {
        throw RuntimeException("use this Fragment:$this, Activity or Fragment must impl interface :$clazz")
    }
}

/**
 * @param clazz the interface context must implemented
 * @param <T> Type
 * @return the interface context must implemented
 */
fun <T> Fragment.requireContextImplement(clazz: Class<T>): T? {
    return if (!clazz.isInstance(activity)) {
        throw RuntimeException("use this Fragment:$this, Activity must impl interface :$clazz")
    } else {
        clazz.cast(activity)
    }
}

/**
 * @param clazz the interface parent must implemented
 * @param <T> Type
 * @return the interface context must implemented
 */
fun <T> Fragment.requireParentImplement(clazz: Class<T>): T? {
    return if (!clazz.isInstance(parentFragment)) {
        throw RuntimeException("use this Fragment:$this, ParentFragment must impl interface :$clazz")
    } else {
        clazz.cast(parentFragment)
    }
}

/** 使用 [clazz] 的全限定类名作为 tag 查找 Fragment */
fun <T : Fragment> FragmentManager.findFragmentByTag(clazz: KClass<T>): T? {
    @Suppress("UNCHECKED_CAST")
    return findFragmentByTag(clazz.java.name) as? T
}

/**
 * Fragment 出栈，包括 flag 对应的 Fragment，如果 flag 对应的 Fragment 不在栈中，此方法什么都不做。
 */
@UsingFragmentClassNameAsFlag
fun FragmentManager.popBackUntil(flag: String, immediate: Boolean = false) {
    if (immediate) {
        popBackStackImmediate(flag, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    } else {
        popBackStack(flag, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }
}

/**
 * Fragment 出栈，不包括 flag 对应的 Fragment，如果 flag 对应的 Fragment 不在栈中，此方法什么都不做。
 */
@UsingFragmentClassNameAsFlag
fun FragmentManager.popBackTo(flag: String, immediate: Boolean = false) {
    if (immediate) {
        popBackStackImmediate(flag, 0)
    } else {
        popBackStack(flag, 0)
    }
}

/**
 * 回到对应的 Fragment，如果 Fragment 在栈中，则该 Fragment 回到栈顶，如果 Fragment 不在栈中，则做一次弹栈操作。
 */
@UsingFragmentClassNameAsFlag
fun FragmentManager.backToFragment(flag: String, immediate: Boolean = false) {
    val target = findFragmentByTag(flag)

    if (target != null) {
        if (isFragmentInStack(target.javaClass)) {
            if (immediate) {
                popBackStackImmediate(flag, 0)
            } else {
                popBackStack(flag, 0)
            }
        } else {
            popBackStack()
        }
    }
}

fun FragmentManager.clearBackStack(immediate: Boolean = false) {
    if (immediate) {
        this.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    } else {
        this.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }
}

@UsingFragmentClassNameAsFlag
fun FragmentManager.isFragmentInStack(clazz: Class<out Fragment>): Boolean {
    val backStackEntryCount = backStackEntryCount
    if (backStackEntryCount == 0) {
        return false
    }
    for (i in 0 until backStackEntryCount) {
        if (clazz.name == getBackStackEntryAt(i).name) {
            return true
        }
    }
    return false
}

inline fun FragmentManager.commit(allowStateLoss: Boolean = false, func: EnhanceFragmentTransaction.() -> Unit) {
    val fragmentTransaction = beginTransaction()
    EnhanceFragmentTransaction(this, fragmentTransaction).func()
    if (allowStateLoss) {
        fragmentTransaction.commitAllowingStateLoss()
    } else {
        fragmentTransaction.commit()
    }
}

inline fun FragmentManager.commitNow(allowStateLoss: Boolean = false, func: EnhanceFragmentTransaction.() -> Unit) {
    val fragmentTransaction = beginTransaction()
    EnhanceFragmentTransaction(this, fragmentTransaction).func()
    if (allowStateLoss) {
        fragmentTransaction.commitNowAllowingStateLoss()
    } else {
        fragmentTransaction.commitNow()
    }
}

inline fun FragmentActivity.doFragmentTransaction(allowStateLoss: Boolean = false, func: EnhanceFragmentTransaction.() -> Unit) {
    val transaction = supportFragmentManager.beginTransaction()
    EnhanceFragmentTransaction(supportFragmentManager, transaction).func()
    if (allowStateLoss) {
        transaction.commitAllowingStateLoss()
    } else {
        transaction.commit()
    }
}

inline fun FragmentActivity.doFragmentTransactionNow(allowStateLoss: Boolean = false, func: EnhanceFragmentTransaction.() -> Unit) {
    val transaction = supportFragmentManager.beginTransaction()
    EnhanceFragmentTransaction(supportFragmentManager, transaction).func()
    if (allowStateLoss) {
        transaction.commitNowAllowingStateLoss()
    } else {
        transaction.commitNow()
    }
}

fun <T> T.doFragmentTransactionSafely(
    func: EnhanceFragmentTransaction.() -> Unit
): Boolean where T : FragmentActivity, T : ActivityDelegateOwner {
    return internalCommitNowSafely(func, false)
}

fun <T> T.doFragmentTransactionNowSafely(
    func: EnhanceFragmentTransaction.() -> Unit
): Boolean where T : FragmentActivity, T : ActivityDelegateOwner {
    return internalCommitNowSafely(func, true)
}

private fun <T> T.internalCommitNowSafely(
    func: EnhanceFragmentTransaction.() -> Unit,
    now: Boolean
): Boolean where T : FragmentActivity, T : ActivityDelegateOwner {

    var delegate = findDelegate {
        it is SafelyFragmentTransactionActivityDelegate
    } as? SafelyFragmentTransactionActivityDelegate

    if (delegate == null) {
        delegate = SafelyFragmentTransactionActivityDelegate(now)
        addDelegate(delegate)
    }

    val transaction = supportFragmentManager.beginTransaction()

    EnhanceFragmentTransaction(supportFragmentManager, transaction).func()

    return delegate.safeCommit(this, transaction)
}

inline fun Fragment.doFragmentTransaction(allowStateLoss: Boolean = false, func: EnhanceFragmentTransaction.() -> Unit) {
    val transaction = childFragmentManager.beginTransaction()
    EnhanceFragmentTransaction(childFragmentManager, transaction).func()
    if (allowStateLoss) {
        transaction.commitAllowingStateLoss()
    } else {
        transaction.commit()
    }
}

inline fun Fragment.doFragmentTransactionNow(allowStateLoss: Boolean = false, func: EnhanceFragmentTransaction.() -> Unit) {
    val transaction = childFragmentManager.beginTransaction()
    EnhanceFragmentTransaction(childFragmentManager, transaction).func()
    if (allowStateLoss) {
        transaction.commitNowAllowingStateLoss()
    } else {
        transaction.commitNow()
    }
}

fun <T> T.doFragmentTransactionSafely(
    func: EnhanceFragmentTransaction.() -> Unit,
): Boolean where T : Fragment, T : FragmentDelegateOwner {
    return internalCommitNowSafely(func, false)
}

fun <T> T.doFragmentTransactionNowSafely(
    func: EnhanceFragmentTransaction.() -> Unit,
): Boolean where T : Fragment, T : FragmentDelegateOwner {
    return internalCommitNowSafely(func, true)
}

fun <T> T.internalCommitNowSafely(
    func: EnhanceFragmentTransaction.() -> Unit,
    now: Boolean
): Boolean where T : Fragment, T : FragmentDelegateOwner {

    var delegate: SafelyFragmentTransactionFragmentDelegate? = findDelegate {
        it is SafelyFragmentTransactionFragmentDelegate
    } as? SafelyFragmentTransactionFragmentDelegate

    if (delegate == null) {
        delegate = SafelyFragmentTransactionFragmentDelegate(now)
        addDelegate(delegate)
    }

    val transaction = childFragmentManager.beginTransaction()

    EnhanceFragmentTransaction(childFragmentManager, transaction).func()

    return delegate.safeCommit(this, transaction)
}

private class SafelyFragmentTransactionActivityDelegate(private val now: Boolean) : ActivityDelegate<FragmentActivity> {

    private val mPendingTransactions = mutableListOf<FragmentTransaction>()

    fun safeCommit(@NonNull activityDelegateOwner: ActivityDelegateOwner, @NonNull transaction: FragmentTransaction): Boolean {
        val status = activityDelegateOwner.getStatus()
        val isCommitterResumed = (status == ActivityState.CREATE || status == ActivityState.START || status == ActivityState.RESUME)

        return if (isCommitterResumed) {
            if (now) {
                transaction.commitNow()
            } else {
                transaction.commit()
            }
            false
        } else {
            mPendingTransactions.add(transaction)
            true
        }
    }

    override fun onResumeFragments() {
        if (mPendingTransactions.isNotEmpty()) {
            mPendingTransactions.forEach { it.commit() }
            mPendingTransactions.clear()
        }
    }

}

private class SafelyFragmentTransactionFragmentDelegate(private val now: Boolean) : FragmentDelegate<Fragment> {

    private val pendingTransactions = mutableListOf<FragmentTransaction>()

    fun safeCommit(@NonNull fragment: Fragment, @NonNull transaction: FragmentTransaction): Boolean {
        return if (fragment.isResumed) {
            if (now) {
                transaction.commitNow()
            } else {
                transaction.commit()
            }
            false
        } else {
            pendingTransactions.add(transaction)
            true
        }
    }

    override fun onResume() {
        if (pendingTransactions.isNotEmpty()) {
            pendingTransactions.forEach { it.commit() }
            pendingTransactions.clear()
        }
    }

}

class EnhanceFragmentTransaction constructor(
    private val fragmentManager: FragmentManager,
    private val fragmentTransaction: FragmentTransaction
) : FragmentTransaction() {

    //------------------------------------------------------------------------------------------------
    // extra functions
    //------------------------------------------------------------------------------------------------

    /**
     * 把 [fragment] 添加到回退栈中，并 hide 其他 fragment，
     * 如果 [containerId]==0，则使用 [com.android.base.AndroidSword.setDefaultFragmentContainerId] 中配置的 id，
     * 如果 [tag] == null 则使用 fragment 对应 class 的全限定类名。
     */
    fun addToStack(
        containerId: Int = 0,
        fragment: Fragment,
        tag: String = fragment.javaClassName(),
        transition: Boolean = true
    ): EnhanceFragmentTransaction {
        //set add to stack
        addToBackStack(tag)
        //add
        fragmentTransaction.add(confirmLayoutId(containerId), fragment, tag)
        //hide top
        hideTopFragment()
        if (transition) {
            //set a transition
            setOpeningTransition()
        }
        return this
    }

    /**
     * 以 replace 方式把 [fragment] 添加到回退栈中，
     * 如果 [containerId]==0，则使用 [com.android.base.AndroidSword.setDefaultFragmentContainerId] 中配置的 id，
     * 如果 [tag] == null 则使用 fragment 对应 class 的全限定类名。
     * 此方法可能导致 Fragment 转场动画错乱。
     */
    fun replaceToStack(
        containerId: Int = 0,
        fragment: Fragment,
        tag: String = fragment.javaClassName(),
        transition: Boolean = true
    ): EnhanceFragmentTransaction {
        //set add to stack
        addToBackStack(tag)
        //add
        fragmentTransaction.replace(confirmLayoutId(containerId), fragment, tag)
        //set a transition
        if (transition) {
            setOpeningTransition()
        }
        return this
    }

    private fun confirmLayoutId(layoutId: Int): Int {
        return if (layoutId == 0) {
            FragmentConfig.defaultContainerId()
        } else {
            layoutId
        }
    }

    /**
     * 添加 [fragment]，默认使用 [com.android.base.AndroidSword.setDefaultFragmentContainerId] 中配置的 id，如果 [tag] 为null，则使用 [fragment] 的全限定类名。
     */
    fun addFragment(fragment: Fragment, tag: String = fragment.javaClassName()): FragmentTransaction {
        return fragmentTransaction.add(FragmentConfig.defaultContainerId(), fragment, tag)
    }

    /**
     * 替换为 [fragment]，id 使用 [com.android.base.AndroidSword.setDefaultFragmentContainerId] 中配置的 id，如果 [tag] 为null，则使用 [fragment] 的全限定类名。
     */
    fun replaceFragment(fragment: Fragment, tag: String = fragment.javaClassName(), transition: Boolean = true): FragmentTransaction {
        if (transition) {
            setOpeningTransition()
        }
        return fragmentTransaction.replace(FragmentConfig.defaultContainerId(), fragment, tag)
    }

    /** 隐藏所有的 fragment */
    private fun hideFragments() {
        for (fragment in fragmentManager.fragments) {
            if (fragment != null && fragment.view != null && fragment.isVisible) {
                fragmentTransaction.setMaxLifecycle(fragment, Lifecycle.State.STARTED)
                fragmentTransaction.hide(fragment)
            }
        }
    }

    /** 隐藏第一个可见的 fragment */
    private fun hideTopFragment() {
        fragmentManager.fragments.lastOrNull { it.isVisible && it.view != null }?.let {
            fragmentTransaction.setMaxLifecycle(it, Lifecycle.State.STARTED)
            fragmentTransaction.hide(it)
        }
    }

    fun setOpeningTransition(): FragmentTransaction {
        return fragmentTransaction.setTransition(TRANSIT_FRAGMENT_OPEN)
    }

    fun setClosingTransition(): FragmentTransaction {
        return fragmentTransaction.setTransition(TRANSIT_FRAGMENT_CLOSE)
    }

    fun setFadingTransition(): FragmentTransaction {
        return fragmentTransaction.setTransition(TRANSIT_FRAGMENT_FADE)
    }

    //------------------------------------------------------------------------------------------------
    // original functions
    //------------------------------------------------------------------------------------------------
    override fun setBreadCrumbShortTitle(res: Int): FragmentTransaction {
        return fragmentTransaction.setBreadCrumbShortTitle(res)
    }

    override fun setBreadCrumbShortTitle(text: CharSequence?): FragmentTransaction {
        return fragmentTransaction.setBreadCrumbShortTitle(text)
    }

    override fun setPrimaryNavigationFragment(fragment: Fragment?): FragmentTransaction {
        return fragmentTransaction.setPrimaryNavigationFragment(fragment)
    }

    override fun runOnCommit(runnable: Runnable): FragmentTransaction {
        return fragmentTransaction.runOnCommit(runnable)
    }

    override fun add(fragment: Fragment, tag: String?): FragmentTransaction {
        return fragmentTransaction.add(fragment, tag)
    }

    override fun add(containerViewId: Int, fragment: Fragment): FragmentTransaction {
        return fragmentTransaction.add(containerViewId, fragment)
    }

    override fun add(containerViewId: Int, fragment: Fragment, tag: String?): FragmentTransaction {
        return fragmentTransaction.add(containerViewId, fragment, tag)
    }

    override fun hide(fragment: Fragment): FragmentTransaction {
        return fragmentTransaction.hide(fragment)
    }

    override fun replace(containerViewId: Int, fragment: Fragment): FragmentTransaction {
        return fragmentTransaction.replace(containerViewId, fragment)
    }

    override fun replace(containerViewId: Int, fragment: Fragment, tag: String?): FragmentTransaction {
        return fragmentTransaction.replace(containerViewId, fragment, tag)
    }

    override fun detach(fragment: Fragment): FragmentTransaction {
        return fragmentTransaction.detach(fragment)
    }

    @Deprecated("")
    override fun setAllowOptimization(allowOptimization: Boolean): FragmentTransaction {
        return fragmentTransaction.setAllowOptimization(allowOptimization)
    }

    override fun setCustomAnimations(enter: Int, exit: Int): FragmentTransaction {
        return fragmentTransaction.setCustomAnimations(enter, exit)
    }

    override fun setCustomAnimations(enter: Int, exit: Int, popEnter: Int, popExit: Int): FragmentTransaction {
        return fragmentTransaction.setCustomAnimations(enter, exit, popEnter, popExit)
    }

    override fun addToBackStack(name: String?): FragmentTransaction {
        return fragmentTransaction.addToBackStack(name)
    }

    override fun disallowAddToBackStack(): FragmentTransaction {
        return fragmentTransaction.disallowAddToBackStack()
    }

    override fun setTransitionStyle(styleRes: Int): FragmentTransaction {
        return fragmentTransaction.setTransitionStyle(styleRes)
    }

    override fun setTransition(transit: Int): FragmentTransaction {
        return fragmentTransaction.setTransition(transit)
    }

    override fun attach(fragment: Fragment): FragmentTransaction {
        return fragmentTransaction.attach(fragment)
    }

    override fun show(fragment: Fragment): FragmentTransaction {
        return fragmentTransaction.show(fragment)
    }

    override fun isEmpty(): Boolean {
        return fragmentTransaction.isEmpty
    }

    override fun remove(fragment: Fragment): FragmentTransaction {
        return fragmentTransaction.remove(fragment)
    }

    override fun isAddToBackStackAllowed(): Boolean {
        return fragmentTransaction.isAddToBackStackAllowed
    }

    override fun addSharedElement(sharedElement: View, name: String): FragmentTransaction {
        return fragmentTransaction.addSharedElement(sharedElement, name)
    }

    override fun setBreadCrumbTitle(res: Int): FragmentTransaction {
        return fragmentTransaction.setBreadCrumbTitle(res)
    }

    override fun setBreadCrumbTitle(text: CharSequence?): FragmentTransaction {
        return fragmentTransaction.setBreadCrumbTitle(text)
    }

    override fun setReorderingAllowed(reorderingAllowed: Boolean): FragmentTransaction {
        return fragmentTransaction.setReorderingAllowed(reorderingAllowed)
    }

    @Deprecated("commit will be called automatically")
    override fun commit(): Int {
        throw UnsupportedOperationException("commit will be called automatically")
    }

    @Deprecated("commitAllowingStateLoss will be called automatically")
    override fun commitAllowingStateLoss(): Int {
        throw UnsupportedOperationException("commitAllowingStateLoss will be called automatically")
    }

    @Deprecated(
        "commitNow will be called automatically",
        ReplaceWith("throw UnsupportedOperationException(\"commitNow will be called automatically\")")
    )
    override fun commitNow() {
        throw UnsupportedOperationException("commitNow will be called automatically")
    }

    @Deprecated(
        "commitNowAllowingStateLoss will be called automatically",
        ReplaceWith("throw UnsupportedOperationException(\"commitNowAllowingStateLoss will be called automatically\")")
    )
    override fun commitNowAllowingStateLoss() {
        throw UnsupportedOperationException("commitNowAllowingStateLoss will be called automatically")
    }

}