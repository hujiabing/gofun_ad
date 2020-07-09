package com.gvsoft.gofun_ad.view.carousel;

import android.content.Context;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.gvsoft.gofun_ad.manager.carousel.adapter.CyclePagerAdapter;
import com.gvsoft.gofun_ad.manager.carousel.indicator.Indicator;
import com.gvsoft.gofun_ad.util.AdLogUtils;

import java.lang.ref.WeakReference;
import java.util.Objects;

import static androidx.recyclerview.widget.RecyclerView.NO_POSITION;

public class AdCarouselView extends FrameLayout {

    private Context context;
    private ViewPager2 mViewPager2;
    private Indicator mIndicator;
    private boolean canAutoTurning = false;
    private long autoTurningTime;
    private boolean isTurning = false;
    private AutoTurningRunnable mAutoTurningRunnable;

    private int mPendingCurrentItem = NO_POSITION;

    public AdCarouselView(Context context) {
        this(context, null);
    }

    public AdCarouselView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AdCarouselView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        mViewPager2 = new ViewPager2(context);
        mViewPager2.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mViewPager2.setOffscreenPageLimit(1);

        CycleOnPageChangeCallback mCycleOnPageChangeCallback = new CycleOnPageChangeCallback();
        mViewPager2.registerOnPageChangeCallback(mCycleOnPageChangeCallback);

        mAutoTurningRunnable = new AutoTurningRunnable(this);

        addView(mViewPager2);
    }

    public void setAutoTurning(long autoTurningTime) {
        setAutoTurning(true, autoTurningTime);
    }

    public void setAutoTurning(boolean canAutoTurning, long autoTurningTime) {
        this.canAutoTurning = canAutoTurning;
        this.autoTurningTime = autoTurningTime;
        stopAutoTurning();
        startAutoTurning();
    }

    public void startAutoTurning() {
        if (!canAutoTurning || autoTurningTime <= 0 || isTurning) return;
        isTurning = true;
        postDelayed(mAutoTurningRunnable, autoTurningTime);
    }

    public void stopAutoTurning() {
        isTurning = false;
        removeCallbacks(mAutoTurningRunnable);
    }

    public void setAdapter(@Nullable RecyclerView.Adapter adapter) {
        if (mViewPager2.getAdapter() == adapter) return;
        mViewPager2.setAdapter(adapter);
        setCurrentItem(1, false);
        initIndicator();
        return;
    }

    @Nullable
    public RecyclerView.Adapter getAdapter() {
        return mViewPager2.getAdapter();
    }

    private int getPagerRealCount() {
        RecyclerView.Adapter adapter = getAdapter();
        if (adapter instanceof CyclePagerAdapter) {
            return ((CyclePagerAdapter) adapter).getRealItemCount();
        }
        return 0;
    }

    public void setOrientation(@ViewPager2.Orientation int orientation) {
        mViewPager2.setOrientation(orientation);
    }

    @ViewPager2.Orientation
    public int getOrientation() {
        return mViewPager2.getOrientation();
    }

    public void setPageTransformer(@Nullable ViewPager2.PageTransformer transformer) {
        mViewPager2.setPageTransformer(transformer);
    }

    public void addItemDecoration(@NonNull RecyclerView.ItemDecoration decor) {
        mViewPager2.addItemDecoration(decor);
    }

    public void addItemDecoration(@NonNull RecyclerView.ItemDecoration decor, int index) {
        mViewPager2.addItemDecoration(decor, index);
    }

    public void setCurrentItem(int item) {
        setCurrentItem(item, true);
    }

    public void setCurrentItem(int item, boolean smoothScroll) {
        AdLogUtils.d("setCurrentItem " + item);
        mViewPager2.setCurrentItem(item, smoothScroll);
        if (!smoothScroll && mIndicator != null) {
            mIndicator.onPageSelected(getRealCurrentItem());
        }
    }

    public int getCurrentItem() {
        return mViewPager2.getCurrentItem();
    }

    public int getRealCurrentItem() {
        return getCurrentItem() >= 1 ? getCurrentItem() - 1 : getCurrentItem();
    }

    public void setOffscreenPageLimit(@ViewPager2.OffscreenPageLimit int limit) {
        mViewPager2.setOffscreenPageLimit(limit);
    }

    public int getOffscreenPageLimit() {
        return mViewPager2.getOffscreenPageLimit();
    }

    private OnBannerChangeListener onPagerChangeListener;

    public void setOnPagerChangeListener(@NonNull OnBannerChangeListener callback) {
        onPagerChangeListener = callback;
    }

    public interface OnBannerChangeListener {

        void onPageSelected(int position, int realPosition);

    }

    @NonNull
    public ViewPager2 getViewPager2() {
        return mViewPager2;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int action = ev.getActionMasked();
        if (action == MotionEvent.ACTION_DOWN) {
            if (canAutoTurning && isTurning) {
                stopAutoTurning();
            }
        } else if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL ||
                action == MotionEvent.ACTION_OUTSIDE) {
            if (canAutoTurning) startAutoTurning();
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        startAutoTurning();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopAutoTurning();
    }

    @Nullable
    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);
        ss.mCurrentItem = getCurrentItem();
        AdLogUtils.d("onSaveInstanceState: " + ss.mCurrentItem);
        return ss;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        mPendingCurrentItem = ss.mCurrentItem;
        AdLogUtils.d("onRestoreInstanceState: " + mPendingCurrentItem);
        restorePendingState();
    }

    private void restorePendingState() {
        if (mPendingCurrentItem == NO_POSITION) {
            // No state to restore, or state is already restored
            return;
        }
        int currentItem = Math.max(0, Math.min(mPendingCurrentItem, Objects.requireNonNull(getAdapter()).getItemCount() - 1));
        AdLogUtils.d("restorePendingState: " + currentItem);
        mPendingCurrentItem = NO_POSITION;
        setCurrentItem(currentItem, false);
    }

    public void setIndicator(@Nullable Indicator indicator) {
        if (mIndicator == indicator) return;
        removeIndicatorView();
        mIndicator = indicator;
        initIndicator();
    }

    private void initIndicator() {
        if (mIndicator == null || getAdapter() == null) return;
        addView(mIndicator.getIndicatorView());
        mIndicator.onChanged(getPagerRealCount(), getRealCurrentItem());
    }

    private void removeIndicatorView() {
        if (mIndicator == null) return;
        removeView(mIndicator.getIndicatorView());
    }

    private class CycleOnPageChangeCallback extends ViewPager2.OnPageChangeCallback {
        private static final int INVALID_ITEM_POSITION = -1;
        private boolean isBeginPagerChange;
        private int mTempPosition = INVALID_ITEM_POSITION;

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            if (mIndicator != null) {
                mIndicator.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }
        }

        @Override
        public void onPageSelected(int position) {
            if (isBeginPagerChange) {
                mTempPosition = position;
            }
            if (onPagerChangeListener != null) {
                onPagerChangeListener.onPageSelected(position, getRealCurrentItem());
            }
            if (mIndicator != null) {
                mIndicator.onPageSelected(getRealCurrentItem());
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (state == ViewPager2.SCROLL_STATE_DRAGGING ||
                    (isTurning && state == ViewPager2.SCROLL_STATE_SETTLING)) {
                isBeginPagerChange = true;
            } else if (state == ViewPager2.SCROLL_STATE_IDLE) {
                isBeginPagerChange = false;
                int fixCurrentItem = getFixCurrentItem(mTempPosition);
                if (fixCurrentItem != INVALID_ITEM_POSITION && fixCurrentItem != mTempPosition) {
                    mTempPosition = INVALID_ITEM_POSITION;
                    setCurrentItem(fixCurrentItem, false);
                }
            }
            if (mIndicator != null) {
                mIndicator.onPageScrollStateChanged(state);
            }
        }

        private int getFixCurrentItem(final int position) {
            if (position == INVALID_ITEM_POSITION) return INVALID_ITEM_POSITION;
            final int lastPosition = Objects.requireNonNull(getAdapter()).getItemCount() - 1;
            int fixPosition = INVALID_ITEM_POSITION;
            if (position == 0) {
                fixPosition = lastPosition == 0 ? 0 : lastPosition - 1;
            } else if (position == lastPosition) {
                fixPosition = 1;
            }
            return fixPosition;
        }
    }

    static class AutoTurningRunnable implements Runnable {
        private final WeakReference<AdCarouselView> reference;

        AutoTurningRunnable(AdCarouselView cycleViewPager2) {
            this.reference = new WeakReference<>(cycleViewPager2);
        }

        @Override
        public void run() {
            AdCarouselView cycleViewPager2 = reference.get();
            if (cycleViewPager2 != null && cycleViewPager2.canAutoTurning && cycleViewPager2.isTurning) {
                int itemCount = Objects.requireNonNull(cycleViewPager2.getAdapter()).getItemCount();
                if (itemCount == 0) return;
                int nextItem = (cycleViewPager2.getCurrentItem() + 1) % itemCount;
                cycleViewPager2.setCurrentItem(nextItem, true);
                cycleViewPager2.postDelayed(cycleViewPager2.mAutoTurningRunnable, cycleViewPager2.autoTurningTime);
            }
        }
    }

    static class SavedState extends BaseSavedState {
        int mCurrentItem;

        SavedState(Parcel source) {
            super(source);
            readValues(source, null);
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        SavedState(Parcel source, ClassLoader loader) {
            super(source, loader);
            readValues(source, loader);
        }

        SavedState(Parcelable superState) {
            super(superState);
        }

        private void readValues(Parcel source, ClassLoader loader) {
            mCurrentItem = source.readInt();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(mCurrentItem);
        }

        public static final Creator<SavedState> CREATOR = new ClassLoaderCreator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel source, ClassLoader loader) {
                return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
                        ? new SavedState(source, loader)
                        : new SavedState(source);
            }

            @Override
            public SavedState createFromParcel(Parcel source) {
                return createFromParcel(source, null);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }
}
