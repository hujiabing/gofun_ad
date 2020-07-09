package com.gvsoft.gofun_ad.manager.carousel;

import com.gvsoft.gofun_ad.manager.GoFunAd;

public class AdViewPagerLoader {

    private GoFunAd goFunAd;

    public AdViewPagerLoader(GoFunAd goFunAd) {
        this.goFunAd = goFunAd;
    }

    public void getViewPagerData(OnDataCallback callback) {
        goFunAd.getEngine().getViewPagerData(callback);
    }

}
