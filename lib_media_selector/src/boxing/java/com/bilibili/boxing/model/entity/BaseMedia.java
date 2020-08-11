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

package com.bilibili.boxing.model.entity;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * The base entity for media.
 *
 * @author ChenSL
 */
public abstract class BaseMedia implements Parcelable {

    protected enum TYPE {
        IMAGE, VIDEO
    }

    protected Uri mUri;
    protected String mId;
    protected String mSize;

    public BaseMedia() {
    }

    public BaseMedia(String id, Uri uri) {
        mId = id;
        mUri = uri;
    }

    public abstract TYPE getType();

    public String getId() {
        return mId;
    }

    public long getSize() {
        try {
            long result = Long.parseLong(mSize);
            return result > 0 ? result : 0;
        } catch (NumberFormatException size) {
            return 0;
        }
    }

    public void setId(String id) {
        mId = id;
    }

    public void setSize(String size) {
        mSize = size;
    }

    public Uri getUri() {
        return mUri;
    }

    public void setUri(Uri uri) {
        mUri = uri;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.mUri, flags);
        dest.writeString(this.mId);
        dest.writeString(this.mSize);
    }

    protected BaseMedia(Parcel in) {
        this.mUri = in.readParcelable(Uri.class.getClassLoader());
        this.mId = in.readString();
        this.mSize = in.readString();
    }

}
