package com.huashengfu.StemCellsManager.adapter.service;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.huashengfu.StemCellsManager.R;
import com.huashengfu.StemCellsManager.adapter.BaseAdapter;
import com.huashengfu.StemCellsManager.entity.service.ServiceTypeGroup;
import com.huashengfu.StemCellsManager.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class ServicePromotionFlagAdapter extends BaseAdapter<ServicePromotionFlagViewHolder> {

    private List<String> flags = new ArrayList<>();

    public void addAll(List<String> flags) {
        this.flags.addAll(flags);
    }

    public void add(String flag){
        if(StringUtils.isNullOrBlank(flag))
            return;
        flags.add(flag);
    }

    public List<String> getFlags() {
        return flags;
    }

    public int remove(String flag){
        int position = flags.indexOf(flag);
        flags.remove(flag);
        return position;
    }

    public void clear(){
        flags.clear();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_service_promotion_flag, parent, false);
        ServicePromotionFlagViewHolder viewHolder = new ServicePromotionFlagViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ServicePromotionFlagViewHolder viewHolder = (ServicePromotionFlagViewHolder) holder;

        viewHolder.tvName.setText(flags.get(position));
        if(getOnItemLongClickListener() != null){
            viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    return getOnItemLongClickListener().onItemLongClick(view, flags.get(position));
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return flags.size();
    }
}
