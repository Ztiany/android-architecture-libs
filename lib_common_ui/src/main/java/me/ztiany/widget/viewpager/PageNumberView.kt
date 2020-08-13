package me.ztiany.widget.viewpager

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.android.sdk.ui.R
import me.ztiany.widget.common.dip


class PageNumberView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr), IPagerNumberView {

    private var mPageSize: Int = 0
    private var mPosition: Int = 0

    private var mCenterX: Int = 0

    private val mPaint: Paint
    private val mTextHeight: Int
    private val mTextBaseLine: Int

    init {

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.PageNumberView)

        /*是否可以缩放*/
        val color = typedArray.getColor(R.styleable.PageNumberView_pnv_text_color, Color.BLACK)
        /*用于支持5.0的transition动画*/
        val size = typedArray.getDimensionPixelSize(R.styleable.PageNumberView_pnv_text_size, dip(14))

        typedArray.recycle()

        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaint.textSize = size.toFloat()
        mPaint.color = color

        val fontMetricsInt = mPaint.fontMetricsInt
        mTextHeight = fontMetricsInt.bottom - fontMetricsInt.top
        mTextBaseLine = mTextHeight - fontMetricsInt.bottom
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val text = mPageSize.toString()
        val textWidth = mPaint.measureText(text)
        val divisionWidth = mPaint.measureText(DIVISION)
        setMeasuredDimension((textWidth * 2 + divisionWidth).toInt(), mTextHeight)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val divisionHalfSize = mPaint.measureText(DIVISION) / 2
        val left = mCenterX - divisionHalfSize
        val right = mCenterX + divisionHalfSize
        val positionText = mPosition.toString()
        val sizeText = mPageSize.toString()
        canvas.drawText(DIVISION, left, mTextBaseLine.toFloat(), mPaint)
        canvas.drawText(positionText, left - mPaint.measureText(positionText), mTextBaseLine.toFloat(), mPaint)
        canvas.drawText(sizeText, right, mTextBaseLine.toFloat(), mPaint)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mCenterX = w / 2
    }

    override fun setPageScrolled(position: Int, positionOffset: Float) {
        mPosition = position
        if (mPageSize > 1) {
            if (mPosition == 0) {
                mPosition = mPageSize
            } else if (mPosition == mPageSize + 1) {
                mPosition = 1
            }
        } else {
            mPosition = mPageSize
        }
        invalidate()
    }

    override fun setViewPager(viewPager: BannerViewPager) {
        //no op
    }

    override fun setPageSize(pageSize: Int) {
        mPageSize = pageSize
        mPosition = pageSize//只有一個的時候
        requestLayout()
    }

    companion object {
        private const val DIVISION = "/"
    }

}