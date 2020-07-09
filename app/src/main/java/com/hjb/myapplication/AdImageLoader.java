package com.hjb.myapplication;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.gvsoft.gofun_ad.inter.AdImageLoaderInterface;
import com.gvsoft.gofun_ad.inter.OnAdShowCallback;
import com.gvsoft.gofun_ad.model.AdData;

import java.io.File;

public class AdImageLoader implements AdImageLoaderInterface {

    private RequestManager req;

    @Override
    public void displayImage(Context context, Object path, View imageView, OnAdShowCallback onAdShowCallback) {
        if (path == null || imageView == null) return;
        if (path instanceof AdData && imageView instanceof ImageView) {
            AdData data = (AdData) path;
            String url = data.getViewUrl();

            req = Glide.with(context);
            RequestBuilder<Drawable> load = null;
            if (!TextUtils.isEmpty(data.getFile())) {
                load = req.load(new File(data.getFile()));
            } else {
                load = req.load(url);
            }
            load.centerCrop().addListener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    if (onAdShowCallback != null) {
                        onAdShowCallback.onAdShow();
                    }
                    return false;
                }
            }).into((ImageView) imageView);
        }
    }

    @Override
    public void cancel() {
        if (req != null) {
            req.pauseRequests();
        }
    }
}
