package com.gvsoft.gofun_ad.manager;

import android.content.Context;

import androidx.annotation.NonNull;

import com.gvsoft.gofun_ad.manager.banner.AdBannerLoader;
import com.gvsoft.gofun_ad.manager.carousel.AdViewPagerLoader;
import com.gvsoft.gofun_ad.manager.splash.AdSplashLoader;


public class AdRequestManager {


    protected GoFunAd goFunAd;
    protected Lifecycle lifecycle;
    protected Context context;


    public AdRequestManager(@NonNull GoFunAd goFunAd,
                            @NonNull Lifecycle lifecycle,
                            @NonNull Context context) {
        this.goFunAd = goFunAd;
        this.lifecycle = lifecycle;
        this.context = context;
    }


    public AdSplashLoader asSplash() {
        return new AdSplashLoader(goFunAd, lifecycle);
    }

    public AdBannerLoader asBanner() {
        return new AdBannerLoader(goFunAd, lifecycle);
    }

    public AdViewPagerLoader asViewPager() {
        return new AdViewPagerLoader(goFunAd);
    }

}
