package com.huashengfu.StemCellsManager.adapter.service.detail;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.huashengfu.StemCellsManager.R;
import com.huashengfu.StemCellsManager.adapter.BaseAdapter;
import com.huashengfu.StemCellsManager.entity.service.QA;

import java.util.ArrayList;
import java.util.List;

public class ServiceQAAdapter extends BaseAdapter<ServiceQAViewHolder> {

    private List<QA> qas = new ArrayList<>();

    public void addAll(List<QA> collection) {
        this.qas.addAll(collection);
    }

    public void add(QA qa){
        qas.add(qa);
    }
    public void clear(){
        qas.clear();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_service_detail_qa, parent, false);
        ServiceQAViewHolder viewHolder = new ServiceQAViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ServiceQAViewHolder viewHolder = (ServiceQAViewHolder) holder;

        QA qa = qas.get(position);
        viewHolder.tvQuestion.setText(qa.getQuestion());
        viewHolder.tvAnswer.setText(String.format(holder.itemView.getResources().getString(R.string.str_service_detail_answer), qa.getAnswer()));

        if(getOnItemClickListener() != null){
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getOnItemClickListener().onItemClick(viewHolder.itemView, qa);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return qas.size();
    }
}
