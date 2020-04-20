package com.android.base.app.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.android.base.foundation.fragment.FragmentDelegate;
import com.android.base.foundation.fragment.FragmentDelegateOwner;
import com.github.dmstocking.optional.java.util.function.Predicate;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.UiThread;
import androidx.fragment.app.Fragment;


@UiThread
final class FragmentDelegates implements FragmentDelegate<Fragment>, FragmentDelegateOwner {

    private final Fragment mDelegateOwner;
    private List<FragmentDelegate> mDelegates = new ArrayList<>(4);

    <T extends Fragment & FragmentDelegateOwner> FragmentDelegates(T delegateOwner) {
        mDelegateOwner = delegateOwner;
    }

    @Override
    public void onAttach(Context context) {
        for (FragmentDelegate fragmentDelegate : mDelegates) {
            fragmentDelegate.onAttach(context);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        for (FragmentDelegate fragmentDelegate : mDelegates) {
            fragmentDelegate.onCreate(savedInstanceState);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        for (FragmentDelegate fragmentDelegate : mDelegates) {
            fragmentDelegate.onViewCreated(view, savedInstanceState);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        for (FragmentDelegate fragmentDelegate : mDelegates) {
            fragmentDelegate.onActivityCreated(savedInstanceState);
        }
    }

    @Override
    public void onStart() {
        for (FragmentDelegate fragmentDelegate : mDelegates) {
            fragmentDelegate.onStart();
        }
    }

    @Override
    public void onResume() {
        for (FragmentDelegate fragmentDelegate : mDelegates) {
            fragmentDelegate.onResume();
        }
    }

    @Override
    public void onPause() {
        for (FragmentDelegate fragmentDelegate : mDelegates) {
            fragmentDelegate.onPause();
        }
    }

    @Override
    public void onStop() {
        for (FragmentDelegate fragmentDelegate : mDelegates) {
            fragmentDelegate.onStop();
        }
    }

    @Override
    public void onDestroy() {
        for (FragmentDelegate fragmentDelegate : mDelegates) {
            fragmentDelegate.onDestroy();
        }
    }

    @Override
    public void onDestroyView() {
        for (FragmentDelegate fragmentDelegate : mDelegates) {
            fragmentDelegate.onDestroyView();
        }
    }

    @Override
    public void onDetach() {
        for (FragmentDelegate fragmentDelegate : mDelegates) {
            fragmentDelegate.onDetach();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        for (FragmentDelegate fragmentDelegate : mDelegates) {
            fragmentDelegate.setUserVisibleHint(isVisibleToUser);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        for (FragmentDelegate fragmentDelegate : mDelegates) {
            fragmentDelegate.onSaveInstanceState(savedInstanceState);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        for (FragmentDelegate fragmentDelegate : mDelegates) {
            fragmentDelegate.onHiddenChanged(hidden);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        for (FragmentDelegate fragmentDelegate : mDelegates) {
            fragmentDelegate.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        for (FragmentDelegate fragmentDelegate : mDelegates) {
            fragmentDelegate.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void addDelegate(FragmentDelegate fragmentDelegate) {
        mDelegates.add(fragmentDelegate);
        fragmentDelegate.onAttachToFragment(mDelegateOwner);
    }

    @Override
    public boolean removeDelegate(FragmentDelegate fragmentDelegate) {
        boolean remove = mDelegates.remove(fragmentDelegate);
        if (remove) {
            fragmentDelegate.onDetachFromFragment();
        }
        return remove;
    }

    @Override
    public FragmentDelegate findDelegate(Predicate<FragmentDelegate> predicate) {
        for (FragmentDelegate delegate : mDelegates) {
            if (predicate.test(delegate)) {
                return delegate;
            }
        }
        return null;
    }

}