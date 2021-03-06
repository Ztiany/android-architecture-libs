package com.android.sdk.permission.impl.selfstudy;

import java.util.List;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2019-07-02 13:50
 */
public interface OnPermissionDeniedListener {
    void onPermissionDenied(List<String> permissions);
}
