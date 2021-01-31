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

package com.bilibili.boxing.utils;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;

import java.io.File;
import java.util.concurrent.ExecutionException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * A file helper to make thing easier.
 *
 * @author ChenSL
 */
public class BoxingFileHelper {

    public static final String DEFAULT_SUB_DIR = "/boxing";

    public static boolean createFile(String path) throws ExecutionException, InterruptedException {
        if (TextUtils.isEmpty(path)) {
            return false;
        }
        final File file = new File(path);
        return file.exists() || file.mkdirs();
    }

    @Nullable
    public static String getCacheDir(@NonNull Context context) {
        context = context.getApplicationContext();
        File cacheDir = context.getCacheDir();
        if (cacheDir == null) {
            BoxingLog.d("cache dir do not exist.");
            return null;
        }
        String result = cacheDir.getAbsolutePath() + DEFAULT_SUB_DIR;
        try {
            BoxingFileHelper.createFile(result);
        } catch (ExecutionException | InterruptedException e) {
            BoxingLog.d("cache dir " + result + " not exist");
            return null;
        }
        BoxingLog.d("cache dir is: " + result);
        return result;
    }

    @Nullable
    public static String getBoxingPathInDCIM(Context context) {
        return getInternalDCIM(context, null);
    }

    @Nullable
    public static String getInternalDCIM(Context context, String subDir) {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            File file = context.getExternalFilesDir(Environment.DIRECTORY_DCIM);
            if (file == null) {
                return null;
            }

            if (TextUtils.isEmpty(subDir)) {
                subDir = DEFAULT_SUB_DIR;
            }

            String result = file.getAbsolutePath() + subDir;
            BoxingLog.d("external DCIM is: " + result);
            return result;
        } else {
            BoxingLog.d("external DCIM do not exist.");
            return getCacheDir(context);
        }
    }

    public static boolean isFileValid(Uri uri) {
        if (uri == null) {
            return false;
        }
        String scheme = uri.getScheme();

        if (scheme != null && scheme.contains("file")) {
            String filePath = uri.getPath();
            if (TextUtils.isEmpty(filePath)) {
                return false;
            }
            String path = new File(filePath).getAbsolutePath();
            if (TextUtils.isEmpty(path)) {
                return false;
            }
            File file = new File(path);
            return isFileValid(file);
        }

        return true;
    }

    static boolean isFileValid(File file) {
        return file != null && file.exists() && file.isFile() && file.length() > 0 && file.canRead();
    }

}
