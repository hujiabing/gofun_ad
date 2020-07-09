package com.gvsoft.gofun_ad.manager.carousel.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gvsoft.gofun_ad.R;
import com.gvsoft.gofun_ad.inter.AdImageLoaderInterface;

import java.util.ArrayList;
import java.util.List;

public class AdBannerAdapter<T> extends CyclePagerAdapter<AdBannerAdapter<T>.PagerViewHolder> {

    List<T> mData = new ArrayList<>();
    private AdImageLoaderInterface loaderInterface;

    private Context context;
    private OnItemClickListener mOnItemClickListener = null;

    public AdBannerAdapter(Context context, List<T> data, AdImageLoaderInterface adImageLoaderInterface) {
        this.context = context;
        loaderInterface = adImageLoaderInterface;
        if (data != null) {
            mData.addAll(data);
        }
    }

    public void replace(List<T> data) {
        this.mData.clear();
        if (data != null) {
            this.mData.addAll(data);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getRealItemCount() {
        return mData == null ? 0 : mData.size();
    }

    private T getItem(int position) {
        return mData.get(position);
    }

    @Override
    public void onBindRealViewHolder(@NonNull PagerViewHolder holder, int position) {
        T item = getItem(position);
        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mData.size() > position) {
                        mOnItemClickListener.onItemClick(item, position);
                    }
                }
            });
        }
        if (loaderInterface != null) {
            loaderInterface.displayImage(context, item, holder.mIvBannerImage, null);
        }
    }

    //define interface
    public interface OnItemClickListener<T> {
        void onItemClick(T item, int position);
    }

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    @NonNull
    @Override
    public PagerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PagerViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_banner_view, parent, false));
    }

    class PagerViewHolder extends RecyclerView.ViewHolder {

        private final ImageView mIvBannerImage;
        private final ImageView mIvBannerTag;

        PagerViewHolder(View view) {
            super(view);
            mIvBannerImage = view.findViewById(R.id.iv_banner_image);
            mIvBannerTag = view.findViewById(R.id.iv_banner_tag);
        }
    }
}
