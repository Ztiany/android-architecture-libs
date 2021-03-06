package com.android.sdk.permission.impl.selfstudy;

import android.content.Context;
import android.content.DialogInterface;

import androidx.annotation.NonNull;


public interface IPermissionUIProvider {

    /**
     * 显示需要获取权限的原因，询问是否继续
     *
     * @param context            上下文
     * @param permission         需要的权限
     * @param onContinueListener 继续
     * @param onCancelListener   取消
     */
    void showPermissionRationaleDialog(@NonNull Context context, @NonNull String[] permission, @NonNull DialogInterface.OnClickListener onContinueListener, @NonNull DialogInterface.OnClickListener onCancelListener);

    /**
     * 拒绝权限后，询问是否去设置界面授予应用权限
     *
     * @param context            上下文
     * @param permission         需要的权限
     * @param onContinueListener 继续
     * @param onCancelListener   取消
     */
    void showAskAgainDialog(@NonNull Context context, @NonNull String[] permission, @NonNull DialogInterface.OnClickListener onContinueListener, @NonNull DialogInterface.OnClickListener onCancelListener);

    /**
     * 权限被拒绝后，展示一个提示消息，比如 toast
     *
     * @param contexts   上下文
     * @param permission 被拒绝的权限
     */
    void showPermissionDeniedTip(@NonNull Context contexts, @NonNull String[] permission);

}
