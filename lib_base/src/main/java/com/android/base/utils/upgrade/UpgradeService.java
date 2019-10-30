package com.android.base.utils.upgrade;


import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.android.base.utils.BaseUtils;
import com.android.base.utils.common.Strings;

import java.io.File;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import timber.log.Timber;

/**
 * 下载服务，不要配置在子进程。
 */
public class UpgradeService extends IntentService {

    static final String DOWNLOAD_APK_RESULT_ACTION = BaseUtils.getAppContext().getPackageName() + ".upgrade.result";

    static final String IS_FORCE = "is_force";
    static final String NOTIFY_TYPE_KEY = "notify_type";
    static final String APK_FILE_KEY = "apk_path_key";
    static final String URL_KEY = "url_key";
    static final String VERSION_KEY = "version_key";
    static final String TOTAL_PROGRESS_KEY = "total_progress_key";
    static final String PROGRESS_KEY = "progress_key";
    static final String DIGITAL_ABSTRACT_KEY = "digital_abstract_key";

    public static final int NOTIFY_TYPE_FAILED = 1;
    public static final int NOTIFY_TYPE_PROGRESS = 2;
    public static final int NOTIFY_TYPE_SUCCESS = 3;

    private Handler mHandler;
    private boolean mIsForceUpdate;
    private String mDigitalAbstract;

    private class FlagDownloaderListener implements ApkDownloader.ApkDownloaderListener {

        @Override
        public void onProgress(long total, long progress) {
            Timber.d("onProgress() called with: total = [" + total + "], progress = [" + progress + "]");
            mHandler.post(() -> notifyDownloadResult(NOTIFY_TYPE_PROGRESS, null, total, progress));
        }

        @Override
        public void onSuccess(File desFile) {
            Timber.d("onSuccess() called with: desFile = [" + desFile + "]");
            mHandler.post(() -> notifyDownloadResult(NOTIFY_TYPE_SUCCESS, desFile, 0, 0));
        }

        @Override
        public void onFail(Exception e) {
            Timber.d("onFail() called with: e = [" + e + "]");
            mHandler.post(() -> notifyDownloadResult(NOTIFY_TYPE_FAILED, null, 0, 0));
        }
    }

    public UpgradeService() {
        super("GW-UpgradeService");
    }

    public static void start(Context context, String updateUrl, String versionName, String digitalAbstract, boolean isForceUpdate) {
        if (Strings.isEmpty(updateUrl) || Strings.isEmpty(versionName)) {
            return;
        }
        Intent intent = new Intent(context, UpgradeService.class);
        intent.putExtra(URL_KEY, updateUrl);
        intent.putExtra(IS_FORCE, isForceUpdate);
        intent.putExtra(VERSION_KEY, versionName);
        intent.putExtra(DIGITAL_ABSTRACT_KEY, digitalAbstract);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String url = intent.getStringExtra(URL_KEY);
        String versionName = intent.getStringExtra(VERSION_KEY);
        mIsForceUpdate = intent.getBooleanExtra(IS_FORCE, false);
        mDigitalAbstract = intent.getStringExtra(DIGITAL_ABSTRACT_KEY);
        if (!TextUtils.isEmpty(url) && !TextUtils.isEmpty(versionName)) {
            new ApkDownloader(url, AppUpgradeChecker.upgradeInteractor.generateAppDownloadPath(versionName), mDigitalAbstract, new FlagDownloaderListener()).start();
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mHandler = new Handler(Looper.getMainLooper());
    }

    private void notifyDownloadResult(int notifyType, File desFile, long total, long progress) {
        Intent intent = new Intent();
        intent.setAction(DOWNLOAD_APK_RESULT_ACTION);
        intent.putExtra(IS_FORCE, mIsForceUpdate);
        intent.putExtra(NOTIFY_TYPE_KEY, notifyType);
        intent.putExtra(TOTAL_PROGRESS_KEY, total);
        intent.putExtra(PROGRESS_KEY, progress);
        if (desFile != null) {
            intent.putExtra(APK_FILE_KEY, desFile);
        }
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

}