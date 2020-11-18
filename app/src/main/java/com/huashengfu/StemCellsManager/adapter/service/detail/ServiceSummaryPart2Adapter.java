package com.huashengfu.StemCellsManager.adapter.service.detail;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.huashengfu.StemCellsManager.R;
import com.huashengfu.StemCellsManager.adapter.BaseAdapter;
import com.huashengfu.StemCellsManager.entity.response.sms.OutlineResult;

import java.util.ArrayList;
import java.util.List;

public class ServiceSummaryPart2Adapter extends BaseAdapter<ServiceSummaryPart2ViewHolder> {

    private List<OutlineResult> outlineResults = new ArrayList<>();

    public void addAll(List<OutlineResult> collection) {
        this.outlineResults.addAll(collection);
    }

    public void add(OutlineResult outlineResult){
        outlineResults.add(outlineResult);
    }

    public void clear(){
        outlineResults.clear();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_service_detail_service_summary_part_2, parent, false);
        ServiceSummaryPart2ViewHolder viewHolder = new ServiceSummaryPart2ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ServiceSummaryPart2ViewHolder viewHolder = (ServiceSummaryPart2ViewHolder) holder;

        OutlineResult outlineResult = outlineResults.get(position);
        viewHolder.tvTitle.setText(outlineResult.getTitle());
        viewHolder.tvContent.setText(outlineResult.getDetails());
    }

    @Override
    public int getItemCount() {
        return outlineResults.size();
    }
}
