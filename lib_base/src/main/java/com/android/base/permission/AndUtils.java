package com.android.base.permission;

import android.content.Context;
import android.support.annotation.NonNull;

import com.yanzhenjie.permission.checker.DoubleChecker;

/**
 * @author Ztiany
 * Date : 2018-09-07 11:38
 */
class AndUtils {

    /**
     * direct check permissions
     */
    static boolean hasPermission(@NonNull Context context, @NonNull String... permissions) {
        return new DoubleChecker().hasPermission(context, permissions);
    }

}
