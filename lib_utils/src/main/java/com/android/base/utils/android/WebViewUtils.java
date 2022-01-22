package com.android.base.utils.android;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;

import androidx.annotation.RequiresApi;

/**
 * <pre>
 * 关于内存泄漏：最好的方式还是开启独立的进程
 *
 * 关于Cookie：
 *
 *          之前同步 cookie 需要用到 CookieSyncManager 类，现在这个类已经被抛弃了。
 *          现在WebView已经可以在需要的时候自动同步 cookie 了，
 *          所以不再需要创建 CookieSyncManager 类的对象来进行强制性的同步 cookie 了。
 *          现在只需要获得 CookieManager 的对象将 cookie 设置进去就可以了。
 *
 *          从服务器的返回头中取出 cookie 根据Http请求的客户端不同，获取 cookie 的方式也不同。比如以HttpURLCollection
 *
 *          <code>
 *               String cookieStr = conn.getHeaderField("Set-Cookie");
 *          </code>
 *
 *          步骤：
 *                   1：利用HttpClient进行api请求登录，然后获取cookie
 *                   2：调用syncCookie分发把cookie写入到WebCookie的数据库中
 * </pre>
 */
public class WebViewUtils {

    private static final String TAG = WebViewUtils.class.getSimpleName();

    private WebViewUtils() {
        throw new UnsupportedOperationException();
    }

    public static void destroy(WebView webView) {
        try {
            if (webView == null) {
                return;
            }
            webView.setWebChromeClient(null);
            webView.setWebViewClient(null);
            webView.onPause();
            if (webView.getParent() != null) {
                ((ViewGroup) webView.getParent()).removeAllViews();
            }
            webView.setVisibility(View.GONE);//解决崩溃问题 Receiver not Register
            webView.removeAllViews();
            if (webView.getHandler() != null) {
                webView.getHandler().removeCallbacks(null);
            }
            webView.pauseTimers();
            webView.destroy();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 将cookie设置到WebView,  客户端通过以下代码设置cookie，如果两次设置相同，会覆盖上一次的
     *
     * @param url    要加载的 url
     * @param cookie 要同步的 cookie
     */
    public static void syncCookie(String url, String cookie, Context context) {
        /*
            注意：
            1，同步 cookie 要在 WebView 加载 url 之前，否则 WebView 无法获得相应的 cookie，也就无法通过验证。
            2，cookie应该被及时更新，否则很可能导致WebView拿的是旧的session id和服务器进行通信。
            3，CookieManager会将这个Cookie存入该应用程序data/data/package_name/app_WebView/Cookies.db
         */
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            CookieSyncManager.createInstance(context);
        }
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.setCookie(url, cookie);//如果没有特殊需求，这里只需要将session id以"key=value"形式作为cookie即可
        CookieSyncManager.getInstance().sync();
    }

    /**
     * 获取指定 url 的cookie
     * <pre>
     * 打开网页，WebView从数据库中读取该cookie值，放到http请求的头部，传递到服务器
     * </pre>
     */
    public static String getCookie(String url) {
        CookieManager cookieManager = CookieManager.getInstance();
        return cookieManager.getCookie(url);
    }

    public static void clearCookie(Context context) {
        // 这个两个在 API level 21 被抛弃
        CookieSyncManager.createInstance(context);
        CookieManager.getInstance().removeSessionCookie();
        CookieManager.getInstance().removeAllCookie();
        CookieSyncManager.getInstance().sync();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            clearCookie21();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private static void clearCookie21() {
        // 推荐使用这两个， level 21 新加的
        // 移除所有过期 cookie
        CookieManager.getInstance().removeSessionCookies(aBoolean -> {
            Log.d(TAG, "clearCookie21 removeSessionCookies:" + aBoolean);
        });
        // 移除所有的 cookie
        CookieManager.getInstance().removeAllCookies(aBoolean -> {
            Log.d(TAG, "clearCookie21 removeAllCookies:" + aBoolean);
        });
    }

}