package com.gvsoft.gofun_ad.inter;

import android.content.Context;
import android.view.View;

import java.io.Serializable;


public interface AdImageLoaderInterface<T extends View> extends Serializable {

    void displayImage(Context context, Object path, T imageView, OnAdShowCallback callback);

    void cancel();
}
