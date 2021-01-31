package com.android.sdk.mediaselector.common;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2017-08-09 10:54
 */
public class MediaUtils {

    private MediaUtils() {
        throw new UnsupportedOperationException("MediaUtils");
    }

    public static final String MIMETYPE_IMAGE = "image/*";
    public static final String MIMETYPE_ALL = "*/*";

    ///////////////////////////////////////////////////////////////////////////
    // Camera
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 判断系统中是否存在可以启动的相机应用
     *
     * @return 存在返回true，不存在返回false
     */
    public static boolean hasCamera(Context context) {
        PackageManager packageManager = context.getPackageManager();
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    /**
     * @param targetFile 源文件，裁剪之后新的图片覆盖此文件
     */
    public static Intent makeCaptureIntent(Context context, File targetFile, String authority) {
        StorageUtils.makeFilePath(targetFile);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (Build.VERSION.SDK_INT < 24) {
            Uri fileUri = Uri.fromFile(targetFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        } else {
            Uri fileUri = FileProvider.getUriForFile(context, authority, targetFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        return intent;
    }

    ///////////////////////////////////////////////////////////////////////////
    // UCrop
    ///////////////////////////////////////////////////////////////////////////

    public static void toUCrop(Context context, Fragment fragment, String srcPath, CropOptions cropConfig, int requestCode) {

        Uri srcUri = new Uri.Builder()
                .scheme("file")
                .appendPath(srcPath)
                .build();

        String targetPath = StorageUtils.createInternalPicturePath(context, StorageUtils.JPEG);

        Uri targetUri = new Uri.Builder()
                .scheme("file")
                .appendPath(targetPath)
                .build();

        //参数
        UCrop.Options crop = new UCrop.Options();
        crop.setCompressionFormat(Bitmap.CompressFormat.JPEG);
        crop.withMaxResultSize(cropConfig.getOutputX(), cropConfig.getAspectY());
        crop.withAspectRatio(cropConfig.getAspectX(), cropConfig.getAspectY());

        //颜色
        int color = MediaSelectorConfiguration.getPrimaryColor(context);
        crop.setToolbarColor(color);
        crop.setStatusBarColor(color);

        //开始裁减
        if (fragment != null) {
            UCrop.of(srcUri, targetUri)
                    .withOptions(crop)
                    .start(context, fragment, requestCode);
        } else {
            if (!(context instanceof AppCompatActivity)) {
                throw new IllegalArgumentException("the context must be instance of AppCompatActivity");
            }
            UCrop.of(srcUri, targetUri)
                    .withOptions(crop)
                    .start((AppCompatActivity) context, requestCode);
        }
    }

    public static Uri getUCropResult(Intent data) {
        if (data == null) {
            return null;
        }
        Throwable throwable = UCrop.getError(data);
        if (throwable != null) {
            throwable.printStackTrace();
            return null;
        }
        return UCrop.getOutput(data);
    }

    ///////////////////////////////////////////////////////////////////////////
    // 从各种Uri中获取真实的路径
    ///////////////////////////////////////////////////////////////////////////

    /**
     * @see "https://stackoverflow.com/questions/20067508/get-real-path-from-uri-android-kitkat-new-storage-access-framework/20559175"
     */
    public static String getAbsolutePath(final Context context, final Uri uri) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     * @see "https://stackoverflow.com/questions/20067508/get-real-path-from-uri-android-kitkat-new-storage-access-framework/20559175"
     */
    private static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = MediaStore.Images.Media.DATA;
        final String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

}