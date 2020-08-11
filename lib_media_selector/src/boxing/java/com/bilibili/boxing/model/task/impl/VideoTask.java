/*
 *  Copyright (C) 2017 Bilibili
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.bilibili.boxing.model.task.impl;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;

import com.bilibili.boxing.model.callback.IMediaTaskCallback;
import com.bilibili.boxing.model.entity.impl.VideoMedia;
import com.bilibili.boxing.model.task.IMediaTask;
import com.bilibili.boxing.utils.BoxingExecutor;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.WorkerThread;

/**
 * A Task to load {@link VideoMedia} in database.
 *
 * @author ChenSL
 */
@WorkerThread
public class VideoTask implements IMediaTask<VideoMedia> {

    @Override
    public void load(final ContentResolver cr, final int page, String id, final IMediaTaskCallback<VideoMedia> callback) {
        loadVideosVersionChecked(cr, page, callback);
    }

    private void loadVideosVersionChecked(ContentResolver cr, int page, @NonNull final IMediaTaskCallback<VideoMedia> callback) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            loadVideosBelowAndroidQ(cr, page, callback);
        } else {
              loadVideosAboveAndroidQ(cr, page, callback);
        }
    }

    private void loadVideosBelowAndroidQ(ContentResolver cr, int page, @NonNull final IMediaTaskCallback<VideoMedia> callback) {
        final List<VideoMedia> videoMedias = new ArrayList<>();

        String[] mediaCol = new String[]{
                MediaStore.Video.Media.DATA,
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.TITLE,
                MediaStore.Video.Media.MIME_TYPE,
                MediaStore.Video.Media.SIZE,
                MediaStore.Video.Media.DATE_TAKEN,
                MediaStore.Video.Media.DURATION
        };

        try (Cursor cursor = cr.query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                mediaCol,
                null, null,
                MediaStore.Images.Media.DATE_MODIFIED + " desc" + " LIMIT " + page * IMediaTask.PAGE_LIMIT + " , " + IMediaTask.PAGE_LIMIT)) {

            int count;

            if (cursor != null && cursor.moveToFirst()) {
                count = cursor.getCount();
                do {
                    String data = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
                    String id = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media._ID));
                    String title = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.TITLE));
                    String type = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.MIME_TYPE));
                    String size = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.SIZE));
                    String date = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATE_TAKEN));
                    String duration = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DURATION));

                    VideoMedia video = new VideoMedia.Builder(id, Uri.fromFile(new File(data)))
                            .setTitle(title)
                            .setDuration(duration)
                            .setSize(size)
                            .setDataTaken(date)
                            .setMimeType(type)
                            .build();

                    videoMedias.add(video);

                } while (!cursor.isLast() && cursor.moveToNext());
                postMedias(callback, videoMedias, count);
            } else {
                postMedias(callback, videoMedias, 0);
            }
        }
    }


    private void loadVideosAboveAndroidQ(ContentResolver cr, int page, IMediaTaskCallback<VideoMedia> callback) {
        final List<VideoMedia> videoMedias = new ArrayList<>();

        String[] mediaCol = new String[]{
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.TITLE,
                MediaStore.Video.Media.MIME_TYPE,
                MediaStore.Video.Media.SIZE,
                MediaStore.Video.Media.DATE_TAKEN,
                MediaStore.Video.Media.DURATION
        };

        try (Cursor cursor = cr.query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                mediaCol,
                null, null,
                MediaStore.Images.Media.DATE_MODIFIED + " desc" + " LIMIT " + page * IMediaTask.PAGE_LIMIT + " , " + IMediaTask.PAGE_LIMIT)) {

            int count;

            if (cursor != null && cursor.moveToFirst()) {
                count = cursor.getCount();
                do {
                    String id = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media._ID));
                    String title = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.TITLE));
                    String type = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.MIME_TYPE));
                    String size = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.SIZE));
                    String date = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATE_TAKEN));
                    String duration = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DURATION));
                    Uri uri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, Integer.parseInt(id));

                    VideoMedia video = new VideoMedia.Builder(id, uri)
                            .setTitle(title)
                            .setDuration(duration)
                            .setSize(size)
                            .setDataTaken(date)
                            .setMimeType(type)
                            .build();

                    videoMedias.add(video);

                } while (!cursor.isLast() && cursor.moveToNext());
                postMedias(callback, videoMedias, count);
            } else {
                postMedias(callback, videoMedias, 0);
            }
        }
    }


    private void postMedias(
            @NonNull final IMediaTaskCallback<VideoMedia> callback,
            final List<VideoMedia> videoMedias,
            final int count) {
        BoxingExecutor.getInstance().runUI(() -> callback.postMedia(videoMedias, count));
    }

}