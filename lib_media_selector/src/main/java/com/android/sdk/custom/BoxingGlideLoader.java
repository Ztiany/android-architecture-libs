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

package com.android.sdk.custom;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.bilibili.boxing.loader.IBoxingCallback;
import com.bilibili.boxing.loader.IBoxingMediaLoader;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * use https://github.com/bumptech/glide as media loader.
 *
 * @author ChenSL
 */
final class BoxingGlideLoader implements IBoxingMediaLoader {

    @Override
    public void displayThumbnail(@NonNull ImageView img, @NonNull String absPath, int width, int height) {
        String path = "file://" + absPath;
        try {
            RequestOptions requestOptions = new RequestOptions();
            Glide.with(img.getContext()).load(path).apply(requestOptions).into(img);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void displayRaw(@NonNull final ImageView imageView, @NonNull String absPath, int width, int height, final IBoxingCallback iBoxingCallback) {
        String path = "file://" + absPath;
        Glide.with(imageView.getContext())
                .asBitmap()
                .load(path)
                .listener(new RequestListener<Bitmap>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                        if (iBoxingCallback != null) {
                            iBoxingCallback.onFail(e);
                            return true;
                        }
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                        if (resource != null && iBoxingCallback != null) {
                            imageView.setImageBitmap(resource);
                            iBoxingCallback.onSuccess();
                            return true;
                        }
                        return false;
                    }
                })
                .into(imageView);
    }

}