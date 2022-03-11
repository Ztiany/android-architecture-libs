package com.android.base.image;

/**
 * <pre>
 *
 * </pre>
 *
 * @author Ztiany
 * Date : 2017-03-27 18:09
 */

public class ImageLoaderFactory {

    private static final GlideImageLoader IMAGE_LOADER = new GlideImageLoader();

    public static ImageLoader getImageLoader() {
        return IMAGE_LOADER;
    }

}
