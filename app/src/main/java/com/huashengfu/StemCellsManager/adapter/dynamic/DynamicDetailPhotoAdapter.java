package com.huashengfu.StemCellsManager.adapter.dynamic;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.huashengfu.StemCellsManager.R;
import com.huashengfu.StemCellsManager.adapter.BaseAdapter;
import com.huashengfu.StemCellsManager.entity.dynamic.DynamicDetail;
import com.huashengfu.StemCellsManager.utils.GlideRoundTransformation;

import java.util.ArrayList;
import java.util.List;

public class DynamicDetailPhotoAdapter extends BaseAdapter<DynamicDetailViewHolder> {

    private List<DynamicDetail> dynamicDetails = new ArrayList<>();

    public void addAll(List<DynamicDetail> collection) {
        this.dynamicDetails.addAll(collection);
    }

    public void add(DynamicDetail dynamicDetail){
        dynamicDetails.add(dynamicDetail);
    }

    public void clear(){
        dynamicDetails.clear();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_dynamic_detail_photo, parent, false);
        DynamicDetailPhotoViewHolder holder = new DynamicDetailPhotoViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        DynamicDetail dynamic = dynamicDetails.get(position);

        DynamicDetailPhotoViewHolder viewHolder = (DynamicDetailPhotoViewHolder) holder;
        Glide.with(viewHolder.itemView.getContext())
                .load(dynamic.getUrl())
                .placeholder(R.drawable.image_loading_pic)
                .transform(new GlideRoundTransformation(holder.itemView.getContext(), 5))
                .into(viewHolder.ivImage);

        if(dynamic.isVideo()){
            viewHolder.ivPlay.setVisibility(View.VISIBLE);
        }else{
            viewHolder.ivPlay.setVisibility(View.GONE);
        }

        if(getOnItemClickListener() != null){
            viewHolder.ivPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getOnItemClickListener().onItemClick(viewHolder.ivPlay, dynamic);
                }
            });

            viewHolder.ivImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getOnItemClickListener().onItemClick(viewHolder.ivImage, dynamic);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return dynamicDetails.size();
    }
}
