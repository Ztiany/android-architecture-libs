package com.android.base.utils.android.views

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.text.style.ReplacementSpan

/**
 * 圆角背景Span
 */
class RoundedBackgroundSpan(private val backgroundColor: Int, private val textColor: Int, private val padding: Int, private val corner: Int) : ReplacementSpan() {

    override fun getSize(paint: Paint, text: CharSequence, start: Int, end: Int, fm: Paint.FontMetricsInt?): Int {
        return (padding.toFloat() + paint.measureText(text.subSequence(start, end).toString()) + padding.toFloat()).toInt()
    }

    override fun draw(canvas: Canvas, text: CharSequence, start: Int, end: Int, x: Float, top: Int, y: Int, bottom: Int, paint: Paint) {
        val width = paint.measureText(text.subSequence(start, end).toString())
        val rect = RectF(x, top.toFloat(), x + width + (2 * padding).toFloat(), bottom.toFloat())
        paint.color = backgroundColor
        canvas.drawRoundRect(rect, corner.toFloat(), corner.toFloat(), paint)
        paint.color = textColor
        canvas.drawText(text, start, end, x + padding, y.toFloat(), paint)
    }

}