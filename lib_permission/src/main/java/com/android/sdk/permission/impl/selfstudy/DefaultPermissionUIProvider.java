package com.android.sdk.permission.impl.selfstudy;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.widget.Toast;

import com.android.sdk.permission.R;

import org.jetbrains.annotations.NotNull;

import androidx.appcompat.app.AlertDialog;

import static com.android.sdk.permission.impl.selfstudy.PermissionUtils.createPermissionAskAgainText;
import static com.android.sdk.permission.impl.selfstudy.PermissionUtils.createPermissionDeniedTip;
import static com.android.sdk.permission.impl.selfstudy.PermissionUtils.createPermissionRationaleText;


class DefaultPermissionUIProvider implements IPermissionUIProvider {

    private static final int COLOR_STRING = Color.parseColor("#FF4081");

    @Override
    public void showPermissionRationaleDialog(@NotNull Context context, @NotNull final String[] permission,
                                              @NotNull final DialogInterface.OnClickListener onContinueListener, @NotNull DialogInterface.OnClickListener onCancelListener) {

        AlertDialog dialog = new AlertDialog.Builder(context)
                .setMessage(createPermissionRationaleText(context, permission, COLOR_STRING))
                .setCancelable(false)
                .setPositiveButton(R.string.Permission_Confirm, onContinueListener)
                .setNegativeButton(R.string.Permission_Cancel, onCancelListener)
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
                .setPositiveButton(R.string.Permission_to_set_permission, onToSetPermissionListener)
                .setNegativeButton(R.string.Permission_Cancel, onCancelListener)
                .create();

        dialog.show();
    }

    @Override
    public void showPermissionDeniedTip(@NotNull Context contexts, @NotNull String[] permission) {
        Toast.makeText(contexts, createPermissionDeniedTip(contexts, permission, COLOR_STRING), Toast.LENGTH_SHORT).show();
    }

}
