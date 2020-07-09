package com.gvsoft.gofun_ad.util;

public enum AdResourceType {

    AD_IMAGE("ad_image", 0),
    AD_VIDEO("ad_video", 1),
    AD_LOTTIE("ad_lottie", 2);

    public String name;
    private int type;

    AdResourceType(String name, int type) {
        this.name = name;
        this.type = type;
    }
}
