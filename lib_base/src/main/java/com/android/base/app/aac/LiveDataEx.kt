package com.android.base.app.aac

import androidx.lifecycle.MutableLiveData

fun <T> MutableLiveData<T>.init(t: T): MutableLiveData<T> {
    this.postValue(t)
    return this
}

fun <T> MutableLiveData<T>.touchOff() {
    this.postValue(this.value)
}

fun <T> MutableLiveData<List<T>>.append(list: List<T>) {
    val value = this.value
    if (value == null) {
        this.postValue(list)
    } else {
        val mutableList = value.toMutableList()
        mutableList.addAll(list)
        this.postValue(mutableList)
    }
}