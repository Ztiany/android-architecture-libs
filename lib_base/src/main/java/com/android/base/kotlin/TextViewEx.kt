package com.android.base.kotlin

import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.DrawableRes
import com.android.base.interfaces.adapter.TextWatcherAdapter

inline fun TextView.textWatcher(init: KTextWatcher.() -> Unit) = addTextChangedListener(KTextWatcher().apply(init))

class KTextWatcher : TextWatcher {

    val TextView.isEmpty
        get() = text.isEmpty()

    val TextView.isNotEmpty
        get() = text.isNotEmpty()

    val TextView.isBlank
        get() = text.isBlank()

    val TextView.isNotBlank
        get() = text.isNotBlank()

    private var _beforeTextChanged: ((CharSequence?, Int, Int, Int) -> Unit)? = null
    private var _onTextChanged: ((CharSequence?, Int, Int, Int) -> Unit)? = null
    private var _afterTextChanged: ((Editable?) -> Unit)? = null

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        _beforeTextChanged?.invoke(s, start, count, after)
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        _onTextChanged?.invoke(s, start, before, count)
    }

    override fun afterTextChanged(s: Editable?) {
        _afterTextChanged?.invoke(s)
    }

    fun beforeTextChanged(listener: (CharSequence?, Int, Int, Int) -> Unit) {
        _beforeTextChanged = listener
    }

    fun onTextChanged(listener: (CharSequence?, Int, Int, Int) -> Unit) {
        _onTextChanged = listener
    }

    fun afterTextChanged(listener: (Editable?) -> Unit) {
        _afterTextChanged = listener
    }

}

fun TextView.topDrawable(@DrawableRes id: Int) {
    this.setCompoundDrawablesWithIntrinsicBounds(0, id, 0, 0)
}

fun TextView.leftDrawable(@DrawableRes id: Int) {
    this.setCompoundDrawablesWithIntrinsicBounds(id, 0, 0, 0)
}

fun TextView.rightDrawable(@DrawableRes id: Int) {
    this.setCompoundDrawablesWithIntrinsicBounds(0, 0, id, 0)
}

fun TextView.bottomDrawable(@DrawableRes id: Int) {
    this.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, id)
}

fun TextView.topDrawable(drawable: Drawable, retain: Boolean = false) {
    if (retain) {
        val compoundDrawables = this.compoundDrawables
        this.setCompoundDrawablesWithIntrinsicBounds(compoundDrawables[0], drawable, compoundDrawables[2], compoundDrawables[3])
    } else {
        this.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null)
    }
}

fun TextView.leftDrawable(drawable: Drawable, retain: Boolean = false) {
    if (retain) {
        val compoundDrawables = this.compoundDrawables
        this.setCompoundDrawablesWithIntrinsicBounds(drawable, compoundDrawables[1], compoundDrawables[2], compoundDrawables[3])
    } else {
        this.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
    }
}

fun TextView.rightDrawable(drawable: Drawable, retain: Boolean = false) {
    if (retain) {
        val compoundDrawables = this.compoundDrawables
        this.setCompoundDrawablesWithIntrinsicBounds(compoundDrawables[0], compoundDrawables[1], drawable, compoundDrawables[3])
    } else {
        this.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null)
    }
}

fun TextView.bottomDrawable(drawable: Drawable, retain: Boolean = false) {
    if (retain) {
        val compoundDrawables = this.compoundDrawables
        this.setCompoundDrawablesWithIntrinsicBounds(compoundDrawables[0], compoundDrawables[1], compoundDrawables[2], drawable)
    } else {
        this.setCompoundDrawablesWithIntrinsicBounds(null, null, null, drawable)
    }
}

fun TextView.clearComponentDrawable() {
    this.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
}

fun Button.enableByEditText(et: EditText, checker: (s: CharSequence?) -> Boolean = { it -> !it.isNullOrEmpty() }) {
    val btn = this
    et.addTextChangedListener(object : TextWatcherAdapter {
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            btn.isEnabled = checker(s)
        }
    })
}