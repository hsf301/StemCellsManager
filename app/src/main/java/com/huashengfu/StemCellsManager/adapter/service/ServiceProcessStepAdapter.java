package com.huashengfu.StemCellsManager.adapter.service;

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
import com.huashengfu.StemCellsManager.entity.response.sms.Process;
import com.huashengfu.StemCellsManager.entity.service.ProcessSchedule;
import com.huashengfu.StemCellsManager.entity.service.ProcessStep;

import java.util.ArrayList;
import java.util.List;

public class ServiceProcessStepAdapter extends BaseAdapter<ServiceProcessStepViewHolder> {

    private List<ProcessStep> processSteps = new ArrayList<>();

//    public void addAll(List<ProcessStep> collection) {
//        processSteps.addAll(collection);
//    }

    public void addAll(List<Process> collection){
        for(Process process : collection){
            process.getStep().getProcessSchedules().addAll(process.getProcessList());
            processSteps.add(process.getStep());
        }
    }

    public void add(ProcessStep processStep){
        processSteps.add(processStep);
    }

    public void add(ProcessSchedule processSchedule){
        processSteps.get(getItemCount() - 1).getProcessSchedules().add(processSchedule);
        notifyDataSetChanged();

    }

    public List<ProcessStep> getProcessSteps() {
        return processSteps;
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_service_process_step, parent, false);
        ServiceProcessStepViewHolder viewHolder = new ServiceProcessStepViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ServiceProcessStepViewHolder viewHolder = (ServiceProcessStepViewHolder) holder;

        ProcessStep processStep = processSteps.get(position);

        holder.setIsRecyclable(false);

        viewHolder.tvNumber.setText("Step" + (position+1));
        viewHolder.etTitle.setHint(
                String.format(viewHolder.itemView.getResources().getString(R.string.str_service_publis_process_step),
                        String.valueOf(position+1))
        );

        viewHolder.etTitle.setText(processStep.getTitle());
        viewHolder.etTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                processStep.setTitle(editable.toString());
            }
        });

        viewHolder.llStep.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog dialog = new AlertDialog.Builder(holder.itemView.getContext())
                        .setMessage(R.string.dialog_message_delete_step)
                        .setPositiveButton(R.string.btn_ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                processSteps.remove(processStep);
                                notifyItemChanged(position);
                                notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton(R.string.btn_cancle, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .create();
                dialog.show();
                return true;
            }
        });

        viewHolder.adapter.setProcessSchedules(processStep.getProcessSchedules());
        viewHolder.adapter.setLast(position == getItemCount() - 1);
        viewHolder.adapter.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return processSteps.size();
    }
}
