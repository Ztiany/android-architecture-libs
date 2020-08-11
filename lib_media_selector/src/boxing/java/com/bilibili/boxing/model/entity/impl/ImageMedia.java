/*
 *  Copyright (C) 2017 Bilibili
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.bilibili.boxing.model.entity.impl;


import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.bilibili.boxing.model.entity.BaseMedia;
import com.bilibili.boxing.utils.BoxingFileHelper;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Objects;

import androidx.annotation.NonNull;

/**
 * Id and absolute path is necessary.Builder Mode can be used too.
 *
 * @author ChenSL
 */
public class ImageMedia extends BaseMedia implements Parcelable {

    private static final long MAX_GIF_SIZE = 1024 * 1024L;

    private boolean mIsSelected;
    private Uri mThumbnailPath;
    private int mHeight;
    private int mWidth;
    private IMAGE_TYPE mImageType;
    private String mMimeType;

    public enum IMAGE_TYPE {
        PNG, JPG, GIF
    }

    public ImageMedia(String id, Uri imageUri) {
        super(id, imageUri);
    }

    public ImageMedia(@NonNull File file) {
        this.mId = String.valueOf(System.currentTimeMillis());
        this.mUri = Uri.fromFile(file);
        this.mSize = String.valueOf(file.length());
        this.mIsSelected = true;
    }

    public ImageMedia(Builder builder) {
        super(builder.mId, builder.mImageUri);
        this.mThumbnailPath = builder.mThumbnailPath;
        this.mSize = builder.mSize;
        this.mHeight = builder.mHeight;
        this.mIsSelected = builder.mIsSelected;
        this.mWidth = builder.mWidth;
        this.mMimeType = builder.mMimeType;
        this.mImageType = getImageTypeByMime(builder.mMimeType);
    }

    @Override
    public TYPE getType() {
        return TYPE.IMAGE;
    }

    public boolean isSelected() {
        return mIsSelected;
    }

    public void setSelected(boolean selected) {
        mIsSelected = selected;
    }

    public boolean isGifOverSize() {
        return isGif() && getSize() > MAX_GIF_SIZE;
    }

    public boolean isGif() {
        return getImageType() == IMAGE_TYPE.GIF;
    }

    /**
     * get mime type displayed in database.
     *
     * @return "image/gif" or "image/jpeg".
     */
    public String getMimeType() {
        if (getImageType() == IMAGE_TYPE.GIF) {
            return "image/gif";
        } else if (getImageType() == IMAGE_TYPE.JPG) {
            return "image/jpeg";
        }
        return "image/jpeg";
    }

    public IMAGE_TYPE getImageType() {
        return mImageType;
    }

    private IMAGE_TYPE getImageTypeByMime(String mimeType) {
        if (!TextUtils.isEmpty(mimeType)) {
            if ("image/gif".equals(mimeType)) {
                return IMAGE_TYPE.GIF;
            } else if ("image/png".equals(mimeType)) {
                return IMAGE_TYPE.PNG;
            } else {
                return IMAGE_TYPE.JPG;
            }
        }
        return IMAGE_TYPE.PNG;
    }

    public void setImageType(IMAGE_TYPE imageType) {
        mImageType = imageType;
    }

    public String getId() {
        return mId;
    }

    public int getHeight() {
        return mHeight;
    }

    public int getWidth() {
        return mWidth;
    }

    public void setSize(String size) {
        mSize = size;
    }

    public void setHeight(int height) {
        mHeight = height;
    }

    public void setWidth(int width) {
        mWidth = width;
    }

    @NotNull
    @Override
    public String toString() {
        return "ImageMedia{" +
                ", mThumbnailPath='" + mThumbnailPath + '\'' +
                ", mSize='" + mSize + '\'' +
                ", mHeight=" + mHeight +
                ", mWidth=" + mWidth;
    }

    @NonNull
    public Uri getThumbnailPath() {
        if (BoxingFileHelper.isFileValid(mThumbnailPath)) {
            return mThumbnailPath;
        }
        return mUri;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ImageMedia)) return false;
        ImageMedia that = (ImageMedia) o;
        return isSelected() == that.isSelected() &&
                getHeight() == that.getHeight() &&
                getWidth() == that.getWidth() &&
                Objects.equals(getThumbnailPath(), that.getThumbnailPath()) &&
                getImageType() == that.getImageType() &&
                Objects.equals(getMimeType(), that.getMimeType());
    }

    @Override
    public int hashCode() {
        return Objects.hash(isSelected(), getThumbnailPath(), getHeight(), getWidth(), getImageType(), getMimeType());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeByte(this.mIsSelected ? (byte) 1 : (byte) 0);
        dest.writeParcelable(this.mThumbnailPath, flags);
        dest.writeInt(this.mHeight);
        dest.writeInt(this.mWidth);
        dest.writeInt(this.mImageType == null ? -1 : this.mImageType.ordinal());
        dest.writeString(this.mMimeType);
    }

    protected ImageMedia(Parcel in) {
        super(in);
        this.mIsSelected = in.readByte() != 0;
        this.mThumbnailPath = in.readParcelable(Uri.class.getClassLoader());
        this.mHeight = in.readInt();
        this.mWidth = in.readInt();
        int tmpMImageType = in.readInt();
        this.mImageType = tmpMImageType == -1 ? null : IMAGE_TYPE.values()[tmpMImageType];
        this.mMimeType = in.readString();
    }

    public static final Creator<ImageMedia> CREATOR = new Creator<ImageMedia>() {
        @Override
        public ImageMedia createFromParcel(Parcel source) {
            return new ImageMedia(source);
        }

        @Override
        public ImageMedia[] newArray(int size) {
            return new ImageMedia[size];
        }
    };

    public static class Builder {

        private String mId;
        private Uri mImageUri;
        private boolean mIsSelected;
        private Uri mThumbnailPath;
        private String mSize;
        private int mHeight;
        private int mWidth;
        private String mMimeType;

        public Builder(String id, Uri imageUri) {
            this.mId = id;
            this.mImageUri = imageUri;
        }

        public Builder setSelected(boolean selected) {
            this.mIsSelected = selected;
            return this;
        }

        public Builder setThumbnailPath(Uri thumbnailPath) {
            mThumbnailPath = thumbnailPath;
            return this;
        }

        public Builder setHeight(int height) {
            mHeight = height;
            return this;
        }

        public Builder setWidth(int width) {
            mWidth = width;
            return this;
        }

        public Builder setMimeType(String mimeType) {
            mMimeType = mimeType;
            return this;
        }

        public Builder setSize(String size) {
            this.mSize = size;
            return this;
        }

        public ImageMedia build() {
            return new ImageMedia(this);
        }

    }

}
