package com.android.base.architecture.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/*
 * ViewBinding 封装【基类反射方式】，具体参考：
 *
 *      1. https://juejin.cn/post/6906153878312452103
 *      2. https://github.com/DylanCaiCoding/ViewBindingKtx
 */

@JvmName("inflateWithGeneric")
fun <VB : ViewBinding> Any.inflateBindingWithParameterizedType(layoutInflater: LayoutInflater): VB =
    withGenericBindingClass(this) { clazz ->
        clazz.getMethod("inflate", LayoutInflater::class.java).invoke(null, layoutInflater) as VB
    }

@JvmName("inflateWithGeneric")
fun <VB : ViewBinding> Any.inflateBindingWithParameterizedType(
    layoutInflater: LayoutInflater,
    parent: ViewGroup?
): VB =
    withGenericBindingClass(this) { clazz ->
        clazz.getMethod("inflate", LayoutInflater::class.java, ViewGroup::class.java)
            .invoke(null, layoutInflater, parent) as VB
    }

@JvmName("inflateWithGeneric")
fun <VB : ViewBinding> Any.inflateBindingWithParameterizedType(
    layoutInflater: LayoutInflater,
    parent: ViewGroup?,
    attachToParent: Boolean
): VB =
    withGenericBindingClass(this) { clazz ->
        clazz.getMethod(
            "inflate",
            LayoutInflater::class.java,
            ViewGroup::class.java,
            Boolean::class.java
        ).invoke(null, layoutInflater, parent, attachToParent) as VB
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

/**This class can be used when their is no xml layout.*/
abstract class PViewBinding<out T : View>(private val rootView: T) : ViewBinding {

    override fun getRoot(): View {
        return rootView
    }

}