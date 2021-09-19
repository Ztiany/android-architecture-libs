package com.android.base.image;


import android.graphics.drawable.Drawable;

public class DisplayConfig {

    private boolean mCacheMemory = true;
    private boolean mCacheDisk = true;
    private int mTransform;
    private int mScaleType;
    private int mErrorPlaceholder = NO_PLACE_HOLDER;
    private int mLoadingPlaceholder = NO_PLACE_HOLDER;
    private int mRoundedCornersRadius;
    private float thumbnail;
    private int height;
    private int width;

    /*placeholder*/
    static final int NO_PLACE_HOLDER = -1;
    private Drawable mErrorDrawable = null;
    private Drawable mLoadingDrawable = null;

    /*scale type*/
    public static final int SCALE_NONE = 0;
    public static final int SCALE_CENTER_CROP = 1;
    public static final int SCALE_FIT_CENTER = 2;

    /*transform*/
    public static final int TRANSFORM_NONE = 1;
    public static final int TRANSFORM_CIRCLE = 2;
    public static final int TRANSFORM_ROUNDED_CORNERS = 3;

    /*animation*/
    public static final int ANIM_NONE = 1;

    private DisplayConfig() {
    }

    public static DisplayConfig create() {
        return new DisplayConfig();
    }

    public DisplayConfig setErrorPlaceholder(int errorPlaceholder) {
        mErrorPlaceholder = errorPlaceholder;
        mErrorDrawable = null;
        return this;
    }

    public DisplayConfig setLoadingPlaceholder(int loadingPlaceholder) {
        mLoadingPlaceholder = loadingPlaceholder;
        mErrorDrawable = null;
        return this;
    }

    public Drawable getErrorDrawable() {
        return mErrorDrawable;
    }

    public Drawable getLoadingDrawable() {
        return mLoadingDrawable;
    }

    /**
     * @param scaleType {@link #SCALE_CENTER_CROP} or{@link #SCALE_FIT_CENTER}
     * @return DisplayConfig
     */
    public DisplayConfig scaleType(int scaleType) {
        mScaleType = scaleType;
        return this;
    }

    public DisplayConfig cacheMemory(boolean cacheMemory) {
        mCacheMemory = cacheMemory;
        return this;
    }

    public DisplayConfig setCacheDisk(boolean cacheDisk) {
        mCacheDisk = cacheDisk;
        return this;
    }

    public DisplayConfig setThumbnail(float thumbnail) {
        this.thumbnail = thumbnail;
        return this;
    }

    public DisplayConfig setErrorDrawable(Drawable errorDrawable) {
        mErrorDrawable = errorDrawable;
        mErrorPlaceholder = NO_PLACE_HOLDER;
        return this;
    }

    public DisplayConfig setLoadingDrawable(Drawable loadingDrawable) {
        mLoadingPlaceholder = NO_PLACE_HOLDER;
        mLoadingDrawable = loadingDrawable;
        return this;
    }

    /**
     * @param transform {@link #TRANSFORM_ROUNDED_CORNERS} or{@link #TRANSFORM_CIRCLE}
     * @return DisplayConfig
     */
    public DisplayConfig setTransform(int transform) {
        mTransform = transform;
        return this;
    }

    public DisplayConfig setRoundedCornersRadius(int roundedCornersRadius) {
        mRoundedCornersRadius = roundedCornersRadius;
        return this;
    }

    public DisplayConfig setSize(int width, int height) {
        this.width = width;
        this.height = height;
        return this;
    }

    ///////////////////////////////////////////////////////////////////////////
    // Getter
    ///////////////////////////////////////////////////////////////////////////

    int getScaleType() {
        return mScaleType;
    }

    boolean isCacheMemory() {
        return mCacheMemory;
    }

    boolean isCacheDisk() {
        return mCacheDisk;
    }

    int getTransform() {
        return mTransform;
    }

    int getErrorPlaceholder() {
        return mErrorPlaceholder;
    }

    int getLoadingPlaceholder() {
        return mLoadingPlaceholder;
    }

    int getRoundedCornersRadius() {
        return mRoundedCornersRadius;
    }

    public float getThumbnail() {
        return thumbnail;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

}