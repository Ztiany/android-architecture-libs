package com.android.base.componentization.router;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.SparseArray;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityOptionsCompat;

import java.io.Serializable;
import java.util.ArrayList;


/**
 * @author Ztiany
 * Email: 1169654504@qq.com
 * Date : 2017-06-22 14:50
 */
public interface IPostcard {

    int getEnterAnim();

    int getExitAnim();

    Bundle getExtras();

    Uri getUri();

    IPostcard setUri(Uri uri);

    Object navigation();

    Object navigation(Context context);

    Object navigation(Context context, AppNavigationCallback callback);

    void navigation(Activity mContext, int requestCode);

    void navigation(Activity mContext, int requestCode, AppNavigationCallback callback);

    IPostcard with(Bundle bundle);

    IPostcard withFlags(int flag);

    int getFlags();

    IPostcard withObject(@Nullable String key, @Nullable Object value);

    IPostcard withString(@Nullable String key, @Nullable String value);

    IPostcard withBoolean(@Nullable String key, boolean value);

    IPostcard withShort(@Nullable String key, short value);

    IPostcard withInt(@Nullable String key, int value);

    IPostcard withLong(@Nullable String key, long value);

    IPostcard withDouble(@Nullable String key, double value);

    IPostcard withByte(@Nullable String key, byte value);

    IPostcard withChar(@Nullable String key, char value);

    IPostcard withFloat(@Nullable String key, float value);

    IPostcard withCharSequence(@Nullable String key, @Nullable CharSequence value);

    IPostcard withParcelable(@Nullable String key, @Nullable Parcelable value);

    IPostcard withParcelableArray(@Nullable String key, @Nullable Parcelable[] value);

    IPostcard withParcelableArrayList(@Nullable String key, @Nullable ArrayList<? extends Parcelable> value);

    IPostcard withSparseParcelableArray(@Nullable String key, @Nullable SparseArray<? extends Parcelable> value);

    IPostcard withIntegerArrayList(@Nullable String key, @Nullable ArrayList<Integer> value);

    IPostcard withStringArrayList(@Nullable String key, @Nullable ArrayList<String> value);

    IPostcard withCharSequenceArrayList(@Nullable String key, @Nullable ArrayList<CharSequence> value);

    IPostcard withSerializable(@Nullable String key, @Nullable Serializable value);

    IPostcard withByteArray(@Nullable String key, @Nullable byte[] value);

    IPostcard withShortArray(@Nullable String key, @Nullable short[] value);

    IPostcard withCharArray(@Nullable String key, @Nullable char[] value);

    IPostcard withFloatArray(@Nullable String key, @Nullable float[] value);

    IPostcard withCharSequenceArray(@Nullable String key, @Nullable CharSequence[] value);

    IPostcard withBundle(@Nullable String key, @Nullable Bundle value);

    IPostcard withTransition(int enterAnim, int exitAnim);

    @RequiresApi(16)
    IPostcard withOptionsCompat(ActivityOptionsCompat compat);

}
