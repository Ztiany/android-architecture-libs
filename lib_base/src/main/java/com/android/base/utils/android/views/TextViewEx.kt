package com.android.base.utils.android.views

import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.InputFilter
import android.text.Spanned
import android.text.TextWatcher
import android.text.method.LinkMovementMethod
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.android.base.interfaces.adapter.TextWatcherAdapter
import com.google.android.material.textfield.TextInputLayout

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

fun TextView.setTopDrawable(@DrawableRes id: Int) {
    this.setCompoundDrawablesWithIntrinsicBounds(0, id, 0, 0)
}

fun TextView.setLeftDrawable(@DrawableRes id: Int) {
    this.setCompoundDrawablesWithIntrinsicBounds(id, 0, 0, 0)
}

fun TextView.setRightDrawable(@DrawableRes id: Int) {
    this.setCompoundDrawablesWithIntrinsicBounds(0, 0, id, 0)
}

fun TextView.setBottomDrawable(@DrawableRes id: Int) {
    this.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, id)
}

fun TextView.setTopDrawable(drawable: Drawable, retainOthers: Boolean = false) {
    if (retainOthers) {
        val compoundDrawables = this.compoundDrawables
        this.setCompoundDrawablesWithIntrinsicBounds(compoundDrawables[0], drawable, compoundDrawables[2], compoundDrawables[3])
    } else {
        this.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null)
    }
}

fun TextView.setLeftDrawable(drawable: Drawable, retainOthers: Boolean = false) {
    if (retainOthers) {
        val compoundDrawables = this.compoundDrawables
        this.setCompoundDrawablesWithIntrinsicBounds(drawable, compoundDrawables[1], compoundDrawables[2], compoundDrawables[3])
    } else {
        this.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
    }
}

fun TextView.setRightDrawable(drawable: Drawable, retainOthers: Boolean = false) {
    if (retainOthers) {
        val compoundDrawables = this.compoundDrawables
        this.setCompoundDrawablesWithIntrinsicBounds(compoundDrawables[0], compoundDrawables[1], drawable, compoundDrawables[3])
    } else {
        this.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null)
    }
}

fun TextView.setBottomDrawable(drawable: Drawable, retainOthers: Boolean = false) {
    if (retainOthers) {
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

fun TextInputLayout.textStr(): String {
    val editText = this.editText
    return editText?.text?.toString() ?: ""
}

fun EditText.textStr(): String {
    return this.text?.toString() ?: ""
}

fun TextView.enableSpanClickable() {
    // 响应点击事件的话必须设置以下属性
    movementMethod = LinkMovementMethod.getInstance()
    // 去掉点击事件后的高亮
    highlightColor = ContextCompat.getColor(context, android.R.color.transparent)
}

fun EditText.disableEmojiEntering() {
    val filter = EmojiExcludeFilter()
    val oldFilters = filters
    val oldFiltersLength = oldFilters.size
    val newFilters = arrayOfNulls<InputFilter>(oldFiltersLength + 1)
    if (oldFiltersLength > 0) {
        System.arraycopy(oldFilters, 0, newFilters, 0, oldFiltersLength)
    }
    //添加新的过滤规则
    newFilters[oldFiltersLength] = filter
    filters = newFilters
}

private class EmojiExcludeFilter : InputFilter {
    override fun filter(source: CharSequence, start: Int, end: Int, dest: Spanned, dstart: Int, dend: Int): CharSequence? {
        for (i in start until end) {
            val type = Character.getType(source[i])
            if (type == Character.SURROGATE.toInt() || type == Character.OTHER_SYMBOL.toInt()) {
                return ""
            }
        }
        return null
    }
}