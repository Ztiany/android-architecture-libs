# A library for checking app upgrade

usage: 

```kotlin
AppUpgradeChecker.installInteractor(AppUpgradeInteractor())

//usage, when automatic check new version:

AppUpgradeChecker.checkAppUpgrade()

//usage, when click to check new version:

AppUpgradeChecker.checkAppUpgrade(false).observe(this) {
                     if (it.isDownloading) {
                            showMessage("正在下载更新")
                            return@observe
                     }
                     if (it.isLoading) {
                            showLoadingDialog(false)
                     } else {
                            dismissLoadingDialog()
                     }
                     val error = it.error
                     if (error != null) {
                            handleError(error)
                     }
                     val upgradeInfo = it.upgradeInfo
                     if (upgradeInfo != null && !upgradeInfo.isNewVersion) {
                            showMessage("已是最新版本")
                     }
                 }
```

example of AppUpgradeInteractor implementation:

```kotlin
class AppUpgradeInteractor : UpgradeInteractor {

    private var loadingDialogReference: WeakReference<Dialog>? = null
    private var upgradeDialogReference: WeakReference<Dialog>? = null

    private val notificationHelper by lazy { NotificationHelper() }

    private fun newLoadingDialogIfNeed(context: Context): Dialog {
        loadingDialogReference?.get()?.dismiss()
        val dialog = LoadingDialog(context)
        loadingDialogReference = WeakReference(dialog)
        return dialog
    }

    override fun checkUpgrade(): Flowable<UpgradeInfo> {
        return AppContext.commonService().checkAppUpgrade()
                .map { buildUpgradeInfo(it) }
    }

    private fun buildUpgradeInfo(response: UpgradeResponse): UpgradeInfo {
        return UpgradeInfo(
                isForce = response.isforce,
                isNewVersion = AppUtils.getAppVersionCode() < response.versionCode,
                versionName = response.version,
                downloadUrl = response.url,
                description = response.content,
                digitalAbstract = "",
                raw = response
        )
    }

    override fun showUpgradeDialog(context: Context, upgradeInfo: UpgradeInfo, onCancel: () -> Unit, onConfirm: () -> Unit) {
        Timber.d("showUpgradeDialog")
        upgradeDialogReference?.get()?.dismiss()
        val upgradeDialog = UpgradeDialog(context) {
            content = upgradeInfo.description
            isForce = upgradeInfo.isForce
            versionName = upgradeInfo.versionName
            onConfirmListener = onConfirm
        }
        upgradeDialog.show()
        upgradeDialogReference = WeakReference(upgradeDialog)
    }

    override fun showInstallTipsDialog(context: Context, force: Boolean, onCancel: () -> Unit, onConfirm: () -> Unit) {
        if (!force) {
            return
        }
        showConfirmDialog(context) {
            message = "新版本已经下载完成，请点击“确认”进行安装"
            cancelable = false
            negativeText = null
            negativeListener = {
                it.dismiss()
                onCancel()
            }
            positiveListener = { onConfirm() }
            autoDismiss = false
        }
    }

    override fun showDownloadingFailed(context: Context, force: Boolean, onCancel: () -> Unit, onConfirm: () -> Unit) {
        showConfirmDialog(context) {
            message = force.yes { "下载更新失败，需要重试" } otherwise { "下载更新失败，是否重试？" }
            cancelable = false
            negativeText = force.yes { null } otherwise { "取消" }
            negativeListener = { onCancel() }
            positiveListener = { onConfirm() }
        }
    }

    override fun showDownloadingDialog(context: Context, force: Boolean) {
        Timber.d("showDownloadingDialog")
        notificationHelper.cancelNotification()

        if (force) {
            newLoadingDialogIfNeed(context).run {
                setCancelable(false)
                show()
            }
        }
    }

    override fun dismissDownloadingDialog() {
        Timber.d("showDownloadingDialog")
        notificationHelper.cancelNotification()
        loadingDialogReference?.get()?.dismiss()
    }

    override fun onProgress(total: Long, progress: Long) {
        Timber.d("onProgress, total = $total, progress = $progress")
        notificationHelper.notifyProgress(total, progress)
    }

    override fun installApk(file: File, upgradeInfo: UpgradeInfo) {
        Timber.d("installApk")
        if (AndroidVersion.atLeast(26)) {
            //Android8.0未知来源应用安装权限方案
            AndPermission.with(AppContext.get())
                    .install()
                    .file(file)
                    .onDenied { Timber.d("installApk onDenied") }
                    .onGranted { Timber.d("installApk onGranted") }
                    .start()
        } else {
            //正常安装
            XAppUtils.installApp(AppContext.get(), file, AppSettings.appFileProviderAuthorities())
        }
    }

    override fun checkApkFile(apkFile: File, digitalAbstract: String) = true

    override fun generateAppDownloadPath(versionName: String): String = DirectoryManager.createAppDownloadPath(versionName)

}
```

example of NotificationHelper

```java
public class NotificationHelper {

    private static final int ID = 10;
    private static final String CHANNEL_ID = "bh-upgrade";

    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder mBuilder;

    private void init() {
        mNotificationManager = (NotificationManager) AppContext.get().getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "upgrade", NotificationManager.IMPORTANCE_LOW);
            channel.setDescription("for show downloading apk progress");
            channel.setSound(null, null);
            mNotificationManager.createNotificationChannel(channel);
        }

        mBuilder = new NotificationCompat.Builder(AppContext.get(), CHANNEL_ID)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentTitle(AppUtils.getAppName())
                .setContentText("某某某APP正在下载新版本")
                .setSmallIcon(R.drawable.icon_notification);
    }

    void cancelNotification() {
        if (mBuilder != null) {
            mBuilder.setProgress(0, 0, false);
            mNotificationManager.notify(ID, mBuilder.build());
            mNotificationManager.cancel(ID);
        }
        mNotificationManager = null;
        mBuilder = null;
    }

    void notifyProgress(long total, long progress) {
        if (mNotificationManager == null) {
            init();
        }
        if (total == -1) {
            mBuilder.setProgress((int) total, (int) progress, true);
        } else {
            mBuilder.setProgress((int) total, (int) progress, false);
        }
        mNotificationManager.notify(ID, mBuilder.build());
    }

}
```