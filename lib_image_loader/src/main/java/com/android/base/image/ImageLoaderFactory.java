package com.android.base.image;

/**
 * @author Ztiany
 */
public class ImageLoaderFactory {

    private static final GlideImageLoader IMAGE_LOADER = new GlideImageLoader();

    public static ImageLoader getImageLoader() {
        return IMAGE_LOADER;
    }

}
