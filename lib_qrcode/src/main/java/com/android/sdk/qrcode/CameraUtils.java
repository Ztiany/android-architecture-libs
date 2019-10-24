package com.android.sdk.qrcode;

import android.content.Context;
import android.util.DisplayMetrics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import io.fotoapparat.parameter.Resolution;

public class CameraUtils {

    private static final double MAX_ASPECT_DISTORTION = 0.15;//最大比例偏差
    private static final int MIN_PREVIEW_PIXELS = 480 * 800;//小于此预览尺寸直接移除

    private static final class SizeComparator implements Comparator<Resolution> {
        @Override
        public int compare(Resolution lhs, Resolution rhs) {
            return -(rhs.width * rhs.height - lhs.width * lhs.height);
        }
    }

    public static Resolution findBestPictureSize(Context context, Collection<Resolution> collection) {
        if (collection.isEmpty()) {
            return null;
        }
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        Resolution bestPictureResolution = findBestPictureResolution(new int[]{displayMetrics.heightPixels, displayMetrics.widthPixels}, new ArrayList<>(collection));
        Debug.log("bestPictureResolution:" + bestPictureResolution);
        return bestPictureResolution;
    }

    public static Resolution findBestPreviewSize(Context context, Collection<Resolution> collection) {
        if (collection.isEmpty()) {
            return null;
        }
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        Resolution bestPreviewResolution = findBestPreviewResolution(new int[]{displayMetrics.heightPixels, displayMetrics.widthPixels}, new ArrayList<>(collection));
        Debug.log("bestPreviewResolution:" + bestPreviewResolution);
        return bestPreviewResolution;
    }

    /**
     * 找到最好的适配分辨率
     */
    private static Resolution findBestPictureResolution(int[] screenSize, List<Resolution> rawSupportedPicResolutions) {
        // 排序
        List<Resolution> sortedSupportedPicResolutions = new ArrayList<>(rawSupportedPicResolutions);
        //递增排序，重要
        Collections.sort(sortedSupportedPicResolutions, new SizeComparator());
        // 移除不符合条件的分辨率——高：宽
        float screenAspectRatio = 1.0F * screenSize[0] / screenSize[1];
        Debug.log("screenSize:" + Arrays.toString(screenSize));
        Debug.log("screenAspectRatio:" + screenAspectRatio);
        Iterator<Resolution> it = sortedSupportedPicResolutions.iterator();

        while (it.hasNext()) {
            Resolution size = it.next();
            int width = size.width;
            int height = size.height;
            // 在camera分辨率与屏幕分辨率宽高比不相等的情况下，找出差距最小的一组分辨率
            // 由于camera的分辨率是width>height，我们设置的portrait模式中，width<height
            // 因此这里要先交换然后在比较宽高比
            boolean isCandidatePortrait = width > height;
            int maybeFlippedWidth = isCandidatePortrait ? height : width;
            int maybeFlippedHeight = isCandidatePortrait ? width : height;
            float aspectRatio = 1.0F * maybeFlippedHeight / maybeFlippedWidth;
            Debug.log("maybeFlippedWidth:" + maybeFlippedWidth + " maybeFlippedHeight:" + maybeFlippedHeight);
            Debug.log("aspectRatio:" + aspectRatio);
            float distortion = Math.abs(aspectRatio - screenAspectRatio);
            if (distortion > MAX_ASPECT_DISTORTION) {//移除不满足比例的分辨率
                it.remove();
            }
        }
        if (sortedSupportedPicResolutions.isEmpty()) {
            return rawSupportedPicResolutions.get(rawSupportedPicResolutions.size() - 1);
        }
        // 如果没有找到合适的，并且还有候选的像素，对于照片，则取其中最大比例的
        return sortedSupportedPicResolutions.get(sortedSupportedPicResolutions.size() - 1);

    }

    private static Resolution findBestPreviewResolution(int[] screenSize, List<Resolution> rawSupportedSizes) {
        // 按照分辨率从大到小排序
        List<Resolution> supportedPreviewResolutions = new ArrayList<>(rawSupportedSizes);
        Collections.sort(supportedPreviewResolutions, new SizeComparator());
        // 移除不符合条件的分辨率——高：宽
        double screenAspectRatio = 1.0F * screenSize[0] / screenSize[1];

        Iterator<Resolution> it = supportedPreviewResolutions.iterator();
        Resolution size;
        while (it.hasNext()) {
            size = it.next();
            int width = size.width;
            int height = size.height;
            // 移除低于下限的分辨率
            if (width * height < MIN_PREVIEW_PIXELS) {
                it.remove();
                continue;
            }
            // 在camera分辨率与屏幕分辨率宽高比不相等的情况下，找出差距最小的一组分辨率
            // 由于camera的分辨率是width>height，我们设置的portrait模式中，width<height
            // 因此这里要先交换然preview宽高比后在比较
            boolean isCandidatePortrait = width > height;
            int maybeFlippedWidth = isCandidatePortrait ? height : width;
            int maybeFlippedHeight = isCandidatePortrait ? width : height;
            float aspectRatio = 1.0F * maybeFlippedHeight / maybeFlippedWidth;
            double distortion = Math.abs(aspectRatio - screenAspectRatio);
            if (distortion > MAX_ASPECT_DISTORTION) {//移除不符合比例的分辨率
                it.remove();
                continue;
            }
            // 找到与屏幕分辨率完全匹配的预览界面分辨率直接返回
            if (maybeFlippedWidth == screenSize[0] && maybeFlippedHeight == screenSize[1]) {
                return size;
            }
        }
        if (supportedPreviewResolutions.isEmpty()) {
            return rawSupportedSizes.get(rawSupportedSizes.size() - 1);
        }
        // 如果没有找到最合适的
        return supportedPreviewResolutions.get(supportedPreviewResolutions.size() - 1);
    }

}