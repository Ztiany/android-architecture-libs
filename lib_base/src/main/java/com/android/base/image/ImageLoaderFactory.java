package com.android.base.image;

/**
 * <pre>
 *
 * </pre>
 *
 * @author Ztiany
 *         Email: 1169654504@qq.com
 *         Date : 2017-03-27 18:09
 */

public class ImageLoaderFactory {

    private static final GlideImageLoader IMAGE_LOADER = new GlideImageLoader();

    public static ImageLoader getImageLoader() {
        return IMAGE_LOADER;
    }

}
