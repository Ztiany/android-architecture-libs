package com.android.sdk.mediaselector.custom;

import android.os.Parcel;
import android.os.Parcelable;

import com.android.sdk.mediaselector.common.CropOptions;
import com.bilibili.boxing.BoxingMediaLoader;
import com.bilibili.boxing.model.callback.MediaFilter;

import org.jetbrains.annotations.NotNull;

import androidx.annotation.Nullable;
import timber.log.Timber;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2020-08-11 10:08
 */
public class Instructor implements Parcelable {

    static {
        BoxingMediaLoader.getInstance().init(new BoxingGlideLoader());
    }

    static final int PICTURE = 1;
    static final int VIDEO = 2;

    private boolean mNeedCamera;
    private int mTakingType = PICTURE;
    private boolean mCopyToInternal;
    private CropOptions mCropOptions;
    private boolean mIsMulti;
    private int mMaxCount = 9;
    private MediaFilter mMediaFilter;

    private BaseMediaSelector mBaseMediaSelector;

    Instructor(BaseMediaSelector mediaSelector) {
        mBaseMediaSelector = mediaSelector;
    }

    protected Instructor(Parcel in) {
        mNeedCamera = in.readByte() != 0;
        mTakingType = in.readInt();
        mCopyToInternal = in.readByte() != 0;
        mCropOptions = in.readParcelable(CropOptions.class.getClassLoader());
        mIsMulti = in.readByte() != 0;
        mMaxCount = in.readInt();
        mMediaFilter = in.readParcelable(MediaFilter.class.getClassLoader());
    }

    public static final Creator<Instructor> CREATOR = new Creator<Instructor>() {
        @Override
        public Instructor createFromParcel(Parcel in) {
            return new Instructor(in);
        }

        @Override
        public Instructor[] newArray(int size) {
            return new Instructor[size];
        }
    };

    public Instructor needMulti(int count) {
        mIsMulti = true;
        if (count > 9) {
            count = 9;
        }
        mMaxCount = count;
        return this;
    }

    public Instructor takePicture() {
        mTakingType = PICTURE;
        return this;
    }

    public Instructor takeVideo() {
        mTakingType = VIDEO;
        return this;
    }

    public Instructor needCamera() {
        mNeedCamera = true;
        return this;
    }

    public Instructor copyToInternal() {
        mCopyToInternal = true;
        return this;
    }

    public Instructor crop(CropOptions cropOptions) {
        mCropOptions = cropOptions;
        return this;
    }

    public Instructor crop() {
        mCropOptions = new CropOptions();
        return this;
    }

    public Instructor setMediaFilter(MediaFilter mediaFilter) {
        mMediaFilter = mediaFilter;
        return this;
    }

    public MediaFilter getMediaFilter() {
        return mMediaFilter;
    }

    public boolean start() {
        Timber.d("start()");
        return mBaseMediaSelector.start(this);
    }

    @Nullable
    public CropOptions getCropOptions() {
        return mCropOptions;
    }

    public boolean isNeedCrop() {
        return mCropOptions != null;
    }

    public boolean isNeedCamera() {
        return mNeedCamera;
    }

    public int getTakingType() {
        return mTakingType;
    }

    public boolean isCopyToInternal() {
        return mCopyToInternal;
    }

    public boolean isMulti() {
        return mIsMulti;
    }

    public int getMaxCount() {
        return mMaxCount;
    }

    void setMediaSelector(@NotNull BaseMediaSelector baseMediaSelector) {
        mBaseMediaSelector = baseMediaSelector;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (mNeedCamera ? 1 : 0));
        dest.writeInt(mTakingType);
        dest.writeByte((byte) (mCopyToInternal ? 1 : 0));
        dest.writeParcelable(mCropOptions, flags);
        dest.writeByte((byte) (mIsMulti ? 1 : 0));
        dest.writeInt(mMaxCount);
        dest.writeParcelable(mMediaFilter, flags);
    }

}