package com.android.base.permission;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;

import com.android.base.R;
import com.blankj.utilcode.util.ToastUtils;

import org.jetbrains.annotations.NotNull;

import androidx.appcompat.app.AlertDialog;

import static com.android.base.permission.PermissionUtils.createPermissionAskAgainText;
import static com.android.base.permission.PermissionUtils.createPermissionDeniedTip;
import static com.android.base.permission.PermissionUtils.createPermissionRationaleText;


class DefaultPermissionUIProvider implements IPermissionUIProvider {

    private static final int COLOR_STRING = Color.parseColor("#FF4081");

    @Override
    public void showPermissionRationaleDialog(@NotNull Context context, @NotNull final String[] permission,
                                              @NotNull final DialogInterface.OnClickListener onContinueListener, @NotNull DialogInterface.OnClickListener onCancelListener) {

        AlertDialog dialog = new AlertDialog.Builder(context)
                .setMessage(createPermissionRationaleText(context, permission, COLOR_STRING))
                .setCancelable(false)
                .setPositiveButton(R.string.Base_Confirm, onContinueListener)
                .setNegativeButton(R.string.Base_Cancel, onCancelListener)
                .create();

        dialog.show();
    }

    @Override
    public void showAskAgainDialog(@NotNull Context context, @NotNull final String[] permission,
                                   @NotNull DialogInterface.OnClickListener onToSetPermissionListener,
                                   @NotNull DialogInterface.OnClickListener onCancelListener) {

        AlertDialog dialog = new AlertDialog.Builder(context)
                .setMessage(createPermissionAskAgainText(context, permission, COLOR_STRING))
                .setCancelable(false)
                .setPositiveButton(R.string.Base_to_set_permission, onToSetPermissionListener)
                .setNegativeButton(R.string.Base_Cancel, onCancelListener)
                .create();

        dialog.show();
    }

    @Override
    public void showPermissionDeniedTip(@NotNull Context contexts, @NotNull String[] permission) {
        ToastUtils.showShort(createPermissionDeniedTip(contexts, permission, COLOR_STRING));
    }

}
