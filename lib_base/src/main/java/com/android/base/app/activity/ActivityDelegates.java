package com.android.base.app.activity;

import android.content.Intent;
import android.os.Bundle;

import com.github.dmstocking.optional.java.util.function.Predicate;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.UiThread;
import androidx.appcompat.app.AppCompatActivity;

/**
 * @author Ztiany
 * Email: 1169654504@qq.com
 * Date : 2016-12-20 11:43
 */
@UiThread
final class ActivityDelegates {

    private final List<ActivityDelegate> mDelegates;
    private AppCompatActivity mBaseActivity;

    ActivityDelegates(AppCompatActivity baseActivity) {
        mDelegates = new ArrayList<>(4);
        mBaseActivity = baseActivity;
    }

    void callOnCreateBeforeSetContentView(@Nullable Bundle savedInstanceState) {
        for (ActivityDelegate activityDelegate : mDelegates) {
            activityDelegate.onCreateBeforeSetContentView(savedInstanceState);
        }
    }

    void callOnCreateAfterSetContentView(@Nullable Bundle savedInstanceState) {
        for (ActivityDelegate activityDelegate : mDelegates) {
            activityDelegate.onCreateAfterSetContentView(savedInstanceState);
        }
    }

    void callOnRestoreInstanceState(Bundle savedInstanceState) {
        for (ActivityDelegate activityDelegate : mDelegates) {
            activityDelegate.onRestoreInstanceState(savedInstanceState);
        }
    }

    void callOnPostCreate(@Nullable Bundle savedInstanceState) {
        for (ActivityDelegate activityDelegate : mDelegates) {
            activityDelegate.onPostCreate(savedInstanceState);
        }
    }

    void callOnSaveInstanceState(@NonNull  Bundle outState) {
        for (ActivityDelegate activityDelegate : mDelegates) {
            activityDelegate.onSaveInstanceState(outState);
        }
    }

    void callOnDestroy() {
        for (ActivityDelegate activityDelegate : mDelegates) {
            activityDelegate.onDestroy();
        }
    }

    void callOnStop() {
        for (ActivityDelegate activityDelegate : mDelegates) {
            activityDelegate.onStop();
        }
    }

    void callOnPause() {
        for (ActivityDelegate activityDelegate : mDelegates) {
            activityDelegate.onPause();
        }
    }

    void callOnResume() {
        for (ActivityDelegate activityDelegate : mDelegates) {
            activityDelegate.onResume();
        }
    }

    void callOnStart() {
        for (ActivityDelegate activityDelegate : mDelegates) {
            activityDelegate.onStart();
        }
    }

    void callOnRestart() {
        for (ActivityDelegate activityDelegate : mDelegates) {
            activityDelegate.onRestart();
        }
    }

    void callOnActivityResult(int requestCode, int resultCode, Intent data) {
        for (ActivityDelegate activityDelegate : mDelegates) {
            activityDelegate.onActivityResult(requestCode, resultCode, data);
        }
    }

    void callOnRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        for (ActivityDelegate activityDelegate : mDelegates) {
            activityDelegate.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    void callOnResumeFragments() {
        for (ActivityDelegate activityDelegate : mDelegates) {
            activityDelegate.onResumeFragments();
        }
    }

    @SuppressWarnings("unchecked")
    void addActivityDelegate(ActivityDelegate activityDelegate) {
        mDelegates.add(activityDelegate);
        activityDelegate.onAttachedToActivity(mBaseActivity);
    }

    boolean removeActivityDelegate(ActivityDelegate activityDelegate) {
        boolean remove = mDelegates.remove(activityDelegate);
        if (remove) {
            activityDelegate.onDetachedFromActivity();
        }
        return remove;
    }

    ActivityDelegate findDelegate(Predicate<ActivityDelegate> predicate) {
        for (ActivityDelegate delegate : mDelegates) {
            if (predicate.test(delegate)) {
                return delegate;
            }
        }
        return null;
    }

}
