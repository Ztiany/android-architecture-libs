package me.ztiany.widget.shape

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.res.use
import com.android.sdk.ui.R

/**
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2019-01-16 10:09
 */
class TriangleView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    private val mPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    @Suppress var bottomColor: Int = 0
        set(value) {
            field = value
            invalidate()
        }

    var strokeColor: Int = 0
        set(value) {
            field = value
            invalidate()
        }

    @Suppress var triangleSolidColor: Int = 0
        set(value) {
            field = value
            invalidate()
        }

    var strokeWidth: Float = 0F
        set(value) {
            field = value
            invalidate()
        }

    @Direction var direction: Int = 0
        set(value) {
            field = value
            invalidate()
        }

    var trianglePercent: Float = 0F
        set(value) {
            field = value
            invalidate()
        }

    private val mTrianglePath: Path = Path()

    init {
        initAttribute(context, attrs)
    }

    private fun initAttribute(context: Context, attrs: AttributeSet?) {
        context.obtainStyledAttributes(attrs, R.styleable.TriangleView).use {
            strokeColor = it.getColor(R.styleable.TriangleView_tv_triangle_stroke_color, Color.BLACK)
            bottomColor = it.getColor(R.styleable.TriangleView_tv_triangle_bottom_color, -10)
            if (bottomColor == -10) {
                bottomColor = strokeColor
            }
            triangleSolidColor = it.getColor(R.styleable.TriangleView_tv_triangle_solid_color, Color.TRANSPARENT)
            strokeWidth = it.getDimension(R.styleable.TriangleView_tv_triangle_stroke_width, 0F)
            direction = it.getInt(R.styleable.TriangleView_tv_triangle_direction, Direction.TOP)
            trianglePercent = it.getFloat(R.styleable.TriangleView_tv_triangle_angle_percent, 0.5F)
        }

        mPaint.strokeWidth = strokeWidth
        mPaint.strokeCap = Paint.Cap.ROUND
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (measuredHeight <= 0 || measuredWidth <= 0) {
            return
        }
        when (direction) {
            Direction.TOP -> drawTopTriangle(canvas)
            Direction.BOTTOM -> drawBottomTriangle(canvas)
            Direction.LEFT -> drawLeftTriangle(canvas)
            Direction.RIGHT -> drawRightTriangle(canvas)
        }
    }

    private fun drawLeftTriangle(canvas: Canvas) {
        val trianglePoint = trianglePercent * measuredHeight
        val drawingHeight = measuredHeight.toFloat()
        val drawingWidth = measuredWidth.toFloat()
        val haftStroke = strokeWidth * 0.5F

        //draw solid
        if (needDrawSolid()) {
            mTrianglePath.reset()
            mTrianglePath.moveTo(drawingWidth, 0F)
            mTrianglePath.lineTo(drawingWidth, drawingHeight)
            mTrianglePath.lineTo(0F, trianglePoint)
            mTrianglePath.close()
            mPaint.style = Paint.Style.FILL
            mPaint.color = triangleSolidColor
            canvas.drawPath(mTrianglePath, mPaint)
        }
        //draw stroke
        if (strokeWidth != 0F) {
            mPaint.color = strokeColor
            canvas.drawLine(0F, trianglePoint, drawingWidth, 0F, mPaint)
            canvas.drawLine(0F, trianglePoint, drawingWidth, drawingHeight, mPaint)
            mPaint.color = bottomColor
            canvas.drawLine(drawingWidth - haftStroke, 0F, drawingWidth - haftStroke, drawingHeight, mPaint)
        }
    }

    private fun drawRightTriangle(canvas: Canvas) {
        val trianglePoint = trianglePercent * measuredHeight
        val drawingHeight = measuredHeight.toFloat()
        val drawingWidth = measuredWidth.toFloat()
        val haftStroke = strokeWidth * 0.5F

        //draw solid
        if (needDrawSolid()) {
            mTrianglePath.reset()
            mTrianglePath.moveTo(0F, 0F)
            mTrianglePath.lineTo(0F, drawingHeight)
            mTrianglePath.lineTo(drawingWidth, trianglePoint)
            mTrianglePath.close()
            mPaint.style = Paint.Style.FILL
            mPaint.color = triangleSolidColor
            canvas.drawPath(mTrianglePath, mPaint)
        }
        //draw stroke
        if (strokeWidth != 0F) {
            mPaint.color = strokeColor
            canvas.drawLine(0F, 0F, drawingWidth, trianglePoint, mPaint)
            canvas.drawLine(drawingWidth, trianglePoint, 0F, drawingHeight, mPaint)
            mPaint.color = bottomColor
            canvas.drawLine(haftStroke, 0F, haftStroke, drawingHeight, mPaint)
        }
    }

    private fun drawBottomTriangle(canvas: Canvas) {
        val trianglePoint = trianglePercent * measuredWidth
        val drawingHeight = measuredHeight.toFloat()
        val drawingWidth = measuredWidth.toFloat()
        val haftStroke = strokeWidth * 0.5F

        //draw solid
        if (needDrawSolid()) {
            mTrianglePath.reset()
            mTrianglePath.moveTo(0F, 0F)
            mTrianglePath.lineTo(trianglePoint, drawingHeight)
            mTrianglePath.lineTo(drawingWidth, 0F)
            mTrianglePath.close()
            mPaint.style = Paint.Style.FILL
            mPaint.color = triangleSolidColor
            canvas.drawPath(mTrianglePath, mPaint)
        }
        //draw stroke
        if (strokeWidth != 0F) {
            mPaint.color = strokeColor
            canvas.drawLine(0F, 0F, trianglePoint, drawingHeight, mPaint)
            canvas.drawLine(trianglePoint, drawingHeight, drawingWidth, 0F, mPaint)
            mPaint.color = bottomColor
            canvas.drawLine(0F, haftStroke, drawingWidth, haftStroke, mPaint)
        }
    }

    private fun drawTopTriangle(canvas: Canvas) {
        val trianglePoint = trianglePercent * measuredWidth
        val drawingHeight = measuredHeight.toFloat()
        val drawingWidth = measuredWidth.toFloat()
        val haftStroke = strokeWidth * 0.5F

        //draw solid
        if (needDrawSolid()) {
            mTrianglePath.reset()
            mTrianglePath.moveTo(0F, drawingHeight)
            mTrianglePath.lineTo(drawingWidth, drawingHeight)
            mTrianglePath.lineTo(trianglePoint, 0F)
            mTrianglePath.close()
            mPaint.style = Paint.Style.FILL
            mPaint.color = triangleSolidColor
            canvas.drawPath(mTrianglePath, mPaint)
        }
        //draw stroke
        if (strokeWidth != 0F) {
            mPaint.color = strokeColor
            canvas.drawLine(0F, drawingHeight, trianglePoint, 0F, mPaint)
            canvas.drawLine(trianglePoint, 0F, drawingWidth, drawingHeight, mPaint)
            mPaint.color = bottomColor
            canvas.drawLine(drawingWidth, drawingHeight - haftStroke, 0F, drawingHeight - haftStroke, mPaint)
        }
    }

    private fun needDrawSolid() = triangleSolidColor != Color.TRANSPARENT

}