package me.ztiany.widget.compat

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.widget.TextView
import com.google.android.material.textfield.TextInputEditText

/** replacing [TextInputEditText] fix bug on ColorOS(MeiZu), refer [魅族设备 TextInputLayout 崩溃](https://github.com/android-in-china/Compatibility/issues/11)*/
class FixedTextInputEditText(context: Context, attrs: AttributeSet?) : TextInputEditText(context, attrs) {

    override fun getHint(): CharSequence? {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            return super.getHint()
        }
        return try {
            getSuperHintHack()
        } catch (e: Exception) {
            super.getHint()
        }
    }

    private fun getSuperHintHack(): CharSequence? {
        val f = TextView::class.java.getDeclaredField("mHint")
        f.isAccessible = true
        return f.get(this) as? CharSequence
    }

}