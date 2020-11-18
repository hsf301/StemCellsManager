package com.huashengfu.StemCellsManager.adapter.service.abandonment;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.huashengfu.StemCellsManager.R;
import com.huashengfu.StemCellsManager.adapter.BaseAdapter;
import com.huashengfu.StemCellsManager.entity.service.ServiceType;
import com.huashengfu.StemCellsManager.entity.service.ServiceTypeGroup;

import java.util.ArrayList;
import java.util.List;

public class ServiceTypeChildAdapter extends BaseAdapter<ServiceTypeChildViewHolder> {

    private List<ServiceType> serviceTypes = new ArrayList<>();
    private ServiceTypeGroup serviceTypeGroup;
    private int select = -1;

    public void setServiceTypeGroup(ServiceTypeGroup serviceTypeGroup) {
        this.serviceTypeGroup = serviceTypeGroup;
        serviceTypes.clear();
        serviceTypes.addAll(serviceTypeGroup.getServiceTypes());
    }

    //    public void addAll(List<ServiceType> serviceTypes) {
//        this.serviceTypes.addAll(serviceTypes);
//    }

    public void clear(){
        serviceTypes.clear();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_service_type_child, parent, false);
        ServiceTypeChildViewHolder viewHolder = new ServiceTypeChildViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ServiceTypeChildViewHolder viewHolder = (ServiceTypeChildViewHolder) holder;

        ServiceType serviceType = serviceTypes.get(position);
        viewHolder.tvName.setText(serviceType.getName());
        if(select == position){
            viewHolder.ivSelect.setVisibility(View.VISIBLE);
        }else{
            viewHolder.ivSelect.setVisibility(View.GONE);
        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                select = position;
                serviceTypeGroup.setSelect(position);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return serviceTypes.size();
    }
}
