package com.gvsoft.gofun_ad.manager.carousel;

import androidx.annotation.NonNull;

import com.gvsoft.gofun_ad.inter.AdImageLoaderInterface;
import com.gvsoft.gofun_ad.inter.AdLottieLoaderInterface;
import com.gvsoft.gofun_ad.inter.OnAdClickListener;
import com.gvsoft.gofun_ad.manager.GoFunAd;
import com.gvsoft.gofun_ad.manager.Lifecycle;
import com.gvsoft.gofun_ad.manager.LifecycleListener;
import com.gvsoft.gofun_ad.model.AdData;
import com.gvsoft.gofun_ad.util.AdLogUtils;
import com.gvsoft.gofun_ad.util.AdUtils;
import com.gvsoft.gofun_ad.util.MainThreadUtils;
import com.gvsoft.gofun_ad.view.banner.AdBannerView;

import java.util.List;

public class AdBannerBuilder implements LifecycleListener {
    private static String TAG = "==AdBannerBuilder==----";
    private AdBannerView adView;
    private GoFunAd goFunAd;
    private AdImageLoaderInterface imageLoader;
    private AdLottieLoaderInterface lottieLoader;
    private OnAdClickListener onAdClickListener;
    protected Lifecycle lifecycle;
    private final Runnable addSelfToLifecycle = new Runnable() {
        @Override
        public void run() {
            lifecycle.addListener(AdBannerBuilder.this);
        }
    };

    /**
     * 开屏页支持视频,json,图片,并且每次加载只加载上次缓存的
     * 轮播图一次性展示
     * banner 根据时间间隔替换
     */
    public AdBannerBuilder(GoFunAd goFunAd, @NonNull Lifecycle lifecycle,
                           AdImageLoaderInterface imageLoader, AdLottieLoaderInterface lottieLoader,
                           OnAdClickListener onAdClickListener) {
        this.goFunAd = goFunAd;
        this.lifecycle = lifecycle;
        this.imageLoader = imageLoader;
        this.lottieLoader = lottieLoader;
        this.onAdClickListener = onAdClickListener;
        if (AdUtils.isOnBackgroundThread()) {
            MainThreadUtils.runOnUiThread(addSelfToLifecycle);
        } else {
            lifecycle.addListener(AdBannerBuilder.this);
        }
    }

    /**
     * 取缓存数据展示 获取新的数据缓存
     *
     * @param
     */
    public void into(AdBannerView adView) {
        AdBannerBuilder.this.adView = adView;
        if (adView != null) {
            adView
                    .setLottieLoader(lottieLoader)
                    .setImageLoader(imageLoader)
                    .setOnAdClickListener(onAdClickListener);
        }
        getData();
    }

    int count = 3;
    int position = 0;
    boolean restart = false;

    private void getData() {
        goFunAd.getEngine().getBannerData(new OnDataCallback() {
            @Override
            public void onDataReady(List<AdData> data) {
                if (isDestroy){
                    return;
                }
                if (data != null && data.size() > 0) {
//                    AdData adData = data.get(0);
                    AdLogUtils.e(TAG + "getData==>" + position);
                    AdData adData = data.get(position);
                    position++;
                    if (position > 2) {
                        position = 0;
                    }
                    MainThreadUtils.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (adView != null && adData != null) {
                                adView
                                        .setData(adData)
                                        .load();
                            }
                        }
                    });
                }

            }

            @Override
            public void onFail() {

            }
        });
    }


    @Override
    public void onStart() {
        if (!restart) {
            restart = true;
            return;
        }

        if (adView != null) {
            adView.onRestart();
        }
        AdLogUtils.e(TAG + "onStart");
        getData();
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
