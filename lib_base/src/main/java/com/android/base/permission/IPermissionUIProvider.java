package com.android.base.permission;

import android.content.Context;
import android.content.DialogInterface;


public interface IPermissionUIProvider {

    /**
     * 显示需要获取权限的原因，询问是否继续
     *
     * @param context            上下文
     * @param permission         需要的权限
     * @param onContinueListener 继续
     * @param onCancelListener   取消
     */
    void showPermissionRationaleDialog(Context context, final String[] permission, final DialogInterface.OnClickListener onContinueListener, DialogInterface.OnClickListener onCancelListener);

    /**
     * 拒绝权限后，询问是否去设置界面授予应用权限
     *
     * @param context            上下文
     * @param permission         需要的权限
     * @param onContinueListener 继续
     * @param onCancelListener   取消
     */
    void showAskAgainDialog(Context context, final String[] permission, DialogInterface.OnClickListener onContinueListener, DialogInterface.OnClickListener onCancelListener);

    /**
     * 权限被拒绝后，展示一个提示消息，比如 toast
     *
     * @param contexts   上下文
     * @param permission 被拒绝的权限
     */
    void showPermissionDeniedTip(Context contexts, String[] permission);

}
