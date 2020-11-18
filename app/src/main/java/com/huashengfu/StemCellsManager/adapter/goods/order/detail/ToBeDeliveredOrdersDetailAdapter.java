package com.huashengfu.StemCellsManager.adapter.goods.order.detail;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.huashengfu.StemCellsManager.R;
import com.huashengfu.StemCellsManager.adapter.BaseAdapter;
import com.huashengfu.StemCellsManager.adapter.goods.order.ToBeDeliveredOrdersViewHolder;
import com.huashengfu.StemCellsManager.entity.goods.OrderDetail;
import com.huashengfu.StemCellsManager.utils.GlideRoundTransformation;

import java.util.ArrayList;
import java.util.List;

public class ToBeDeliveredOrdersDetailAdapter extends BaseAdapter<ToBeDeliveredOrdersViewHolder> {

    private List<OrderDetail> orderDetails = new ArrayList<>();

    public void addAll(List<OrderDetail> collection) {
        this.orderDetails.addAll(collection);
    }

    public void clear(){
        orderDetails.clear();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_goods_order_detail, parent, false);
        ToBeDeliveredOrdersDetailViewHolder viewHolder = new ToBeDeliveredOrdersDetailViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ToBeDeliveredOrdersDetailViewHolder viewHolder = (ToBeDeliveredOrdersDetailViewHolder) holder;

        OrderDetail orderDetail = this.orderDetails.get(position);

        Glide.with(holder.itemView.getContext())
                .load(orderDetail.getGoodsSkuPic())
                .placeholder(R.drawable.image_loading_pic_small)
                .transform(new GlideRoundTransformation(holder.itemView.getContext(), 2))
                .into(viewHolder.ivImage);

        viewHolder.tvName.setText(orderDetail.getGoodsName());
        viewHolder.tvSkuName.setText(orderDetail.getGoodsSkuName());
        viewHolder.tvCount.setText(String.valueOf(orderDetail.getGoodsQuantity()));
    }

    @Override
    public int getItemCount() {
        return orderDetails.size();
    }
}
