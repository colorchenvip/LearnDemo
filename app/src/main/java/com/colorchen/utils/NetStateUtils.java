package com.colorchen.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by color on 16/8/18 17:29.
 */

public class NetStateUtils {
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info =  manager.getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isWifiNetworkConnected(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        boolean isWifiNetwork = false;
        if (networkInfo != null) {
            //是否是WIFI环境接入
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                isWifiNetwork = true;
            } else {
                isWifiNetwork = false;
            }
        }
        return isWifiNetwork;
    }
}
