package com.gvsoft.gofun_ad.manager.carousel;

import android.graphics.Color;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.gvsoft.gofun_ad.manager.carousel.indicator.DotsIndicator;
import com.gvsoft.gofun_ad.manager.carousel.indicator.Indicator;
import com.gvsoft.gofun_ad.view.carousel.AdCarouselView;

import java.util.ArrayList;
import java.util.List;

public class CycleViewPager2Helper {
    private AdCarouselView cycleViewPager2;
    private RecyclerView.Adapter adapter;
    @ViewPager2.Orientation
    private int orientation = ViewPager2.ORIENTATION_HORIZONTAL;
    @ViewPager2.OffscreenPageLimit
    private int limit = 1;
    private CompositePageTransformer compositePageTransformer;
    private List<RecyclerView.ItemDecoration> itemDecorations;
    private AdCarouselView.OnBannerChangeListener onPagerChangeListener;


    private long autoTurningTime;

    private Indicator indicator;

    public CycleViewPager2Helper(@NonNull AdCarouselView cycleViewPager2) {
        this.cycleViewPager2 = cycleViewPager2;
    }

    public CycleViewPager2Helper setAdapter(@Nullable RecyclerView.Adapter adapter) {
        this.adapter = adapter;
        return this;
    }

    public CycleViewPager2Helper setOrientation(@ViewPager2.Orientation int orientation) {
        this.orientation = orientation;
        return this;
    }

    public CycleViewPager2Helper setOffscreenPageLimit(@ViewPager2.OffscreenPageLimit int limit) {
        this.limit = limit;
        return this;
    }

    public CycleViewPager2Helper addPageTransformer(@NonNull ViewPager2.PageTransformer pageTransformer) {
        if (compositePageTransformer == null) {
            compositePageTransformer = new CompositePageTransformer();
        }
        compositePageTransformer.addTransformer(pageTransformer);
        return this;
    }

    private CycleViewPager2Helper addItemDecoration(@NonNull RecyclerView.ItemDecoration itemDecoration) {
        if (itemDecorations == null) {
            itemDecorations = new ArrayList<>();
        }
        itemDecorations.add(itemDecoration);
        return this;
    }

    public CycleViewPager2Helper setOnPagerChangeListener(@NonNull AdCarouselView.OnBannerChangeListener callback) {
        onPagerChangeListener = callback;
        return this;
    }

    public CycleViewPager2Helper setAutoTurning(long autoTurningTime) {
        this.autoTurningTime = autoTurningTime;
        return this;
    }

    public CycleViewPager2Helper setIndicator(@Nullable Indicator indicator) {
        this.indicator = indicator;
        return this;
    }

    public CycleViewPager2Helper defaultIndicator() {
        return setDotsIndicator(8, Color.parseColor("#6034FF")
                , Color.parseColor("#DCE0DF"), 16, 0,
                25, 0, DotsIndicator.Direction.CENTER);
    }

    public CycleViewPager2Helper setDotsIndicator(float radius, @ColorInt int selectedColor,
                                                  @ColorInt int unSelectedColor, float dotsPadding,
                                                  int leftMargin, int bottomMargin, int rightMargin,
                                                  @DotsIndicator.Direction int direction) {
        DotsIndicator dotsIndicator = new DotsIndicator(cycleViewPager2.getContext());
        dotsIndicator.setRadius(radius);
        dotsIndicator.setSelectedColor(selectedColor);
        dotsIndicator.setUnSelectedColor(unSelectedColor);
        dotsIndicator.setDotsPadding(dotsPadding);
        dotsIndicator.setLeftMargin(leftMargin);
        dotsIndicator.setBottomMargin(bottomMargin);
        dotsIndicator.setRightMargin(rightMargin);
        dotsIndicator.setDirection(direction);
        this.indicator = dotsIndicator;
        return this;
    }

    public void build() {
        cycleViewPager2.setOrientation(orientation);
        cycleViewPager2.setOffscreenPageLimit(limit);

        if (adapter != null) {
            cycleViewPager2.setAdapter(adapter);
        }
        if (itemDecorations != null && !itemDecorations.isEmpty()) {
            for (RecyclerView.ItemDecoration itemDecoration : itemDecorations) {
                cycleViewPager2.addItemDecoration(itemDecoration);
            }
        }
        if (compositePageTransformer != null) {
            cycleViewPager2.setPageTransformer(compositePageTransformer);
        }
        if (onPagerChangeListener != null) {
            cycleViewPager2.setOnPagerChangeListener(onPagerChangeListener);
        }
        cycleViewPager2.setIndicator(indicator);
        if (autoTurningTime > 0) {
            cycleViewPager2.setAutoTurning(autoTurningTime);
        }
    }
}
