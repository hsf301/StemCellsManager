package com.huashengfu.StemCellsManager.adapter.goods.order;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.huashengfu.StemCellsManager.R;
import com.huashengfu.StemCellsManager.adapter.BaseAdapter;
import com.huashengfu.StemCellsManager.entity.goods.Orders;
import com.huashengfu.StemCellsManager.utils.GlideRoundTransformation;

import java.util.ArrayList;
import java.util.List;

public class ToBeDeliveredOrdersAdapter extends BaseAdapter<ToBeDeliveredOrdersViewHolder> {

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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_goods_tobe_delivered, parent, false);
        ToBeDeliveredOrdersViewHolder viewHolder = new ToBeDeliveredOrdersViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ToBeDeliveredOrdersViewHolder viewHolder = (ToBeDeliveredOrdersViewHolder) holder;

        Orders orders = this.orders.get(position);

        viewHolder.tvPrice.setText(String.valueOf(orders.getTotalAmount()));

        viewHolder.adapter.clear();
        viewHolder.adapter.addAll(orders.getDetailsList());
        viewHolder.adapter.notifyDataSetChanged();

        if(getOnItemClickListener() != null){
            viewHolder.btnDeliver.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getOnItemClickListener().onItemClick(viewHolder.btnDeliver, orders);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }
}
