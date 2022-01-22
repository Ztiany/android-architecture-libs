package com.android.base.utils.android;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.provider.CalendarContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;

import androidx.annotation.Nullable;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import timber.log.Timber;

public class IntentUtils {

    public static final String GOOGLE_PLAY_PACKAGE_NAME = "com.android.vending";
    public static final String WANDOUJIA_PACKAGE_NAME = "com.wandoujia.phoenix2";
    public static final String TENCENT_PACKAGE_NAME = "com.tencent.android.qqdownloader";

    /**
     * 打开应用市场
     *
     * @param context 上下文
     * @return 是否成功
     */
    public static boolean openMarket(Context context) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        //跳转到应用市场，非GooglePlay市场一般情况也实现了这个接口
        intent.setData(Uri.parse("market://details?id=" + context.getPackageName()));
        //存在手机里没安装应用市场的情况，跳转会包异常，做一个接收判断
        if (intent.resolveActivity(context.getPackageManager()) != null) { //可以接收
            context.startActivity(intent);
            return true;
        } else {
            return false;
        }
    }

    /**
     * 优先打开规定的应用市场
     *
     * @param context         上下文
     * @param specificMarkets 你规定的能打开app的包名,优先打开规定的包名
     * @return true表示打开成功
     */
    public static boolean openSpecificMarket(Context context, String... specificMarkets) {
        String appId = context.getPackageName();
        Intent rateIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appId));
        boolean marketFound = false;
        // find all applications able to handle our rateIntent
        final List<ResolveInfo> otherApps = context.getPackageManager().queryIntentActivities(rateIntent, 0);
        List<String> specifiedPackage = Arrays.asList(specificMarkets);
        for (ResolveInfo otherApp : otherApps) {
            // look for Google Play application
            if (specifiedPackage.contains(otherApp.activityInfo.applicationInfo.packageName)) {
                ActivityInfo otherAppActivity = otherApp.activityInfo;
                ComponentName componentName = new ComponentName(otherAppActivity.applicationInfo.packageName, otherAppActivity.name);
                // make sure it does NOT open in the stack of your activity
                rateIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                // task reparenting if needed
                rateIntent.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                // if the Google Play was already open in a search result
                //  this make sure it still go to the app page you requested
                rateIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                // this make sure only the Google Play app is allowed to
                // intercept the intent
                rateIntent.setComponent(componentName);
                context.startActivity(rateIntent);
                marketFound = true;
                break;
            }
        }
        return marketFound || openMarket(context);
    }

    /**
     * @param email   email email必须放到数组中
     * @param subject 主题
     * @param text    发送的内容
     */
    public static boolean sendEmail(Context context, String[] email, String subject, String text) {
        Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        emailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        emailIntent.setType("message/rfc822");
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, email);
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, text);
        if (emailIntent.resolveActivity(context.getPackageManager()) != null) {
            try {
                context.startActivity(emailIntent);
                return true;
            } catch (Exception e) {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * 参考：https://developer.android.com/guide/topics/providers/calendar-provider.html?hl=zh-cn#intent-insert
     *
     * @param context       上下文
     * @param beginTime     开始时间
     * @param endTime       结束时间
     * @param title         标题
     * @param description   描述
     * @param eventLocation 位置
     * @param emails        extra 字段提供以逗号分隔的受邀者电子邮件地址列表。
     * @return 是否添加成功
     */
    public static boolean insertEvent(Context context, Calendar beginTime, Calendar endTime, String title, String description, String eventLocation, @Nullable String emails) {
        emails = TextUtils.isEmpty(emails) ? "" : emails;
        Intent intent = new Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI)
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis())
                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis())
                .putExtra(CalendarContract.Events.TITLE, title)
                .putExtra(CalendarContract.Events.DESCRIPTION, description)
                .putExtra(CalendarContract.Events.EVENT_LOCATION, eventLocation)
                .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY)
                .putExtra(Intent.EXTRA_EMAIL, emails);
        ComponentName componentName = intent.resolveActivity(context.getPackageManager());
        if (componentName != null) {
            context.startActivity(intent);
            return true;
        } else {
            Timber.d("insertEvent() fail");
            return false;
        }
    }

    /**
     * 打开网络设置界面
     */
    public static boolean openNetworkSettings(Context context) {
        try {
            Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            return true;
        } catch (Exception ignore) {
            return false;
        }
    }

    /**
     * 打开系统设置界面
     */
    public static boolean openSystemSettings(Context context) {
        try {
            Intent intent = new Intent(android.provider.Settings.ACTION_SETTINGS);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            return true;
        } catch (Exception ignore) {
            return false;
        }
    }

    /**
     * 通知系统有图片保存了
     */
    public static void notifyImageSaved(Context context, String path) {
        //https://juejin.im/post/5ae0541df265da0b9d77e45a
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(), path, "", "");
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + path)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}