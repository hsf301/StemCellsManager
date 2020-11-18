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

public class ToBeConfirmedOrdersAdapter extends BaseAdapter<ToBeConfirmedOrdersViewHolder> {

    private List<Orders> orders = new ArrayList<>();

    public void addAll(List<Orders> collection) {
        this.orders.addAll(collection);
    }

    public int remove(Orders goods){
        int position = orders.indexOf(goods);
        orders.remove(goods);
        return position;
    }

    public void clear(){
        orders.clear();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_goods_tobe_confirmed, parent, false);
        ToBeConfirmedOrdersViewHolder viewHolder = new ToBeConfirmedOrdersViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ToBeConfirmedOrdersViewHolder viewHolder = (ToBeConfirmedOrdersViewHolder) holder;

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
