package com.android.sdk.biometrics;

import android.content.Context;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2020-03-10 14:35
 */
public interface DialogFactory {

    <T extends DialogInteractor> T createDialog(Context context);

}
