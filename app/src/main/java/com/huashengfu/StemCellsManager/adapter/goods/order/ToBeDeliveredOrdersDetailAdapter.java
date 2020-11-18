package com.huashengfu.StemCellsManager.adapter.goods.order;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.huashengfu.StemCellsManager.R;
import com.huashengfu.StemCellsManager.adapter.BaseAdapter;
import com.huashengfu.StemCellsManager.entity.goods.Orders;

import java.util.ArrayList;
import java.util.List;

public class ToBeDeliveredOrdersDetailAdapter extends BaseAdapter<ToBeDeliveredOrdersDetailViewHolder> {

    private List<Orders> orders = new ArrayList<>();

    public void addAll(List<Orders> collection) {
        this.orders.addAll(collection);
    }

    public void add(Orders orders){
        this.orders.add(orders);
    }

    public int remove(Orders orders){
        int position = -1;
        for(int i = 0; i< this.orders.size(); i++){
            if(orders.getOrderId().equals(this.orders.get(i).getOrderId())){
                position = i;
                break;
            }
        }

        if(position >= 0)
            this.orders.remove(position);
        return position;
    }

    public void clear(){
        orders.clear();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_goods_tobe_delivered_detail, parent, false);
        ToBeDeliveredOrdersDetailViewHolder viewHolder = new ToBeDeliveredOrdersDetailViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ToBeDeliveredOrdersDetailViewHolder viewHolder = (ToBeDeliveredOrdersDetailViewHolder) holder;

        Orders orders = this.orders.get(position);

        viewHolder.tvPrice.setText(String.valueOf(orders.getTotalAmount()));

        viewHolder.adapter.clear();
        viewHolder.adapter.addAll(orders.getDetailsList());
        viewHolder.adapter.notifyDataSetChanged();

    }

    @Override
    public int getItemCount() {
        return orders.size();
    }
}
