package com.android.base.app;

import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.net.ConnectivityManager;

import com.android.base.receiver.NetStateReceiver;
import com.android.base.utils.BaseUtils;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.Utils;

import java.util.concurrent.atomic.AtomicBoolean;

import io.reactivex.Flowable;
import io.reactivex.processors.BehaviorProcessor;
import timber.log.Timber;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2018-10-12 18:19
 */
@SuppressWarnings("WeakerAccess,unused")
public final class ApplicationDelegate {

    private Application mApplication;

    private CrashHandler mCrashHandler;
    private BehaviorProcessor<Boolean> mAppStatus;

    private AtomicBoolean mOnCreateCalled = new AtomicBoolean(false);
    private AtomicBoolean mOnAttachBaseCalled = new AtomicBoolean(false);

    ApplicationDelegate() {
    }

    public void attachBaseContext(@SuppressWarnings("unused") Context base) {
        if (!mOnAttachBaseCalled.compareAndSet(false, true)) {
            throw new IllegalStateException("Can only be called once");
        }
        //no op
    }

    public void onCreate(Application application) {
        if (!mOnCreateCalled.compareAndSet(false, true)) {
            throw new IllegalStateException("Can only be called once");
        }
        mApplication = application;
        //工具类初始化
        BaseUtils.init(application);
        //异常日志记录
        mCrashHandler = CrashHandler.register(application);
        //网络状态
        listenNetworkState();
        //App前台后台
        listenActivityLifecycleCallbacks();
    }

    public void onTerminate() {
        //no op
    }

    public void onConfigurationChanged(Configuration newConfig) {
        //no op
    }

    public void onTrimMemory(int level) {
        //no op
    }

    public void onLowMemory() {
        //no op
    }

    private void listenNetworkState() {
        NetStateReceiver netStateReceiver = new NetStateReceiver();
        mApplication.registerReceiver(netStateReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    private void listenActivityLifecycleCallbacks() {
        mAppStatus = BehaviorProcessor.create();
        AppUtils.registerAppStatusChangedListener(this, new Utils.OnAppStatusChangedListener() {
            @Override
            public void onForeground() {
                Timber.d("app进入前台");
                mAppStatus.onNext(true);
            }

            @Override
            public void onBackground() {
                Timber.d("app进入后台");
                mAppStatus.onNext(false);
            }
        });
    }

    /**
     * 获取可观察的 app 生命周期
     */
    Flowable<Boolean> appAppState() {
        return mAppStatus;
    }

    void setCrashProcessor(Sword.CrashProcessor crashProcessor) {
        if (mCrashHandler != null) {
            mCrashHandler.setCrashProcessor(crashProcessor);
        }
    }

    Application getApplication() {
        return mApplication;
    }

}