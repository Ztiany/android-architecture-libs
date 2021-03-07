package com.android.base.app.ui

import android.app.Activity
import android.app.Dialog
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type


///////////////////////////////////////////////////////////////////////////
// 成员反射方式，参考 https://juejin.cn/post/6906153878312452103。
///////////////////////////////////////////////////////////////////////////

inline fun <reified VB : ViewBinding> Activity.viewBinding(setContentView: Boolean = false) = lazy {
    viewBinding<VB>(layoutInflater).apply {
        if (setContentView) {
            setContentView(root)
        }
    }
}

inline fun <reified VB : ViewBinding> Dialog.viewBinding(setContentView: Boolean = false) = lazy {
    viewBinding<VB>(layoutInflater).apply {
        if (setContentView) {
            setContentView(root)
        }
    }
}

inline fun <reified VB : ViewBinding> viewBinding(layoutInflater: LayoutInflater): VB {
    return VB::class.java.getMethod("inflate", LayoutInflater::class.java).invoke(null, layoutInflater) as VB
}

///////////////////////////////////////////////////////////////////////////
// 基类反射方式，参考 https://juejin.cn/post/6906153878312452103。
///////////////////////////////////////////////////////////////////////////

@JvmName("inflateWithGeneric")
fun <VB : ViewBinding> Any.inflateBindingWithParameterizedType(layoutInflater: LayoutInflater): VB =
        withGenericBindingClass(this) { clazz ->
            clazz.getMethod("inflate", LayoutInflater::class.java).invoke(null, layoutInflater) as VB
        }

@JvmName("inflateWithGeneric")
fun <VB : ViewBinding> Any.inflateBindingWithParameterizedType(layoutInflater: LayoutInflater, parent: ViewGroup?): VB =
        withGenericBindingClass(this) { clazz ->
            clazz.getMethod("inflate", LayoutInflater::class.java, ViewGroup::class.java).invoke(null, layoutInflater, parent) as VB
        }

@JvmName("inflateWithGeneric")
fun <VB : ViewBinding> Any.inflateBindingWithParameterizedType(layoutInflater: LayoutInflater, parent: ViewGroup?, attachToParent: Boolean): VB =
        withGenericBindingClass(this) { clazz ->
            clazz.getMethod("inflate", LayoutInflater::class.java, ViewGroup::class.java, Boolean::class.java).invoke(null, layoutInflater, parent, attachToParent) as VB
        }

private fun <VB : ViewBinding> withGenericBindingClass(any: Any, block: (Class<VB>) -> VB): VB {
    return block(any.findViewBindingType as Class<VB>)
}

private val Any.findViewBindingType: Type
    get() {
        var genericSuperclass = javaClass.genericSuperclass
        var superclass = javaClass.superclass

        while (superclass != null) {
            if (genericSuperclass is ParameterizedType) {
                val target = genericSuperclass.actualTypeArguments.find {
                    ViewBinding::class.java.isAssignableFrom(it as Class<*>)
                }
                if (target != null) {
                    return target
                }
            }
            genericSuperclass = superclass.genericSuperclass
            superclass = superclass.superclass
        }

        throw IllegalStateException("Can't find the type of view binding form ${this::class.java.name}.")
    }