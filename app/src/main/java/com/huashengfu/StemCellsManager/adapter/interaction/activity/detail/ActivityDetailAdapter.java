package com.huashengfu.StemCellsManager.adapter.interaction.activity.detail;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.huashengfu.StemCellsManager.Constants;
import com.huashengfu.StemCellsManager.R;
import com.huashengfu.StemCellsManager.adapter.BaseAdapter;
import com.huashengfu.StemCellsManager.entity.response.activity.Detail;
import com.huashengfu.StemCellsManager.utils.GlideRoundTransformation;

import java.util.ArrayList;
import java.util.List;

public class ActivityDetailAdapter extends BaseAdapter<ActivityDetailViewHolder> {

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

    public List<Detail> getDetails() {
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_activity_preview_detail, parent, false);
        ActivityDetailViewHolder viewHolder = new ActivityDetailViewHolder(view);
        return viewHolder;
    }

    private void move(int from ,int to){
        details.add(to, details.remove(from));
        notifyItemMoved(from, to);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ActivityDetailViewHolder viewHolder = (ActivityDetailViewHolder) holder;

        Detail detail = details.get(position);

        if(detail.getType().equals(Constants.Type.Detail.pic)){
            viewHolder.ivImage.setVisibility(View.VISIBLE);
            viewHolder.tvDesc.setVisibility(View.GONE);

            Glide.with(holder.itemView.getContext())
                    .load(detail.getDetails())
                    .placeholder(R.drawable.image_loading_pic)
                    .transform(new GlideRoundTransformation(holder.itemView.getContext(), 5))
                    .into(viewHolder.ivImage);
        }else if(detail.getType().equals(Constants.Type.Detail.text)){
            viewHolder.ivImage.setVisibility(View.GONE);
            viewHolder.tvDesc.setVisibility(View.VISIBLE);
            viewHolder.tvDesc.setText(detail.getDetails());
        }

    }

    @Override
    public int getItemCount() {
        return details.size();
    }
}
