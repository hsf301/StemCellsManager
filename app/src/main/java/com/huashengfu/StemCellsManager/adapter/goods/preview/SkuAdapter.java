package com.huashengfu.StemCellsManager.adapter.goods.preview;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.huashengfu.StemCellsManager.R;
import com.huashengfu.StemCellsManager.adapter.BaseAdapter;
import com.huashengfu.StemCellsManager.entity.response.goods.Sku;
import com.huashengfu.StemCellsManager.utils.GlideRoundTransformation;

import java.util.ArrayList;
import java.util.List;

public class SkuAdapter extends BaseAdapter<SkuViewHolder> {

    private List<Sku> skus = new ArrayList<>();

    public void addAll(List<Sku> collection) {
        if(collection == null)
            return;
        this.skus.addAll(collection);
    }

    public void add(Sku sku){
        skus.add(sku);
    }

    public void clear(){
        skus.clear();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_goods_preview_sku, parent, false);
        SkuViewHolder viewHolder = new SkuViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        SkuViewHolder viewHolder = (SkuViewHolder) holder;

        Sku sku = skus.get(position);

        Glide.with(holder.itemView.getContext())
                .load(sku.getSkuPic())
                .placeholder(R.drawable.image_loading_pic_small)
                .transform(new GlideRoundTransformation(holder.itemView.getContext(), 5))
                .into(viewHolder.ivImage);
    }

    @Override
    public int getItemCount() {
        return skus.size();
    }
}
