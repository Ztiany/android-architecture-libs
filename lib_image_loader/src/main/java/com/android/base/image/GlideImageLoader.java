package com.android.base.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ExecutionException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

class GlideImageLoader implements ImageLoader {

    private boolean canFragmentLoadImage(Fragment fragment) {
        return fragment.isResumed() || fragment.isAdded() || fragment.isVisible();
    }

    @Override
    public void display(Fragment fragment, ImageView imageView, final String url, final LoadListener<Drawable> loadListener) {
        if (canFragmentLoadImage(fragment)) {
            Glide.with(fragment).load(url).into(new InnerImageTarget(imageView, url, loadListener));
        }
    }

    @Override
    public void display(Fragment fragment, ImageView imageView, String url, DisplayConfig displayConfig, LoadListener<Drawable> loadListener) {
        if (!canFragmentLoadImage(fragment)) {
            return;
        }
        RequestBuilder<Drawable> requestBuilder = Glide.with(fragment).load(url);
        if (displayConfig != null) {
            RequestOptions requestOptions = buildRequestOptions(displayConfig);
            requestBuilder = requestBuilder.apply(requestOptions);
            if (displayConfig.getThumbnail() != 0) {
                requestBuilder = requestBuilder.thumbnail(displayConfig.getThumbnail());
            }
        }
        requestBuilder.into(new InnerImageTarget(imageView, url, loadListener));
    }

    @Override
    public void display(ImageView imageView, String url, LoadListener<Drawable> loadListener) {
        Glide.with(imageView.getContext()).load(url).into(new InnerImageTarget(imageView, url, loadListener));
    }

    @Override
    public void display(ImageView imageView, String url, DisplayConfig config, LoadListener<Drawable> loadListener) {
        RequestBuilder<Drawable> requestBuilder = Glide.with(imageView.getContext()).load(url);
        if (config != null) {
            RequestOptions requestOptions = buildRequestOptions(config);
            requestBuilder = requestBuilder.apply(requestOptions);
            if (config.getThumbnail() != 0) {
                requestBuilder = requestBuilder.thumbnail(config.getThumbnail());
            }
        }
        requestBuilder.into(new InnerImageTarget(imageView, url, loadListener));
    }


    @Override
    public void removeAllListener(String url) {
        ProgressManager.getInstance().removeListener(url);
    }

    @Override
    public void addListener(@NonNull String url, @NonNull ProgressListener progressListener) {
        ProgressManager.getInstance().addLoadListener(url, progressListener);
    }

    @Override
    public void setListener(String url, ProgressListener progressListener) {
        ProgressManager.getInstance().setListener(url, progressListener);
    }

    private static class InnerImageTarget extends ImageViewTarget<Drawable> {

        private final LoadListener<Drawable> mLoadListener;

        InnerImageTarget(ImageView view, String url, LoadListener<Drawable> loadListener) {
            super(view);
            mLoadListener = loadListener;
        }

        @Override
        protected void setResource(@Nullable Drawable resource) {
            getView().setImageDrawable(resource);
        }

        @Override
        public void onResourceReady(@NotNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
            super.onResourceReady(resource, transition);
            mLoadListener.onLoadSuccess(resource);
        }

        @Override
        public void onLoadStarted(@Nullable Drawable placeholder) {
            mLoadListener.onLoadStart();
            super.onLoadStarted(placeholder);
        }

        @Override
        public void onLoadFailed(@Nullable Drawable errorDrawable) {
            super.onLoadFailed(errorDrawable);
            mLoadListener.onLoadFail();
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // Progress end
    ///////////////////////////////////////////////////////////////////////////

    @Override
    public void display(Fragment fragment, ImageView imageView, String url) {
        if (canFragmentLoadImage(fragment)) {
            Glide.with(fragment).load(url).into(imageView);
        }
    }

    @Override
    public void display(Fragment fragment, ImageView imageView, String url, DisplayConfig displayConfig) {
        display(imageView, Glide.with(fragment).load(url), displayConfig);
    }

    @Override
    public void display(Fragment fragment, ImageView imageView, Source source) {
        display(fragment, imageView, source, null);
    }

    @Override
    public void display(Fragment fragment, ImageView imageView, Source source, DisplayConfig displayConfig) {
        if (!canFragmentLoadImage(fragment)) {
            return;
        }
        RequestManager requestManager = Glide.with(fragment);
        RequestBuilder<?> drawableTypeRequest = setToRequest(requestManager, source);
        display(imageView, drawableTypeRequest, displayConfig);
    }

    @Override
    public void display(ImageView imageView, String url) {
        display(imageView, Glide.with(imageView.getContext()).load(url), null);
    }

    @Override
    public void display(ImageView imageView, String url, DisplayConfig config) {
        display(imageView, Glide.with(imageView.getContext()).load(url), config);
    }

    @Override
    public void display(ImageView imageView, Source source) {
        RequestBuilder<?> drawableTypeRequest = setToRequest(Glide.with(imageView.getContext()), source);
        display(imageView, drawableTypeRequest, null);
    }

    @Override
    public void display(ImageView imageView, Source source, DisplayConfig config) {
        RequestBuilder<?> drawableTypeRequest = setToRequest(Glide.with(imageView.getContext()), source);
        display(imageView, drawableTypeRequest, config);
    }

    private void display(ImageView imageView, RequestBuilder request, DisplayConfig displayConfig) {
        if (displayConfig != null) {
            RequestOptions requestOptions = buildRequestOptions(displayConfig);
            request = request.apply(requestOptions);
            if (displayConfig.getThumbnail() != 0) {
                request = request.thumbnail(displayConfig.getThumbnail());
            }
        }
        request.into(imageView);
    }

    ///////////////////////////////////////////////////////////////////////////
    // pause and resume
    ///////////////////////////////////////////////////////////////////////////

    @Override
    public void pause(Fragment fragment) {
        Glide.with(fragment).pauseRequests();
    }

    @Override
    public void resume(Fragment fragment) {
        Glide.with(fragment).resumeRequests();
    }

    @Override
    public void pause(Context context) {
        Glide.with(context).pauseRequests();
    }

    @Override
    public void resume(Context context) {
        Glide.with(context).resumeRequests();
    }

    ///////////////////////////////////////////////////////////////////////////
    //Preload
    ///////////////////////////////////////////////////////////////////////////

    @Override
    public void preload(Context context, Source source) {
        RequestManager requestManager = Glide.with(context);
        setToRequest(requestManager, source).preload();
    }

    @Override
    public void preload(Context context, Source source, int width, int height) {
        RequestManager requestManager = Glide.with(context);
        setToRequest(requestManager, source).preload(width, height);
    }

    @Override
    public void clear(Context context) {
        Glide.get(context).clearDiskCache();
    }

    @Override
    public void clear(View view) {
        Context context = view.getContext();
        if (context != null) {
            Glide.with(context).clear(view);
        }
    }

    @Override
    public void clear(Fragment fragment, View view) {
        Glide.with(fragment).clear(view);
    }

    ///////////////////////////////////////////////////////////////////////////
    //Load Bitmap
    ///////////////////////////////////////////////////////////////////////////

    @Override
    public void loadBitmap(Context context, Source source, boolean cache, final LoadListener<Bitmap> bitmapLoadListener) {
        loadBitmap(context, source, cache, 0, 0, bitmapLoadListener);
    }

    @Override
    public void loadBitmap(Fragment fragment, Source source, boolean cache, LoadListener<Bitmap> bitmapLoadListener) {
        loadBitmap(fragment, source, cache, 0, 0, bitmapLoadListener);
    }

    @Override
    public void loadBitmap(Fragment fragment, Source source, boolean cache, int width, int height, LoadListener<Bitmap> bitmapLoadListener) {
        loadBitmapInternal(Glide.with(fragment), source, cache, width, height, bitmapLoadListener);
    }

    @Override
    public Bitmap loadBitmap(Context context, Source source, boolean cache, int width, int height) {
        RequestBuilder<Bitmap> requestBuilder = Glide.with(context).asBitmap();
        requestBuilder = setToRequest(requestBuilder, source);
        if (width > 0 && height > 0) {
            try {
                return requestBuilder.submit(width, height).get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        } else {
            try {
                return requestBuilder.submit().get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public void loadBitmap(Context context, Source source, boolean cache, int width, int height, LoadListener<Bitmap> bitmapLoadListener) {
        loadBitmapInternal(Glide.with(context), source, cache, width, height, bitmapLoadListener);
    }

    private void loadBitmapInternal(RequestManager requestManager, Source source, boolean cache, int width, int height, LoadListener<Bitmap> bitmapLoadListener) {
        RequestOptions requestOptions = new RequestOptions();
        if (cache) {
            requestOptions = requestOptions.skipMemoryCache(false).diskCacheStrategy(DiskCacheStrategy.ALL);
        } else {
            requestOptions = requestOptions.skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE);
        }
        if (width > 0 && height > 0) {
            requestOptions = requestOptions.override(width, height);
        }

        RequestBuilder<Bitmap> requestBuilder = requestManager.asBitmap().apply(requestOptions);
        requestBuilder = setToRequest(requestBuilder, source);
        requestBuilder.into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NotNull Bitmap resource, Transition<? super Bitmap> transition) {
                bitmapLoadListener.onLoadSuccess(resource);
            }

            @Override
            public void onLoadFailed(@Nullable Drawable errorDrawable) {
                bitmapLoadListener.onLoadFail();
            }

            @Override
            public void onLoadStarted(@Nullable Drawable placeholder) {
                bitmapLoadListener.onLoadStart();
            }
        });
    }

    ///////////////////////////////////////////////////////////////////////////
    // Build request
    ///////////////////////////////////////////////////////////////////////////

    private RequestOptions buildRequestOptions(DisplayConfig displayConfig) {
        RequestOptions requestOptions = new RequestOptions();
        /*DiskCache*/
        if (displayConfig.isCacheDisk()) {
            requestOptions = requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
        } else {
            requestOptions = requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
        }

        /*MemoryCache*/
        if (displayConfig.isCacheMemory()) {
            requestOptions = requestOptions.skipMemoryCache(false);
        } else {
            requestOptions = requestOptions.skipMemoryCache(true);
        }

        /*SCALE_TYPE*/
        if (displayConfig.getScaleType() == DisplayConfig.SCALE_CENTER_CROP) {
            requestOptions = requestOptions.centerCrop();
        } else if (displayConfig.getScaleType() == DisplayConfig.SCALE_FIT_CENTER) {
            requestOptions = requestOptions.fitCenter();
        }

        /*transform*/
        if (displayConfig.getTransform() == DisplayConfig.TRANSFORM_CIRCLE) {
            requestOptions = requestOptions.circleCrop();
        } else if (displayConfig.getTransform() == DisplayConfig.TRANSFORM_ROUNDED_CORNERS) {
            requestOptions = requestOptions.transform(new RoundedCorners(displayConfig.getRoundedCornersRadius()));
        } else if (displayConfig.getTransform() == DisplayConfig.TRANSFORM_NONE) {
            requestOptions = requestOptions.dontTransform();//不做渐入渐出的转换
        }

        /*Placeholder*/
        if (displayConfig.getErrorPlaceholder() != DisplayConfig.NO_PLACE_HOLDER) {
            requestOptions = requestOptions.error(displayConfig.getErrorPlaceholder());
        }
        if (displayConfig.getErrorDrawable() != null) {
            requestOptions = requestOptions.error(displayConfig.getErrorDrawable());
        }

        if (displayConfig.getLoadingPlaceholder() != DisplayConfig.NO_PLACE_HOLDER) {
            requestOptions = requestOptions.placeholder(displayConfig.getLoadingPlaceholder());
        }

        if (displayConfig.getLoadingDrawable() != null) {
            requestOptions = requestOptions.placeholder(displayConfig.getLoadingDrawable());
        }

        if (displayConfig.getHeight() > 0 && displayConfig.getWidth() > 0) {
            requestOptions = requestOptions.override(displayConfig.getWidth(), displayConfig.getHeight());
        }

        return requestOptions;
    }

    private RequestBuilder<?> setToRequest(RequestManager requestManager, Source source) {
        if (source.mFile != null) {
            return requestManager.load(source.mFile);
        } else if (source.mUrl != null) {
            return requestManager.load(source.mUrl);
        } else if (source.mResource != 0) {
            return requestManager.load(source.mResource);
        } else if (source.mUri != null) {
            return requestManager.load(source.mUri);
        } else if (source.mBytes != null) {
            return requestManager.load(source.mBytes);
        } else {
            throw new IllegalArgumentException("UnSupport source");
        }
    }

    private <T> RequestBuilder<T> setToRequest(RequestBuilder<T> requestBuilder, Source source) {
        if (source.mFile != null) {
            return requestBuilder.load(source.mFile);
        } else if (source.mUrl != null) {
            return requestBuilder.load(source.mUrl);
        } else if (source.mResource != 0) {
            return requestBuilder.load(source.mResource);
        } else if (source.mUri != null) {
            return requestBuilder.load(source.mUri);
        } else if (source.mBytes != null) {
            return requestBuilder.load(source.mBytes);
        } else {
            throw new IllegalArgumentException("UnSupport source");
        }
    }

}
