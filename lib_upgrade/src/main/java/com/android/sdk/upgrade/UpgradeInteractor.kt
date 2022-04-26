package com.android.sdk.upgrade

import android.content.Context
import androidx.core.app.NotificationCompat
import okhttp3.OkHttpClient
import java.io.File

/**
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2019-10-29 19:16
 */
interface UpgradeInteractor {

    /**when this method called, you should to ask server if there is a new version.*/
    fun checkUpgrade(
        onStart: (() -> Unit),
        onError: ((Throwable) -> Unit),
        onSuccess: ((UpgradeInfo) -> Unit)
    )

    /**a dialog used to show information about information of new apk.*/
    fun showUpgradeDialog(
        context: Context,
        upgradeInfo: UpgradeInfo,
        onCancel: () -> Unit,
        onConfirm: () -> Unit
    )

    /**a dialog used to ask user to install the downloaded apk.*/
    fun showInstallTipsDialog(
        context: Context,
        forceUpgrade: Boolean,
        onCancel: () -> Unit,
        onConfirm: () -> Unit
    )

    /**a loading dialog used to notice the user the downloading is failed*/
    fun showDownloadingFailed(
        context: Context,
        forceUpgrade: Boolean,
        error: UpgradeException,
        onCancel: () -> Unit,
        onConfirm: () -> Unit
    )

    /**a loading dialog used to notice the user it is downloading the new apk.*/
    fun showDownloadingDialog(context: Context, forceUpgrade: Boolean)

    /**dismiss the loading shown by [showDownloadingDialog]*/
    fun dismissDownloadingDialog()

    /**notify the progress of downloading. you may want to show a notification. note: the [total] may always be -1, if so it is better to show an indeterminate progress by calling [NotificationCompat.Builder.setProgress].*/
    fun onProgress(total: Long, progress: Long)

    /** downloading is finished, then perform installing.*/
    fun installApk(file: File, upgradeInfo: UpgradeInfo)

    /**compare the digital abstract, like MD5.*/
    fun checkApkFile(apkFile: File, digitalAbstract: String): Boolean

    /** make sure the app has the permission to write the path you give. */
    fun generateAppDownloadPath(versionName: String): String

    fun createHttpClient(): OkHttpClient

}