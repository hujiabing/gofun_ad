package com.gvsoft.gofun_ad.manager;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.gvsoft.gofun_ad.manager.banner.AdBannerLoader;
import com.gvsoft.gofun_ad.manager.carousel.AdViewPagerLoader;
import com.gvsoft.gofun_ad.manager.splash.AdSplashLoader;
import com.gvsoft.gofun_ad.util.Preconditions;

public class GoFunAd {


    private Context context;
    private final AdEngine engine;//单例

    private static volatile GoFunAd goFunAd;//单例
    private static volatile boolean isInitializing;
    private final AdRequestManagerRetriever requestManagerRetriever;//单例

    GoFunAd(@NonNull Context context,
            @NonNull AdEngine engine,
            @NonNull AdRequestManagerRetriever requestManagerRetriever) {
        this.context = context;
        this.engine = engine;
        this.requestManagerRetriever = requestManagerRetriever;
    }

    public static AdSplashLoader asSplash(@NonNull FragmentActivity context) {
        return getRetriever(context).get(context).asSplash();
    }

    public static AdBannerLoader asBanner(@NonNull FragmentActivity context) {
        return getRetriever(context).get(context).asBanner();
    }


    public static AdViewPagerLoader asViewPager(@NonNull Context context) {
        return getRetriever(context).get(context).asViewPager();
    }

    @NonNull
    private static AdRequestManagerRetriever getRetriever(@Nullable Context context) {
        // Context could be null for other reasons (ie the user passes in null), but in practice it will
        // only occur due to errors with the Fragment lifecycle.
        Preconditions.checkNotNull(
                context,
                "You cannot start a load on a not yet attached View or a Fragment where getActivity() "
                        + "returns null (which usually occurs when getActivity() is called before the Fragment "
                        + "is attached or after the Fragment is destroyed).");
        return GoFunAd.get(context).getRequestManagerRetriever();
    }

    public static GoFunAd get(@NonNull Context context) {
        if (goFunAd == null) {
            synchronized (GoFunAd.class) {
                if (goFunAd == null) {
                    checkAndInitializeGoFunAd(context);
                }
            }
        }

        return goFunAd;
    }

    @NonNull
    public AdRequestManagerRetriever getRequestManagerRetriever() {
        return this.requestManagerRetriever;
    }

    private static void checkAndInitializeGoFunAd(Context context) {
        if (isInitializing) {
            throw new IllegalStateException("You cannot call GoFunAd.get() in registerComponents(),"
                    + " use the provided GoFunAd instance instead");
        }
        isInitializing = true;
        goFunAd = new GoFunAdBuilder().build(context);
        isInitializing = false;
    }

    public AdEngine getEngine() {
        return engine;
    }
}
