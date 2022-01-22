package com.android.base.utils.android;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.webkit.MimeTypeMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.android.base.utils.BaseUtils;
import com.android.base.utils.common.Files;
import com.android.base.utils.common.Strings;

import java.io.File;
import java.util.List;

import timber.log.Timber;


public class AppUtils {

    private AppUtils() {
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
                Intent intent = getInstallAppIntent24(context, file, Strings.isEmpty(authority) ? (context.getPackageName() + ".fileProvider") : authority);
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
        intent.setDataAndType(contentUri, MimeTypeMap.getSingleton().getMimeTypeFromExtension(Files.getFileExtension(file)));

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
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(Files.getFileExtension(file));
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

    public static void restartApp(Activity activity, Class<? extends Activity> target) {
        Intent intent = new Intent(activity, target);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(intent);
        activity.overridePendingTransition(0, 0);
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    /**
     * Activity 分发结果给 Fragment【特定场景下才需要，比如 ARouter 不支持 Fragment.startActivityForResult()】
     */
    public static void dispatchActivityResult(FragmentActivity activity, int requestCode, int resultCode, Intent data) {
        dispatchActivityResult(activity.getSupportFragmentManager(), requestCode, resultCode, data);
    }

    /**
     * Activity 分发结果给 Fragment【特定场景下才需要，比如 ARouter 不支持 Fragment.startActivityForResult()】
     */
    public static void dispatchActivityResult(FragmentManager fragmentManager, int requestCode, int resultCode, Intent data) {
        List<Fragment> fragments = fragmentManager.getFragments();
        if (fragments.isEmpty()) {
            return;
        }
        for (Fragment fragment : fragments) {
            if (fragment != null) {
                fragment.onActivityResult(requestCode, resultCode, data);
                dispatchActivityResult(fragment.getChildFragmentManager(), requestCode, resultCode, data);
            }
        }
    }

    public static String getAppVersionName() {
        return getAppVersionName(BaseUtils.getAppContext().getPackageName());
    }

    public static int getAppVersionCode() {
        return getAppVersionCode(BaseUtils.getAppContext().getPackageName());
    }

    /**
     * Return the application's version name.
     *
     * @param packageName The name of the package.
     * @return the application's version name
     */
    public static String getAppVersionName(final String packageName) {
        if (Strings.isSpace(packageName)) return "";
        try {
            PackageManager pm = BaseUtils.getAppContext().getPackageManager();
            PackageInfo pi = pm.getPackageInfo(packageName, 0);
            return pi == null ? null : pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * Return the application's version code.
     *
     * @param packageName The name of the package.
     * @return the application's version code
     */
    public static int getAppVersionCode(final String packageName) {
        if (Strings.isSpace(packageName)) return -1;
        try {
            PackageManager pm = BaseUtils.getAppContext().getPackageManager();
            PackageInfo pi = pm.getPackageInfo(packageName, 0);
            return pi == null ? -1 : pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * Return the application's name.
     *
     * @return the application's name
     */
    public static String getAppName() {
        return getAppName(BaseUtils.getAppContext().getPackageName());
    }

    /**
     * Return the application's name.
     *
     * @param packageName The name of the package.
     * @return the application's name
     */
    public static String getAppName(final String packageName) {
        if (Strings.isSpace(packageName)) return "";
        try {
            PackageManager pm = BaseUtils.getAppContext().getPackageManager();
            PackageInfo pi = pm.getPackageInfo(packageName, 0);
            return pi == null ? null : pi.applicationInfo.loadLabel(pm).toString();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }

}