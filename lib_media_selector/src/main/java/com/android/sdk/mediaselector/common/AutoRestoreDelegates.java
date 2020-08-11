package com.android.sdk.mediaselector.common;

import android.content.Intent;
import android.os.Bundle;

import com.android.base.foundation.activity.ActivityDelegate;
import com.android.base.foundation.activity.ActivityDelegateOwner;
import com.android.base.foundation.fragment.FragmentDelegate;
import com.android.base.foundation.fragment.FragmentDelegateOwner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2020-08-07 21:56
 */
@RestrictTo(RestrictTo.Scope.LIBRARY)
public class AutoRestoreDelegates {

    public static void autoCallback(AppCompatActivity appCompatActivity, ActivityStateHandler stateHandler) {
        if (appCompatActivity instanceof ActivityDelegateOwner) {
            ((ActivityDelegateOwner) appCompatActivity).addDelegate(new ActivityDelegate() {
                @Override
                public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
                    stateHandler.onSaveInstanceState(savedInstanceState);
                }

                @Override
                public void onRestoreInstanceState(Bundle savedInstanceState) {
                    stateHandler.onRestoreInstanceState(savedInstanceState);
                }

                @Override
                public void onActivityResult(int requestCode, int resultCode, Intent data) {
                    stateHandler.onActivityResult(requestCode, resultCode, data);
                }
            });
        }
    }

    public static void autoCallback(Fragment fragment, ActivityStateHandler stateHandler) {
        if (fragment instanceof FragmentDelegateOwner) {
            ((FragmentDelegateOwner) fragment).addDelegate(new FragmentDelegate() {
                @Override
                public void onActivityCreated(@Nullable Bundle savedInstanceState) {
                    stateHandler.onRestoreInstanceState(savedInstanceState);
                }

                @Override
                public void onSaveInstanceState(Bundle savedInstanceState) {
                    stateHandler.onSaveInstanceState(savedInstanceState);
                }

                @Override
                public void onActivityResult(int requestCode, int resultCode, Intent data) {
                    stateHandler.onActivityResult(requestCode, resultCode, data);
                }

            });
        }
    }

}