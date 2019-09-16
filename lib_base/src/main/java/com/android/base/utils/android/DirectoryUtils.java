package com.android.base.utils.android;

import android.os.Environment;

import com.android.base.utils.BaseUtils;

import java.io.File;

import androidx.annotation.NonNull;
import timber.log.Timber;

public class DirectoryUtils {

    private DirectoryUtils() {
    }

    /**
     * 获取SD卡上私有的外部存储
     *
     * @return /storage/emulated/0/Android/data/包名/cache/
     */
    public static File getAppExternalCacheStorage() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return BaseUtils.getAppContext().getExternalCacheDir();
        } else {
            return BaseUtils.getAppContext().getCacheDir();
        }
    }

    /**
     * 获取SD卡上外部存储
     *
     * @return /storage/emulated/0/
     */
    public static File getExternalStorage() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return Environment.getExternalStorageDirectory();
        } else {
            return BaseUtils.getAppContext().getCacheDir();
        }
    }

    /**
     * 获取公共的外部存储目录
     *
     * @param type {@link Environment#DIRECTORY_DOWNLOADS},
     *             {@link Environment#DIRECTORY_DCIM}, ect
     * @return DIRECTORY_DCIM = /storage/sdcard0/DCIM ,
     * DIRECTORY_DOWNLOADS =  /storage/sdcard0/Download ...ect
     */
    public static File getExternalStoragePublicDirectory(@NonNull String type) {
        String state = Environment.getExternalStorageState();
        File dir;
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            dir = Environment.getExternalStoragePublicDirectory(type);
        } else {
            dir = new File(BaseUtils.getAppContext().getCacheDir(), type);
        }
        if (dir != null && !dir.exists()) {
            boolean mkdirs = dir.mkdirs();
            Timber.d("getExternalStoragePublicDirectory type = " + type + " mkdirs = " + mkdirs);
        }
        return dir;
    }


}
