package com.android.base.image;

/**
 * @author Ztiany
 * Date : 2018-08-20 10:40
 */
public interface ProgressListener {

    void onProgress(String url, ProgressInfo progressInfo);

    default void onError(long id, String url, Throwable throwable){}

}
