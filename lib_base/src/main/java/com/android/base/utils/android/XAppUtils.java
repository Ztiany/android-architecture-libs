package com.android.base.utils.android;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.webkit.MimeTypeMap;

import com.android.base.utils.common.FileUtils;
import com.android.base.utils.common.StringChecker;

import java.io.File;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.FileProvider;
import timber.log.Timber;

/**
 * 对 {@link com.blankj.utilcode.util.AppUtils} 的补充
 */
public class XAppUtils {

    private XAppUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 安装App，支持 Android 6.0 FileProvider。
     *
     * @param context   caller
     * @param file      文件
     * @param authority FileProvider authorities, default is {@code PackageName + ".fileProvider"}
     */
    public static boolean installApp(Context context, File file, @Nullable String authority) {
        if (file == null || !file.exists()) {
            return false;
        }
        try {
            if (Build.VERSION.SDK_INT <= 23) {
                context.startActivity(getInstallAppIntent23(file));
            } else {
                Intent intent = getInstallAppIntent24(context, file, StringChecker.isEmpty(authority) ? (context.getPackageName() + ".fileProvider") : authority);
                context.startActivity(intent);
            }
            Timber.d("installApp open  activity successfully");
            return true;
        } catch (Exception e) {
            Timber.e(e, "installApp");
        }
        return false;
    }

    /**
     * 获取安装App(支持7.0)的意图
     *
     * @param file 文件
     * @return intent
     */
    @NonNull
    @RequiresApi(24)
    private static Intent getInstallAppIntent24(Context context, File file, String authority) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        Uri contentUri = FileProvider.getUriForFile(context, authority, file);
        intent.setDataAndType(contentUri, MimeTypeMap.getSingleton().getMimeTypeFromExtension(FileUtils.getFileExtension(file)));

        // 然后全部授权
        List<ResolveInfo> resolveLists = context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo resolveInfo : resolveLists) {
            String packageName = resolveInfo.activityInfo.packageName;
            context.grantUriPermission(packageName, contentUri, Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        }

        return intent;
    }

    /**
     * 获取安装App(支持6.0)的意图
     *
     * @param file 文件
     * @return intent
     */
    private static Intent getInstallAppIntent23(File file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        String type;
        if (Build.VERSION.SDK_INT < 23) {
            type = "application/vnd.android.package-archive";
        } else {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(FileUtils.getFileExtension(file));
        }
        intent.setDataAndType(Uri.fromFile(file), type);
        return intent;
    }

    /**
     * 用来判断服务是否运行
     *
     * @param context     上下文
     * @param serviceName 判断的服务名字
     * @return true 在运行 false 不在运行
     */
    public static boolean isServiceRunning(Context context, String serviceName) {
        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (activityManager == null) {
            return false;
        }
        List<ActivityManager.RunningServiceInfo> serviceList = activityManager.getRunningServices(100);
        if (serviceList.isEmpty()) {
            return false;
        }
        for (int i = 0; i < serviceList.size(); i++) {
            if (serviceName.equals(serviceList.get(i).service.getClassName())) {
                isRunning = true;
                break;
            }
        }
        return isRunning;
    }

}