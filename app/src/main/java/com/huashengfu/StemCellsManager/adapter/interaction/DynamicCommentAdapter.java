package com.huashengfu.StemCellsManager.adapter.interaction;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.huashengfu.StemCellsManager.Constants;
import com.huashengfu.StemCellsManager.R;
import com.huashengfu.StemCellsManager.adapter.BaseAdapter;
import com.huashengfu.StemCellsManager.entity.interaction.DynamicComment;
import com.huashengfu.StemCellsManager.utils.GlideRoundTransformation;

import java.util.ArrayList;
import java.util.List;

public class DynamicCommentAdapter extends BaseAdapter<DynamicCommentViewHolder> {

    private List<DynamicComment> dynamicComments = new ArrayList<>();

    public void addAll(List<DynamicComment> collection) {
        dynamicComments.addAll(collection);
    }

    public void clear(){
        dynamicComments.clear();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_interaction_dynamic_comment, parent, false);
        DynamicCommentViewHolder viewHolder = new DynamicCommentViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        DynamicCommentViewHolder viewHolder = (DynamicCommentViewHolder) holder;

        DynamicComment dynamicComment = dynamicComments.get(position);

        viewHolder.tvReview.setText(String.format(holder.itemView.getResources().getString(R.string.str_interaction_review), dynamicComment.getCommentSum()));
        viewHolder.tvContent.setText(dynamicComment.getSubTitle());

        Glide.with(holder.itemView.getContext())
                .load(dynamicComment.getPicUrl())
                .placeholder(R.drawable.image_loading_pic_small)
                .transform(new GlideRoundTransformation(holder.itemView.getContext(), 2))
                .into(viewHolder.ivImage);

        if(dynamicComment.getStatus().equals(Constants.Status.Comment.yes)){
            viewHolder.dot.setVisibility(View.GONE);
        }else{
            viewHolder.dot.setVisibility(View.VISIBLE);
        }

        if(getOnItemClickListener() != null){
            viewHolder.btnView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getOnItemClickListener().onItemClick(viewHolder.btnView, dynamicComment);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return dynamicComments.size();
    }
}
