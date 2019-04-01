package com.bupt.colorfulroute.util;

import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import java.util.List;

/**
 * @author CheatGZ
 * @date 2019/3/24.
 * description：
 */
public class JudgeNetAndGPS {

    //判断网络连接是否可用
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
        } else {
            //如果仅仅是用来判断网络连接，则可以使用 cm.getActiveNetworkInfo().isAvailable();
            NetworkInfo[] allNetworkInfo = connectivityManager.getAllNetworkInfo();
            if (allNetworkInfo != null) {
                for (int i = 0; i < allNetworkInfo.length; i++) {
                    if (allNetworkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


    //判断GPS是否打开
    public static boolean isGpsEnabled(Context context) {
        LocationManager locationManager = ((LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE));
        List<String> accessibleProviders = locationManager.getProviders(true);
        return accessibleProviders != null && accessibleProviders.size() > 0;
    }

    //判断WIFI是否打开
    public static boolean isWifiEnabled(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        TelephonyManager systemService = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        return ((connectivityManager.getActiveNetworkInfo() != null && connectivityManager
                .getActiveNetworkInfo().getState() == NetworkInfo.State.CONNECTED) || systemService
                .getNetworkType() == TelephonyManager.NETWORK_TYPE_UMTS);
    }

    //判断是否移动网络
    public static boolean is4rd(Context context) {
        ConnectivityManager systemService = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = systemService.getActiveNetworkInfo();
        if (networkInfo != null
                && networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
            return true;
        }
        return false;
    }

    //判断是否WIFI网络
    public static boolean isWifi(Context context) {
        ConnectivityManager systemService = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = systemService.getActiveNetworkInfo();
        if (networkInfo != null
                && networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        }
        return false;
    }

}
