package com.android.sdk.upgrade

import android.app.IntentService
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import timber.log.Timber
import java.io.File

/**
 * 下载服务，不要配置在子进程。
 */
class UpgradeService : IntentService("UpgradeService") {

    private lateinit var handler: Handler

    private inner class FlagDownloaderListener : ApkDownloader.ApkDownloaderListener {

        override fun onProgress(total: Long, progress: Long) {
            Timber.d("onProgress() called with: total = [$total], progress = [$progress]")
            handler.post { notifyDownloadProgress(total, progress) }
        }

        override fun onSuccess(desFile: File) {
            Timber.d("onSuccess() called with: desFile = [$desFile]")
            handler.post { notifyDownloadSucceeded(desFile) }
        }

        override fun onFail(e: UpgradeException) {
            Timber.d("onFail() called with: e = [$e]")
            handler.post { notifyDownloadFailed(e) }
        }

    }

    override fun onHandleIntent(intent: Intent?) {
        //get params
        intent ?: return
        val url = intent.getStringExtra(URL_KEY) ?: return
        val versionName = intent.getStringExtra(VERSION_KEY) ?: return
        val digitalAbstract = intent.getStringExtra(DIGITAL_ABSTRACT_KEY) ?: ""
        //start downloading
        ApkDownloader(
            url,
            AppUpgradeChecker.upgradeInteractor.generateAppDownloadPath(versionName),
            digitalAbstract,
            FlagDownloaderListener()
        ).start()
    }

    override fun onCreate() {
        super.onCreate()
        handler = Handler(Looper.getMainLooper())
    }

    private fun notifyDownloadFailed(e: UpgradeException) {
        AppUpgradeChecker.onDownloadingFailed(e)
    }

    private fun notifyDownloadSucceeded(desFile: File) {
        AppUpgradeChecker.onDownloadingSucceeded(desFile)
    }

    private fun notifyDownloadProgress(total: Long, progress: Long) {
        AppUpgradeChecker.onDownloadingProgress(total, progress)
    }

    companion object {

        const val URL_KEY = "url_key"
        const val VERSION_KEY = "version_key"
        const val DIGITAL_ABSTRACT_KEY = "digital_abstract_key"

        fun start(context: Context, updateUrl: String?, versionName: String?, digitalAbstract: String?) {
            if (isEmpty(updateUrl) || isEmpty(versionName)) {
                return
            }
            val intent = Intent(context, UpgradeService::class.java)
            intent.putExtra(URL_KEY, updateUrl)
            intent.putExtra(VERSION_KEY, versionName)
            intent.putExtra(DIGITAL_ABSTRACT_KEY, digitalAbstract)
            context.startService(intent)
        }
    }

}