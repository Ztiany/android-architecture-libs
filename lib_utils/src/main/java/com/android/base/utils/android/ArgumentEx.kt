package com.android.base.utils.android

import android.app.Activity
import android.content.Intent
import android.os.Binder
import android.os.Bundle
import android.os.Parcelable
import androidx.core.app.BundleCompat
import androidx.fragment.app.Fragment
import java.io.Serializable
import kotlin.properties.ReadOnlyProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/* copy from https://github.com/pengxurui/DemoHall */

fun <T> Fragment.requireArgument(name: String): T {
    @Suppress("UNCHECKED_CAST")
    return arguments?.get(name) as? T
        ?: throw IllegalStateException("Property $name not exists.")
}

fun <T> Fragment.getArgument(name: String, defaultValue: T? = null): T? {
    val arguments = arguments ?: return defaultValue
    @Suppress("UNCHECKED_CAST")
    return arguments.get(name) as? T
}

fun <T> Fragment.argumentNullable(name: String? = null) = FragmentArgumentPropertyNullable<T>(name)

fun <T> Fragment.argument(
    name: String? = null,
    defaultValue: T? = null
) = FragmentArgumentProperty(name, defaultValue)

fun <T> Activity.argumentNullable(name: String? = null) = ActivityArgumentDelegateNullable<T>()

fun <T> Activity.argument(
    name: String? = null,
    defaultValue: T? = null
) = ActivityArgumentProperty(name, defaultValue)

private fun confirmPropertyName(name: String?, property: KProperty<*>): String {
    return name ?: property.name
}

// --------------------------------------------------------------------------------------
// Fragment
// --------------------------------------------------------------------------------------

class FragmentArgumentProperty<T>(
    /**value name*/
    private val name: String? = null,
    /**default cannot be null*/
    private val defaultValue: T? = null
) : ReadWriteProperty<Fragment, T> {

    override fun getValue(thisRef: Fragment, property: KProperty<*>): T {
        return thisRef.arguments?.getValue(confirmPropertyName(name, property)) as? T
            ?: defaultValue
            ?: throw IllegalStateException(
                "Property ${
                    confirmPropertyName(
                        name,
                        property
                    )
                } could not be read"
            )
    }

    override fun setValue(thisRef: Fragment, property: KProperty<*>, value: T) {
        val arguments = thisRef.arguments ?: Bundle().also { thisRef.arguments = it }
        if (arguments.containsKey(property.name)) {
            // The Value is not expected to be modified
            return
        }
        arguments[confirmPropertyName(name, property)] = value
    }
}

class FragmentArgumentPropertyNullable<T>(
    private val name: String? = null
) : ReadWriteProperty<Fragment, T?> {

    override fun getValue(thisRef: Fragment, property: KProperty<*>): T? {
        return thisRef.arguments?.getValue(confirmPropertyName(name, property))
    }

    override fun setValue(thisRef: Fragment, property: KProperty<*>, value: T?) {
        val arguments = thisRef.arguments ?: Bundle().also { thisRef.arguments = it }
        if (arguments.containsKey(confirmPropertyName(name, property))) {
            // The Value is not expected to be modified
            return
        }
        arguments[confirmPropertyName(name, property)] = value
    }
}

// --------------------------------------------------------------------------------------
// Activity
// --------------------------------------------------------------------------------------

class ActivityArgumentProperty<T>(
    private val name: String? = null,
    private val defaultValue: T? = null
) : ReadOnlyProperty<Activity, T> {

    override fun getValue(thisRef: Activity, property: KProperty<*>): T {
        return thisRef.intent?.extras?.getValue(confirmPropertyName(name, property)) as? T
            ?: defaultValue
            ?: throw IllegalStateException(
                "Property ${
                    confirmPropertyName(
                        name,
                        property
                    )
                } could not be read"
            )
    }
}

class ActivityArgumentDelegateNullable<T>(
    private val name: String? = null
) : ReadOnlyProperty<Activity, T?> {

    override fun getValue(thisRef: Activity, property: KProperty<*>): T? {
        return thisRef.intent?.extras?.getValue(confirmPropertyName(name, property))
    }
}

// --------------------------------------------------------------------------------------

operator fun <T> Bundle.set(key: String, value: T?) {
    when (value) {
        is Boolean -> putBoolean(key, value)
        is Byte -> putByte(key, value)
        is Char -> putChar(key, value)
        is Short -> putShort(key, value)
        is Int -> putInt(key, value)
        is Long -> putLong(key, value)
        is Float -> putFloat(key, value)
        is Double -> putDouble(key, value)
        is String? -> putString(key, value)
        is CharSequence? -> putCharSequence(key, value)
        is Serializable? -> putSerializable(key, value) // also ArrayList
        is Parcelable? -> putParcelable(key, value)
        is Bundle? -> putBundle(key, value)
        is BooleanArray? -> putBooleanArray(key, value)
        is ByteArray? -> putByteArray(key, value)
        is CharArray? -> putCharArray(key, value)
        is ShortArray? -> putShortArray(key, value)
        is IntArray? -> putIntArray(key, value)
        is LongArray? -> putLongArray(key, value)
        is FloatArray? -> putFloatArray(key, value)
        is DoubleArray? -> putDoubleArray(key, value)
        is Binder -> BundleCompat.putBinder(this, key, value)
        is ArrayList<*>? -> throw IllegalStateException("ArrayList<*> $key is not supported")
        is Array<*>? -> throw IllegalStateException("Array<*> $key is not supported")
        else -> throw IllegalStateException("Type $key is not supported")
    }
}

fun <T> Bundle.getValue(key: String): T? {
    @Suppress("UNCHECKED_CAST")
    return get(key) as T?
}

operator fun <T> Intent.set(key: String, value: T) {
    when (value) {
        is Boolean -> putExtra(key, value)
        is Byte -> putExtra(key, value)
        is Char -> putExtra(key, value)
        is Short -> putExtra(key, value)
        is Int -> putExtra(key, value)
        is Long -> putExtra(key, value)
        is Float -> putExtra(key, value)
        is Double -> putExtra(key, value)
        is String? -> putExtra(key, value)
        is CharSequence? -> putExtra(key, value)
        is Serializable? -> putExtra(key, value)
        is Parcelable? -> putExtra(key, value)
        is Bundle? -> putExtra(key, value)
        is BooleanArray? -> putExtra(key, value)
        is ByteArray? -> putExtra(key, value)
        is CharArray? -> putExtra(key, value)
        is ShortArray? -> putExtra(key, value)
        is IntArray? -> putExtra(key, value)
        is LongArray? -> putExtra(key, value)
        is FloatArray? -> putExtra(key, value)
        is DoubleArray? -> putExtra(key, value)
        is ArrayList<*>? -> throw IllegalStateException("ArrayList<*> $key is not supported")
        is Array<*>? -> throw IllegalStateException("Array<*> $key is not supported")
        else -> throw IllegalStateException("Type $key is not supported")
    }
}