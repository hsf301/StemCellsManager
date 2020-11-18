package com.huashengfu.StemCellsManager.adapter.goods.order;

import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.huashengfu.StemCellsManager.R;
import com.huashengfu.StemCellsManager.adapter.BaseAdapter;
import com.huashengfu.StemCellsManager.entity.goods.OrderDetail;
import com.huashengfu.StemCellsManager.entity.goods.Orders;
import com.huashengfu.StemCellsManager.utils.GlideRoundTransformation;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class CompletedOrdersAdapter extends BaseAdapter<CompletedOrdersViewHolder> {

    private List<Orders> orders = new ArrayList<>();

    public void addAll(List<Orders> collection) {
        this.orders.addAll(collection);
    }

    public void add(Orders orders){
        this.orders.add(orders);
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_goods_completed, parent, false);
        CompletedOrdersViewHolder viewHolder = new CompletedOrdersViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        CompletedOrdersViewHolder viewHolder = (CompletedOrdersViewHolder) holder;

        Orders orders = this.orders.get(position);

        viewHolder.tvPrice.setText(String.valueOf(orders.getTotalAmount()));

        viewHolder.llList.removeAllViews();
        for(OrderDetail detail : orders.getDetailsList()){
            View view = LayoutInflater.from(viewHolder.itemView.getContext()).inflate(R.layout.adapter_goods_order_detail,null);
            viewHolder.llList.addView(view);

            ImageView ivImage = view.findViewById(R.id.iv_image);
            TextView tvName = view.findViewById(R.id.tv_name);
            TextView tvSkuName = view.findViewById(R.id.tv_skuname);
            TextView tvCount = view.findViewById(R.id.tv_count);


            Glide.with(holder.itemView.getContext())
                    .load(detail.getGoodsSkuPic())
                    .placeholder(R.drawable.image_loading_pic_small)
                    .transform(new GlideRoundTransformation(holder.itemView.getContext(), 2))
                    .into(ivImage);

            tvName.setText(detail.getGoodsName());
            tvSkuName.setText(detail.getGoodsSkuName());
            tvCount.setText(String.valueOf(detail.getGoodsQuantity()));
        }

//        viewHolder.adapter.clear();
//        viewHolder.adapter.addAll(orders.getDetailsList());
//        viewHolder.adapter.notifyDataSetChanged();

    }

    @Override
    public int getItemCount() {
        return orders.size();
    }
}
