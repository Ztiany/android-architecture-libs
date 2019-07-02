package com.android.sdk.push;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2019-02-26 11:36
 */
public class Utils {

    private static final String PUSH_SP_NAME = "push_sp_name";

    public static void savePushId(String key, String id) {
        SharedPreferences sharedPreferences = PushContext.getApplication().getSharedPreferences(PUSH_SP_NAME, Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(key, id).apply();
    }

    public static String getPushId(String key) {
        SharedPreferences sharedPreferences = PushContext.getApplication().getSharedPreferences(PUSH_SP_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, "");
    }

}
