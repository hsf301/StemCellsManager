package com.huashengfu.StemCellsManager.adapter.service.abandonment;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.huashengfu.StemCellsManager.R;
import com.huashengfu.StemCellsManager.adapter.BaseAdapter;
import com.huashengfu.StemCellsManager.entity.service.ServiceTypeGroup;

import java.util.ArrayList;
import java.util.List;

public class ServiceTypeParentAdapter extends BaseAdapter<ServiceTypeParentViewHolder> {

    private List<ServiceTypeGroup> serviceTypeGroups = new ArrayList<>();
    private int select = -1;

    public void addAll(List<ServiceTypeGroup> collection) {
        serviceTypeGroups.addAll(collection);
    }

    public void clear(){
        serviceTypeGroups.clear();
    }

    public List<ServiceTypeGroup> getServiceTypeGroups() {
        return serviceTypeGroups;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_service_type_parent, parent, false);
        ServiceTypeParentViewHolder viewHolder = new ServiceTypeParentViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ServiceTypeParentViewHolder viewHolder = (ServiceTypeParentViewHolder) holder;

        ServiceTypeGroup serviceTypeGroup = serviceTypeGroups.get(position);
        viewHolder.tvName.setText(serviceTypeGroup.getName());
        viewHolder.adapter.clear();
        viewHolder.adapter.setServiceTypeGroup(serviceTypeGroup);
        viewHolder.adapter.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return serviceTypeGroups.size();
    }
}
