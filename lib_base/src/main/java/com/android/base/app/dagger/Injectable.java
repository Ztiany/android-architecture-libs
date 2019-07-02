package com.android.base.app.dagger;

/**
 * 当 Activity 实现此接口时，如果需要为内部的 Fragment 提供注入容器，需要实现 HasSupportFragmentInjector，具体如下面代码：
 *
 * <pre>{@code
 *
 *       class InjectableActivity implements Injectable, HasSupportFragmentInjector{
 *              @Inject
 *              DispatchingAndroidInjector<Fragment> fragmentInjector;
 *
 *              public AndroidInjector<Fragment> supportFragmentInjector() {
 *                         return fragmentInjector;
 *              }
 *       }
 * }
 *
 * 标记接口，用于标记此类需要被注入依赖。
 *
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2018-12-11 12:38
 */
public interface Injectable {

    default boolean enableInject() {
        return true;
    }

}

