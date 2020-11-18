package com.huashengfu.StemCellsManager.adapter.service.detail;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.huashengfu.StemCellsManager.R;
import com.huashengfu.StemCellsManager.adapter.BaseAdapter;
import com.huashengfu.StemCellsManager.entity.service.ProcessSchedule;

import java.util.List;

public class ServiceProcessScheduleAdapter extends BaseAdapter<ServiceProcessScheduleViewHolder> {

    private List<ProcessSchedule> processSchedules;

    public void setProcessSchedules(List<ProcessSchedule> processSchedules) {
        this.processSchedules = processSchedules;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_service_detail_process_schedule, parent, false);
        ServiceProcessScheduleViewHolder viewHolder = new ServiceProcessScheduleViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ServiceProcessScheduleViewHolder viewHolder = (ServiceProcessScheduleViewHolder) holder;

        ProcessSchedule processSchedule = processSchedules.get(position);

        viewHolder.tvNumber.setText("D" + (position+1));
        viewHolder.tvTips.setText(processSchedule.getName());
        viewHolder.tvContent.setText(processSchedule.getContent());
    }

    @Override
    public int getItemCount() {
        return processSchedules.size();
    }
}
