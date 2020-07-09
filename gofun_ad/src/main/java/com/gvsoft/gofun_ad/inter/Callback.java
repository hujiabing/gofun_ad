package com.gvsoft.gofun_ad.inter;

import com.gvsoft.gofun_ad.model.AdData;

import java.io.File;

public interface Callback {
    void onFailure(AdData data);

    void onResponse(AdData data, File file);
}
