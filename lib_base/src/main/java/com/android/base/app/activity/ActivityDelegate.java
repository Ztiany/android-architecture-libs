package com.android.base.app.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


/**
 * Activity生命周期代理
 *
 * @author Ztiany
 * Date : 2016-05-06 15:04
 * Email: 1169654504@qq.com
 */
@SuppressWarnings("unused")
public interface ActivityDelegate<T extends Activity> {

    default void onAttachedToActivity(@NonNull T activity) {
    }

    default void onDetachedFromActivity() {
    }

    default void onCreateBeforeSetContentView(@Nullable Bundle savedInstanceState) {
    }

    default void onCreateAfterSetContentView(@Nullable Bundle savedInstanceState) {
    }

    default void onSaveInstanceState(Bundle savedInstanceState) {
    }

    default void onStart() {
    }

    default void onResume() {
    }

    default void onPause() {
    }

    default void onStop() {
    }

    default void onDestroy() {
    }

    default void onActivityResult(int requestCode, int resultCode, Intent data) {
    }

    default void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
    }

    default void onRestoreInstanceState(Bundle savedInstanceState) {
    }

    default void onRestart() {
    }

    default void onPostCreate(@Nullable Bundle savedInstanceState) {
    }

    default void onResumeFragments() {
    }

}
