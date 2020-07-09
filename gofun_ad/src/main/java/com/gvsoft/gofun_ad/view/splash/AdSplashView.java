package com.gvsoft.gofun_ad.view.splash;

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
import com.gvsoft.gofun_ad.inter.OnAdCompleteListener;
import com.gvsoft.gofun_ad.inter.OnAdShowCallback;
import com.gvsoft.gofun_ad.inter.TimerDownCallback;
import com.gvsoft.gofun_ad.model.AdData;
import com.gvsoft.gofun_ad.util.AdPathUtils;
import com.gvsoft.gofun_ad.util.AdResourceType;
import com.gvsoft.gofun_ad.util.TimerCountDownUtil;
import com.gvsoft.gofun_ad.view.AdImageView;
import com.gvsoft.gofun_ad.view.AdLottieView;
import com.gvsoft.gofun_ad.view.AdVideoView;

public class AdSplashView extends RelativeLayout implements OnAdShowCallback {

    private AdImageLoaderInterface imageLoader;
    private AdLottieLoaderInterface lottieLoader;
    private AdData mData;
    private Context context;
    private OnAdCompleteListener onAdCompleteListener;
    private AdResourceType adType;
    private long DEFAULT_TIME_OUT = 5000;
    private int skip_time = 0;
    private TimerCountDownUtil timer;
    private RelativeLayout mRlContent;
    private View mLinBottomLogo;
    private ImageView mIvLogo;
    private ImageView mIvAdTag;
    private View subview;

    public AdSplashView(Context context) {
        this(context, null);
    }

    public AdSplashView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AdSplashView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initView();
    }

    private void initView() {
        LayoutInflater.from(context).inflate(R.layout.ad_splash_view, this, true);
        mRlContent = findViewById(R.id.rl_content);
        mLinBottomLogo = findViewById(R.id.lin_bottom_logo);
        mIvLogo = findViewById(R.id.iv_logo);
        mIvAdTag = findViewById(R.id.iv_ad_tag);
    }

    public AdSplashView setLottieLoader(AdLottieLoaderInterface imageLoader) {
        this.lottieLoader = imageLoader;
        return this;
    }

    public AdSplashView setImageLoader(AdImageLoaderInterface imageLoader) {
        this.imageLoader = imageLoader;
        return this;
    }

    public AdSplashView setOnAdCompleteListener(OnAdCompleteListener onAdCompleteListener) {
        this.onAdCompleteListener = onAdCompleteListener;
        return this;
    }

    public AdSplashView setData(AdData data) {
        mData = data;
        setView();
        return this;
    }

    private Runnable timeOutRunnable = new Runnable() {
        @Override
        public void run() {
            if (onAdCompleteListener != null) {
                onAdCompleteListener.onComplete();
            }
        }
    };

    public void removeTimeOut() {
        removeCallbacks(timeOutRunnable);
    }


    /**
     * 单位秒
     *
     * @param skipTime
     */
    private void setAutoSkip(int skipTime) {
        if (isDestroy) return;
        skip_time = skipTime;
        if (skipTime == 0) return;
        timer = new TimerCountDownUtil(skipTime, new TimerDownCallback() {
            @Override
            public void onTime(int time) {
                skip_time = time;
                if (onAdCompleteListener != null) {
                    onAdCompleteListener.onTimeDown(time);
                }
            }

            @Override
            public void onFinish() {
                skip_time = 0;
                if (onAdCompleteListener != null) {
                    onAdCompleteListener.onComplete();
                }
            }
        });
        timer.start();
    }


    @Override
    public void onAdShow() {
        if (isDestroy) return;
        removeTimeOut();
        if (mData != null) {
            post(new Runnable() {
                @Override
                public void run() {
                    mIvAdTag.setVisibility(mData.getShowAdTab() == 1 ? VISIBLE : INVISIBLE);
                    setAutoSkip(mData.getPlayTime());
                }
            });
        }
    }

    public AdSplashView setTimeOut(long timeOut) {
        postDelayed(timeOutRunnable, timeOut > 0 ? timeOut : DEFAULT_TIME_OUT);
        return this;
    }


    OnAdClickListener onAdClickListener;

    public AdSplashView setOnAdClickListener(OnAdClickListener onAdClickListener) {
        this.onAdClickListener = onAdClickListener;
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
        mLinBottomLogo.setVisibility(mData.getShowLogoTab() == 1 ? VISIBLE : GONE);
        if (adType == AdResourceType.AD_VIDEO) {// 0 图片 1 lottie 2 视频
            ((AdVideoView) subview).load(context, mData, this::onAdShow);
        } else if (adType == AdResourceType.AD_LOTTIE) {
            if (lottieLoader != null && subview instanceof AdLottieView) {
                ((AdLottieView) subview).setLottieLoader(lottieLoader).load(mData, this::onAdShow);
            }
        } else {
            if (imageLoader != null && subview instanceof AdImageView) {
                ((AdImageView) subview).setImageLoader(imageLoader).load(mData, this::onAdShow);
            }
        }
    }

    private void setView() {
        if (isDestroy) return;
        if (mData != null) {
            if (TextUtils.isEmpty(mData.getViewUrl())) {
                return;
            }
            adType = AdPathUtils.getAdTypeByUrl(mData.getViewUrl());
            if (adType == AdResourceType.AD_VIDEO) {// 0 图片 1 lottie 2 视频
                subview = new AdVideoView(context);
                subview.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                mRlContent.addView(subview, 0);
            } else if (adType == AdResourceType.AD_LOTTIE) {
                subview = new AdLottieView(context);
                subview.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                mRlContent.addView(subview, 0);
            } else {
                subview = new AdImageView(context);
                subview.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                mRlContent.addView(subview, 0);
            }

        }
    }

    public void onRestart() {
        if (subview != null && subview instanceof AdVideoView) {
            ((AdVideoView) subview).reLoad(context);
        }
        setAutoSkip(skip_time);
    }

    public void onStop() {
        if (subview != null && subview instanceof AdVideoView) {
            ((AdVideoView) subview).stopPlay();
        }
        removeTime();
    }

    boolean isDestroy = false;

    public void onDestroy() {
        isDestroy = true;
        removeTimeOut();
        if (subview != null && subview instanceof AdVideoView) {
            ((AdVideoView) subview).onDestroy();
        }
        removeTime();
    }

    private void removeTime() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }


}
