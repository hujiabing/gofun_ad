package com.gvsoft.gofun_ad.view.banner;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;

import com.gvsoft.gofun_ad.R;
import com.gvsoft.gofun_ad.inter.AdImageLoaderInterface;
import com.gvsoft.gofun_ad.inter.AdLottieLoaderInterface;
import com.gvsoft.gofun_ad.inter.OnAdClickListener;
import com.gvsoft.gofun_ad.model.AdData;
import com.gvsoft.gofun_ad.util.AdPathUtils;
import com.gvsoft.gofun_ad.util.AdResourceType;
import com.gvsoft.gofun_ad.view.AdImageView;
import com.gvsoft.gofun_ad.view.AdLottieView;
import com.gvsoft.gofun_ad.view.AdVideoView;

public class AdBannerView extends RelativeLayout {

    private AdImageLoaderInterface imageLoader;
    private AdLottieLoaderInterface lottieLoader;
    private AdData mData;
    private Context context;
    private AdResourceType adType;
    private ImageView mIvAdTag;
    private View subview;

    public AdBannerView(Context context) {
        this(context, null);
    }

    public AdBannerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AdBannerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initView();
    }

    private void initView() {
        LayoutInflater.from(context).inflate(R.layout.ad_view, this, true);
        mIvAdTag = findViewById(R.id.iv_ad_tag);
    }

    public AdBannerView setData(AdData data) {
        mData = data;
        setView();
        return this;
    }

    public void load() {
        if (isDestroy) return;
        if (mData == null) return;
        if (onAdClickListener != null && !TextUtils.isEmpty(mData.getActionUrl()) && subview != null) {
            subview.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    onAdClickListener.onAdClick(mData);
                }
            });
        }
        if (adType == AdResourceType.AD_VIDEO) {// 0 图片 1 lottie 2 视频
            ((AdVideoView) subview).load(context, mData, null);
        } else if (adType == AdResourceType.AD_LOTTIE) {
            if (lottieLoader != null && subview instanceof AdLottieView) {
                ((AdLottieView) subview).setLottieLoader(lottieLoader).load(mData, null);
            }
        } else {
            if (imageLoader != null && subview instanceof AdImageView) {
                ((AdImageView) subview).setImageLoader(imageLoader).load(mData, null);
            }
        }
    }

    private void setView() {
        if (isDestroy) return;
        if (mData != null) {
            if (TextUtils.isEmpty(mData.getViewUrl())) {
                return;
            }
            removeAllViews();
            adType = AdPathUtils.getAdTypeByUrl(mData.getViewUrl());
            if (adType == AdResourceType.AD_VIDEO) {// 0 图片 1 lottie 2 视频
                subview = new AdVideoView(context);
                subview.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                addView(subview, 0);
            } else if (adType == AdResourceType.AD_LOTTIE) {
                subview = new AdLottieView(context);
                subview.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                addView(subview, 0);
            } else {
                subview = new AdImageView(context);
                subview.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                addView(subview, 0);
            }
        }
    }

    OnAdClickListener onAdClickListener;

    public AdBannerView setOnAdClickListener(OnAdClickListener onAdClickListener) {
        this.onAdClickListener = onAdClickListener;
        return this;
    }

    public AdBannerView setLottieLoader(AdLottieLoaderInterface imageLoader) {
        this.lottieLoader = imageLoader;
        return this;
    }

    public AdBannerView setImageLoader(AdImageLoaderInterface imageLoader) {
        this.imageLoader = imageLoader;
        return this;
    }

    public void onRestart() {
        if (subview != null && subview instanceof AdVideoView) {
            ((AdVideoView) subview).reLoad(context);
        }
    }

    public void onStop() {
        if (subview != null && subview instanceof AdVideoView) {
            ((AdVideoView) subview).stopPlay();
        }
    }

    boolean isDestroy = false;

    public void onDestroy() {
        isDestroy = true;
        if (subview != null && subview instanceof AdVideoView) {
            ((AdVideoView) subview).onDestroy();
        }
    }
}
