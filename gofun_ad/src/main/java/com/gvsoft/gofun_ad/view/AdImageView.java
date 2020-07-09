package com.gvsoft.gofun_ad.view;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

import com.gvsoft.gofun_ad.inter.AdImageLoaderInterface;
import com.gvsoft.gofun_ad.inter.OnAdShowCallback;

public class AdImageView extends AppCompatImageView {

    private AdImageLoaderInterface imageLoader;

    private Context mContext;

    public AdImageView(Context context) {
        this(context, null);
    }

    public AdImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AdImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    public AdImageView setImageLoader(AdImageLoaderInterface imageLoader) {
        this.imageLoader = imageLoader;
        return this;
    }

    public void load(Object data, OnAdShowCallback callback) {
        if (imageLoader != null) {
            imageLoader.displayImage(mContext, data, this, callback);
        }
    }

    public void load(Object data) {
        load(data, null);
    }

    public void cancel() {
        if (imageLoader != null) {
            imageLoader.cancel();
        }
    }

}
