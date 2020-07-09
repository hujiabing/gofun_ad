package com.hjb.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.gvsoft.gofun_ad.inter.OnAdClickListener;
import com.gvsoft.gofun_ad.inter.OnAdCompleteListener;
import com.gvsoft.gofun_ad.manager.GoFunAd;
import com.gvsoft.gofun_ad.manager.carousel.OnDataCallback;
import com.gvsoft.gofun_ad.model.AdData;
import com.gvsoft.gofun_ad.util.AdLogUtils;
import com.gvsoft.gofun_ad.view.AdImageView;
import com.gvsoft.gofun_ad.view.AdLottieView;
import com.gvsoft.gofun_ad.view.banner.AdBannerView;
import com.gvsoft.gofun_ad.view.splash.AdSplashView;
import com.gvsoft.gofun_ad_lottie.AdLottieViewLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {


    @BindView(R.id.animation_view)
    AdLottieView mAnimationView;
    @BindView(R.id.rl_banner)
    RelativeLayout mRlBanner;
    @BindView(R.id.iv_img)
    AdImageView mIvImg;
    @BindView(R.id.ad_view)
    AdSplashView mAdView;

    @BindView(R.id.ad_banner)
    AdBannerView mAdBanner;
    @BindView(R.id.rl_container)
    RelativeLayout mRlContainer;


    private String picture;
    private String video;
    private List<AdData> list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        picture = "http://pic1.win4000.com/pic/e/88/1a3166c6ef_250_300.jpg";
        video = "http://video.pp.cn/fs08/2017/01/16/3/200_528893ee2d1573573679809fb7a75b70.mp4";
        String s0 = "https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=1253337603,395256110&fm=26&gp=0.jpg";
        String s1 = "http://pic1.win4000.com/pic/e/88/1a3166c6ef_250_300.jpg";
        String s2 = "https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=1276311334,1321953888&fm=26&gp=0.jpg";
        String s3 = "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=3621776991,4158598969&fm=26&gp=0.jpg";
        String s4 = "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=1337035159,297537113&fm=26&gp=0.jpg";
        String s5 = "https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=1553210546,907004571&fm=11&gp=0.jpg";
        String s6 = "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=2237446662,1891834123&fm=26&gp=0.jpg";
        List<String> strings = new ArrayList<>();
        strings.add(s0);
        strings.add(s1);
        strings.add(s2);
        strings.add(s3);
        strings.add(s4);
        strings.add(s5);
        strings.add(s6);
        list = new ArrayList<>();
        for (int i = 0; i < strings.size(); i++) {
            AdData e = new AdData();
            e.setId(i + "");
            e.setViewUrl(strings.get(i));
            e.setPlayTime(15);
            list.add(e);
        }
        GoFunAd
                .asViewPager(this)
                .getViewPagerData(new OnDataCallback() {
                    @Override
                    public void onDataReady(List<AdData> data) {
                        AdLogUtils.e("===onDataReady=====" + Thread.currentThread().getName());
                        BannerDialog build = new BannerDialog.Builder(MainActivity.this).setCycleTime(5000).setOnBannerClickListener(new BannerDialog.OnBannerClickListener() {
                            @Override
                            public void OnBannerClick(int position, Object bean) {
                            }
                        }).setOnPageChangedListener(new BannerDialog.OnPagerChangeListener() {
                            @Override
                            public void onPageSelected(int position, int realPosition, Object item) {

                            }
                        }).setData(list).build();
                        build.show();
                    }

                    @Override
                    public void onFail() {

                    }
                });
        GoFunAd
                .asBanner(this)
                .setLoader(new AdImageLoader(), new AdLottieViewLoader())
                .setAdCallback(new OnAdClickListener<AdData>() {
                    @Override
                    public void onAdClick(AdData data) {
                        AdLogUtils.e("=====onAdClick==="+data.toString());
                    }
                })
                .into(mAdBanner);

    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @OnClick({R.id.iv_img, R.id.animation_view, R.id.ad_view, R.id.rl_banner})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_img:
                AdData adData2 = new AdData();
                adData2.setViewUrl(picture);
                mIvImg.setImageLoader(new AdImageLoader()).load(adData2);

                break;
            case R.id.animation_view:
                BannerDialog build = new BannerDialog.Builder(MainActivity.this).setCycleTime(5000).setOnBannerClickListener(new BannerDialog.OnBannerClickListener() {
                    @Override
                    public void OnBannerClick(int position, Object bean) {
                    }
                }).setOnPageChangedListener(new BannerDialog.OnPagerChangeListener() {
                    @Override
                    public void onPageSelected(int position, int realPosition, Object item) {

                    }
                }).setData(list).build();
                build.show();
                break;
            case R.id.ad_view:
                GoFunAd
                        .asSplash(this)
                        .setLoader(new AdImageLoader(), new AdLottieViewLoader())
                        .setAdCallback(new OnAdCompleteListener() {
                            @Override
                            public void onTimeDown(int time) {

                            }

                            @Override
                            public void onComplete() {

                            }
                        }, new OnAdClickListener<AdData>() {
                            @Override
                            public void onAdClick(AdData data) {
                                AdLogUtils.e("=====main===>" + data.toString());
                            }
                        })
                        .into(mAdView);
                break;
            case R.id.rl_banner:
                startActivity(new Intent(this,ThirdActivity.class));
                break;
        }
    }
}