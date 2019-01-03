package com.example.user.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;

import java.util.Date;
import java.util.UUID;

public class SystemUtility {

    /** 結束App */
    public static void finishApp(Activity activity) {
        ActivityCompat.finishAffinity(activity);
    }

    /**
     * 檢查目前有無網路的狀態
     */
    public static boolean checkNetworkEnable(Context context) {
        ConnectivityManager mgrConnectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = mgrConnectivity.getActiveNetworkInfo();

        if (activeNetwork != null && activeNetwork.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 檢查目前是否有開GPS定位（GPS跟Wi-Fi定位），若無開啟GPS則顯示提示Dialog <br>
     *
     * @param context Context
     * @return 若有開GPS為true
     */
    public static boolean checkGPSEnable(final Context context) {
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }

        if (!gps_enabled && !network_enabled) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 檢查目前有sim卡
     *
     * @param context Context
     * @return 若有sim卡為true
     */
    public static boolean isSimReady(Context context) {
        TelephonyManager telMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return (telMgr.getSimState() == TelephonyManager.SIM_STATE_READY);
    }

    /** 取得時間 */
    public static String getTime() {
        long mTime = new Date().getTime();//System.currentTimeMillis();
        return String.valueOf(mTime);
    }

    /**
     * 取得UUID
     *
     * @param context Context
     * @return uuid
     */

    public static String getUUID(Context context) {
        String uuid = UUID.randomUUID().toString();
        return uuid;
    }

    /**
     * 取得IMEI
     *
     * @param context Context
     * @return imei
     */
//    public static String getIMEI(Context context) {
//        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
//        String imei = tm.getDeviceId();
//        if ((imei == null) || (imei.trim().equals(""))) {
//            return "";
//        } else {
//            tm = null;
//            return imei;
//        }
//    }

    /**
     * getAppId 取得Package Name
     */
    public static String getAppId() {
//        return context.getApplicationContext().getPackageName();
        return "com.fisc";
    }

    /**
     * getAppVersion 取得程式版本
     */
    public static String getAppVersion(Context context) {
        String appVersion = "";
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packageInfo = null;
        try {
            packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            appVersion = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
        }
        return appVersion;
    }

    /**
     * getDeviceID  取得裝置PUID
     */
    public static String getDeviceID() {

        // get psuedo unique id
        String serial = "";

        // 13 位
        String szDevIDShort = "35" +
                Build.BOARD.length() % 10 + Build.BRAND.length() % 10 +
                Build.CPU_ABI.length() % 10 + Build.DEVICE.length() % 10 +
                Build.DISPLAY.length() % 10 + Build.HOST.length() % 10 +
                Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10 +
                Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10 +
                Build.TAGS.length() % 10 + Build.TYPE.length() % 10 +
                Build.USER.length() % 10;

        try {
            serial = Build.class.getField("SERIAL").get(null).toString();
            // API >= 9 使用 serial 號
            return new UUID(szDevIDShort.hashCode(), serial.hashCode()).toString();
            // sample
        } catch (Exception exception) {
        }

        // 15 位
        return new UUID(szDevIDShort.hashCode(), serial.hashCode()).toString();
    }

    /**
     * getOSVersion  取得裝置系統版本
     */
    public static String getOSVersion() {
        return "Android;" + Build.VERSION.RELEASE;
    }

}
