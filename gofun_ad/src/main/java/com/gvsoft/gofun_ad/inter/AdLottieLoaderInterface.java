package com.gvsoft.gofun_ad.inter;

import android.content.Context;
import android.widget.RelativeLayout;

import java.io.Serializable;


public interface AdLottieLoaderInterface extends Serializable {

    void displayLottie(Context context, Object path, RelativeLayout view, OnAdShowCallback callback);

    void cancel();
}
