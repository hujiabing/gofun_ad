package com.gvsoft.gofun_ad.manager.splash;

import androidx.annotation.NonNull;

import com.gvsoft.gofun_ad.inter.AdImageLoaderInterface;
import com.gvsoft.gofun_ad.inter.AdLottieLoaderInterface;
import com.gvsoft.gofun_ad.inter.OnAdClickListener;
import com.gvsoft.gofun_ad.inter.OnAdCompleteListener;
import com.gvsoft.gofun_ad.inter.OnDataReadyCallback;
import com.gvsoft.gofun_ad.manager.GoFunAd;
import com.gvsoft.gofun_ad.manager.Lifecycle;
import com.gvsoft.gofun_ad.manager.LifecycleListener;
import com.gvsoft.gofun_ad.model.AdData;
import com.gvsoft.gofun_ad.util.AdLogUtils;
import com.gvsoft.gofun_ad.util.AdUtils;
import com.gvsoft.gofun_ad.util.MainThreadUtils;
import com.gvsoft.gofun_ad.view.splash.AdSplashView;

public class AdSplashBuilder implements LifecycleListener {
    private static String TAG = "==AdSplashBuilder==";
    private AdSplashView adView;
    private GoFunAd goFunAd;
    private AdImageLoaderInterface imageLoader;
    private AdLottieLoaderInterface lottieLoader;
    private OnAdCompleteListener completeListener;
    private OnAdClickListener onAdClickListener;
    private long timeOut;
    protected Lifecycle lifecycle;
    private final Runnable addSelfToLifecycle = new Runnable() {
        @Override
        public void run() {
            lifecycle.addListener(AdSplashBuilder.this);
        }
    };

    /**
     * 开屏页支持视频,json,图片,并且每次加载只加载上次缓存的
     * 轮播图一次性展示
     * banner 根据时间间隔替换
     */
    public AdSplashBuilder(GoFunAd goFunAd, long timeOut, @NonNull Lifecycle lifecycle,
                           AdImageLoaderInterface imageLoader, AdLottieLoaderInterface lottieLoader,
                           OnAdCompleteListener onAdCompleteListener, OnAdClickListener onAdClickListener) {
        this.goFunAd = goFunAd;
        this.lifecycle = lifecycle;
        this.imageLoader = imageLoader;
        this.lottieLoader = lottieLoader;
        this.completeListener = onAdCompleteListener;
        this.onAdClickListener = onAdClickListener;
        this.timeOut = timeOut;

        if (AdUtils.isOnBackgroundThread()) {
            MainThreadUtils.runOnUiThread(addSelfToLifecycle);
        } else {
            lifecycle.addListener(AdSplashBuilder.this);
        }
    }

    /**
     * 取缓存数据展示 获取新的数据缓存
     *
     * @param
     */
    public void into(AdSplashView adView) {
        if (adView != null) {
            adView.setTimeOut(timeOut)
                    .setOnAdCompleteListener(completeListener)
                    .setLottieLoader(lottieLoader)
                    .setImageLoader(imageLoader)
                    .setOnAdClickListener(onAdClickListener);
        }
        goFunAd.getEngine().getSingleData(new OnDataReadyCallback() {
            @Override
            public void skip() {
                AdLogUtils.e("==没有本地数据跳过==");
                if (adView != null) {
                    adView.removeTimeOut();
                }
                if (completeListener != null) {
                    completeListener.onComplete();
                }
            }

            @Override
            public void onDataReady(AdData adData) {
                MainThreadUtils.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (isDestroy) {
                            return;
                        }
                        AdSplashBuilder.this.adView = adView;
                        if (adView != null && adData != null) {
                            adView
                                    .setData(adData)
                                    .load();
                        }
                    }
                });
            }
        });
    }


    @Override
    public void onStart() {
        if (adView != null) {
            adView.onRestart();
        }
    }

    @Override
    public void onStop() {
        AdLogUtils.e(TAG + "onStop");
        if (adView != null) {
            adView.onStop();
        }
    }

    private boolean isDestroy = false;

    @Override
    public void onDestroy() {
        isDestroy = true;
        AdLogUtils.e(TAG + "onDestroy");
        if (adView != null) {
            adView.onDestroy();
        }
    }
}
