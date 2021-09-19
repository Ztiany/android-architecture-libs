package com.android.base.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.UiThread;
import androidx.annotation.WorkerThread;
import androidx.fragment.app.Fragment;

// @formatter:off
public interface ImageLoader {

    void display(ImageView imageView, String url);
    void display(ImageView imageView, Source source);
    void display(ImageView imageView, String url, DisplayConfig config);
    void display(ImageView imageView, Source source, DisplayConfig config);
    void display(ImageView imageView, String url, LoadListener<Drawable> loadListener);
    void display(ImageView imageView, String url, DisplayConfig config, LoadListener<Drawable> loadListener);

    void display(Fragment fragment, ImageView imageView, String url);
    void display(Fragment fragment, ImageView imageView, Source source);
    void display(Fragment fragment, ImageView imageView, String url, DisplayConfig displayConfig);
    void display(Fragment fragment, ImageView imageView, Source source, DisplayConfig displayConfig);
    void display(Fragment fragment, ImageView imageView, String url, LoadListener<Drawable> loadListener);
    void display(Fragment fragment, ImageView imageView, String url, DisplayConfig displayConfig, LoadListener<Drawable> loadListener);

    ///////////////////////////////////////////////////////////////////////////
    // pause and resume
    ///////////////////////////////////////////////////////////////////////////

    void pause(Fragment fragment);

    void resume(Fragment fragment);

    void pause(Context context);

    void resume(Context context);

    ///////////////////////////////////////////////////////////////////////////
    // preload
    ///////////////////////////////////////////////////////////////////////////

    void preload(Context context, Source source);

    void preload(Context context, Source source, int width, int height);

    ///////////////////////////////////////////////////////////////////////////
    // LoadBitmap
    ///////////////////////////////////////////////////////////////////////////
    void loadBitmap(Context context, Source source, boolean cache, LoadListener<Bitmap> bitmapLoadListener);

    void loadBitmap(Fragment fragment, Source source, boolean cache, LoadListener<Bitmap> bitmapLoadListener);

    void loadBitmap(Context context, Source source, boolean cache, int width, int height, LoadListener<Bitmap> bitmapLoadListener);

    void loadBitmap(Fragment fragment, Source source, boolean cache, int width, int height, LoadListener<Bitmap> bitmapLoadListener);

    @WorkerThread
    Bitmap loadBitmap(Context context, Source source, boolean cache, int width, int height);

    ///////////////////////////////////////////////////////////////////////////
    // clear
    ///////////////////////////////////////////////////////////////////////////

    @WorkerThread
    void clear(Context context);

    void clear(View view);

    void clear(Fragment fragment, View view);

    ///////////////////////////////////////////////////////////////////////////
    // progress
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 移除对应 URL 的所有监听器
     *
     * @param url URL
     */
    @UiThread
    void removeAllListener(String url);

    /**
     * 添加一个对应 URL 的监听器，针对相同的 URL 可以有多个 ProgressListener，但相同的对象可以不会被重复添加
     *
     * @param url              URL
     * @param progressListener 监听器
     */
    @UiThread
    void addListener(@NonNull String url, @NonNull ProgressListener progressListener);

    /**
     * 添加一个对应 URL 的监听器，针对相同的 URL 只会有一个 ProgressListener，已经添加的 ProgressListener 会被新的替换，与{@link #addListener(String, ProgressListener)}是独立的
     *
     * @param url              URL
     * @param progressListener 监听器，如果 progressListener 为 null，则表示移除
     */
    void setListener(String url, @Nullable ProgressListener progressListener);

}