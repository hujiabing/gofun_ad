package com.gvsoft.gofun_ad.manager.banner;

import androidx.annotation.NonNull;

import com.gvsoft.gofun_ad.inter.AdImageLoaderInterface;
import com.gvsoft.gofun_ad.inter.AdLottieLoaderInterface;
import com.gvsoft.gofun_ad.manager.GoFunAd;
import com.gvsoft.gofun_ad.manager.Lifecycle;

public class AdBannerLoader {


    private GoFunAd goFunAd;
    private Lifecycle lifecycle;

    public AdBannerLoader(GoFunAd goFunAd, @NonNull Lifecycle lifecycle) {
        this.goFunAd = goFunAd;
        this.lifecycle = lifecycle;
    }

    public AdBannerCallback setLoader(AdImageLoaderInterface imageLoader) {
        return setLoader(imageLoader, null);
    }

    public AdBannerCallback setLoader(AdLottieLoaderInterface lottieLoader) {
        return setLoader(null, lottieLoader);
    }

    public AdBannerCallback setLoader(AdImageLoaderInterface imageLoader, AdLottieLoaderInterface lottieLoader) {

        return new AdBannerCallback(goFunAd, lifecycle, imageLoader, lottieLoader);
    }

}
