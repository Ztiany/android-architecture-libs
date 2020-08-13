package me.ztiany.widget.ratio;

import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;

import com.android.sdk.ui.R;


class RatioHelper {

    private static final int[] ratioAttrs = {R.attr.layout_ratio};
    private float mRatio;
    private View mView;

    RatioHelper(View view) {
        mView = view;
    }

    void resolveAttr(AttributeSet attrs) {
        TypedArray typedArray = mView.getContext().obtainStyledAttributes(attrs, ratioAttrs);
        mRatio = typedArray.getFloat(0, 0F);
        typedArray.recycle();
    }

    void setRatio(float ratio) {
        mRatio = ratio;
    }

    int[] measure(int widthMeasureSpec, int heightMeasureSpec) {

        if (mRatio != 0) {
            int widthMode = View.MeasureSpec.getMode(widthMeasureSpec);
            int heightMode = View.MeasureSpec.getMode(heightMeasureSpec);

            int width = View.MeasureSpec.getSize(widthMeasureSpec) - mView.getPaddingLeft() - mView.getPaddingRight();
            int height = View.MeasureSpec.getSize(heightMeasureSpec) - mView.getPaddingTop() - mView.getPaddingBottom();

            if (widthMode == View.MeasureSpec.EXACTLY && heightMode != View.MeasureSpec.EXACTLY) {

                height = (int) (width / mRatio + 0.5f);
                heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(height + mView.getPaddingTop() + mView.getPaddingBottom(), View.MeasureSpec.EXACTLY);

            } else if (widthMode != View.MeasureSpec.EXACTLY && heightMode == View.MeasureSpec.EXACTLY) {

                width = (int) (height * mRatio + 0.5f);
                widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(width + mView.getPaddingLeft() + mView.getPaddingRight(), View.MeasureSpec.EXACTLY);

            }
        }
        return new int[]{widthMeasureSpec, heightMeasureSpec};
    }

}
