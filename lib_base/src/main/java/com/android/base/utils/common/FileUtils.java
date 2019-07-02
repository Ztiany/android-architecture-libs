package com.android.base.utils.common;

import android.util.Log;

import java.io.File;

/**
 * @author Ztiany
 * Email: 1169654504@qq.com
 * Date : 2016-11-30 16:29
 */
public class FileUtils {

    private static final String TAG = FileUtils.class.getSimpleName();

    private FileUtils() {
        throw new UnsupportedOperationException("no need instantiation");
    }

    public static boolean makeFilePath(File file) {
        if (file == null) {
            return false;
        }
        File parent = file.getParentFile();
        return parent.exists() || makeDir(parent);
    }

    private static boolean makeDir(File file) {
        return file != null && (file.exists() || file.mkdirs());
    }

    public static long sizeOfFile(File file) {
        if (file == null || !file.exists()) {
            return 0;
        }
        if (file.isDirectory()) {
            long length = 0;
            for (File subFile : file.listFiles()) {
                length += sizeOfFile(subFile);
            }
            return length;
        } else {
            return file.length();
        }
    }

    public static void deleteFile(File file) {
        if (file.isDirectory()) {
            for (File subFile : file.listFiles()) {
                deleteFile(subFile);
            }
        } else {
            boolean delete = file.delete();
            Log.d(TAG, "delete:" + delete);
        }
    }

    public static boolean isFileExists(File file) {
        return file != null && file.isFile() && file.exists();
    }

    /**
     * 获取全路径中的文件拓展名
     *
     * @param file 文件
     * @return 文件拓展名
     */
    public static String getFileExtension(File file) {
        if (file == null) return null;
        return getFileExtension(file.getPath());
    }

    /**
     * 获取全路径中的文件拓展名
     *
     * @param filePath 文件路径
     * @return 文件拓展名
     */
    public static String getFileExtension(String filePath) {
        if (StringChecker.isEmpty(filePath)) {
            return filePath;
        }
        int lastPoi = filePath.lastIndexOf('.');
        int lastSep = filePath.lastIndexOf(File.separator);
        if (lastPoi == -1 || lastSep >= lastPoi) {
            return "";
        }
        return filePath.substring(lastPoi + 1);
    }
}
