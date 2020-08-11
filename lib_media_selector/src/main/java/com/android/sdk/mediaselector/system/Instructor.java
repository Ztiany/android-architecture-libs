package com.android.sdk.mediaselector.system;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.android.sdk.mediaselector.common.CropOptions;
import com.android.sdk.mediaselector.common.StorageUtils;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2020-08-07 09:55
 */
public class Instructor implements Parcelable {

    static final int CAMERA = 1;
    static final int ALBUM = 2;
    static final int FILE = 3;

    private BaseSystemMediaSelector mMediaSelector;

    private int mTakingType;
    private String mMimeType;
    private CropOptions mCropOptions;
    private boolean mNeedCrop;
    private String mCameraPhotoSavePath;
    private boolean mIsMultiple;
    private boolean mCopyToInternal;

    Instructor(BaseSystemMediaSelector mediaSelector, int type) {
        mTakingType = type;
        mMediaSelector = mediaSelector;
    }

    protected Instructor(Parcel in) {
        mTakingType = in.readInt();
        mMimeType = in.readString();
        mCropOptions = in.readParcelable(CropOptions.class.getClassLoader());
        mNeedCrop = in.readByte() != 0;
        mCameraPhotoSavePath = in.readString();
        mIsMultiple = in.readByte() != 0;
        mCopyToInternal = in.readByte() != 0;
    }

    void setMediaSelector(BaseSystemMediaSelector mediaSelector) {
        mMediaSelector = mediaSelector;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mTakingType);
        dest.writeString(mMimeType);
        dest.writeParcelable(mCropOptions, flags);
        dest.writeByte((byte) (mNeedCrop ? 1 : 0));
        dest.writeString(mCameraPhotoSavePath);
        dest.writeByte((byte) (mIsMultiple ? 1 : 0));
        dest.writeByte((byte) (mCopyToInternal ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
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

    public boolean start() {
        switch (mTakingType) {
            case CAMERA:
                return mMediaSelector.takePhotoFromCamera(this);
            case ALBUM:
                return mMediaSelector.takePhotoFormSystem(this);
            case FILE:
                return mMediaSelector.takeFile(this);
        }
        return false;
    }

    public String getCameraPhotoSavePath() {
        if (TextUtils.isEmpty(mCameraPhotoSavePath)) {
            mCameraPhotoSavePath = StorageUtils.createInternalPicturePath(mMediaSelector.getContext(), StorageUtils.JPEG);
        }
        return mCameraPhotoSavePath;
    }

    public CropOptions getCropOptions() {
        return mCropOptions;
    }

    public boolean needCrop() {
        return mNeedCrop;
    }

    public String getMimeType() {
        return mMimeType;
    }

    public Instructor setNeedCrop() {
        mNeedCrop = true;
        mCropOptions = new CropOptions();
        return this;
    }

    public Instructor setNeedCrop(CropOptions cropOptions) {
        mNeedCrop = true;
        mCropOptions = cropOptions;
        return this;
    }

    public Instructor setMultiple(boolean multiple) {
        mIsMultiple = multiple;
        return this;
    }

    public void setCopyToInternal(boolean copyToInternal) {
        mCopyToInternal = copyToInternal;
    }

    /**
     * @param mimeType there are many MIMETYPEs defined in {@link android.media.MediaFormat}
     */
    public Instructor setMimeType(String mimeType) {
        mMimeType = mimeType;
        return this;
    }

    public boolean isMultiple() {
        return mIsMultiple;
    }

    public boolean isCopyToInternal() {
        return mCopyToInternal;
    }

}