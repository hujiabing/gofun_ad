package com.gvsoft.gofun_ad.util;

import android.util.Log;

import com.gvsoft.gofun_ad.BuildConfig;

import static com.gvsoft.gofun_ad.util.AdLogUtils.LogLevel.d;
import static com.gvsoft.gofun_ad.util.AdLogUtils.LogLevel.e;
import static com.gvsoft.gofun_ad.util.AdLogUtils.LogLevel.i;
import static com.gvsoft.gofun_ad.util.AdLogUtils.LogLevel.v;
import static com.gvsoft.gofun_ad.util.AdLogUtils.LogLevel.w;

public class AdLogUtils {


    private static String TAG = "===AD_LOG===>";

    private static final boolean LOG_ENABLE = BuildConfig.GOFUN_DEBUG;


    public static void i(Class<?> clazz, String msg) {
        if (LOG_ENABLE)
            showLogCompletion(clazz.getName(), msg, i);
    }

    public static void i(String msg) {
        if (LOG_ENABLE)
            showLogCompletion(msg, i);
    }

    public static void i(String TAG, String msg) {
        if (LOG_ENABLE)
            showLogCompletion(TAG, msg, i);
    }

    public static void d(String TAG, String msg) {
        if (LOG_ENABLE)
            showLogCompletion(TAG, msg, d);
    }



    public static void d(Class<?> clazz, String msg) {
        if (LOG_ENABLE)
            showLogCompletion(clazz.getName(), msg, d);
    }

    public static void d(String msg) {
        if (LOG_ENABLE)
            showLogCompletion(msg, d);
    }

    public static void w(Class<?> clazz, String msg) {
        if (LOG_ENABLE)
            showLogCompletion(clazz.getName(), msg, w);
    }

    public static void w(String msg) {
        if (LOG_ENABLE)
            showLogCompletion(msg, w);
    }


    public static void e(String TAG, String msg) {
        if (LOG_ENABLE)
            showLogCompletion(TAG, msg, e);
    }

    public static void e(Class<?> clazz, String msg) {
        if (LOG_ENABLE)
            showLogCompletion(clazz.getName(), msg, e);
    }

    public static void e(String msg) {
        if (LOG_ENABLE)
            showLogCompletion(msg, e);
    }


    public static void v(Class<?> clazz, String msg) {
        if (LOG_ENABLE)
            showLogCompletion(clazz.getName(), msg, v);
    }

    public static void v(String msg) {
        if (LOG_ENABLE)
            showLogCompletion(msg, v);
    }


    private static int showCount = 4 * 1024;

    private static void showLogCompletion(String log, LogLevel level) {
        showLogCompletion(TAG, log, level);
    }

    /**
     * 分段打印出较长log文本
     *
     * @param log   原log文本
     * @param level
     */
    private static void showLogCompletion(String TAG, String log, LogLevel level) {
        if (log.length() > showCount) {
            String show = log.substring(0, showCount);
            switch (level) {
                case v:
                    Log.v(TAG, show);
                    break;
                case d:
                    Log.d(TAG, show);
                    break;
                case i:
                    Log.i(TAG, show);
                    break;
                case w:
                    Log.w(TAG, show);
                    break;
                case e:
                    Log.e(TAG, show);
                    break;
            }
            if ((log.length() - showCount) > showCount) {//剩下的文本还是大于规定长度
                String partLog = log.substring(showCount, log.length());
                showLogCompletion(partLog, level);
            } else {
                String surplusLog = log.substring(showCount, log.length());
                switch (level) {
                    case v:
                        Log.v(TAG, surplusLog);
                        break;
                    case d:
                        Log.d(TAG, surplusLog);
                        break;
                    case i:
                        Log.i(TAG, surplusLog);
                        break;
                    case w:
                        Log.w(TAG, surplusLog);
                        break;
                    case e:
                        Log.e(TAG, surplusLog);
                        break;
                }
            }

        } else {
            switch (level) {
                case v:
                    Log.v(TAG, log);
                    break;
                case d:
                    Log.d(TAG, log);
                    break;
                case i:
                    Log.i(TAG, log);
                    break;
                case w:
                    Log.w(TAG, log);
                    break;
                case e:
                    Log.e(TAG, log);
                    break;
            }
        }
    }

    enum LogLevel {
        v, d, i, w, e
    }

}
