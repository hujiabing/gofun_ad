package com.gvsoft.gofun_ad.manager.splash;

import androidx.annotation.NonNull;

import com.gvsoft.gofun_ad.inter.AdImageLoaderInterface;
import com.gvsoft.gofun_ad.inter.AdLottieLoaderInterface;
import com.gvsoft.gofun_ad.inter.OnAdClickListener;
import com.gvsoft.gofun_ad.inter.OnAdCompleteListener;
import com.gvsoft.gofun_ad.manager.GoFunAd;
import com.gvsoft.gofun_ad.manager.Lifecycle;

public class AdSplashCallback {

    private long timeOut;
    private GoFunAd goFunAd;
    private Lifecycle lifecycle;
    private AdImageLoaderInterface imageLoader;
    private AdLottieLoaderInterface lottieLoader;

    public AdSplashCallback(GoFunAd goFunAd, @NonNull Lifecycle lifecycle, long timeOut,
                            AdImageLoaderInterface imageLoader, AdLottieLoaderInterface lottieLoader) {
        this.timeOut = timeOut;
        this.goFunAd = goFunAd;
        this.lifecycle = lifecycle;
        this.imageLoader = imageLoader;
        this.lottieLoader = lottieLoader;
    }


    public AdSplashBuilder setAdCallback(OnAdCompleteListener completeListener,
                                         OnAdClickListener onAdClickListener) {

        return new AdSplashBuilder(goFunAd, timeOut, lifecycle, imageLoader, lottieLoader, completeListener, onAdClickListener);
    }

}
