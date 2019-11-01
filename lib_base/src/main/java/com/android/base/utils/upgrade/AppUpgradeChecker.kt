package com.android.base.utils.upgrade

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.android.base.app.Sword
import com.android.base.data.State
import com.android.base.rx.observeOnUI
import com.android.base.utils.BaseUtils
import com.android.base.utils.android.XAppUtils
import com.app.base.upgrade.UpgradeInteractor
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.Utils
import timber.log.Timber
import java.io.File

/**
 * A tool for checking app upgrade.
 *
 * usage, when automatic check new version:
 *
 * ```
 *       AppUpgradeChecker.checkAppUpgrade()
 * ```
 *
 * usage, when click to check new version:
 * ```
 *       if (AppUpgradeChecker.isDownloading) {
 *          showMessage("正在下载更新")
 *          return@setOnClickListener
 *       }
 *
 *       AppUpgradeChecker.checkAppUpgrade(false)
 *
 *       if (!subscribed) {
 *          subscribeUpgradeInfo()
 *          subscribed = true
 *      }
 * ```
 */
object AppUpgradeChecker {

    private var successOnce = false

    var isDownloading = false
        private set

    val isChecking: Boolean
        get() = currentState.isLoading

    private var currentState = State.noChange<UpgradeInfo>()
        set(value) {
            field = value
            notifyStateChanged(value)
        }

    private fun notifyStateChanged(state: State<UpgradeInfo>) {
        innerLiveState.value = state
    }

    private val innerLiveState = MutableLiveData<State<UpgradeInfo>>()

    val upgradeState: LiveData<State<UpgradeInfo>>
        get() = innerLiveState

    private var resultReceiver: ResultBroadcastReceiver? = null

    private val isUpgradeServiceRunning: Boolean
        get() = XAppUtils.isServiceRunning(BaseUtils.getAppContext(), UpgradeService::class.java.name)

    private lateinit var upgradeInfo: UpgradeInfo

    lateinit var upgradeInteractor: UpgradeInteractor

    fun installInteractor(upgradeInteractor: UpgradeInteractor) {
        AppUpgradeChecker.upgradeInteractor = upgradeInteractor
    }

    fun checkAppUpgrade(silence: Boolean = true) {
        Timber.d("checkAppUpgrade-->currentState == $currentState")
        /*正在检查*/
        if (currentState.isLoading) {
            return
        }
        /*已经检查过了*/
        if (silence && successOnce) {
            return
        }
        /*正在下载*/
        if (isUpgradeServiceRunning) {
            return
        }
        realCheck()
    }

    private fun realCheck() {
        currentState = State.loading()

        upgradeInteractor.checkUpgrade()
                .observeOnUI()
                .subscribe(
                        { upgradeInfo ->
                            successOnce = true
                            processUpdateInfo(upgradeInfo)
                        },
                        {
                            currentState = State.error(it)
                        }
                )
    }

    private fun processUpdateInfo(upgradeInfo: UpgradeInfo) {
        currentState = State.success(upgradeInfo)
        if (upgradeInfo.isNewVersion) {
            AppUpgradeChecker.upgradeInfo = upgradeInfo
            safeContext {
                upgradeInteractor.showUpgradeDialog(it, upgradeInfo,
                        onCancel = {
                            //do nothing
                        },
                        onConfirm = {
                            doUpdate()
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
        //register result receiver
        var receiver = resultReceiver
        if (receiver == null) {
            receiver = ResultBroadcastReceiver()
            resultReceiver = receiver
            LocalBroadcastManager.getInstance(BaseUtils.getAppContext()).registerReceiver(receiver, IntentFilter(UpgradeService.DOWNLOAD_APK_RESULT_ACTION))
        }
        //start downloading
        isDownloading = true
        UpgradeService.start(BaseUtils.getAppContext(), upgradeInfo.downloadUrl, upgradeInfo.versionName, upgradeInfo.digitalAbstract, upgradeInfo.isForce)
    }

    private class ResultBroadcastReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action != UpgradeService.DOWNLOAD_APK_RESULT_ACTION) {
                return
            }
            when (intent.getIntExtra(UpgradeService.NOTIFY_TYPE_KEY, -1)) {
                UpgradeService.NOTIFY_TYPE_SUCCESS -> processOnDownloadingFileSuccessful(intent)
                UpgradeService.NOTIFY_TYPE_FAILED -> processOnDownloadingFileFailed()
                UpgradeService.NOTIFY_TYPE_PROGRESS -> {
                    val total = intent.getLongExtra(UpgradeService.TOTAL_PROGRESS_KEY, 0)
                    val progress = intent.getLongExtra(UpgradeService.PROGRESS_KEY, 0)
                    upgradeInteractor.onProgress(total, progress)
                }
            }
        }
    }

    private fun processOnDownloadingFileSuccessful(intent: Intent) {
        isDownloading = false
        //start installing
        val apkFile = intent.getSerializableExtra(UpgradeService.APK_FILE_KEY) as File
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
                    currentState = State.success()
                },
                onConfirm = {
                    startInstall(apkFile)
                })
    }

    private fun processOnDownloadingFileFailed() {
        isDownloading = false
        upgradeInteractor.dismissDownloadingDialog()
        safeContext {
            showDownloadingFailedTips(it, upgradeInfo.isForce)
        }
    }

    private fun showDownloadingFailedTips(safeContext: Activity, isForce: Boolean) {
        upgradeInteractor.showDownloadingFailed(safeContext, isForce,
                onConfirm = {
                    doUpdate()
                },
                onCancel = {
                    currentState = State.success()
                })
    }

    private fun safeContext(onContext: (Activity) -> Unit) {
        val topActivity = Sword.topActivity
        if (topActivity != null) {
            onContext(topActivity)
        } else {
            AppUtils.registerAppStatusChangedListener(this, object : Utils.OnAppStatusChangedListener {
                override fun onBackground() = Unit
                override fun onForeground() {
                    AppUtils.unregisterAppStatusChangedListener(this)
                    Sword.topActivity?.let { onContext(it) }
                }
            })
        }
    }

    private fun startInstall(apkFile: File) {
        upgradeInteractor.installApk(apkFile, upgradeInfo)
    }

}