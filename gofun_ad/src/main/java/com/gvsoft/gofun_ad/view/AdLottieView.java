package com.gvsoft.gofun_ad.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.gvsoft.gofun_ad.inter.AdLottieLoaderInterface;
import com.gvsoft.gofun_ad.inter.OnAdShowCallback;

public class AdLottieView extends RelativeLayout {

    private AdLottieLoaderInterface lottieLoader;
    private Context mContext;

    public AdLottieView(Context context) {
        this(context, null);
    }

    public AdLottieView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AdLottieView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public AdLottieView setLottieLoader(AdLottieLoaderInterface imageLoader) {
        this.lottieLoader = imageLoader;
        return this;
    }

    public void load(Object data) {
        load(data,null);
    }

    public void load(Object data, OnAdShowCallback callback) {
        if (data == null) {
            return;
        }
        if (lottieLoader != null) {
            lottieLoader.displayLottie(mContext, data, AdLottieView.this, callback);
        }
    }


    private void initView(Context context) {
        mContext = context;
    }

}
