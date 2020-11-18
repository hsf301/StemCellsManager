package com.huashengfu.StemCellsManager.adapter.service.detail;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.huashengfu.StemCellsManager.R;
import com.huashengfu.StemCellsManager.adapter.BaseAdapter;
import com.huashengfu.StemCellsManager.entity.service.Discount;

import java.util.ArrayList;
import java.util.List;

public class ServiceDiscountAdapter extends BaseAdapter<ServiceDiscountViewHolder> {

    private List<Discount> discounts = new ArrayList<>();

    public void addAll(List<Discount> collection) {
        this.discounts.addAll(collection);
    }

    public void add(Discount discount){
        discounts.add(discount);
    }

    public void clear(){
        discounts.clear();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_service_detail_discount, parent, false);
        ServiceDiscountViewHolder viewHolder = new ServiceDiscountViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ServiceDiscountViewHolder viewHolder = (ServiceDiscountViewHolder) holder;

        Discount discount = discounts.get(position);
        viewHolder.tvName.setText(discount.getName());
    }

    @Override
    public int getItemCount() {
        return discounts.size();
    }
}
