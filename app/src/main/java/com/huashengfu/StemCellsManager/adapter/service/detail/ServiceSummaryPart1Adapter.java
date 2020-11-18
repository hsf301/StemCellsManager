package com.huashengfu.StemCellsManager.adapter.service.detail;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.huashengfu.StemCellsManager.R;
import com.huashengfu.StemCellsManager.adapter.BaseAdapter;
import com.huashengfu.StemCellsManager.entity.response.sms.Outline;

import java.util.ArrayList;
import java.util.List;

public class ServiceSummaryPart1Adapter extends BaseAdapter<ServiceSummaryPart1ViewHolder> {

    private List<Outline> outlines = new ArrayList<>();

    public void addAll(List<Outline> collection) {
        this.outlines.addAll(collection);
    }

    public void add(Outline outline){
        outlines.add(outline);
    }

    public void clear(){
        outlines.clear();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_service_detail_service_summary_part_1, parent, false);
        ServiceSummaryPart1ViewHolder viewHolder = new ServiceSummaryPart1ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ServiceSummaryPart1ViewHolder viewHolder = (ServiceSummaryPart1ViewHolder) holder;

        Outline outline = outlines.get(position);

        Glide.with(holder.itemView.getContext())
                .load(outline.getIcon())
                .into(viewHolder.ivIcon);

        viewHolder.tvTitle.setText(outline.getName());
        viewHolder.tvContent.setText(outline.getContent());
    }

    @Override
    public int getItemCount() {
        return outlines.size();
    }
}
