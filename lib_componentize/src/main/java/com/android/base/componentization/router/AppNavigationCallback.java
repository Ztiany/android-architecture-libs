package com.android.base.componentization.router;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2018-04-12 18:10
 */
public interface AppNavigationCallback  {

    void onFound(Postcard postcard);

    void onLost(Postcard postcard);

    void onArrival(Postcard postcard);

    void onInterrupt(Postcard postcard);

}
