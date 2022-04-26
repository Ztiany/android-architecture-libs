package com.android.sdk.upgrade

import android.annotation.SuppressLint
import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.base.utils.BaseUtils
import com.android.base.utils.android.AppUtils
import com.android.base.utils.android.ServiceUtils
import java.io.File

/**
 * A tool for checking app upgrade and downloading new app to install.
 *
 * usage, when automatic check new version:
 *
 * ```
 * AppUpgradeChecker.checkAppUpgrade()
 * ```
 *
 * usage, when click to check new version:
 *
 * ```
 *  AppUpgradeChecker.checkAppUpgrade(false).observe(this) {
 *                       if (it.isDownloading) {
 *                           showMessage("正在下载更新")
 *                           return@observe
 *                       }
 *
 *                       if (it.isLoading) {
 *                           showLoadingDialog(false)
 *                       } else {
 *                           dismissLoadingDialog()
 *                       }
 *
 *                       val error = it.error
 *                       if (error != null) {
 *                           handleError(error)
 *                       }
 *
 *                       val upgradeInfo = it.upgradeInfo
 *                       if (upgradeInfo != null && !upgradeInfo.isNewVersion) {
 *                           showMessage("已是最新版本")
 *                       }
 *                   }
 * ```
 */
object AppUpgradeChecker {

    private var succeededOnce = false

    private val isUpgradeServiceRunning: Boolean
        get() = ServiceUtils.isServiceRunning(UpgradeService::class.java.name)

    private lateinit var upgradeInfo: UpgradeInfo

    private var internalUpgradeInteractor: UpgradeInteractor? = null

    val upgradeInteractor: UpgradeInteractor
        get() {
            if (internalUpgradeInteractor == null) {
                throw NullPointerException("You have to set UpgradeInteractor first")
            }
            return internalUpgradeInteractor as UpgradeInteractor
        }

    fun installInteractor(upgradeInteractor: UpgradeInteractor) {
        internalUpgradeInteractor = upgradeInteractor
    }

    @SuppressLint("CheckResult")
    fun checkAppUpgrade(
        justOnce: Boolean = true
    ): LiveData<CheckingState> {
        val liveData = MutableLiveData<CheckingState>()

        /*已经检查过了*/
        if (justOnce && succeededOnce) {
            return liveData
        }

        /*正在下载*/
        if (isUpgradeServiceRunning) {
            liveData.postValue(CheckingState(isDownloading = true))
            return liveData
        }

        /*执行检测*/
        upgradeInteractor.checkUpgrade(
            onStart = {
                liveData.postValue(CheckingState(isLoading = true))
            },
            onError = {
                liveData.postValue(CheckingState(error = it))
            },
            onSuccess = {
                succeededOnce = true
                processUpdateInfo(it)
                liveData.postValue(CheckingState(upgradeInfo = it))
            }
        )

        return liveData
    }

    private fun processUpdateInfo(upgradeInfo: UpgradeInfo) {
        if (isUpgradeServiceRunning) {
            return
        }

        if (upgradeInfo.isNewVersion) {
            AppUpgradeChecker.upgradeInfo = upgradeInfo
            safeContext {
                upgradeInteractor.showUpgradeDialog(it, upgradeInfo,
                    onCancel = {
                        //do nothing
                    },
                    onConfirm = {
                        if (!isUpgradeServiceRunning) {
                            doUpdate()
                        }
                    }
                )
            }
        }
    }

    private fun doUpdate() {
        //show loading dialog
        safeContext {
            upgradeInteractor.showDownloadingDialog(it, upgradeInfo.isForce)
        }
        //start downloading
        UpgradeService.start(
            BaseUtils.getAppContext(),
            upgradeInfo.downloadUrl,
            upgradeInfo.versionName,
            upgradeInfo.digitalAbstract
        )
    }

    private fun processOnDownloadingFileSuccessful(file: File?) {
        //start installing
        val apkFile = file ?: return
        startInstall(apkFile)
        //dismiss download dialog
        upgradeInteractor.dismissDownloadingDialog()
        // if it is force upgrade, we show a no cancelable dialog to make user have to install the new apk.
        safeContext {
            showInstallTipsDialog(it, apkFile)
        }
    }

    private fun showInstallTipsDialog(topActivity: Activity, apkFile: File) {
        upgradeInteractor.showInstallTipsDialog(topActivity, upgradeInfo.isForce,
            onCancel = {
                // no op
            },
            onConfirm = {
                startInstall(apkFile)
            })
    }

    private fun processOnDownloadingFileFailed(e: UpgradeException) {
        upgradeInteractor.dismissDownloadingDialog()
        safeContext {
            showDownloadingFailedTips(it, e, upgradeInfo.isForce)
        }
    }

    private fun showDownloadingFailedTips(
        safeContext: Activity,
        error: UpgradeException,
        isForce: Boolean
    ) {
        upgradeInteractor.showDownloadingFailed(safeContext, isForce, error,
            onConfirm = {
                doUpdate()
            },
            onCancel = {
                // no op
            })
    }

    private fun safeContext(onContext: (Activity) -> Unit) {
        val topActivity: Activity? = AppUtils.getTopActivity()
        if (topActivity != null) {
            onContext(topActivity)
        } else {
            AppUtils.addOnAppStatusChangedListener(object : AppUtils.OnAppStatusChangedListener {

                override fun onBackground(activity: Activity?) = Unit

                override fun onForeground(activity: Activity?) {
                    AppUtils.removeOnAppStatusChangedListener(this)
                    AppUtils.getTopActivity()?.let { onContext(it) }
                }
            })
        }
    }

    private fun startInstall(apkFile: File) {
        upgradeInteractor.installApk(apkFile, upgradeInfo)
    }

    internal fun onDownloadingProgress(total: Long, progress: Long) {
        upgradeInteractor.onProgress(total, progress)
    }

    internal fun onDownloadingFailed(e: UpgradeException) {
        processOnDownloadingFileFailed(e)
    }

    internal fun onDownloadingSucceeded(desFile: File) {
        processOnDownloadingFileSuccessful(desFile)
    }

}