package com.android.base.utils.android.network;

import static com.android.base.utils.android.network.NetworkState.STATE_GPRS;
import static com.android.base.utils.android.network.NetworkState.STATE_NONE;
import static com.android.base.utils.android.network.NetworkState.STATE_WIFI;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;

import androidx.annotation.NonNull;

import com.android.base.utils.android.compat.AndroidVersion;

import timber.log.Timber;


/**
 * 网络监听，需要权限：
 * <pre>{@code
 *      <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
 * }
 * </pre>
 *
 * @author Ztiany
 * email 1169654504@qq.com
 * date 2015-12-08 14:50
 * @see <a href="http://www.jianshu.com/p/983889116526">Android：检测网络状态 & 监听网络变化</a>
 */
public abstract class NetStateReceiver extends BroadcastReceiver {

    private static NetworkState mStatus = null;

    private State state_wifi = null;
    private State state_gprs = null;

    @Override
    public void onReceive(Context context, Intent intent) {
        NetworkState tempStatus;

        // 获得网络连接服务
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        getState(connManager);

        if (null != state_wifi && State.CONNECTED == state_wifi) { // 判断是否正在使用WIFI网络
            tempStatus = STATE_WIFI;
            Timber.d("mStatus=" + mStatus + "改变后的网络为WIFI");
        } else if (null != state_gprs && State.CONNECTED == state_gprs) { // 判断是否正在使用GPRS网络
            tempStatus = STATE_GPRS;
            Timber.d("mStatus=" + mStatus + "改变后的网络为GPRS");
        } else {
            tempStatus = STATE_NONE;
            Timber.d("mStatus=" + mStatus + "改变后的网络为无连接");
        }

        if (mStatus != tempStatus) {
            Timber.d("mStatus与改变后的网络不同，网络真的改变了");
            //在此处 通知网络更新
            onNetworkStateChanged(tempStatus);
        } else {
            Timber.d("mStatus与改变后的网络相同，不处理");
        }

        mStatus = tempStatus;

        state_wifi = null;
        state_gprs = null;
    }

    @SuppressLint("MissingPermission")
    private void getState(ConnectivityManager connManager) {

        if (AndroidVersion.atLeast(23)) {

            //获取所有网络连接的信息
            Network[] networks = connManager.getAllNetworks();
            NetworkInfo networkInfo;
            for (Network network : networks) {
                //获取ConnectivityManager对象对应的NetworkInfo对象
                networkInfo = connManager.getNetworkInfo(network);
                if (networkInfo == null) {
                    continue;
                }
                if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                    state_wifi = networkInfo.getState();
                } else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                    state_gprs = networkInfo.getState();
                }
                if (state_wifi != null && state_gprs != null) {
                    break;
                }
            }

            if (!NetworkUtils.isConnected()) {
                Timber.d("api 23 after getState check isConnected = false");
                state_wifi = null;
                state_gprs = null;
            }

            Timber.d("api 23 after getState state_wifi = " + state_wifi + " state_gprs = " + state_gprs);
        } else {

            try {
                state_wifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState(); // 获取网络连接状态
            } catch (Exception e) {
                Timber.d("测试机没有WIFI模块");
            }
            try {
                state_gprs = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState(); // 获取网络连接状态
            } catch (Exception e) {
                Timber.d("测试机没有GPRS模块");
            }
            Timber.d("api 23 before getState state_wifi = " + state_wifi + " state_gprs = " + state_gprs);
        }
    }

    public abstract void onNetworkStateChanged(@NonNull NetworkState tempStatus);

}
