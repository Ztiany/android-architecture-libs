package androidx.appcompat.widget

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.widget.ToggleButton
import androidx.annotation.DrawableRes
import androidx.core.view.TintableBackgroundView

/** don't forget calling "AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)" before use this ButtonView.*/
@SuppressLint("RestrictedApi")
class TintableToggleButton @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : AppCompatToggleButton(TintContextWrapper.wrap(context), attrs, defStyleAttr), TintableBackgroundView {

    private var backgroundTintHelper: AppCompatBackgroundHelper? = null

    init {
        backgroundTintHelper = AppCompatBackgroundHelper(this)
        backgroundTintHelper?.loadFromAttributes(attrs, defStyleAttr)
    }

    override fun setSupportBackgroundTintList(tint: ColorStateList?) {
        backgroundTintHelper?.supportBackgroundTintList = tint
    }

    override fun getSupportBackgroundTintMode() = backgroundTintHelper?.supportBackgroundTintMode

    override fun setSupportBackgroundTintMode(tintMode: PorterDuff.Mode?) {
        backgroundTintHelper?.supportBackgroundTintMode = tintMode
    }

    override fun getSupportBackgroundTintList() = backgroundTintHelper?.supportBackgroundTintList

    override fun setBackgroundResource(@DrawableRes resId: Int) {
        super.setBackgroundResource(resId)
        backgroundTintHelper?.onSetBackgroundResource(resId)
    }

    override fun setBackgroundDrawable(background: Drawable?) {
        super.setBackgroundDrawable(background)
        backgroundTintHelper?.onSetBackgroundDrawable(background)
    }

}