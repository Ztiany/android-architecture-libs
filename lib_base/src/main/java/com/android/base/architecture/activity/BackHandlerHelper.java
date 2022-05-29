package com.android.base.architecture.activity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import java.util.List;

/**
 * <pre>
 *        1.Fragment 需要自己处理 BackPress 事件，如果不处理，就交给子 Fragment 处理。都不处理则由 Activity 处理。
 *        2.BackPress 的传递由低层往深层传递，同一层级的中外层中的 Fragment 优先处理。
 *        3.在 Fragment 中嵌套使用 Fragment 时，请使用 getSupportChildFragmentManager。
 * </pre>
 */
@SuppressWarnings("WeakerAccess")
public class BackHandlerHelper {

    /**
     * 将back事件分发给 FragmentManager 中管理的子Fragment，如果该 FragmentManager 中的所有Fragment都
     * 没有处理back事件，则尝试 FragmentManager.popBackStack()
     *
     * @return 如果处理了back键则返回 <b>true</b>
     * @see #handleBackPress(Fragment)
     * @see #handleBackPress(FragmentActivity)
     */
    public static boolean handleBackPress(FragmentManager fragmentManager) {
        List<Fragment> fragments = fragmentManager.getFragments();
        for (int i = fragments.size() - 1; i >= 0; i--) {
            Fragment child = fragments.get(i);
            if (isFragmentBackHandled(child)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 将back事件分发给Fragment中的子Fragment,
     * 该方法调用了 {@link #handleBackPress(FragmentManager)}
     *
     * @return 如果处理了back键则返回 <b>true</b>
     */
    public static boolean handleBackPress(Fragment fragment) {
        return handleBackPress(fragment.getChildFragmentManager());
    }

    /**
     * 将back事件分发给Activity中的子Fragment,
     * 该方法调用了 {@link #handleBackPress(FragmentManager)}
     *
     * @return 如果处理了back键则返回 <b>true</b>
     */
    public static boolean handleBackPress(FragmentActivity fragmentActivity) {
        return handleBackPress(fragmentActivity.getSupportFragmentManager());
    }

    /**
     * 判断Fragment是否处理了Back键
     *
     * @return 如果处理了back键则返回 <b>true</b>
     */
    @SuppressWarnings("WeakerAccess")
    public static boolean isFragmentBackHandled(Fragment fragment) {
        return fragment != null
                && fragment.isVisible()
                && fragment.getUserVisibleHint() //getUserVisibleHint默认情况下为true，在ViewPager中会被使用到。
                && fragment instanceof OnBackPressListener
                && ((OnBackPressListener) fragment).onBackPressed();
    }

}