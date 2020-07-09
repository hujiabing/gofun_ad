package com.gvsoft.gofun_ad.util;

import android.os.Handler;
import android.os.Looper;


public class MainThreadUtils {

    private static Handler mHandler = null;

    static {
        mHandler = new Handler(Looper.getMainLooper());
    }


    public static void runOnUiThread(Runnable task) {
        delayedRunOnMainThread(task, 0);
    }

    public static void delayedRunOnMainThread(Runnable task, long delayTime) {
        getMainHandler().postDelayed(task, delayTime);
    }

    private static Handler getMainHandler() {

        return mHandler;
    }

    public static void removeMainThreadTask(Runnable task) {
        getMainHandler().removeCallbacks(task);
    }


}
