package com.android.base.utils.android;

import android.app.Activity;
import android.content.res.Configuration;
import android.content.res.Resources;

import androidx.annotation.NonNull;

/**
 * <p>
 * 在系统修改了字体缩放后，依然保持原始的字体比例，以此来防止因为字体过大而导致 UI 布局错乱。
 * </p>
 * usage in activity:
 * <pre>
 * override fun onConfigurationChanged(newConfig: Configuration) {
 *              screenAdaptation.onConfigurationChanged(newConfig)
 *              super.onConfigurationChanged(newConfig)
 * }
 *
 * override fun getResources(): Resources {
 *              return screenAdaptation.fixResources(super.getResources())
 * }
 *
 * </pre>
 *
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2020-03-27 11:46
 * @see <a href='https://juejin.im/entry/5c63821a6fb9a049a81fd075'>聊聊Android中的字体适配</a>
 */
public class TextSizeAdaptation {

    private final Activity mActivity;

    public TextSizeAdaptation(Activity activity) {
        mActivity = activity;
    }

    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        if (newConfig.fontScale != 1F) {
            mActivity.getResources();
        }
    }

    @NonNull
    public Resources fixResources(Resources resources) {
        if (resources.getConfiguration().fontScale != 1F) {
            Configuration configuration = new Configuration();
            configuration.setToDefaults();
            resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        }
        return resources;
    }

}
