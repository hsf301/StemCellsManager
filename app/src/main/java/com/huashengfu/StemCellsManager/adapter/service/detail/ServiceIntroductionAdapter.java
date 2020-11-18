package com.huashengfu.StemCellsManager.adapter.service.detail;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.huashengfu.StemCellsManager.Constants;
import com.huashengfu.StemCellsManager.R;
import com.huashengfu.StemCellsManager.adapter.BaseAdapter;
import com.huashengfu.StemCellsManager.entity.service.Introduction;

import java.util.ArrayList;
import java.util.List;

public class ServiceIntroductionAdapter extends BaseAdapter<ServiceIntroductionViewHolder> {

    private ArrayList<Introduction> introductions = new ArrayList<>();
    private boolean modify = false;

    public void add(Introduction introduction){
        introductions.add(introduction);
    }

    public void addAll(List<Introduction> collection){
        for(Introduction introduction : collection){
            if(introduction.getType().equalsIgnoreCase(Constants.Type.Detail.pic))
                introduction.setPhoto(true);
            else if(introduction.getType().equalsIgnoreCase(Constants.Type.Detail.text))
                introduction.setText(true);
        }
        introductions.addAll(collection);
    }

    public void clear(){
        introductions.clear();
    }

    public List<Introduction> getIntroductions() {
        return introductions;
    }

    public boolean isModify() {
        return modify;
    }

    public void setModify(boolean modify) {
        this.modify = modify;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_service_detail_introduction, parent, false);
        ServiceIntroductionViewHolder viewHolder = new ServiceIntroductionViewHolder(view);
        return viewHolder;
    }

    private void move(int from ,int to){
        introductions.add(to, introductions.remove(from));
        notifyItemMoved(from, to);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ServiceIntroductionViewHolder viewHolder = (ServiceIntroductionViewHolder) holder;

        Introduction introduction = introductions.get(position);

        if(introduction.isPhoto()){
            viewHolder.ivImage.setVisibility(View.VISIBLE);
            viewHolder.tvContent.setVisibility(View.GONE);

            Glide.with(holder.itemView.getContext())
                    .load(introduction.getDetails())
                    .into(viewHolder.ivImage);

        }else if(introduction.isText()){
            viewHolder.ivImage.setVisibility(View.GONE);
            viewHolder.tvContent.setVisibility(View.VISIBLE);
            viewHolder.tvContent.setText(introduction.getDetails());
        }
    }

    @Override
    public int getItemCount() {
        return introductions.size();
    }
}
