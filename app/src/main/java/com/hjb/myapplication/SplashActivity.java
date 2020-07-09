package com.hjb.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.gvsoft.gofun_ad.inter.OnAdClickListener;
import com.gvsoft.gofun_ad.inter.OnAdCompleteListener;
import com.gvsoft.gofun_ad.manager.GoFunAd;
import com.gvsoft.gofun_ad.model.AdData;
import com.gvsoft.gofun_ad.util.AdLogUtils;
import com.gvsoft.gofun_ad.view.splash.AdSplashView;
import com.gvsoft.gofun_ad_lottie.AdLottieViewLoader;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SplashActivity extends AppCompatActivity {

    @BindView(R.id.ad_view)
    AdSplashView mAdView;
    @BindView(R.id.tv_skip_time)
    TextView mTvSkipTime;
    @BindView(R.id.lin_splash_skip)
    LinearLayout mLinSplashSkip;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setStatusBarFullTransparent(this);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        GoFunAd
                .asSplash(this)
                .setTimeOut(5000)
                .setLoader(new AdImageLoader(), new AdLottieViewLoader())
                .setAdCallback(new OnAdCompleteListener() {
                    @Override
                    public void onTimeDown(int time) {
                        AdLogUtils.i("===onTimeDown===>" + time);
                        mLinSplashSkip.setVisibility(View.VISIBLE);
                        mTvSkipTime.setText(String.valueOf(time));
                    }

                    @Override
                    public void onComplete() {
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        finish();
                    }

                }, new OnAdClickListener<AdData>() {
                    @Override
                    public void onAdClick(AdData data) {
                        AdLogUtils.e("=====splash===>" + data.toString());
                    }
                })
                .into(mAdView);
    }

    @OnClick({R.id.ad_view, R.id.lin_splash_skip})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ad_view:
                break;
            case R.id.lin_splash_skip:
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
                break;
        }
    }
}
