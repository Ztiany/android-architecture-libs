package com.android.base.app.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

public interface FragmentDelegate<T extends Fragment> {

    /**
     * 当该Delegate被添加到Fragment中
     */
    default void onAttachToFragment(T fragment) {
    }

    /**
     * 调用此方法时，清除Fragment的引用
     */
    default void onDetachFromFragment() {
    }

    default void onAttach(Context context) {
    }

    default void onCreate(@Nullable Bundle savedInstanceState) {
    }

    default void onActivityCreated(@Nullable Bundle savedInstanceState) {
    }

    default void onSaveInstanceState(Bundle savedInstanceState) {
    }

    default void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
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

    default void onDestroyView() {
    }

    default void onDetach() {
    }

    default void setUserVisibleHint(boolean isVisibleToUser) {
    }

    default void onHiddenChanged(boolean hidden) {
    }

    default void onActivityResult(int requestCode, int resultCode, Intent data) {
    }

    default void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
    }

}
