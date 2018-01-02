package com.xun.liverobot.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.telephony.TelephonyManager;

import com.xun.liverobot.app.LiveRobotApplication;

/**
 * Created by xunwang on 2017/12/29.
 */

public class AppUtil {
    public static String getDeviceId() {
        TelephonyManager tm = (TelephonyManager) LiveRobotApplication.getInstance().getSystemService(Context.TELEPHONY_SERVICE);
        @SuppressLint("MissingPermission") String deviceId = tm.getDeviceId();
        return deviceId;
    }
}
