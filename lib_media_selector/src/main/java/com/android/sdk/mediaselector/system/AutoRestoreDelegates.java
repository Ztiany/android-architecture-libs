package com.android.sdk.mediaselector.system;

import android.content.Intent;
import android.os.Bundle;

import com.android.base.foundation.activity.ActivityDelegate;
import com.android.base.foundation.activity.ActivityDelegateOwner;
import com.android.base.foundation.fragment.FragmentDelegate;
import com.android.base.foundation.fragment.FragmentDelegateOwner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2020-08-07 21:56
 */
class AutoRestoreDelegates {

    static void autoCallback(AppCompatActivity appCompatActivity, SystemMediaSelector systemMediaSelector) {
        if (appCompatActivity instanceof ActivityDelegateOwner) {
            ((ActivityDelegateOwner) appCompatActivity).addDelegate(new ActivityDelegate() {
                @Override
                public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
                    systemMediaSelector.onSaveInstanceState(savedInstanceState);
                }

                @Override
                public void onRestoreInstanceState(Bundle savedInstanceState) {
                    systemMediaSelector.onRestoreInstanceState(savedInstanceState);
                }

                @Override
                public void onActivityResult(int requestCode, int resultCode, Intent data) {
                    systemMediaSelector.onActivityResult(requestCode, resultCode, data);
                }
            });
        }
    }

    static void autoCallback(Fragment fragment, SystemMediaSelector systemMediaSelector) {
        if (fragment instanceof FragmentDelegateOwner) {
            ((FragmentDelegateOwner) fragment).addDelegate(new FragmentDelegate() {
                @Override
                public void onActivityCreated(@Nullable Bundle savedInstanceState) {
                    systemMediaSelector.onRestoreInstanceState(savedInstanceState);
                }

                @Override
                public void onSaveInstanceState(Bundle savedInstanceState) {
                    systemMediaSelector.onSaveInstanceState(savedInstanceState);
                }

                @Override
                public void onActivityResult(int requestCode, int resultCode, Intent data) {
                    systemMediaSelector.onActivityResult(requestCode, resultCode, data);
                }

            });
        }
    }

}