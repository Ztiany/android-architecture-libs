package com.app.base.upgrade

import android.content.Context
import com.android.base.utils.upgrade.UpgradeInfo
import io.reactivex.Flowable
import java.io.File

/**
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2019-10-29 19:16
 */
interface UpgradeInteractor {

    fun checkUpgrade(): Flowable<UpgradeInfo>

    fun showUpgradeDialog(context: Context, upgradeInfo: UpgradeInfo, onCancel: () -> Unit, onConfirm: () -> Unit)

    fun showInstallTipsDialog(context: Context, force: Boolean, onCancel: () -> Unit, onConfirm: () -> Unit)

    fun showDownloadingFailed(context: Context, force: Boolean, onCancel: () -> Unit, onConfirm: () -> Unit)

    fun showDownloadingDialog(context: Context, force: Boolean)

    fun dismissDownloadingDialog()

    fun onProgress(total: Long, progress: Long)

    fun installApk(file: File, upgradeInfo: UpgradeInfo)

    fun checkApkFile(apkFile: File, digitalAbstract: String): Boolean

    fun generateAppDownloadPath(versionName: String): String

}