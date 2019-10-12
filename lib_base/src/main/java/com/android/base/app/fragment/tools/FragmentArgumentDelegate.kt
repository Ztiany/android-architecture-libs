package com.android.base.app.fragment.tools

import android.os.Binder
import android.os.Bundle
import androidx.core.app.BundleCompat
import androidx.fragment.app.Fragment
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

fun <T : Any> fragmentArgument(name: String? = null, defaultValue: T? = null): ReadWriteProperty<Fragment, T> {
    return FragmentArgumentDelegate(name, defaultValue)
}

fun <T : Any?> nullableFragmentArgument(name: String? = null): ReadWriteProperty<Fragment, T?> {
    return NullableFragmentArgumentDelegate(name)
}

internal class FragmentArgumentDelegate<T : Any>(
        /**value name*/
        private val name: String? = null,
        /**default cannot be null*/
        private val defaultValue: T? = null
) : ReadWriteProperty<Fragment, T> {

    private lateinit var value: T

    override operator fun getValue(thisRef: Fragment, property: KProperty<*>): T {
        if (!::value.isInitialized) {

            val args = thisRef.arguments

            if (args == null) {
                if (defaultValue != null) {
                    value = defaultValue
                } else {
                    throw IllegalStateException("Cannot read property ${confirmPropertyName(name, property)} if no arguments have been set")
                }
            } else {
                @Suppress("UNCHECKED_CAST")
                value = args.get(confirmPropertyName(name, property)) as? T
                        ?: throw IllegalStateException("Property ${confirmPropertyName(name, property)} could not be read")
            }

        }

        return value
    }

    override operator fun setValue(thisRef: Fragment, property: KProperty<*>, value: T) {
        this.value = value
        saveValue(thisRef, name, property, value)
    }

}

internal class NullableFragmentArgumentDelegate<T : Any?>(
        /**value name*/
        private val name: String? = null
) : ReadWriteProperty<Fragment, T?> {

    private var value: T? = null

    override operator fun getValue(thisRef: Fragment, property: KProperty<*>): T? {
        if (value == null) {
            @Suppress("UNCHECKED_CAST")
            value = thisRef.arguments?.get(confirmPropertyName(name, property)) as? T
        }

        return value
    }

    override operator fun setValue(thisRef: Fragment, property: KProperty<*>, value: T?) {
        this.value = value
        saveValue(thisRef, name, property, value)
    }

}

private fun confirmPropertyName(name: String?, property: KProperty<*>): String {
    return name ?: property.name
}

private fun saveValue(thisRef: Fragment, name: String?, property: KProperty<*>, value: Any?) {
    var args = thisRef.arguments
    val key = confirmPropertyName(name, property)

    if (value == null) {
        args?.remove(key)
        return
    }

    if (args == null) {
        args = Bundle()
        thisRef.arguments = args
    }

    when (value) {
        is String -> args.putString(key, value)
        is Int -> args.putInt(key, value)
        is Short -> args.putShort(key, value)
        is Long -> args.putLong(key, value)
        is Byte -> args.putByte(key, value)
        is ByteArray -> args.putByteArray(key, value)
        is Char -> args.putChar(key, value)
        is CharArray -> args.putCharArray(key, value)
        is CharSequence -> args.putCharSequence(key, value)
        is Float -> args.putFloat(key, value)
        is Bundle -> args.putBundle(key, value)
        is Binder -> BundleCompat.putBinder(args, key, value)
        is android.os.Parcelable -> args.putParcelable(key, value)
        is java.io.Serializable -> args.putSerializable(key, value)
        else -> throw IllegalStateException("Type \"$value\" of property ${property.name} is not supported")
    }
}