package com.android.sdk.mediaselector.common;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import androidx.annotation.RestrictTo;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2020-08-07 09:57
 */
@RestrictTo(RestrictTo.Scope.LIBRARY)
public class StorageUtils {

    private StorageUtils() {
        throw new UnsupportedOperationException("StorageUtils");
    }

    private static final String MEDIA_SELECTOR_FOLDER = "media-selector";

    public static final String JPEG = ".jpeg";

    public static String createInternalPicturePath(Context context, String postfix) {
        if (!postfix.startsWith(".")) {
            postfix = "." + postfix;
        }
        File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), MEDIA_SELECTOR_FOLDER + "/" + tempFileName() + postfix);
        StorageUtils.makeFilePath(file);
        return file.getAbsolutePath();
    }

    /**
     * 统一生成图片的名称格式
     */
    private static String tempFileName() {
        return new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault()).format(new Date()) + "_" + UUID.randomUUID().toString();
    }

    public static boolean makeFilePath(File file) {
        if (file == null) {
            return false;
        }
        File parent = file.getParentFile();
        if (parent == null) {
            return false;
        }
        return parent.exists() || parent.mkdirs();
    }

    public static void makeNewFile(File file) {
        makeFilePath(file);
        try {
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isSpace(final String s) {
        if (s == null) return true;
        for (int i = 0, len = s.length(); i < len; ++i) {
            if (!Character.isWhitespace(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static String getFileNameNoExtension(final String filePath) {
        if (isSpace(filePath)) return "";
        int lastPoi = filePath.lastIndexOf('.');
        int lastSep = filePath.lastIndexOf(File.separator);
        if (lastSep == -1) {
            return (lastPoi == -1 ? filePath : filePath.substring(0, lastPoi));
        }
        if (lastPoi == -1 || lastSep > lastPoi) {
            return filePath.substring(lastSep + 1);
        }
        return filePath.substring(lastSep + 1, lastPoi);
    }

    public static String getFileExtension(final String filePath) {
        if (isSpace(filePath)) return "";
        int lastPoi = filePath.lastIndexOf('.');
        int lastSep = filePath.lastIndexOf(File.separator);
        if (lastPoi == -1 || lastSep >= lastPoi) return "";
        return filePath.substring(lastPoi + 1);
    }

    public static String addFilePostfix(final String filePath, String postfix) {
        if (isSpace(filePath)) return "";
        File file = new File(filePath);
        return file.getParentFile().getAbsolutePath() + File.separator + getFileNameNoExtension(filePath) + postfix + "." + getFileExtension(filePath);
    }

}