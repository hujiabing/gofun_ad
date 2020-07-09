package com.gvsoft.gofun_ad.manager.carousel;

import com.gvsoft.gofun_ad.model.AdData;

import java.util.List;

public interface OnDataCallback {
    /**
     * 获取数据成功
     *
     * @param data
     */
    void onDataReady(List<AdData> data);

    /**
     * 获取数据失败
     */
    void onFail();
}
