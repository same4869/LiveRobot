package com.xun.liverobot.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by xunwang on 2017/12/20.
 */

public class NetworkUtils {
    public static Boolean isNetConneted(Context context) {
        ConnectivityManager connectManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectManager.getActiveNetworkInfo();
        if (networkInfo == null) {
            return false;
        } else {
            return networkInfo.isAvailable() && networkInfo.isConnected();
        }
    }
}
