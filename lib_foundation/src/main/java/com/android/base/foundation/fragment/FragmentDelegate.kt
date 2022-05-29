package com.android.base.foundation.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.annotation.UiThread
import androidx.fragment.app.Fragment

@UiThread
interface FragmentDelegate<T : Fragment> {

    /**该Delegate被添加到Fragment中*/
    fun onAttachToFragment(fragment: T) {}

    /**调用此方法时，清除Fragment的引用*/
    fun onDetachFromFragment() {}

    fun onAttach(context: Context) {}
    fun onCreate(savedInstanceState: Bundle?) {}
    fun onActivityCreated(savedInstanceState: Bundle?) {}
    fun onSaveInstanceState(savedInstanceState: Bundle) {}
    fun onViewCreated(view: View, savedInstanceState: Bundle?) {}
    fun onStart() {}
    fun onResume() {}
    fun onPause() {}
    fun onStop() {}
    fun onDestroy() {}
    fun onDestroyView() {}
    fun onDetach() {}

    fun setUserVisibleHint(isVisibleToUser: Boolean) {}
    fun onHiddenChanged(hidden: Boolean) {}

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {}
    fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {}

}