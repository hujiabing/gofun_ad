package com.gvsoft.gofun_ad_lottie;


import android.content.Context;
import android.text.TextUtils;
import android.widget.RelativeLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.LottieOnCompositionLoadedListener;
import com.gvsoft.gofun_ad.inter.AdLottieLoaderInterface;
import com.gvsoft.gofun_ad.inter.OnAdShowCallback;
import com.gvsoft.gofun_ad.model.AdData;

import java.io.File;
import java.io.FileInputStream;

public class AdLottieViewLoader implements AdLottieLoaderInterface {

    LottieAnimationView mLottieView;

    @Override
    public void displayLottie(Context context, Object path, RelativeLayout view, final OnAdShowCallback callback) {
        try {
            if (path == null || view == null) return;
            if (path instanceof AdData) {
                AdData data = (AdData) path;
                view.removeAllViews();
                mLottieView = new LottieAnimationView(context);
                mLottieView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
                view.addView(mLottieView);
                if (!TextUtils.isEmpty(data.getFile())) {
                    FileInputStream inputStream = new FileInputStream(new File(data.getFile()));
                    mLottieView.setAnimation(inputStream, data.getViewUrl());
                } else {
                    mLottieView.setAnimationFromUrl(data.getViewUrl(), data.getViewUrl());
                }
                mLottieView.addLottieOnCompositionLoadedListener(new LottieOnCompositionLoadedListener() {
                    @Override
                    public void onCompositionLoaded(LottieComposition composition) {
                        mLottieView.setComposition(composition);
                        mLottieView.setMinAndMaxProgress(0f, 1f);
                        mLottieView.enableMergePathsForKitKatAndAbove(true);
                        mLottieView.playAnimation();
                        if (callback != null) {
                            callback.onAdShow();
                        }
                    }
                });
            }
        } catch (Exception e) {

        }
    }

    @Override
    public void cancel() {
        if (mLottieView != null) {
            mLottieView.cancelAnimation();
        }
    }
}
