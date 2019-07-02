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

package com.android.sdk.mediaselector;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import com.bilibili.boxing.loader.IBoxingCrop;
import com.bilibili.boxing.model.config.BoxingCropOption;
import com.yalantis.ucrop.UCrop;
import com.ztiany.mediaselector.R;

/**
 * use Ucrop(https://github.com/Yalantis/uCrop) as the implement for {@link IBoxingCrop}
 *
 * @author ChenSL
 */
final class BoxingUcrop implements IBoxingCrop {

    @Override
    public void onStartCrop(Context context, Fragment fragment, @NonNull BoxingCropOption cropConfig,
                            @NonNull String path, int requestCode) {

        Uri uri = new Uri.Builder()
                .scheme("file")
                .appendPath(path)
                .build();
        //参数
        UCrop.Options crop = new UCrop.Options();
        crop.setCompressionFormat(Bitmap.CompressFormat.JPEG);
        crop.withMaxResultSize(cropConfig.getMaxWidth(), cropConfig.getMaxHeight());
        crop.withAspectRatio(cropConfig.getAspectRatioX(), cropConfig.getAspectRatioY());
        //颜色
        int color = ContextCompat.getColor(context, R.color.boxing_colorPrimaryDark);
        crop.setToolbarColor(color);
        crop.setStatusBarColor(color);
        //开始裁减
        UCrop.of(uri, cropConfig.getDestination())
                .withOptions(crop)
                .start(context, fragment, requestCode);
    }

    @Override
    public Uri onCropFinish(int resultCode, Intent data) {
        if (data == null) {
            return null;
        }
        Throwable throwable = UCrop.getError(data);
        if (throwable != null) {
            return null;
        }
        return UCrop.getOutput(data);
    }
}
