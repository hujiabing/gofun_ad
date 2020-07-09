package com.gvsoft.gofun_ad.manager.splash;

import androidx.annotation.NonNull;

import com.gvsoft.gofun_ad.inter.AdImageLoaderInterface;
import com.gvsoft.gofun_ad.inter.AdLottieLoaderInterface;
import com.gvsoft.gofun_ad.manager.GoFunAd;
import com.gvsoft.gofun_ad.manager.Lifecycle;

public class AdSplashLoader {


    private long timeOut = 5000;
    private GoFunAd goFunAd;
    private Lifecycle lifecycle;

    public AdSplashLoader(GoFunAd goFunAd, @NonNull Lifecycle lifecycle) {
        this.goFunAd = goFunAd;
        this.lifecycle = lifecycle;
    }

    public AdSplashCallback setLoader(AdImageLoaderInterface imageLoader) {
        return setLoader(imageLoader, null);
    }

    public AdSplashCallback setLoader(AdLottieLoaderInterface lottieLoader) {
        return setLoader(null, lottieLoader);
    }


    public AdSplashCallback setLoader(AdImageLoaderInterface imageLoader,
                                      AdLottieLoaderInterface lottieLoader) {
        return new AdSplashCallback(goFunAd, lifecycle, timeOut, imageLoader, lottieLoader);
    }

    public AdSplashLoader setTimeOut(long timeOut) {
        this.timeOut = timeOut;
        return this;
    }

}
