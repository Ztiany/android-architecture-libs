package com.android.base.utils.android;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import java.security.MessageDigest;
import java.util.UUID;

public class DeviceIdUtil {

    private static final String TAG = "DeviceIdUtil";

    public static String getDeviceId(Context context) {

        StringBuilder sbDeviceId = new StringBuilder();

        String imei = getIMEI(context);
        android.util.Log.d(TAG, "imei " + imei);

        //手机型号 +手机
        String androidID = getAndroidId(context);
        android.util.Log.d(TAG, "androidID " + androidID);

        //serial  8.0 拿不到
        String serial = getSerial();
        android.util.Log.d(TAG, "serial " + serial);

        String id = getDeviceUUID().replace("-", "");
        android.util.Log.d(TAG, "id " + id);

        //追加imei
        if (imei != null && imei.length() > 0) {
            sbDeviceId.append(imei);
            sbDeviceId.append("|");
        }

        //追加 android id
        if (androidID != null && androidID.length() > 0) {
            sbDeviceId.append(androidID);
            sbDeviceId.append("|");
        }

        //追加serial
        if (serial != null && serial.length() > 0) {
            sbDeviceId.append(serial);
            sbDeviceId.append("|");
        }

        //追加硬件uuid
        if (id != null && id.length() > 0) {
            sbDeviceId.append(id);
        }

        //一系列的字符串硬件标识有，生成SHA1，统一DeviceId长度
        if (sbDeviceId.length() > 0) {
            try {
                byte[] hash = getHashByString(sbDeviceId.toString());
                String sha1 = bytesToHex(hash);
                if (sha1 != null && sha1.length() > 0) {
                    //返回最终的DeviceId
                    return sha1;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }
        return null;
    }

    /**
     * 转16进制字符串
     *
     * @param data 数据
     * @return 16进制字符串
     */
    private static String bytesToHex(byte[] data) {
        StringBuilder sb = new StringBuilder();
        String stmp;
        for (int n = 0; n < data.length; n++) {
            stmp = (Integer.toHexString(data[n] & 0xFF));
            if (stmp.length() == 1)
                sb.append("0");
            sb.append(stmp);
        }
        return sb.toString().toUpperCase(java.util.Locale.CHINA);
    }

    /**
     * 取SHA1
     *
     * @param data 数据
     * @return 对应的hash值
     */
    private static byte[] getHashByString(String data) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
            messageDigest.reset();
            messageDigest.update(data.getBytes("UTF-8"));
            return messageDigest.digest();
        } catch (Exception e) {
            return "".getBytes();
        }
    }

    // //获得硬件uuid（根据硬件相关属性，生成uuid）（无需权限）  数字  0   -10
    private static String getDeviceUUID() {

        String dev = "100001" + Build.BOARD +
                Build.BRAND +
                Build.DEVICE +
                Build.HARDWARE +
                Build.ID +
                Build.MODEL +
                Build.PRODUCT +
                Build.SERIAL;

        return new UUID(dev.hashCode(), Build.SERIAL.hashCode()).toString();
    }

    private static String getSerial() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                return Build.getSerial();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    /**
     * 获得设备的AndroidId
     *
     * @param context 上下文
     * @return 设备的AndroidId
     */
    private static String getAndroidId(Context context) {
        try {
            return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    //需要获得 READ_PHONE_STATE 权限，>=6.0，默认返回null
    private static String getIMEI(Context context) {
        try {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            return tm.getDeviceId();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

}