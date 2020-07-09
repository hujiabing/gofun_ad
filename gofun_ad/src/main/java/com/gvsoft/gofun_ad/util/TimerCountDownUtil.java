package com.gvsoft.gofun_ad.util;

import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;

import com.gvsoft.gofun_ad.inter.TimerDownCallback;

public class TimerCountDownUtil {
    TimerDownCallback timerDownCallback;

    private final long mMillisInFuture;

    private final long mCountdownInterval = 1000;

    private long mStopTimeInFuture;

    private boolean mCancelled = false;

    public TimerCountDownUtil(int second, TimerDownCallback timerDownCallback) {
        mMillisInFuture = second * 1000;
        this.timerDownCallback = timerDownCallback;
    }

    public synchronized final void cancel() {
        mCancelled = true;
        mHandler.removeMessages(MSG);
    }

    public synchronized final TimerCountDownUtil start() {
        mCancelled = false;
        if (mMillisInFuture <= 0) {
            if (timerDownCallback != null) {
                timerDownCallback.onFinish();
            }
            return this;
        }
        mStopTimeInFuture = SystemClock.elapsedRealtime() + mMillisInFuture;
        mHandler.sendMessage(mHandler.obtainMessage(MSG));

        return this;
    }

    private static final int MSG = 1;

    // handles counting down
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            synchronized (TimerCountDownUtil.this) {
                if (mCancelled) {
                    return;
                }
                final long millisLeft = mStopTimeInFuture - SystemClock.elapsedRealtime();
                if (millisLeft <= 0) {
                    if (timerDownCallback != null) {
                        timerDownCallback.onFinish();
                    }
                } else {
                    long lastTickStart = SystemClock.elapsedRealtime();
                    if (timerDownCallback != null) {
                        timerDownCallback.onTime((int) ((millisLeft + 1000) / 1000));
                    }
                    // take into account user's onTick taking time to execute
                    long lastTickDuration = SystemClock.elapsedRealtime() - lastTickStart;
                    long delay;

                    if (millisLeft < mCountdownInterval) {
                        // just delay until done
                        delay = millisLeft - lastTickDuration;

                        // special case: user's onTick took more than interval to
                        // complete, trigger onFinish without delay
                        if (delay < 0) delay = 0;
                    } else {
                        delay = mCountdownInterval - lastTickDuration;

                        // special case: user's onTick took more than interval to
                        // complete, skip to next interval
                        while (delay < 0) delay += mCountdownInterval;
                    }

                    sendMessageDelayed(obtainMessage(MSG), delay);

                }
            }
        }
    };

}
