package com.xun.liverobot.utils;

import android.util.Log;

import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * Created by xunwang on 2017/12/20.
 */

public class LogUtil {
    private static String mClassname = LogUtil.class.getName();
    private static ArrayList<String> mMethods = new ArrayList<>();

    static {
        Method[] ms = LogUtil.class.getDeclaredMethods();
        Method[] var1 = ms;
        int var2 = ms.length;

        for (int var3 = 0; var3 < var2; ++var3) {
            Method m = var1[var3];
            mMethods.add(m.getName());
        }

    }

    public static void d(String tag, String msg) {
        Log.d(tag, getMsgWithLineNumber(msg));
    }

    public static void e(String tag, String msg) {
        Log.e(tag, msg);
    }

    public static String getMsgWithLineNumber(String msg) {
        try {
            StackTraceElement[] var1 = (new Throwable()).getStackTrace();
            int var2 = var1.length;

            for (int var3 = 0; var3 < var2; ++var3) {
                StackTraceElement st = var1[var3];
                if (!mClassname.equals(st.getClassName()) && !mMethods.contains(st.getMethodName())) {
                    int b = st.getClassName().lastIndexOf(".") + 1;
                    String TAG = st.getClassName().substring(b);
                    String message = TAG + "->" + st.getMethodName() + "():" + st.getLineNumber() + "->" + msg;
                    return message;
                }
            }
        } catch (Exception var8) {
            ;
        }

        return msg;
    }
}
