package me.ztiany.widget.shape

import android.R.attr.radius
import android.view.View
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel


/**
 * the Shapeable function in material only supports following components:
 *
 *  1. Chip
 *  2. MaterialCard
 *  3. MaterialButton
 *  4. ShapeableImageView
 *  5. FloatingActionButton
 *
 * But the MaterialShapeDrawable is pretty functional, we can use it to support more.
 *
 * references:
 *
 *  1. [Material Components——Shape的处理](https://xuyisheng.top/mdc-shape/)
 *  2. [Material Components——ShapeableImageView](https://xuyisheng.top/mdc-shape/)
 *  3. [to create rounded corners for a view without having to create a separate drawable](https://stackoverflow.com/questions/59046711/android-is-there-a-simple-way-to-create-rounded-corners-for-a-view-without-havi)
 *
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2021-03-07 14:09
 */
class ShapeableHelper {

    fun applyShape(owner: View) {
        val shapeAppearanceModel = ShapeAppearanceModel()
                .toBuilder()
                .setAllCorners(CornerFamily.ROUNDED, radius.toFloat())
                .build()

        owner.background = MaterialShapeDrawable(shapeAppearanceModel)
    }

}