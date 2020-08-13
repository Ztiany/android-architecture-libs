package com.android.sdk.biometrics;

import android.content.Context;
import android.graphics.Point;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatDialog;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2020-03-10 15:59
 */
public class DefaultFingerprintDialog extends AppCompatDialog implements DialogInteractor {

    private DialogStyleConfiguration mDialogStyleConfiguration;

    private TextView mTitleTv;

    private TextView mTipsTv;

    @SuppressWarnings("WeakerAccess")
    public DefaultFingerprintDialog(Context context) {
        super(context);

        setCancelable(false);

        setContentView(R.layout.biometrics_dialog_layout);

        mTitleTv = findViewById(R.id.biometricsTvTitle);
        mTipsTv = findViewById(R.id.biometricsTvTips);

        View cancelBtn = findViewById(R.id.biometricsTvCancel);
        assert cancelBtn != null;
        cancelBtn.setOnClickListener(v -> dismiss());

        if (mDialogStyleConfiguration != null) {
            applyStyle(mDialogStyleConfiguration);
        }
    }

    private void applyStyle(DialogStyleConfiguration dialogStyleConfiguration) {
        mTitleTv.setText(dialogStyleConfiguration.getTitle());
    }

    @Override
    public void configStyle(DialogStyleConfiguration configuration) {
        mDialogStyleConfiguration = configuration;
    }

    @Override
    public void setTip(CharSequence message) {
        if (mTipsTv != null) {
            mTipsTv.setText(message);
        }
    }

    @Override
    public void setOnDismissAction(Runnable action) {
        setOnDismissListener(dialog -> action.run());
    }

    @Override
    public void setOnAdditionalOperationListener(AdditionalOperationListener listener) {

    }

    @Override
    public void show() {
        int appScreenWidth = getAppScreenWidth();
        int appScreenHeight = getAppScreenHeight();
        int realScreenWidth = (appScreenWidth < appScreenHeight) ? appScreenWidth : appScreenHeight;

        Window window = getWindow();
        if (window != null) {
            WindowManager.LayoutParams attributes = window.getAttributes();
            attributes.width = (int) (realScreenWidth * 0.8);
            window.setAttributes(attributes);
        }

        super.show();
    }

    private int getAppScreenWidth() {
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        if (wm == null) return -1;
        Point point = new Point();
        wm.getDefaultDisplay().getSize(point);
        return point.x;
    }

    private int getAppScreenHeight() {
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        if (wm == null) return -1;
        Point point = new Point();
        wm.getDefaultDisplay().getSize(point);
        return point.y;
    }

}