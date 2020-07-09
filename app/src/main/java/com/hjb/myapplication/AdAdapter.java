package com.hjb.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class AdAdapter extends RecyclerView.Adapter<AdAdapter.BaseViewHolder> {

    private List<AdBean> data = new ArrayList<>();

    public AdAdapter(Context context, List<AdBean> data) {
        this.context = context;
        if (data != null) {
            this.data.addAll(data);
        }
    }

    public void add(List<AdBean> data) {
        if (data != null) {
            this.data.addAll(data);
        }
        notifyDataSetChanged();
    }

    private Context context;

    @Override
    public int getItemViewType(int position) {
        AdBean item = getItem(position);
        if (item.getType() == 1) {
            return 1;
        } else {
            return 0;
        }
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        if (viewType == 0) {
            return new ImageViewHolder(LayoutInflater.from(context).inflate(R.layout.item_ad_image, parent, false));
        } else {
            return new LottieViewHolder(LayoutInflater.from(context).inflate(R.layout.item_ad_lottie, parent, false));
        }
    }

    private AdBean getItem(int position) {
        return data.get(position);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        AdBean item = getItem(position);
        if (holder instanceof ImageViewHolder) {
            ImageViewHolder viewHolder = (ImageViewHolder) holder;
            Glide.with(context).load(item.getUrl()).into(viewHolder.mIvImg);
        }else {
            LottieViewHolder viewHolder = (LottieViewHolder) holder;
            viewHolder.mLottieView.setAnimation("badge_motion_chengshihuoditu.json");
            viewHolder.mLottieView.playAnimation();
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    static class LottieViewHolder extends BaseViewHolder {

        LottieAnimationView mLottieView;

        LottieViewHolder(View view) {
            super(view);
            mLottieView = view.findViewById(R.id.lottie_view);
        }
    }


    static class ImageViewHolder extends BaseViewHolder {

        ImageView mIvImg;

        ImageViewHolder(View view) {
            super(view);
            mIvImg = view.findViewById(R.id.iv_img);
        }
    }

    static class BaseViewHolder extends RecyclerView.ViewHolder {

        BaseViewHolder(View view) {
            super(view);

        }
    }

}
