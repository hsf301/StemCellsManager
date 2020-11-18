package com.huashengfu.StemCellsManager.adapter.goods;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.huashengfu.StemCellsManager.Constants;
import com.huashengfu.StemCellsManager.R;
import com.huashengfu.StemCellsManager.adapter.BaseAdapter;
import com.huashengfu.StemCellsManager.entity.goods.Goods;
import com.huashengfu.StemCellsManager.utils.GlideRoundTransformation;

import java.util.ArrayList;
import java.util.List;

public class GoodsAdapter extends BaseAdapter<GoodsViewHolder> {

    private List<Goods> goods = new ArrayList<>();

    public void addAll(List<Goods> collection) {
        this.goods.addAll(collection);
    }

    public int remove(Goods goods){
        int position = this.goods.indexOf(goods);
        this.goods.remove(goods);
        return position;
    }

    public void updatePic(Goods goods){
        for(Goods tmp : this.goods){
            if(tmp.getGoodsId() == goods.getGoodsId()){
                tmp.setFirstPicUrl(goods.getFirstPicUrl());
                return;
            }
        }
    }

    public void updateInfo(Goods goods){
        for(Goods tmp : this.goods){
            if(tmp.getGoodsId() == goods.getGoodsId()){
                tmp.setName(goods.getName());
                tmp.setCategoryId(goods.getCategoryId());
                tmp.setMinPrice(goods.getMinPrice());
                tmp.getLabels().clear();
                tmp.getLabels().addAll(goods.getLabels());
                return;
            }
        }
    }

    public void clear(){
        goods.clear();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_goods, parent, false);
        GoodsViewHolder viewHolder = new GoodsViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        GoodsViewHolder viewHolder = (GoodsViewHolder) holder;

        Goods goods = this.goods.get(position);

        Glide.with(holder.itemView.getContext())
                .load(goods.getFirstPicUrl())
                .placeholder(R.drawable.image_loading_pic)
                .transform(new GlideRoundTransformation(holder.itemView.getContext(), 5))
                .into(viewHolder.ivImage);

        viewHolder.tvTitle.setText(goods.getName());
        viewHolder.tvPrice.setText(String.valueOf(goods.getMinPrice()));
        viewHolder.tvCount.setText(String.format(holder.itemView.getResources().getString(R.string.str_goods_count), goods.getSellSum()));

        if(goods.getIsUpperShelf().equals(Constants.Status.Goods.yes)) {
            viewHolder.btnOffline.setText(R.string.btn_offline_goods);
        }else {
            viewHolder.btnOffline.setText(R.string.btn_online_goods);
        }

        if(getOnItemClickListener() != null){
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getOnItemClickListener().onItemClick(viewHolder.itemView, goods);
                }
            });

            viewHolder.btnOffline.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getOnItemClickListener().onItemClick(viewHolder.btnOffline, goods);
                }
            });

            viewHolder.ivMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getOnItemClickListener().onItemClick(viewHolder.ivMenu, goods);
                }
            });
        }

        viewHolder.adapter.clear();
        viewHolder.adapter.addAll(goods.getLabels());
        viewHolder.adapter.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return goods.size();
    }
}
