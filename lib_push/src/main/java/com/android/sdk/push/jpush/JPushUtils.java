package com.android.sdk.push.jpush;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.jpush.android.api.JPushInterface;
import timber.log.Timber;

import static com.android.sdk.push.jpush.TagAliasOperatorHelper.ACTION_ADD;
import static com.android.sdk.push.jpush.TagAliasOperatorHelper.ACTION_CLEAN;
import static com.android.sdk.push.jpush.TagAliasOperatorHelper.ACTION_DELETE;
import static com.android.sdk.push.jpush.TagAliasOperatorHelper.ACTION_SET;

/**
 * @author Wangwb
 * Email: 253123123@qq.com
 * Date : 2019-01-28 11:29
 */
class JPushUtils {

    private static final String TAG = "JPushReceiver";

    /**
     * 打印 Bundle Extras
     */
    static String printBundle(Bundle bundle) {
        if (bundle == null) {
            return "null";
        }

        StringBuilder sb = new StringBuilder();

        for (String key : bundle.keySet()) {
            switch (key) {
                case JPushInterface.EXTRA_NOTIFICATION_ID:
                    sb.append("\nkey:").append(key).append(", value:").append(bundle.getInt(key));
                    break;
                case JPushInterface.EXTRA_CONNECTION_CHANGE:
                    sb.append("\nkey:").append(key).append(", value:").append(bundle.getBoolean(key));
                    break;
                case JPushInterface.EXTRA_EXTRA:
                    if (TextUtils.isEmpty(bundle.getString(JPushInterface.EXTRA_EXTRA))) {
                        Timber.i(TAG, "This message has no Extra data");
                        continue;
                    }

                    try {
                        JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                        Iterator<String> it = json.keys();

                        while (it.hasNext()) {
                            String myKey = it.next();
                            sb.append("\nkey:").append(key).append(", value: [").append(myKey).append(" - ").append(json.optString(myKey)).append("]");
                        }
                    } catch (JSONException e) {
                        Timber.e(TAG, "Get message extra JSON error!");
                    }
                    break;
                default:
                    sb.append("\nkey:").append(key).append(", value:").append(bundle.get(key));
                    break;
            }
        }
        return sb.toString();
    }

    /**
     * 获取极光推送AppKey
     */
    @SuppressWarnings("unused")
    static String appKey(Context context) {
        Bundle metaData = null;
        String appKey = null;
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            if (null != ai)
                metaData = ai.metaData;
            if (null != metaData) {
                appKey = metaData.getString("JPUSH_APPKEY");
                Timber.d("JPush AppKey: " + appKey + ", pkg: " + context.getPackageName());
                if ((null == appKey) || appKey.length() != 24) {
                    appKey = null;
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return appKey;
    }

    /**
     * 校验Tag Alias 只能是数字，英文字母和中文
     */
    @SuppressWarnings("all")
    private static boolean isValidTagAndAlias(String s) {
        Pattern p = Pattern.compile("^[\u4E00-\u9FA50-9a-zA-Z_!@#$&*+=.|]+$");
        Matcher m = p.matcher(s);
        return m.matches();
    }

    static boolean isConnected(Context context) {
        ConnectivityManager conn = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        @SuppressLint("MissingPermission")
        NetworkInfo info = conn.getActiveNetworkInfo();
        return (info == null || !info.isConnected());
    }

    /**
     * 获取设备ID
     */
    @SuppressWarnings("unused")
    static String getDeviceId(Context context) {
        return JPushInterface.getUdid(context);
    }

    static void setTag(Context context, String tag) {
        if (!JPushUtils.isValidTagAndAlias(tag)) {
            Timber.e("Tag Alias 命名不符合规范");
            return;
        }

        TagAliasOperatorHelper.TagAliasBean tagAliasBean = createTagAliasBean(tag);
        tagAliasBean.action = ACTION_SET;

        TagAliasOperatorHelper.getInstance().handleAction(context, ++TagAliasOperatorHelper.sequence, tagAliasBean);
    }

    static void deleteTag(Context context, String tag) {
        if (!JPushUtils.isValidTagAndAlias(tag)) {
            Timber.e("Tag Alias 命名不符合规范");
            return;
        }

        TagAliasOperatorHelper.TagAliasBean tagAliasBean = createTagAliasBean(tag);
        tagAliasBean.action = ACTION_DELETE;

        TagAliasOperatorHelper.getInstance().handleAction(context, ++TagAliasOperatorHelper.sequence, tagAliasBean);
    }

    static void clearTags(Context context) {
        TagAliasOperatorHelper.TagAliasBean tagAliasBean = createTagAliasBean(null);
        tagAliasBean.action = ACTION_CLEAN;

        TagAliasOperatorHelper.getInstance().handleAction(context, ++TagAliasOperatorHelper.sequence, tagAliasBean);
    }

    @NonNull
    private static TagAliasOperatorHelper.TagAliasBean createTagAliasBean(String tag) {
        TagAliasOperatorHelper.TagAliasBean tagAliasBean = new TagAliasOperatorHelper.TagAliasBean();
        tagAliasBean.isAliasAction = false;

        if (!TextUtils.isEmpty(tag)) {
            LinkedHashSet<String> tags = new LinkedHashSet<>();
            tags.add(tag);
            tagAliasBean.tags = tags;
        }

        return tagAliasBean;
    }

    static void setAlias(Context context, String alias) {
        TagAliasOperatorHelper.TagAliasBean tagAliasBean = new TagAliasOperatorHelper.TagAliasBean();

        tagAliasBean.action = ACTION_ADD;
        tagAliasBean.isAliasAction = true;
        tagAliasBean.alias = alias;

        TagAliasOperatorHelper.getInstance().handleAction(context, ++TagAliasOperatorHelper.sequence, tagAliasBean);
    }

    static void clearAlias(Context context) {
        TagAliasOperatorHelper.TagAliasBean tagAliasBean = new TagAliasOperatorHelper.TagAliasBean();

        tagAliasBean.action = ACTION_DELETE;
        tagAliasBean.isAliasAction = true;

        TagAliasOperatorHelper.getInstance().handleAction(context, ++TagAliasOperatorHelper.sequence, tagAliasBean);
    }

}