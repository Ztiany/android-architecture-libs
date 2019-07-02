package com.android.sdk.qrcode;

import android.os.AsyncTask;

import io.fotoapparat.parameter.Resolution;


class ProcessDataTask extends AsyncTask<Void, Void, String> {

    private final Resolution mSize;
    private byte[] mData;
    private Delegate mDelegate;

    ProcessDataTask(byte[] data, Resolution size, @SuppressWarnings("unused") int rotation, Delegate delegate) {
        mData = data;
        mSize = size;
        mDelegate = delegate;
    }

    ProcessDataTask perform() {
        executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        return this;
    }

    void cancelTask() {
        if (getStatus() != Status.FINISHED) {
            cancel(true);
        }
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        mDelegate = null;
    }

    //https://stackoverflow.com/questions/16252791/zxing-camera-in-portrait-mode-on-android
    @Override
    protected String doInBackground(Void... params) {
        int width = mSize.width;
        int height = mSize.height;
        byte[] rotatedData = new byte[mData.length];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                rotatedData[x * height + height - y - 1] = mData[x + y * width];
            }
        }
        int tmp = width;
        width = height;
        height = tmp;
        try {
            if (mDelegate == null) {
                return null;
            }
            return mDelegate.processData(rotatedData, width, height);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public interface Delegate {
        String processData(byte[] data, int width, int height);
    }

}
