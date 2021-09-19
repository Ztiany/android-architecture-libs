package com.lzy.ninegrid;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.android.sdk.ui.R;

import java.util.ArrayList;
import java.util.List;

public class NineGridView extends ViewGroup {

    // 单张图片时的最大大小,单位dp
    private int singleImageWidth = 250;
    private int singleImageMaxHeight = singleImageWidth;

    // 单张图片的宽高比(宽/高)
    private float singleImageRatio = 1.0f;

    // 最大显示的图片数
    private int maxImageSize = 9;

    // 宫格间距，单位dp
    private int gridSpacing = 3;

    // 列数
    private int columnCount;
    // 行数
    private int rowCount;
    // 宫格宽度
    private int gridWidth;
    // 宫格高度
    private int gridHeight;

    private List<ImageView> imageViews;
    private List<Image> mImageInfo;
    private NineGridViewAdapter mAdapter;

    public NineGridView(Context context) {
        this(context, null);
    }

    public NineGridView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NineGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        gridSpacing = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, gridSpacing, dm);
        singleImageWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, singleImageWidth, dm);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.NineGridView);

        gridSpacing = (int) a.getDimension(R.styleable.NineGridView_ngv_gridSpacing, gridSpacing);
        singleImageWidth = a.getDimensionPixelSize(R.styleable.NineGridView_ngv_singleImageSize, singleImageWidth);
        singleImageRatio = a.getFloat(R.styleable.NineGridView_ngv_singleImageRatio, singleImageRatio);
        maxImageSize = a.getInt(R.styleable.NineGridView_ngv_maxSize, maxImageSize);

        a.recycle();

        imageViews = new ArrayList<>();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = 0;
        int totalWidth = width - getPaddingLeft() - getPaddingRight();

        if (mImageInfo != null && !mImageInfo.isEmpty()) {
            int size = mImageInfo.size();
            if (size == 1) {
                gridWidth = Math.min(singleImageWidth, totalWidth);
                gridHeight = Math.min((int) (gridWidth / singleImageRatio), singleImageMaxHeight);
            } else if (size == 2) {
                //2张图宽高按总宽度的 1/2
                gridWidth = gridHeight = (int) ((totalWidth - gridSpacing * 2) / 2.0F);
            } else {
                //其他数量宽高都按总宽度的 1/3
                gridWidth = gridHeight = (int) ((totalWidth - gridSpacing * 2) / 3.0F);
            }

            width = gridWidth * columnCount + gridSpacing * (columnCount - 1) + getPaddingLeft() + getPaddingRight();
            height = gridHeight * rowCount + gridSpacing * (rowCount - 1) + getPaddingTop() + getPaddingBottom();
        }

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (mImageInfo == null) {
            return;
        }

        int childrenCount = mImageInfo.size();

        for (int i = 0; i < childrenCount; i++) {

            ImageView childrenView = (ImageView) getChildAt(i);

            int rowNum = i / columnCount;
            int columnNum = i % columnCount;

            int left = (gridWidth + gridSpacing) * columnNum + getPaddingLeft();
            int top = (gridHeight + gridSpacing) * rowNum + getPaddingTop();
            int right = left + gridWidth;
            int bottom = top + gridHeight;

            childrenView.layout(left, top, right, bottom);

            if (mAdapter != null) {
                mAdapter.bindItem(getContext(), childrenView, mImageInfo.get(i));
            }
        }
    }

    /**
     * 设置适配器
     */
    public void setAdapter(@NonNull NineGridViewAdapter adapter) {
        if (mAdapter == adapter) {
            return;
        }
        if (mAdapter != null) {
            mAdapter.setOnDataSourceChangedListener(null);
        }

        mAdapter = adapter;

        mAdapter.setOnDataSourceChangedListener(() -> {
            layoutImageViews(mAdapter);
        });

        layoutImageViews(adapter);
    }

    private void layoutImageViews(@NonNull NineGridViewAdapter adapter) {
        List<Image> imageInfo = adapter.getImageInfo();

        if (imageInfo == null || imageInfo.isEmpty()) {
            setVisibility(GONE);
            return;
        } else {
            setVisibility(VISIBLE);
        }

        int imageCount = imageInfo.size();

        if (maxImageSize > 0 && imageCount > maxImageSize) {
            imageInfo = imageInfo.subList(0, maxImageSize);
            imageCount = imageInfo.size();   //再次获取图片数量
        }

        if (imageCount == 2) {
            rowCount = 1;//横行
            columnCount = 2;//纵行
        } else {
            //默认是3列显示，行数根据图片的数量决定
            rowCount = imageCount / 3 + (imageCount % 3 == 0 ? 0 : 1);//横行
            columnCount = 3;//纵行
        }

        //保证View的复用，避免重复创建
        if (mImageInfo == null) {
            for (int i = 0; i < imageCount; i++) {
                ImageView iv = getImageView(i);
                if (iv == null) return;
                addView(iv, generateDefaultLayoutParams());
            }
        } else {
            int oldViewCount = mImageInfo.size();
            if (oldViewCount > imageCount) {
                removeViews(imageCount, oldViewCount - imageCount);
            } else if (oldViewCount < imageCount) {
                for (int i = oldViewCount; i < imageCount; i++) {
                    ImageView iv = getImageView(i);
                    if (iv == null) return;
                    addView(iv, generateDefaultLayoutParams());
                }
            }
        }
        mImageInfo = imageInfo;
        requestLayout();
    }

    /**
     * 获得 ImageView 保证了 ImageView 的重用
     */
    private ImageView getImageView(final int position) {
        ImageView imageView;
        if (position < imageViews.size()) {
            imageView = imageViews.get(position);
        } else {
            imageView = mAdapter.generateImageView(getContext());
            imageView.setOnClickListener(v -> mAdapter.onImageItemClick(getContext(), v, position, mAdapter.getImageInfo()));
            imageViews.add(imageView);
        }
        return imageView;
    }

    /**
     * 设置宫格间距
     */
    public void setGridSpacing(int spacing) {
        gridSpacing = spacing;
    }

    /**
     * 设置只有一张图片时的宽
     */
    public void setSingleImageWidth(int maxImageSize) {
        singleImageWidth = maxImageSize;
    }

    public void setSingleImageMaxHeight(int maxImageSize) {
        singleImageMaxHeight = maxImageSize;
    }

    /**
     * 设置只有一张图片时的宽高比
     */
    public void setSingleImageRatio(float ratio) {
        singleImageRatio = ratio;
    }

    /**
     * 设置最大图片数
     */
    public void setMaxSize(int maxSize) {
        maxImageSize = maxSize;
    }

    public int getMaxSize() {
        return maxImageSize;
    }

    public NineGridViewAdapter getAdapter() {
        return mAdapter;
    }

}