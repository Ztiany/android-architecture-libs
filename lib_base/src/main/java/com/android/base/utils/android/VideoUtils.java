package com.android.base.utils.android;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;

import androidx.annotation.WorkerThread;

public class VideoUtils {

    private VideoUtils() {
    }

    @WorkerThread
    public static Bitmap createVideoThumbnail(final Context context, final Uri uri) {
        Bitmap bitmap;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(context, uri);
        bitmap = retriever.getFrameAtTime();
        retriever.release();
        return bitmap;
    }

}
