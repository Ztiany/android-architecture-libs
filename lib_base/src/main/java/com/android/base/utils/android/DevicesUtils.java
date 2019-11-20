package com.android.base.utils.android;

import android.Manifest;
import android.annotation.SuppressLint;
import android.os.Build;
import android.util.Log;

import com.android.base.utils.android.compat.AndroidVersion;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import androidx.annotation.RequiresPermission;
import timber.log.Timber;

public class DevicesUtils {

    private DevicesUtils() {
        throw new UnsupportedOperationException("no need instantiation");
    }

    public static String getModel() {
        String model = Build.MODEL;
        if (model != null) {
            model = model.trim().replaceAll("\\s*", "");
        } else {
            model = "";
        }
        return model;
    }

    /**
     * for getDeviceId，DevicesId 形式：
     * <pre>
     *      00000000-1082-436b-ffff-ffffc2e337a1
     *      ffffffff-a987-897a-0000-000000de11f7
     * </pre>
     */
    private static final String M_SZ_DEV_ID_SHORT = "35" +
            Build.BOARD.length() % 10 + Build.BRAND.length() % 10 +
            Build.CPU_ABI.length() % 10 + Build.DEVICE.length() % 10 +
            Build.DISPLAY.length() % 10 + Build.HOST.length() % 10 +
            Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10 +
            Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10 +
            Build.TAGS.length() % 10 + Build.TYPE.length() % 10 +
            Build.USER.length() % 10; //13 位

    /**
     * 参考：http://blog.csdn.net/nugongahou110/article/details/47003257
     * 参考：https://stackoverflow.com/questions/2785485/is-there-a-unique-android-device-id/2853253#2853253
     * 参考：https://www.jianshu.com/p/b6f4b0aca6b0
     *
     * @return device id
     */
    @RequiresPermission(value = Manifest.permission.READ_PHONE_STATE)
    public static String getDeviceId() {
        String serial = null;
        try {
            if (AndroidVersion.atLeast(26)) {
                //add in api 26, need Permission.READ_PHONE_STATE
                serial = Build.getSerial();
            } else {
                serial = Build.class.getField("SERIAL").get(null).toString();
            }
        } catch (Exception e) {
            Timber.w("getDeviceId error: " + e.getMessage());
        }
        if (serial == null) {
            //serial 需要一个初始化
            serial = "serial"; // 随便一个初始化
        }
        //使用硬件信息拼凑出来的15位号码
        return new UUID(M_SZ_DEV_ID_SHORT.hashCode(), serial.hashCode()).toString();
    }

    @SuppressLint("ObsoleteSdkInt")
    public static void printSystemInfo() {
        Date date = new Date(System.currentTimeMillis());
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = dateFormat.format(date);
        StringBuilder sb = new StringBuilder();
        sb.append("_______  系统信息  ").append(time).append(" ______________");
        sb.append("\nID                 :").append(Build.ID);
        sb.append("\nBRAND              :").append(Build.BRAND);
        sb.append("\nMODEL              :").append(Build.MODEL);
        sb.append("\nRELEASE            :").append(Build.VERSION.RELEASE);
        sb.append("\nSDK                :").append(Build.VERSION.SDK);

        sb.append("\n_______ OTHER _______");
        sb.append("\nBOARD              :").append(Build.BOARD);
        sb.append("\nPRODUCT            :").append(Build.PRODUCT);
        sb.append("\nDEVICE             :").append(Build.DEVICE);
        sb.append("\nFINGERPRINT        :").append(Build.FINGERPRINT);
        sb.append("\nHOST               :").append(Build.HOST);
        sb.append("\nTAGS               :").append(Build.TAGS);
        sb.append("\nTYPE               :").append(Build.TYPE);
        sb.append("\nTIME               :").append(Build.TIME);
        sb.append("\nINCREMENTAL        :").append(Build.VERSION.INCREMENTAL);

        sb.append("\n_______ CUPCAKE-3 _______");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE) {
            sb.append("\nDISPLAY            :").append(Build.DISPLAY);
        }

        sb.append("\n_______ DONUT-4 _______");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.DONUT) {
            sb.append("\nSDK_INT            :").append(Build.VERSION.SDK_INT);
            sb.append("\nMANUFACTURER       :").append(Build.MANUFACTURER);
            sb.append("\nBOOTLOADER         :").append(Build.BOOTLOADER);
            sb.append("\nCPU_ABI            :").append(Build.CPU_ABI);
            sb.append("\nCPU_ABI2           :").append(Build.CPU_ABI2);
            sb.append("\nHARDWARE           :").append(Build.HARDWARE);
            sb.append("\nUNKNOWN            :").append(Build.UNKNOWN);
            sb.append("\nCODENAME           :").append(Build.VERSION.CODENAME);
        }

        sb.append("\n_______ GINGERBREAD-9 _______");
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
                sb.append("\nSERIAL             :").append(Build.SERIAL);
            }
        } catch (Exception ignore) {
        }

        Log.i("DEVICES", sb.toString());
    }

}