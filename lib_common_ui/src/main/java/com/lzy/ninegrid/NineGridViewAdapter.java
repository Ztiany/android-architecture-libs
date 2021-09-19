package com.lzy.ninegrid;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import java.util.List;
import java.util.Objects;

import androidx.appcompat.widget.AppCompatImageView;

public abstract class NineGridViewAdapter {

    protected Context context;
    private List<Image> imageInfo;

    private OnDataSourceChangedListener mOnDataSourceChangedListener;

    interface OnDataSourceChangedListener {
        void onDataSourceChanged();
    }

    public NineGridViewAdapter(Context context, List<Image> imageInfo) {
        this.context = context;
        this.imageInfo = imageInfo;
    }

    public NineGridViewAdapter(Context context) {
        this.context = context;
    }

    /**
     * 如果要实现图片点击的逻辑，重写此方法即可
     *
     * @param context   上下文
     * @param imageView 被点击的 ImageView
     * @param index     当前点击图片的的索引
     * @param imageInfo 图片地址的数据集合
     */
    protected void onImageItemClick(Context context, View imageView, int index, List<Image> imageInfo) {
    }

    /**
     * 生成ImageView容器的方式，默认使用NineGridImageViewWrapper类，即点击图片后，图片会有蒙板效果
     * 如果需要自定义图片展示效果，重写此方法即可
     *
     * @param context 上下文
     * @return 生成的 ImageView
     */
    protected ImageView generateImageView(Context context) {
        AppCompatImageView imageView = new AppCompatImageView(context);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return imageView;
    }

    public List<Image> getImageInfo() {
        return imageInfo;
    }

    public void setImageInfoList(List<Image> imageInfo) {
        List<Image> old = this.imageInfo;
        this.imageInfo = imageInfo;
        if (mOnDataSourceChangedListener != null && !Objects.equals(old, imageInfo)) {
            mOnDataSourceChangedListener.onDataSourceChanged();
        }
    }

    public abstract void bindItem(Context context, ImageView childrenView, Image image);

    public void setOnDataSourceChangedListener(OnDataSourceChangedListener onDataSourceChangedListener) {
        mOnDataSourceChangedListener = onDataSourceChangedListener;
    }

}