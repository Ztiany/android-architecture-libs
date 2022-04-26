package com.android.sdk.upgrade;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;


/**
 * 下载APK
 *
 * @author Ztiany
 * Date : 2017-01-04 10:40
 */
final class ApkDownloader {

    private final String mUrl;
    private final String mDigitalAbstract;

    private final ApkDownloaderListener mApkDownloaderListener;
    private final UpgradeInteractor mUpgradeInteractor;

    private final File mDesFile;
    private final File mTempDesFile;

    private static final String TEMP_MASK = "temp_%s";
    private long mNotifyTime = 0;

    ApkDownloader(String url, String desFilePath, String digitalAbstract, ApkDownloaderListener apkDownloaderListener) {
        mUrl = url;
        mDigitalAbstract = digitalAbstract;
        mApkDownloaderListener = apkDownloaderListener;
        mUpgradeInteractor = AppUpgradeChecker.INSTANCE.getUpgradeInteractor();
        mDesFile = new File(desFilePath);
        String name = mDesFile.getName();
        mTempDesFile = new File(mDesFile.getParent(), String.format(TEMP_MASK, name));
    }

    void start() {
        OkHttpClient okHttpClient = mUpgradeInteractor.createHttpClient();
        Request request = new Request.Builder().url(mUrl).build();

        ResponseBody body;

        try {
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                body = response.body();
            } else {
                mApkDownloaderListener.onFail(new UpgradeException(UpgradeException.NETWORK_ERROR));
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
            mApkDownloaderListener.onFail(new UpgradeException(UpgradeException.NETWORK_ERROR));
            return;
        }

        //检查body
        if (body == null) {
            mApkDownloaderListener.onFail(new UpgradeException(UpgradeException.NETWORK_ERROR));
            return;
        }

        if (mDesFile.exists() && mDesFile.length() == body.contentLength() && mUpgradeInteractor.checkApkFile(mDesFile, mDigitalAbstract)) {
            mApkDownloaderListener.onSuccess(mDesFile);
        } else {
            InternalUtils.makeParentPath(mDesFile);
            InternalUtils.makeParentPath(mTempDesFile);
            boolean success = startDownload(body);
            if (success) {
                boolean renameToSuccess = mTempDesFile.renameTo(mDesFile);
                if (renameToSuccess) {
                    if (mUpgradeInteractor.checkApkFile(mDesFile, mDigitalAbstract)) {
                        mApkDownloaderListener.onSuccess(mDesFile);
                    } else {
                        mApkDownloaderListener.onFail(new UpgradeException(UpgradeException.DIGITS_COMPARING_ERROR));
                    }
                }
            }
        }
    }

    private boolean startDownload(ResponseBody body) {
        InputStream is = null;
        FileOutputStream fos = null;

        try {
            byte[] buf = new byte[1024 * 2];//缓冲器
            int len;//每次读取的长度
            assert body != null;
            final long total = body.contentLength();//总的长度
            is = body.byteStream();
            long sum = 0;
            fos = new FileOutputStream(mTempDesFile);

            while ((len = is.read(buf)) != -1) {
                sum += len;
                fos.write(buf, 0, len);
                if ((System.currentTimeMillis() - mNotifyTime) >= 300) {
                    mNotifyTime = System.currentTimeMillis();
                    mApkDownloaderListener.onProgress(total, sum);
                }
            }

            mApkDownloaderListener.onProgress(total, total);
            fos.flush();

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            mApkDownloaderListener.onFail(new UpgradeException(UpgradeException.NETWORK_ERROR));
            return false;
        } finally {
            InternalUtils.closeIOQuietly(is, fos);
        }
    }

    interface ApkDownloaderListener {

        void onProgress(long total, long progress);

        void onSuccess(File desFile);

        void onFail(UpgradeException e);

    }

}