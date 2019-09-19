package com.android.base.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.android.base.utils.android.compat.AndroidVersion;
import com.github.dmstocking.optional.java.util.function.Predicate;

import org.jetbrains.annotations.NotNull;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.UiThread;
import androidx.appcompat.app.AppCompatActivity;
import timber.log.Timber;

/**
 * <pre>
 *          1，封装通用流程。
 *          2，onBackPressed 事件分发，优先交给 Fragment 处理。
 *          3，提供 RxJava 的生命周期绑定。
 *  </pre>
 *
 * @author Ztiany
 * Date : 2016-05-04 15:40
 * Email: 1169654504@qq.com
 */
public abstract class BaseActivity extends AppCompatActivity implements ActivityDelegateOwner {

    private final ActivityDelegates mActivityDelegates = new ActivityDelegates(this);

    private ActivityStatus mActivityStatus = ActivityStatus.INITIALIZED;

    private String tag() {
        return this.getClass().getSimpleName();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Timber.tag(tag()).d("---->onCreate before call super");

        initialize(savedInstanceState);
        mActivityDelegates.callOnCreateBeforeSetContentView(savedInstanceState);

        super.onCreate(savedInstanceState);
        Timber.tag(tag()).d("---->onCreate after call super  " + "bundle = " + savedInstanceState);

        Object layout = layout();
        if (layout instanceof View) {
            setContentView((View) layout);
        } else if (layout instanceof Integer) {
            setContentView((Integer) layout);
        } else if (layout == null) {
            Timber.d("layout() return null layout");
        } else {
            throw new IllegalArgumentException("layout() return type no support, layout = " + layout);
        }

        mActivityStatus = ActivityStatus.CREATE;
        mActivityDelegates.callOnCreateAfterSetContentView(savedInstanceState);

        setupView(savedInstanceState);
    }

    @Override
    protected void onRestart() {
        Timber.tag(tag()).d("---->onRestart before call super");
        super.onRestart();
        Timber.tag(tag()).d("---->onRestart after call super  ");
        mActivityDelegates.callOnRestart();
    }

    @Override
    protected void onStart() {
        Timber.tag(tag()).d("---->onStart before call super");
        super.onStart();
        Timber.tag(tag()).d("---->onStart after call super");
        mActivityStatus = ActivityStatus.START;
        mActivityDelegates.callOnStart();
    }

    @Override
    protected void onResume() {
        Timber.tag(tag()).d("---->onResume before call super");
        super.onResume();
        Timber.tag(tag()).d("---->onResume after call super");
        mActivityStatus = ActivityStatus.RESUME;
        mActivityDelegates.callOnResume();
    }

    @Override
    protected void onPause() {
        Timber.tag(tag()).d("---->onPause before call super");
        mActivityStatus = ActivityStatus.PAUSE;
        mActivityDelegates.callOnPause();
        super.onPause();
        Timber.tag(tag()).d("---->onPause after call super  ");
    }

    @Override
    protected void onStop() {
        Timber.tag(tag()).d("---->onStop before call super");
        mActivityStatus = ActivityStatus.STOP;
        mActivityDelegates.callOnStop();
        super.onStop();
        Timber.tag(tag()).d("---->onStop after call super");
    }

    @Override
    protected void onDestroy() {
        Timber.tag(tag()).d("---->onDestroy before call super");
        mActivityStatus = ActivityStatus.DESTROY;
        mActivityDelegates.callOnDestroy();
        super.onDestroy();
        Timber.tag(tag()).d("---->onDestroy after call super");
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mActivityDelegates.callOnPostCreate(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(@NotNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mActivityDelegates.callOnSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mActivityDelegates.callOnRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mActivityDelegates.callOnActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mActivityDelegates.callOnRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        mActivityDelegates.callOnResumeFragments();
    }

    ///////////////////////////////////////////////////////////////////////////
    // interface impl
    ///////////////////////////////////////////////////////////////////////////
    @UiThread
    @Override
    public final void addDelegate(@NonNull ActivityDelegate activityDelegate) {
        mActivityDelegates.addActivityDelegate(activityDelegate);
    }

    @SuppressWarnings("unused")
    @UiThread
    @Override
    public final boolean removeDelegate(@NonNull ActivityDelegate activityDelegate) {
        return mActivityDelegates.removeActivityDelegate(activityDelegate);
    }

    @Override
    public ActivityDelegate findDelegate(Predicate<ActivityDelegate> predicate) {
        return mActivityDelegates.findDelegate(predicate);
    }

    @Override
    public ActivityStatus getStatus() {
        return mActivityStatus;
    }

    /**
     * Before call super.onCreate and setContentView
     *
     * @param savedInstanceState state
     */
    protected void initialize(@Nullable Bundle savedInstanceState) {
    }

    /**
     * provide a layoutId (int) or layout (View)
     *
     * @return layoutId
     */
    @Nullable
    protected abstract Object layout();

    /**
     * after setContentView
     */
    protected abstract void setupView(@Nullable Bundle savedInstanceState);

    @Override
    public void onBackPressed() {
        if (BackHandlerHelper.handleBackPress(this)) {
            Timber.d("onBackPressed() called but child fragment handle it");
        } else {
            superOnBackPressed();
        }
    }

    protected void superOnBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean isDestroyed() {
        if (AndroidVersion.atLeast(17)) {
            return super.isDestroyed();
        } else {
            return getStatus() == ActivityStatus.DESTROY;
        }
    }

}