package com.android.base.permission;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;

import com.android.base.R;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.ToastUtils;

import java.util.Arrays;
import java.util.List;

class DefaultPermissionUIProvider implements IPermissionUIProvider {

    private static final String COLOR_STRING = "#FF4081";

    @Override
    public void showPermissionRationaleDialog(Context context, final String[] permission,
                                              final DialogInterface.OnClickListener onContinueListener, DialogInterface.OnClickListener onCancelListener) {

        AlertDialog dialog = new AlertDialog.Builder(context)
                .setMessage(DefaultPermissionResourceProvider.createPermissionRationaleText(context, permission))
                .setCancelable(false)
                .setPositiveButton(R.string.Base_Confirm, onContinueListener)
                .setNegativeButton(R.string.Base_Cancel, onCancelListener)
                .create();

        dialog.show();
    }

    @Override
    public void showAskAgainDialog(Context context, final String[] permission,
                                   DialogInterface.OnClickListener onToSetPermissionListener,
                                   DialogInterface.OnClickListener onCancelListener) {

        AlertDialog dialog = new AlertDialog.Builder(context)
                .setMessage(DefaultPermissionResourceProvider.createPermissionAskAgainText(context, permission))
                .setCancelable(false)
                .setPositiveButton(R.string.Base_to_set_permission, onToSetPermissionListener)
                .setNegativeButton(R.string.Base_Cancel, onCancelListener)
                .create();

        dialog.show();
    }

    @Override
    public void showPermissionDeniedTip(Context contexts, String[] permission) {
        ToastUtils.showShort(DefaultPermissionResourceProvider.createPermissionDeniedTip(contexts, permission));
    }

    private static final class DefaultPermissionResourceProvider {

        static CharSequence createPermissionRationaleText(Context context, @NonNull String[] perms) {
            String permissionText = createPermissionText(context, Arrays.asList(perms));
            return tintText(context.getString(R.string.Base_request_permission_rationale, permissionText), permissionText);
        }

        static CharSequence createPermissionAskAgainText(Context context, @NonNull String[] permission) {
            String permissionText = createPermissionText(context, Arrays.asList(permission));
            String appName = AppUtils.getAppName();
            String content = context.getString(R.string.Base_permission_denied_ask_again_rationale, appName, permissionText);
            return tintText(content, permissionText);
        }

        static CharSequence createPermissionDeniedTip(Context context, String[] permission) {
            String permissionText = createPermissionText(context, Arrays.asList(permission));
            return tintText(context.getString(
                    R.string.Base_permission_denied, permissionText), permissionText);
        }

        private static CharSequence tintText(String content, String perms) {
            SpannableStringBuilder ssb = new SpannableStringBuilder(content);
            int indexPerm = content.indexOf(perms);
            ssb.setSpan(new ForegroundColorSpan(Color.parseColor(COLOR_STRING)), indexPerm, indexPerm + perms.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            return ssb;
        }

        private static String createPermissionText(Context context, @NonNull List<String> perms) {
            List<String> permList = Permission.transformText(context, perms);
            String characterSeg = context.getString(R.string.Base_character_seg);
            StringBuilder sb = new StringBuilder();
            int size = permList.size();
            for (int i = 0; i < size; i++) {
                sb.append(permList.get(i));
                if (i < size - 1) {
                    sb.append(characterSeg);
                }
            }
            return sb.toString();
        }
    }

}
