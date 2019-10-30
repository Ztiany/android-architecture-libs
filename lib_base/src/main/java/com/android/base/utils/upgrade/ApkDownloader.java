package com.android.base.utils.upgrade;

import com.android.base.utils.common.Files;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static com.blankj.utilcode.util.CloseUtils.closeIOQuietly;

/**
 * 下载APK
 *
 * @author Ztiany
 * Email: 1169654504@qq.com
 * Date : 2017-01-04 10:40
 */
final class ApkDownloader {

    private String mUrl;
    private String mDigitalAbstract;
    private ApkDownloaderListener mApkDownloaderListener;
    private File mDesFile;
    private File mTempDesFile;
    private static final String TEMP_MASK = "temp_%s";
    private long mNotifyTime = 0;


    ApkDownloader(String url, String desFilePath, String digitalAbstract, ApkDownloaderListener apkDownloaderListener) {
        mUrl = url;
        mDigitalAbstract = digitalAbstract;
        mApkDownloaderListener = apkDownloaderListener;
        mDesFile = new File(desFilePath);
        String name = mDesFile.getName();
        mTempDesFile = new File(mDesFile.getParent(), String.format(TEMP_MASK, name));
    }

    void start() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.interceptors().clear();
        OkHttpClient okHttpClient = builder.build();

        Request request = new Request.Builder()
                .url(mUrl)
                .build();

        ResponseBody body;

        try {
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                body = response.body();
            } else {
                mApkDownloaderListener.onFail(new RuntimeException("网络错误"));
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
            mApkDownloaderListener.onFail(e);
            return;
        }

        //检查body
        if (body == null) {
            mApkDownloaderListener.onFail(new RuntimeException("网络错误"));
            return;
        }

        if (mDesFile.exists() && mDesFile.length() == body.contentLength() && AppUpgradeChecker.upgradeInteractor.checkApkFile(mDesFile, mDigitalAbstract)) {
            mApkDownloaderListener.onSuccess(mDesFile);
        } else {
            Files.makeParentPath(mDesFile);
            Files.makeParentPath(mTempDesFile);
            boolean success = startDownload(body);
            if (success) {
                boolean renameToSuccess = mTempDesFile.renameTo(mDesFile);
                if (renameToSuccess) {
                    if (AppUpgradeChecker.upgradeInteractor.checkApkFile(mDesFile, mDigitalAbstract)) {
                        mApkDownloaderListener.onSuccess(mDesFile);
                    } else {
                        mApkDownloaderListener.onFail(new RuntimeException("数字摘要对比失败"));
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
            mApkDownloaderListener.onFail(e);
            return false;
        } finally {
            closeIOQuietly(is, fos);
        }
    }

    interface ApkDownloaderListener {

        void onProgress(long total, long progress);

        void onSuccess(File desFile);

        void onFail(Exception e);

    }

}