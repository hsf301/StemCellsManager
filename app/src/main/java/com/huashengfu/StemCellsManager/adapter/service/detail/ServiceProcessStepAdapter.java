package com.huashengfu.StemCellsManager.adapter.service.detail;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.huashengfu.StemCellsManager.R;
import com.huashengfu.StemCellsManager.adapter.BaseAdapter;
import com.huashengfu.StemCellsManager.entity.service.ProcessSchedule;
import com.huashengfu.StemCellsManager.entity.service.ProcessStep;

import java.util.ArrayList;
import java.util.List;

public class ServiceProcessStepAdapter extends BaseAdapter<ServiceProcessStepViewHolder> {

    private List<ProcessStep> processSteps = new ArrayList<>();

    public void addAll(List<ProcessStep> collection) {
        processSteps.addAll(collection);
    }

    public void add(ProcessStep processStep){
        processSteps.add(processStep);
    }

    public void add(ProcessSchedule processSchedule){
        processSteps.get(getItemCount() - 1).getProcessSchedules().add(processSchedule);
        notifyDataSetChanged();

    }

    public List<List<ProcessSchedule>> getProcessSchedule(){
        List<List<ProcessSchedule>> list = new ArrayList<>();

        for(int i=0; i<getItemCount(); i++){
            list.add(processSteps.get(i).getProcessSchedules());
        }

        return list;
    }

    public void clear(){
        processSteps.clear();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_service_detail_process_step, parent, false);
        ServiceProcessStepViewHolder viewHolder = new ServiceProcessStepViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ServiceProcessStepViewHolder viewHolder = (ServiceProcessStepViewHolder) holder;

        ProcessStep processStep = processSteps.get(position);
        viewHolder.tvNumber.setText("Step" + (position+1));
        viewHolder.tvTips.setText(processStep.getTitle());

        viewHolder.adapter.setProcessSchedules(processStep.getProcessSchedules());
        viewHolder.adapter.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return processSteps.size();
    }
}
