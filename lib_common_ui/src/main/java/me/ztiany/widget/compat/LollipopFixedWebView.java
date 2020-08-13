package me.ztiany.widget.compat;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.util.AttributeSet;
import android.webkit.WebView;

/**
 * @see <a href='https://stackoverflow.com/questions/41025200/android-view-inflateexception-error-inflating-class-android-webkit-webview'>android-view-inflateexception-error-inflating-class-android-webkit-webview</a>
 */
public class LollipopFixedWebView extends WebView {

    public LollipopFixedWebView(Context context) {
        super(getFixedContext(context));
    }

    public LollipopFixedWebView(Context context, AttributeSet attrs) {
        super(getFixedContext(context), attrs);
    }

    public LollipopFixedWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(getFixedContext(context), attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public LollipopFixedWebView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(getFixedContext(context), attrs, defStyleAttr, defStyleRes);
    }

    public LollipopFixedWebView(Context context, AttributeSet attrs, int defStyleAttr, boolean privateBrowsing) {
        super(getFixedContext(context), attrs, defStyleAttr, privateBrowsing);
    }

    public static Context getFixedContext(Context context) {
        return context.createConfigurationContext(new Configuration());
    }

}