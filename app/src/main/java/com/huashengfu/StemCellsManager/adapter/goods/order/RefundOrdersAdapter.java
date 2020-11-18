package com.huashengfu.StemCellsManager.adapter.goods.order;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.huashengfu.StemCellsManager.Constants;
import com.huashengfu.StemCellsManager.R;
import com.huashengfu.StemCellsManager.adapter.BaseAdapter;
import com.huashengfu.StemCellsManager.entity.goods.Orders;

import java.util.ArrayList;
import java.util.List;

public class RefundOrdersAdapter extends BaseAdapter<RefundOrdersViewHolder> {

    private List<Orders> orders = new ArrayList<>();

    public void addAll(List<Orders> collection) {
        this.orders.addAll(collection);
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

    public int update(Orders orders){
        int position = -1;

        for(int i=0; i<this.orders.size(); i++){
            if(this.orders.get(i).getOrderId().equals(orders.getOrderId())){
                position = i;
                this.orders.get(i).setStatus(orders.getStatus());
            }
        }

        return position;
    }

    public void clear(){
        orders.clear();
    }

    public boolean hasRefund(){
        boolean has = false;
        for(Orders tmp : orders){
            if(tmp.getStatus() != Constants.Status.Order.refunded){
                has = true;
            }
        }
        return has;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_goods_refund, parent, false);
        RefundOrdersViewHolder viewHolder = new RefundOrdersViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        RefundOrdersViewHolder viewHolder = (RefundOrdersViewHolder) holder;

        Orders orders = this.orders.get(position);

        viewHolder.tvPrice.setText(String.valueOf(orders.getTotalAmount()));

        if(orders.getStatus() != Constants.Status.Order.refunded)
            viewHolder.dot.setVisibility(View.VISIBLE);
        else
            viewHolder.dot.setVisibility(View.GONE);

        viewHolder.adapter.clear();
        viewHolder.adapter.addAll(orders.getDetailsList());
        viewHolder.adapter.notifyDataSetChanged();

        if(getOnItemClickListener() != null){
            viewHolder.btnDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getOnItemClickListener().onItemClick(viewHolder.btnDetail, orders);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }
}
