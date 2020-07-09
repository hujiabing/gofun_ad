package com.gvsoft.gofun_ad.manager.banner;

import androidx.annotation.NonNull;

import com.gvsoft.gofun_ad.inter.AdImageLoaderInterface;
import com.gvsoft.gofun_ad.inter.AdLottieLoaderInterface;
import com.gvsoft.gofun_ad.inter.OnAdClickListener;
import com.gvsoft.gofun_ad.manager.GoFunAd;
import com.gvsoft.gofun_ad.manager.Lifecycle;
import com.gvsoft.gofun_ad.manager.carousel.AdBannerBuilder;

public class AdBannerCallback {

    private GoFunAd goFunAd;
    private Lifecycle lifecycle;
    private AdImageLoaderInterface imageLoader;
    private AdLottieLoaderInterface lottieLoader;

    public AdBannerCallback(GoFunAd goFunAd, @NonNull Lifecycle lifecycle,
                            AdImageLoaderInterface imageLoader, AdLottieLoaderInterface lottieLoader) {

        this.goFunAd = goFunAd;
        this.lifecycle = lifecycle;
        this.imageLoader = imageLoader;
        this.lottieLoader = lottieLoader;
    }


    public AdBannerBuilder setAdCallback(OnAdClickListener onAdClickListener) {

        return new AdBannerBuilder(goFunAd, lifecycle, imageLoader, lottieLoader, onAdClickListener);
    }
}
