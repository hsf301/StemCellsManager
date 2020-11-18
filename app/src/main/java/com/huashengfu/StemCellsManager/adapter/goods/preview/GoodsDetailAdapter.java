package com.huashengfu.StemCellsManager.adapter.goods.preview;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.huashengfu.StemCellsManager.Constants;
import com.huashengfu.StemCellsManager.R;
import com.huashengfu.StemCellsManager.adapter.BaseAdapter;
import com.huashengfu.StemCellsManager.entity.response.goods.Detail;
import com.huashengfu.StemCellsManager.utils.GlideRoundTransformation;

import java.util.ArrayList;
import java.util.List;

public class GoodsDetailAdapter extends BaseAdapter<GoodsDetailViewHolder> {

    private ArrayList<Detail> details = new ArrayList<>();
    private boolean modify = false;

    public void add(Detail detail){
        details.add(detail);
    }

    public void clear(){
        details.clear();
    }

    public void addAll(List<Detail> collection){
        details.addAll(collection);
    }

    public List<Detail> getDetail() {
        return details;
    }

    public boolean isModify() {
        return modify;
    }

    public void setModify(boolean modify) {
        this.modify = modify;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_goods_preview_detail, parent, false);
        GoodsDetailViewHolder viewHolder = new GoodsDetailViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        GoodsDetailViewHolder viewHolder = (GoodsDetailViewHolder) holder;

        Detail detail = details.get(position);

        if(detail.getType().equals(Constants.Type.Detail.pic)){
            viewHolder.ivImage.setVisibility(View.VISIBLE);
            viewHolder.tvDesc.setVisibility(View.GONE);

            Glide.with(holder.itemView.getContext())
                    .load(detail.getDetailsText())
                    .placeholder(R.drawable.image_loading_pic)
                    .transform(new GlideRoundTransformation(holder.itemView.getContext(), 10))
                    .into(viewHolder.ivImage);
        }else if(detail.getType().equals(Constants.Type.Detail.text)){
            viewHolder.ivImage.setVisibility(View.GONE);
            viewHolder.tvDesc.setVisibility(View.VISIBLE);
            viewHolder.tvDesc.setText(detail.getDetailsText());
        }

    }

    @Override
    public int getItemCount() {
        return details.size();
    }
}
