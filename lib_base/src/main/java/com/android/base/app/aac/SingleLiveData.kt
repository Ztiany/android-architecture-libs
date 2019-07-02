package com.android.base.app.aac

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.Observer

/** https://github.com/Shopify/livedata-ktx */
class SingleLiveData<T> : MediatorLiveData<T>() {

    private var _version = 0
    private val version: Int get() = _version

    override fun observe(owner: LifecycleOwner, observer: Observer<T>) {
        val observerVersion = version
        super.observe(owner, Observer {
            if (observerVersion < version) {
                observer.onChanged(it)
            }
        })
    }

    override fun observeForever(observer: Observer<T>) {
        val observeSinceVersion = version
        super.observeForever {
            if (version > observeSinceVersion) {
                observer.onChanged(it)
            }
        }
    }

    override fun setValue(value: T?) {
        _version++
        super.setValue(value)
    }

}