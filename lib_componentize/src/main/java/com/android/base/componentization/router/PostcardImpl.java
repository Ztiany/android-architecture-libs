package com.android.base.componentization.router;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.SparseArray;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityOptionsCompat;

import com.alibaba.android.arouter.facade.Postcard;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author Ztiany
 * Email: 1169654504@qq.com
 * Date : 2017-06-22 14:50
 */
final class PostcardImpl implements IPostcard {

    private final Postcard mPostcard;

    PostcardImpl(Postcard postcard) {
        mPostcard = postcard;
    }

    @Override
    public int getEnterAnim() {
        return mPostcard.getEnterAnim();
    }

    @Override
    public int getExitAnim() {
        return mPostcard.getExitAnim();
    }

    @Override
    public Bundle getExtras() {
        return mPostcard.getExtras();
    }

    @Override
    public Uri getUri() {
        return mPostcard.getUri();
    }

    @Override
    public IPostcard setUri(Uri uri) {
        mPostcard.setUri(uri);
        return this;
    }

    @Override
    public Object navigation() {
        return mPostcard.navigation();
    }

    @Override
    public Object navigation(Context context) {
        return mPostcard.navigation(context);
    }

    @Override
    public Object navigation(Context context, AppNavigationCallback callback) {
        return mPostcard.navigation(context, new NavigationCallbackWrapper(callback));
    }

    @Override
    public void navigation(Activity mContext, int requestCode) {
        mPostcard.navigation(mContext, requestCode);
    }

    @Override
    public void navigation(Activity mContext, int requestCode, AppNavigationCallback callback) {
        mPostcard.navigation(mContext, requestCode, new NavigationCallbackWrapper(callback));
    }

    @Override
    public IPostcard with(Bundle bundle) {
        mPostcard.with(bundle);
        return this;
    }

    @Override
    public IPostcard withFlags(int flag) {
        mPostcard.withFlags(flag);
        return this;
    }

    @Override
    public int getFlags() {
        return mPostcard.getFlags();
    }

    @Override
    public IPostcard withObject(@Nullable String key, @Nullable Object value) {
        mPostcard.withObject(key, value);
        return this;
    }

    @Override
    public IPostcard withString(@Nullable String key, @Nullable String value) {
        mPostcard.withString(key, value);
        return this;
    }

    @Override
    public IPostcard withBoolean(@Nullable String key, boolean value) {
        mPostcard.withBoolean(key, value);
        return this;
    }

    @Override
    public IPostcard withShort(@Nullable String key, short value) {
        mPostcard.withShort(key, value);
        return this;
    }

    @Override
    public IPostcard withInt(@Nullable String key, int value) {
        mPostcard.withInt(key, value);
        return this;
    }

    @Override
    public IPostcard withLong(@Nullable String key, long value) {
        mPostcard.withLong(key, value);
        return this;
    }

    @Override
    public IPostcard withDouble(@Nullable String key, double value) {
        mPostcard.withDouble(key, value);
        return this;
    }

    @Override
    public IPostcard withByte(@Nullable String key, byte value) {
        mPostcard.withByte(key, value);
        return this;
    }

    @Override
    public IPostcard withChar(@Nullable String key, char value) {
        mPostcard.withChar(key, value);
        return this;
    }

    @Override
    public IPostcard withFloat(@Nullable String key, float value) {
        mPostcard.withFloat(key, value);
        return this;
    }

    @Override
    public IPostcard withCharSequence(@Nullable String key, @Nullable CharSequence value) {
        mPostcard.withCharSequence(key, value);
        return this;
    }

    @Override
    public IPostcard withParcelable(@Nullable String key, @Nullable Parcelable value) {
        mPostcard.withParcelable(key, value);
        return this;
    }

    @Override
    public IPostcard withParcelableArray(@Nullable String key, @Nullable Parcelable[] value) {
        mPostcard.withParcelableArray(key, value);
        return this;
    }

    @Override
    public IPostcard withParcelableArrayList(@Nullable String key, @Nullable ArrayList<? extends Parcelable> value) {
        mPostcard.withParcelableArrayList(key, value);
        return this;
    }

    @Override
    public IPostcard withSparseParcelableArray(@Nullable String key, @Nullable SparseArray<? extends Parcelable> value) {
        mPostcard.withSparseParcelableArray(key, value);
        return this;
    }

    @Override
    public IPostcard withIntegerArrayList(@Nullable String key, @Nullable ArrayList<Integer> value) {
        mPostcard.withIntegerArrayList(key, value);
        return this;
    }

    @Override
    public IPostcard withStringArrayList(@Nullable String key, @Nullable ArrayList<String> value) {
        mPostcard.withStringArrayList(key, value);
        return this;
    }

    @Override
    public IPostcard withCharSequenceArrayList(@Nullable String key, @Nullable ArrayList<CharSequence> value) {
        mPostcard.withCharSequenceArrayList(key, value);
        return this;
    }

    @Override
    public IPostcard withSerializable(@Nullable String key, @Nullable Serializable value) {
        mPostcard.withSerializable(key, value);
        return this;
    }

    @Override
    public IPostcard withByteArray(@Nullable String key, @Nullable byte[] value) {
        mPostcard.withByteArray(key, value);
        return this;
    }

    @Override
    public IPostcard withShortArray(@Nullable String key, @Nullable short[] value) {
        mPostcard.withShortArray(key, value);
        return this;
    }

    @Override
    public IPostcard withCharArray(@Nullable String key, @Nullable char[] value) {
        mPostcard.withCharArray(key, value);
        return this;
    }

    @Override
    public IPostcard withFloatArray(@Nullable String key, @Nullable float[] value) {
        mPostcard.withFloatArray(key, value);
        return this;
    }

    @Override
    public IPostcard withCharSequenceArray(@Nullable String key, @Nullable CharSequence[] value) {
        mPostcard.withCharSequenceArray(key, value);
        return this;
    }

    @Override
    public IPostcard withBundle(@Nullable String key, @Nullable Bundle value) {
        mPostcard.withBundle(key, value);
        return this;
    }

    @Override
    public IPostcard withTransition(int enterAnim, int exitAnim) {
        mPostcard.withTransition(enterAnim, exitAnim);
        return this;
    }

    @Override
    public IPostcard withOptionsCompat(ActivityOptionsCompat compat) {
        mPostcard.withOptionsCompat(compat);
        return this;
    }

}