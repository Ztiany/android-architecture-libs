package com.android.sdk.biometrics;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2020-03-10 14:35
 */
public interface DialogInteractor {

    void show();

    void dismiss();

    void configStyle(DialogStyleConfiguration configuration);

    void setTip(CharSequence message);

    void setOnDismissAction(Runnable action);

    void setOnAdditionalOperationListener(AdditionalOperationListener listener);

    interface AdditionalOperationListener{
        void onOperation(int actionCode);
    }

}
