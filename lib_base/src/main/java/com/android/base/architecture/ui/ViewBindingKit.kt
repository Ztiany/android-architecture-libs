package com.android.base.architecture.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/*
 * 基于反射的 ViewBinding 封装，具体参考：
 *
 *      1. [优雅地封装和使用 ViewBinding](https://juejin.cn/post/6906153878312452103)
 *      2. [ViewBindingKtx](https://github.com/DylanCaiCoding/ViewBindingKtx)
 *      3. [新技术 ViewBinding 最佳实践 & 原理击穿](https://mp.weixin.qq.com/s/FTHSAysWNJOi4XcClYDjKg)
 */

@JvmName("inflateWithGeneric")
@Suppress("UNCHECKED_CAST")
fun <VB : ViewBinding> Any.inflateBindingWithParameterizedType(layoutInflater: LayoutInflater): VB =
    withGenericBindingClass(this) { clazz ->
        clazz.getMethod("inflate", LayoutInflater::class.java).invoke(null, layoutInflater) as VB
    }

@JvmName("inflateWithGeneric")
@Suppress("UNCHECKED_CAST")
fun <VB : ViewBinding> Any.inflateBindingWithParameterizedType(
    layoutInflater: LayoutInflater,
    parent: ViewGroup?
): VB =
    withGenericBindingClass(this) { clazz ->
        clazz.getMethod("inflate", LayoutInflater::class.java, ViewGroup::class.java)
            .invoke(null, layoutInflater, parent) as VB
    }

@JvmName("inflateWithGeneric")
@Suppress("UNCHECKED_CAST")
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

@Suppress("UNCHECKED_CAST")
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
abstract class AbsViewBinding<out T : View>(private val rootView: T) : ViewBinding {

    override fun getRoot(): View {
        return rootView
    }

}