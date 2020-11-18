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
import com.huashengfu.StemCellsManager.entity.service.ProcessSchedule;
import com.huashengfu.StemCellsManager.entity.service.ServiceType;

import java.util.ArrayList;
import java.util.List;

public class ServiceProcessScheduleAdapter extends BaseAdapter<ServiceProcessScheduleViewHolder> {

    private List<ProcessSchedule> processSchedules;
    private boolean last = false;

    public void setLast(boolean last) {
        this.last = last;
    }

    public void setProcessSchedules(List<ProcessSchedule> processSchedules) {
        this.processSchedules = processSchedules;
    }

    public List<ProcessSchedule> getProcessSchedules() {
        return processSchedules;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_service_process_schedule, parent, false);
        ServiceProcessScheduleViewHolder viewHolder = new ServiceProcessScheduleViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ServiceProcessScheduleViewHolder viewHolder = (ServiceProcessScheduleViewHolder) holder;

        ProcessSchedule processSchedule = processSchedules.get(position);

        holder.setIsRecyclable(false);

        viewHolder.tvNumber.setText("D" + (position+1));
        viewHolder.etName.setHint(
                String.format(viewHolder.itemView.getResources().getString(R.string.str_service_publis_process_schedule),
                        String.valueOf(position+1)));

        viewHolder.etName.setText(processSchedule.getName());
        viewHolder.etContent.setText(processSchedule.getContent());

        viewHolder.llSchedule.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog dialog = new AlertDialog.Builder(holder.itemView.getContext())
                        .setMessage(R.string.dialog_message_delete_schedule)
                        .setPositiveButton(R.string.btn_ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                processSchedules.remove(processSchedule);
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

        viewHolder.etName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                processSchedule.setName(editable.toString());
            }
        });

        viewHolder.etContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                processSchedule.setContent(editable.toString());
            }
        });

        if(last && position == getItemCount() - 1){
            viewHolder.etContent.requestFocus();
        }else{
            viewHolder.etContent.clearFocus();
        }
    }

    @Override
    public int getItemCount() {
        return processSchedules.size();
    }
}
