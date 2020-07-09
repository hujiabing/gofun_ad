package com.hjb.myapplication;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.gvsoft.gofun_ad.manager.carousel.CycleViewPager2Helper;
import com.gvsoft.gofun_ad.manager.carousel.adapter.AdBannerAdapter;
import com.gvsoft.gofun_ad.model.AdData;
import com.gvsoft.gofun_ad.view.carousel.AdCarouselView;

import java.util.List;

public class BannerDialog extends Dialog implements View.OnClickListener {

    private AdCarouselView mBanner;

    private BannerDialog(Builder builder) {
        this(R.style.dark_dialog, builder);
    }

    private BannerDialog(int themeResId, Builder builder) {
        super(builder.context, themeResId);
        init(builder);
    }

    private void init(Builder builder) {
        setContentView(R.layout.dialog_banner);
        mBanner = findViewById(R.id.banner);
        mBanner.setLayoutParams(new FrameLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        findViewById(R.id.iv_close).setOnClickListener(this);
        CycleViewPager2Helper pagerHelper = new CycleViewPager2Helper(mBanner);
        AdBannerAdapter adapter = new AdBannerAdapter(builder.context, builder.list, new AdImageLoader());
        if (builder.onBannerClickListener != null) {
            adapter.setOnItemClickListener(new AdBannerAdapter.OnItemClickListener<AdData>() {
                @Override
                public void onItemClick(AdData item, int position) {
                    builder.onBannerClickListener.OnBannerClick(position, item);
                }
            });
        }
        pagerHelper
                .setAdapter(adapter)
                .defaultIndicator();
        if (builder.onPagerChangeListener != null) {
            pagerHelper.setOnPagerChangeListener(new AdCarouselView.OnBannerChangeListener() {
                @Override
                public void onPageSelected(int position, int realPosition) {
                    if (builder.onPagerChangeListener != null) {
                        Object o = null;
                        if (realPosition >= 0 && builder.list != null && builder.list.size() > realPosition) {
                            o = builder.list.get(realPosition);
                        }
                        builder.onPagerChangeListener.onPageSelected(position, realPosition, o);
                    }
                }
            });
        }
        if (builder.dismissCallBack != null) {
            setOnDismissListener(builder.dismissCallBack);
        }
        pagerHelper.setAutoTurning(builder.cycleTime)
                .build();
    }


    public interface OnPagerChangeListener {

        void onPageSelected(int position, int realPosition, Object item);

    }


    @Override
    public void show() {
        super.show();

        try {
            WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;

            getWindow().getDecorView().setPadding(0, 0, 0, 0);

            getWindow().setAttributes(layoutParams);
        } catch (Exception e) {

        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_close:
                /*if(dismissCallBack!=null){
                    dismissCallBack.dismiss();
                }*/
                dismiss();
                break;
        }
    }


    public interface OnBannerClickListener<T> {
        void OnBannerClick(int position, T bean);
    }

    public static class Builder<T> {

        private List<T> list;
        private Context context;
        private OnDismissListener dismissCallBack;
        private OnPagerChangeListener onPagerChangeListener;
        private OnBannerClickListener onBannerClickListener;
        private long cycleTime = 3000L;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setData(List data) {
            list = data;
            return this;
        }

        public Builder setCycleTime(int time) {
            cycleTime = time;
            return this;
        }

        public Builder setOnBannerClickListener(OnBannerClickListener bannerClickListener) {
            this.onBannerClickListener = bannerClickListener;
            return this;
        }

        public Builder setOnPageChangedListener(OnPagerChangeListener onPagerChangeListener) {
            this.onPagerChangeListener = onPagerChangeListener;
            return this;
        }

        public Builder setDismissCallback(OnDismissListener callback) {
            this.dismissCallBack = callback;
            return this;
        }

        public BannerDialog build() {
            return new BannerDialog(this);
        }
    }
}
