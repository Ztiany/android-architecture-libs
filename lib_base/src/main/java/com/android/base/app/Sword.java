package com.android.base.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import com.android.base.app.dagger.Injectable;
import com.android.base.app.fragment.animator.FragmentAnimator;
import com.android.base.app.fragment.tools.FragmentConfig;
import com.android.base.app.ui.LoadingViewFactory;
import com.android.base.app.ui.PageNumber;
import com.android.base.app.ui.RefreshLoadViewFactory;
import com.android.base.app.ui.RefreshViewFactory;
import com.android.base.interfaces.ActivityLifecycleCallbacksAdapter;
import com.android.base.receiver.NetworkState;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.AppUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.UiThread;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import dagger.android.AndroidInjection;
import dagger.android.support.AndroidSupportInjection;
import io.reactivex.Flowable;

/**
 * useful tools for android development, just like a sword.
 *
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2018-04-16 17:12
 */
@SuppressWarnings("unused")
@UiThread
public final class Sword {

    private static final Sword ONLY_BASE = new Sword();

    private Sword() {
        mApplicationDelegate = new ApplicationDelegate();
    }

    public static Sword get() {
        return ONLY_BASE;
    }

    /**
     * LoadingView
     */
    private LoadingViewFactory mLoadingViewFactory;

    /**
     * Application lifecycle delegate
     */
    private ApplicationDelegate mApplicationDelegate;

    /**
     * 错误类型检查
     */
    private ErrorClassifier mErrorClassifier;

    /**
     * dialog 最小展示时间
     */
    private long minimumShowingDialogMills;

    /**
     * 获取 Application 代理
     */
    public ApplicationDelegate getApplicationDelegate() {
        return mApplicationDelegate;
    }

    public Sword registerLoadingFactory(LoadingViewFactory loadingViewFactory) {
        if (mLoadingViewFactory != null) {
            throw new UnsupportedOperationException("LoadingViewFactory had already set");
        }
        mLoadingViewFactory = loadingViewFactory;
        return this;
    }

    public LoadingViewFactory getLoadingViewFactory() {
        if (mLoadingViewFactory == null) {
            throw new NullPointerException("you have not set the LoadingViewFactory by AndroidBase");
        }
        return mLoadingViewFactory;
    }

    public Flowable<NetworkState> networkState() {
        return NetworkState.observableState();
    }


    public interface CrashProcessor {
        void uncaughtException(Thread thread, Throwable ex);
    }

    public Sword setCrashProcessor(CrashProcessor crashProcessor) {
        mApplicationDelegate.setCrashProcessor(crashProcessor);
        return this;
    }

    public Sword setDefaultPageStart(int pageStart) {
        PageNumber.setDefaultPageStart(pageStart);
        return this;
    }

    public Sword setDefaultPageSize(int defaultPageSize) {
        PageNumber.setDefaultPageSize(defaultPageSize);
        return this;
    }

    /**
     * 给 {@link com.android.base.app.fragment.tools.Fragments } 设置一个默认的容器 id，在使用 其相关方法而没有传入特定的容器 id 时，则使用默认的容器 id。
     *
     * @param defaultContainerId 容器id
     */
    public Sword setDefaultFragmentContainerId(int defaultContainerId) {
        FragmentConfig.setDefaultContainerId(defaultContainerId);
        return this;
    }

    /**
     * 设置 dialog 最小展示时间，建议不超过 1 秒
     */
    public Sword setMinimumShowingDialogMills(long minimumShowingDialogMills) {
        this.minimumShowingDialogMills = minimumShowingDialogMills;
        return this;
    }

    public Sword setDefaultFragmentAnimator(FragmentAnimator animator) {
        FragmentConfig.setDefaultFragmentAnimator(animator);
        return this;
    }

    public Sword enableAutoInject() {
        Application.ActivityLifecycleCallbacks activityLifecycleCallbacks = new ActivityLifecycleCallbacksAdapter() {

            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                if (activity instanceof Injectable) {
                    if (((Injectable) activity).enableInject()) {
                        AndroidInjection.inject(activity);
                        if (activity instanceof FragmentActivity) {
                            handedFragmentInject((FragmentActivity) activity);
                        }
                    }
                }
            }

            private void handedFragmentInject(FragmentActivity activity) {
                activity.getSupportFragmentManager().registerFragmentLifecycleCallbacks(new FragmentManager.FragmentLifecycleCallbacks() {
                    @Override
                    public void onFragmentAttached(@NonNull FragmentManager fm, @NonNull Fragment f, @NonNull Context context) {
                        if (f instanceof Injectable) {
                            if (((Injectable) f).enableInject()) {
                                AndroidSupportInjection.inject(f);
                            }
                        }
                    }
                }, true);
            }
        };
        mApplicationDelegate.getApplication().registerActivityLifecycleCallbacks(activityLifecycleCallbacks);
        return this;
    }

    /**
     * 获取可观察的 app 生命周期
     */
    public Flowable<Boolean> appState() {
        return mApplicationDelegate.appAppState();
    }

    /**
     * 获取当前resume的Activity
     *
     * @return activity
     */
    @Nullable
    public Activity getTopActivity() {
        return ActivityUtils.getTopActivity();
    }

    /**
     * App是否在前台运行
     *
     * @return true 表示App在前台运行
     */
    public boolean isForeground() {
        return AppUtils.isAppForeground();
    }

    public interface ErrorClassifier {
        boolean isNetworkError(Throwable throwable);

        boolean isServerError(Throwable throwable);
    }

    public Sword setErrorClassifier(ErrorClassifier errorClassifier) {
        if (mErrorClassifier != null) {
            throw new UnsupportedOperationException("ErrorClassifier had already set");
        }
        mErrorClassifier = errorClassifier;
        return this;
    }

    public ErrorClassifier errorClassifier() {
        return mErrorClassifier;
    }

    public Sword registerRefreshLoadViewFactory(RefreshLoadViewFactory.Factory factory) {
        RefreshLoadViewFactory.registerFactory(factory);
        return this;
    }

    public Sword registerRefreshViewFactory(RefreshViewFactory.Factory factory) {
        RefreshViewFactory.registerFactory(factory);
        return this;
    }

    public long minimumShowingDialogMills() {
        return minimumShowingDialogMills;
    }

}