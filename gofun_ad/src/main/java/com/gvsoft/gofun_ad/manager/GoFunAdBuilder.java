package com.gvsoft.gofun_ad.manager;

import android.content.Context;

import androidx.annotation.NonNull;

public final class GoFunAdBuilder {
    private AdEngine engine;

    GoFunAd build(@NonNull Context context) {
        if (engine == null) {
            engine =
                    new AdEngine(
                            context);
        }
        AdRequestManagerRetriever requestManagerRetriever =
                new AdRequestManagerRetriever();
        return new GoFunAd(context, engine, requestManagerRetriever);
    }
}
