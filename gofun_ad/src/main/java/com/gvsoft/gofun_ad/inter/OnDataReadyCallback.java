package com.gvsoft.gofun_ad.inter;

import com.gvsoft.gofun_ad.model.AdData;

public interface OnDataReadyCallback {

    void skip();

    void onDataReady(AdData adData);

}
