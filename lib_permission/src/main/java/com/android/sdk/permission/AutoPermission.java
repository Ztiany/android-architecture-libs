package com.android.sdk.permission;

import android.content.Context;

import androidx.fragment.app.Fragment;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2020-09-22 16:21
 */
public class AutoPermission {

    private static boolean sUseInternalApi = true;

    public static TypeOption with(Context context) {
        return new TypeOption(context);
    }

    public static TypeOption with(Fragment fragment) {
        return new TypeOption(fragment);
    }

    public static boolean isUseInternalApi() {
        return sUseInternalApi;
    }

    public static void setUseInternalApi(boolean useInternalApi) {
        sUseInternalApi = useInternalApi;
    }

}